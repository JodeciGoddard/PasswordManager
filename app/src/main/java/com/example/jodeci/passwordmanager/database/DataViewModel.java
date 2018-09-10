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

    public void insert(User user){ mRepository.insert(user); }

    public void insert(Items item) { mRepository.insert(item);}

    public User getUser() { return mRepository.getUser(); }

    public Items getItemByID(int id){return mRepository.getItemById(id);}

    public void deleteByItemID(int id){
        mRepository.deleteByItemID(id);
    }

    public void updateItem(Items item) {mRepository.updateItem(item);}

}
