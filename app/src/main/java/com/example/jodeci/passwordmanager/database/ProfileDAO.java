package com.example.jodeci.passwordmanager.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by jodeci on 9/12/2018.
 */


@Dao
public interface ProfileDAO {

    @Insert
    long insert(Profiles profile);

    @Delete
    void deleteProfile(Profiles profile);

    @Update
    void updateProfile(Profiles profile);

    @Query("Select * from profile_table")
    List<Profiles> getAllProfiles();

    @Query("Select name from profile_table")
    List<String> getProfilesAsString();

    @Query("SELECT COUNT(name) FROM profile_table")
    int countProfiles();

    @Query("SELECT * from profile_table where _id = :id")
    Profiles getProfileByID(int id);

}
