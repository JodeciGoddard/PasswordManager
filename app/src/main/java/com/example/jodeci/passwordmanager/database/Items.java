package com.example.jodeci.passwordmanager.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by jodeci on 9/6/2018.
 */

@Entity(tableName = "item_table")
public class Items {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    public int id;

    @NonNull
    @ColumnInfo(name = "Iappname")
    public String appname;

    @NonNull
    @ColumnInfo(name = "Iusername")
    public String username;

    @NonNull
    @ColumnInfo(name = "Ipassword")
    public String password;

    @NonNull
    @ColumnInfo(name = "Iprofile")
    public String profile;

    public Items(String appname, String username, String password, String profile){
        this.appname = appname;
        this.username = username;
        this.password = password;
        this.profile = profile;
    }


}
