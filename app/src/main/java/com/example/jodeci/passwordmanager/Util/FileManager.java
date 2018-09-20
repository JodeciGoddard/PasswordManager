package com.example.jodeci.passwordmanager.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jodeci on 7/17/2018.
 * reading and writing to external storage
 */

public class FileManager {
    private Context context;

    public FileManager(Context context){
        this.context = context;
    }

    FileManager(){
    }

    public void write(String data, String filename) throws FileNotFoundException {
        if(!isExternalStorageWritable()){
            //cannot write to storage
            Log.i("Read/Write: ", "External Storage is unavailable" );
            return;
        }

        File dir = getPublicStorageDir(filename);
        File file = new File(dir, filename);


        try {
            FileOutputStream os = new FileOutputStream(file);
            os.write(data.getBytes());
            os.close();
            Log.i("Read/Write: ", "file write successful");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Read/Write: ", "file write failed");
        }


    }

    public String read(Uri uri){
        String s = "";

        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {

                stringBuilder.append(line + "\n");
            }
                s = stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return s;
    }

    public File getPublicStorageDir(String filename) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "Lock&Key Exports");
        if (!file.mkdirs()) {
            Log.i("Read/Write: ", "Directory not made");
        }
        return file;
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }



}
