package com.example.jodeci.passwordmanager.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

/**
 * Created by jodeci on 9/6/2018.
 */
@Dao
public interface UserDAO {

    @Insert
    long insert(User user);

    @Query("SELECT COUNT(Uusername) FROM user_table")
    int countUsers();

    @Query("SELECT Upassword FROM user_table")
    String getPassword();

    @Query("Select * From user_table limit 1")
    User getUser();

    @Delete
    void deleteItem(Items item);

    @Update
    void updateUser(User user);


}
