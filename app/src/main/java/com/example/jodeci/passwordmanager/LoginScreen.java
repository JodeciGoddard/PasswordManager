package com.example.jodeci.passwordmanager;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jodeci.passwordmanager.database.DataBaseHandler;
import com.example.jodeci.passwordmanager.database.DataViewModel;
import com.example.jodeci.passwordmanager.database.User;

import static android.widget.Toast.makeText;


/*Password Mananger Application that stores simple Text passwords on your device */

public class LoginScreen extends AppCompatActivity {

    FloatingActionButton newUser;
    Button login;
    EditText username;
    EditText password;
    TextView errText;

    private final String NAME_SATE = "savedUsername";

    //MyDBHandler dbHandler;
    DataViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        username = (EditText) findViewById(R.id.txtUsername);
        password = (EditText) findViewById(R.id.txtPassword);
        newUser = (FloatingActionButton) findViewById(R.id.btnRegister);
        login = (Button) findViewById(R.id.btnLogin);
        errText = (TextView) findViewById(R.id.lgErr);

        mViewModel = ViewModelProviders.of(this).get(DataViewModel.class);

        restorUsername();

        //first time user
        if(mViewModel.getUser() == null){
            Intent intent = new Intent(LoginScreen.this, RegisterScreen.class);
            startActivity(intent);
        }

        //dbHandler = new MyDBHandler(this,null, null,1);

//        newUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginScreen.this, RegisterScreen.class);
//                startActivity(intent);
//            }
//        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateUsername()){
                    User user = mViewModel.getUser();
                    String unfileterPasswword = password.getText().toString();
                    if (unfileterPasswword.equals( user.password )){
                        //Correct Password
                        Intent intent = new Intent(LoginScreen.this, ProfileView.class);
                        intent.putExtra("username",user.username);
                        startActivity(intent);
                        errText.setText("");
                    } else {
                        //incorrect password
                        errText.setText("Incorrect Password.");
                        Toast toast = Toast.makeText(getBaseContext(),"Login Failed", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }

            }
        });
    }

    private void restorUsername() {
       username.setText(Preferences.getLastUsername(this));
    }

    @Override
    protected void onPause() {
        super.onPause();

        //save username
        Preferences.saveLastUsername(username.getText().toString(), this);
    }

    //make sure username meets requirements
    private boolean validateUsername(){
        String test = username.getText().toString().trim();
        if (test == null || test.equals("")){
            errText.setText("Please fill in username.");
            return false;
        }

        if (username.getText().toString().matches("^.*[^a-zA-Z0-9].*$")){
            errText.setText("Username contains invalid characters.");
            return false;
        }

        return true;
    }


}
