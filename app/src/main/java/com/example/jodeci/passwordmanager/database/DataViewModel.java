package com.example.jodeci.passwordmanager.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by jodeci on 9/6/2018.
 */

public class DataViewModel extends AndroidViewModel {
    private Repository mRepository;

    public DataViewModel(@NonNull Application application) {
        super(application);
        mRepository = new Repository(application);
    }

    public List<Items> getAllitems() {return mRepository.getmAllitems();}

    public List<Items> getItemWithProfile(String profile){return mRepository.getItemsWithProfile(profile);}

    public List<Profiles> getProfiles() {return mRepository.getAllProfiles(); }

    public List<String> getProfilesAsString(){return  mRepository.getStringProfiles();}

    public void insert(User user){ mRepository.insert(user); }

    public void insert(Items item) { mRepository.insert(item);}

    public long insert(Profiles profile) { return mRepository.insert(profile); }

    public User getUser() { return mRepository.getUser(); }

    public Profiles getProfileByID(int id){
        return mRepository.getProfileByID(id);
    }

    public Items getItemByID(int id){return mRepository.getItemById(id);}

    public void deleteByItemID(int id){
        mRepository.deleteByItemID(id);
    }

    public void deleteProfile(Profiles profile) {mRepository.deleteProfile(profile);}

    public void updateItem(Items item) {mRepository.updateItem(item);}

    public void updateProfile(Profiles profile){ mRepository.updateProfile(profile);}

}
