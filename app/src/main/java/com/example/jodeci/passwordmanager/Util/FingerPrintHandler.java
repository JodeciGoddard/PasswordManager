package com.example.jodeci.passwordmanager.Util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jodeci.passwordmanager.Launch.LoginScreen;
import com.example.jodeci.passwordmanager.R;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Created by jodeci on 10/4/2018.
 */

public class FingerPrintHandler {

    private Context mContext;
    private FingerprintManager mFingerprintManager = null;
    private CancellationSignal mCancellationSignal;
    private FingerprintManager.AuthenticationCallback  mAuthenticationCallback;
    private OnAuthenticationSucceededListener mSucceedListener;
    private OnAuthenticationErrorListener mFailedListener;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private Cipher cipher;
    private String KEY_NAME = "AndroidKey";

    public interface OnAuthenticationSucceededListener {
        void onAuthSucceeded();
    }

    public interface OnAuthenticationErrorListener {
        void onAuthFailed();
    }

    public void setOnAuthenticationSucceededListener (OnAuthenticationSucceededListener listener){
        mSucceedListener = listener;
    }

    public void setOnAuthenticationFailedListener(OnAuthenticationErrorListener listener) {
        mFailedListener = listener;
    }

    public FingerPrintHandler(Context context){
        this.mContext = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mFingerprintManager = context.getSystemService(FingerprintManager.class);
            mCancellationSignal = new CancellationSignal();

            mAuthenticationCallback = new FingerprintManager.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    if(mCancellationSignal == null) return;
                    update("There was an Auth error:  " + errString, false);
                    super.onAuthenticationError(errorCode, errString);
                }

                @Override
                public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                    update("" + helpString, false);
                    super.onAuthenticationHelp(helpCode, helpString);
                }

                @Override
                public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    if( mSucceedListener != null )
                        mSucceedListener.onAuthSucceeded();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    if (mFailedListener != null)
                        mFailedListener.onAuthFailed();
                }
            };
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startListening(){
        if (isFingerScannerAvailableAndSet() ) {

            generateKey();

            try{
                if(cipherInit()){
                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                    mFingerprintManager.authenticate(cryptoObject, mCancellationSignal, 0 /* flags */, mAuthenticationCallback, null);
                }

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    private void update(String s, boolean b) {
        // handle success/fail navigation
        if(b){
            ((LoginScreen) mContext).authenticateSuccess();
            Toast.makeText(mContext,s,Toast.LENGTH_LONG).show();
        } else {
            TextView lblFinger = (TextView) ((LoginScreen) mContext).findViewById(R.id.lgErr);
            lblFinger.setText(s);
        }

    }

    public void stopListening(){
        if ( isFingerScannerAvailableAndSet() ) {
            try {
                mCancellationSignal.cancel();
                mCancellationSignal = null;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public boolean isFingerScannerAvailableAndSet(){
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
            return false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return false;
        if( mFingerprintManager == null )
            return false;
        if( !mFingerprintManager.isHardwareDetected() )
            return false;
        if( !mFingerprintManager.hasEnrolledFingerprints())
            return false;

        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void generateKey() {

        try {

            keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();

        } catch (KeyStoreException | IOException | CertificateException
                | NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | NoSuchProviderException e) {

            e.printStackTrace();

        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }


        try {

            keyStore.load(null);

            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);

            cipher.init(Cipher.ENCRYPT_MODE, key);

            return true;

        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }

    }

}
