package com.example.jodeci.passwordmanager.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by jodeci on 9/6/2018.
 */

@Entity(tableName = "user_table")
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    public int id;

    @NonNull
    @ColumnInfo(name = "Uusername")
    public String username;

    @NonNull
    @ColumnInfo(name = "Upassword")
    public String password;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
}
