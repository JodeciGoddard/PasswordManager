package com.example.jodeci.passwordmanager.database;

/**
 * Created by jodeci on 9/6/2018.
 */

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {User.class, Items.class}, version = 1)
public abstract class DataBaseHandler extends RoomDatabase {

    public abstract UserDAO userDao();
    public abstract ItemDAO itemDao();

    private static DataBaseHandler INSTANCE;

    public static DataBaseHandler getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (DataBaseHandler.class){
                if(INSTANCE == null){
                    //Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DataBaseHandler.class, "application_database").build();
                }
            }
        }
        return INSTANCE;
    }

}
