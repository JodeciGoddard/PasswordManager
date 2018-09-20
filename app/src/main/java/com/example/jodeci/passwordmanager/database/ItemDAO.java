package com.example.jodeci.passwordmanager.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by jodeci on 9/6/2018.
 */

@Dao
public interface ItemDAO {

    @Insert
    long insert(Items item);

    @Query("Delete from item_table where _id = :id")
    void deleteByItemID(int id);

    @Update
    void updateItem(Items item);

    @Query("SELECT * from item_table")
    List<Items> getAllItems();

    @Query("SELECT * from item_table where _id = :id")
    Items getItembID(int id);

    @Query("select * from item_table where Iprofile = :profile ")
    List<Items> getItemsWithProfile(String profile);


}
