package com.example.jodeci.passwordmanager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jodec on 10/23/2017.
 */

public class Preferences {
    private static String PACKAGE_NAME = "com.example.jodeci.passwordmanager.";
    private static String KEY_ID = "KEY_ID";

    public static void incremnetID(String username, Context context){
        SharedPreferences pref = context.getSharedPreferences(PACKAGE_NAME+"username", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        int val = pref.getInt(KEY_ID,0);
        val++;
        editor.putInt(KEY_ID,val);
        editor.commit();
    }

    public static int getNewID(String username, Context context){
        SharedPreferences pref = context.getSharedPreferences(PACKAGE_NAME+"username", Context.MODE_PRIVATE);
        int val = pref.getInt(KEY_ID,0);
        val++;
        return val;
    }
}
