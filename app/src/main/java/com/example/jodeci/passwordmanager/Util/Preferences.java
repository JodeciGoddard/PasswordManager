package com.example.jodeci.passwordmanager.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.jodeci.passwordmanager.database.Profiles;

/**
 * Created by jodeci on 10/23/2017.
 */

public class Preferences {
    private static String PACKAGE_NAME = "com.example.jodeci.passwordmanager.";
    private static String KEY_ID = "KEY_ID";
    private static String USER_ID = "USER_ID";
    private static String PROFILE = "LAST_PROFILE";

    public static void incremnetID(String username, Context context){
        SharedPreferences pref = context.getSharedPreferences(PACKAGE_NAME+"username", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        int val = pref.getInt(KEY_ID,0);
        val++;
        editor.putInt(KEY_ID,val);
        editor.apply();
    }

    public static int getNewID(String username, Context context){
        SharedPreferences pref = context.getSharedPreferences(PACKAGE_NAME+"username", Context.MODE_PRIVATE);
        int val = pref.getInt(KEY_ID,0);
        val++;
        return val;
    }

    public static void saveLastUsername(String username, Context context){
        SharedPreferences pref = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_ID, username);
        editor.commit();
    }

    public static void saveCurrentProfile(Profiles profile, Context context){
        SharedPreferences pref = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(PROFILE, profile.id);
        editor.commit();
    }

    public static int getCurrentProfileID(Context context){
        SharedPreferences pref = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        return pref.getInt(PROFILE, -1);
    }

    public static String getLastUsername(Context context){
        SharedPreferences pref = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        return pref.getString(USER_ID, "");
    }
}
