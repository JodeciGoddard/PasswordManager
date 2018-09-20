package com.example.jodeci.passwordmanager.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by jodeci on 9/11/2018.
 */

@Entity(tableName = "profile_table")
public class Profiles {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "profile_color")
    public int color;

    public Profiles(String name, int color){
        this.name = name;
        this.color = color;
    }

    //returns the first letter in the name as a Capital
    public String firstLetter(){
        String letter = "" + name.charAt(0);
        return letter.toUpperCase();
    }
}
