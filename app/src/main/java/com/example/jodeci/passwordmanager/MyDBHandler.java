package com.example.jodeci.passwordmanager;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "managerDB.db";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_ENTRIES = "entries";

    //columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_APPNAME = "applicationName";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_USERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TABLE_USERS;
        db.execSQL(query);
        onCreate(db);
    }

    private void createEntryTable(String tablename){
        SQLiteDatabase db = getWritableDatabase();
        String query2 = "CREATE TABLE " + (tablename + TABLE_ENTRIES) + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_APPNAME + " TEXT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT " +
                ");";
        db.execSQL(query2);
        db.close();
    }

    //add a new row to users
    public void addUser(Users user){
        ContentValues values = new ContentValues();
        createEntryTable(user.get_username());
        values.put(COLUMN_USERNAME, user.get_username());
        values.put(COLUMN_PASSWORD, user.get_password());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    //add row to entries
    public long addEntry(Entry entry, String tablename){
        long rowid;
        ContentValues values = new ContentValues();
        values.put(COLUMN_APPNAME, entry.get_applicationName());
        values.put(COLUMN_USERNAME, entry.get_appUsername());
        values.put(COLUMN_PASSWORD, entry.get_appPassword());
        SQLiteDatabase db = getWritableDatabase();
        rowid = db.insert(tablename + TABLE_ENTRIES, null, values);
        db.close();
        return rowid;
    }

    public void deleteUser(int id, String tablename){
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_USERS + " WHERE " + COLUMN_ID + "=\"" + id + "\";";
        String query2 = "DROP TABLE IF EXISTS " + (tablename + TABLE_ENTRIES);
        db.execSQL(query);
        db.close();
    }

    public void deleteEntry(int id, String tablename){
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + (tablename + TABLE_ENTRIES) + " WHERE " + COLUMN_ID + "=\"" + id + "\";";
        db.execSQL(query);
        db.close();
    }

    public boolean containsUser(Users user){
        boolean result = false;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=\"" + user.get_username() + "\";";

        //Cursor to results
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() <= 0){
            result = false;
        } else {
            result = true;
        }

        c.close();
        db.close();
        return result;
    }

    public String getUserPassword(Users user){
        String result;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=\"" + user.get_username() + "\";";

        //Cursor to results
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if (c.getCount() == 1){
          if(c.getString(c.getColumnIndex(COLUMN_PASSWORD)) != null ){
              result = c.getString(c.getColumnIndex(COLUMN_PASSWORD));
          } else {
              result = "No password found.";
          }
        } else {
            result = "Incorrect Username.";
        }

        if (!result.equals(user.get_password())){
            result = "Incorrect Password";
        }

        c.close();
        db.close();
        return result;
    }

    public ArrayList<Entry> getEntries(String tablename){
        ArrayList<Entry> e = new ArrayList<Entry>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + (tablename + TABLE_ENTRIES) + " WHERE 1";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_APPNAME)) != null &&
                    c.getString(c.getColumnIndex(COLUMN_USERNAME)) != null &&
                    c.getString(c.getColumnIndex(COLUMN_PASSWORD)) != null ) {
                String s1 = c.getString(c.getColumnIndex(COLUMN_APPNAME));
                String s2 = c.getString(c.getColumnIndex(COLUMN_USERNAME));
                String s3 = c.getString(c.getColumnIndex(COLUMN_PASSWORD));
                int id = c.getInt(c.getColumnIndex(COLUMN_ID));

                Entry ne = new Entry(s1,s2,s3);
                ne.set_id(id);
                e.add(ne);
            }
            c.moveToNext();
        }

        db.close();
        c.close();
        return e;
    }

    public void updateEntry(Entry e, String tablename){
        String id = "" + e.get_id();
        ContentValues values = new ContentValues();
        values.put(COLUMN_APPNAME, e.get_applicationName());
        values.put(COLUMN_USERNAME, e.get_appUsername());
        values.put(COLUMN_PASSWORD, e.get_appPassword());
        SQLiteDatabase db = getWritableDatabase();
        db.update(tablename + TABLE_ENTRIES, values, "_id = ?", new String[]{id});
        db.close();
    }


    public void printdblogEntries(String tablename){
        String log = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + (tablename + TABLE_ENTRIES) + " WHERE 1";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_APPNAME)) != null &&
                    c.getString(c.getColumnIndex(COLUMN_USERNAME)) != null &&
                    c.getString(c.getColumnIndex(COLUMN_PASSWORD)) != null ) {
                String s1 = c.getString(c.getColumnIndex(COLUMN_APPNAME));
                String s2 = c.getString(c.getColumnIndex(COLUMN_USERNAME));
                String s3 = c.getString(c.getColumnIndex(COLUMN_PASSWORD));
                int id = c.getInt(c.getColumnIndex(COLUMN_ID));

                log += id + " " + s1 + " " + s2 + " " + s3 + "\n";
            }
            c.moveToNext();
        }

        Log.i("JODECI", log);
        db.close();
        c.close();
    }

}
