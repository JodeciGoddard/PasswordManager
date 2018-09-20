package com.example.jodeci.passwordmanager.database;

import android.app.Application;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by jodeci on 9/6/2018.
 */

public class Repository {
    private UserDAO muserDao;
    private ItemDAO mitemDao;
    private ProfileDAO mProfileDao;

    public  Repository(Application application){
        DataBaseHandler db = DataBaseHandler.getDatabase(application);
        muserDao = db.userDao();
        mitemDao = db.itemDao();
        mProfileDao = db.profileDao();
    }

    public void insert(User user){
        new insertAsyncTask(muserDao).execute(user);
    }

    public void insert(Items item){
        new insertItemAsyncTask(mitemDao).execute(item);
    }

    public long insert(Profiles profile){
        try {
            return new insertProfileTask(mProfileDao).execute(profile).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Items> getmAllitems(){
        try {
            return new getAllItemsAsyncTask(mitemDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.i("Repostitory: ", "Interupted Exception thrown");
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.i("Repostitory: ", "Execution Exception thrown");
        }
        return null;
    }

    public List<Items> getItemsWithProfile(String profile){
        try {
            return  new getItemWithProfileAsyncTask(mitemDao).execute(profile).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Profiles> getAllProfiles(){
        try {
            return new getProfilesAsyncTask(mProfileDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getStringProfiles(){
        try {
            return new getStringProfilesAsyncTask(mProfileDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUser(){
        try {
            return new getUserTask(muserDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.i("Repostitory: ", "Interupted Exception thrown");
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.i("Repostitory: ", "Execution Exception thrown");
        }
        return null;

    }

    public Items getItemById(int id){
        try {
            return  new getItemByIDAsyncTask(mitemDao).execute(id).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Profiles getProfileByID(int id){
        try {
            return new getProfilesByIDAsyncTask(mProfileDao).execute(id).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteByItemID(int id){
        new deleteItemAsyncTask(mitemDao).execute(id);
    }

    public void deleteProfile(Profiles profile){ new deleteProfileAsyncTask(mProfileDao).execute(profile);}

    public void updateItem(Items item){
        new updateItemAsyncTask(mitemDao).execute(item);
    }

    public void updateProfile(Profiles profile){ new updateProfileAsyncTask(mProfileDao).execute(profile);}

    private static class insertAsyncTask extends AsyncTask<User, Void, Void>{

        private UserDAO mAysncTaskDao;

        insertAsyncTask(UserDAO dao){
            mAysncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            mAysncTaskDao.insert(users[0]);
            return null;
        }
    }

    private static class insertItemAsyncTask extends AsyncTask<Items, Void, Void>{

        private ItemDAO mAysncTaskDao;

        insertItemAsyncTask(ItemDAO dao){
            mAysncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(Items... items) {
            mAysncTaskDao.insert(items[0]);
            return null;
        }
    }

    private static class insertProfileTask extends AsyncTask<Profiles, Void, Long>{

        private ProfileDAO mAysncTaskDao;

        insertProfileTask(ProfileDAO dao){
            mAysncTaskDao = dao;
        }


        @Override
        protected Long doInBackground(Profiles... profiles) {
            return mAysncTaskDao.insert(profiles[0]);
        }
    }

    private static class deleteItemAsyncTask extends AsyncTask<Integer, Void, Void>{

        private ItemDAO mAysncTaskDao;

        deleteItemAsyncTask(ItemDAO dao){
            mAysncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(Integer... ints) {
            mAysncTaskDao.deleteByItemID(ints[0]);
            return null;
        }
    }

    private static class deleteProfileAsyncTask extends AsyncTask<Profiles, Void, Void>{

        private ProfileDAO mAysncTaskDao;

        deleteProfileAsyncTask(ProfileDAO dao){
            mAysncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(Profiles... profiles) {
            mAysncTaskDao.deleteProfile(profiles[0]);
            return null;
        }
    }

    private static class getUserTask extends AsyncTask<Void, Void, User>{

        private UserDAO mAysncdao;

        getUserTask(UserDAO dao) { mAysncdao = dao; }

        @Override
        protected User doInBackground(Void... voids) {
            if(mAysncdao.countUsers() > 0){
                return mAysncdao.getUser();
            }
            return null;
        }
    }

    private static class getAllItemsAsyncTask extends AsyncTask<Void, Void, List<Items>>{

        private ItemDAO mAysncTaskDao;

        getAllItemsAsyncTask(ItemDAO dao){
            mAysncTaskDao = dao;
        }


        @Override
        protected List<Items> doInBackground(Void... voids) {
            return mAysncTaskDao.getAllItems();
        }
    }

    private static class getProfilesAsyncTask extends android.os.AsyncTask<Void, Void, List<Profiles>> {

        private ProfileDAO mAysncTaskDao;

        getProfilesAsyncTask(ProfileDAO dao){
            mAysncTaskDao = dao;
        }


        @Override
        protected List<Profiles> doInBackground(Void... voids) {
            return mAysncTaskDao.getAllProfiles();
        }
    }

    private static class getProfilesByIDAsyncTask extends android.os.AsyncTask<Integer, Void,Profiles> {

        private ProfileDAO mAysncTaskDao;

        getProfilesByIDAsyncTask(ProfileDAO dao){
            mAysncTaskDao = dao;
        }


        @Override
        protected Profiles doInBackground(Integer... integers) {
            return mAysncTaskDao.getProfileByID(integers[0]);
        }
    }

    private static class getStringProfilesAsyncTask extends android.os.AsyncTask<Void, Void, List<String>> {

        private ProfileDAO mAysncTaskDao;

        getStringProfilesAsyncTask(ProfileDAO dao){
            mAysncTaskDao = dao;
        }


        @Override
        protected List<String> doInBackground(Void... voids) {
            return mAysncTaskDao.getProfilesAsString();
        }
    }

    private static class getItemByIDAsyncTask extends android.os.AsyncTask<Integer, Void, Items> {

        private ItemDAO mAysncTaskDao;

        getItemByIDAsyncTask(ItemDAO dao){
            mAysncTaskDao = dao;
        }


        @Override
        protected Items doInBackground(Integer... ints) {
            return mAysncTaskDao.getItembID(ints[0]);
        }
    }

    private static class getItemWithProfileAsyncTask extends android.os.AsyncTask<String, Void, List<Items>> {

        private ItemDAO mAysncTaskDao;

        getItemWithProfileAsyncTask(ItemDAO dao){
            mAysncTaskDao = dao;
        }


        @Override
        protected List<Items> doInBackground(String... strings) {
            return mAysncTaskDao.getItemsWithProfile(strings[0]);
        }
    }

    private static class updateItemAsyncTask extends AsyncTask<Items, Void, Void>{

        private ItemDAO mAysncTaskDao;

        updateItemAsyncTask(ItemDAO dao){
            mAysncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(Items... items) {
            mAysncTaskDao.updateItem(items[0]);
            return null;
        }
    }

    private static class updateProfileAsyncTask extends AsyncTask<Profiles, Void, Void>{

        private ProfileDAO mAysncTaskDao;

        updateProfileAsyncTask(ProfileDAO dao){
            mAysncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(Profiles... profiles) {
            mAysncTaskDao.updateProfile(profiles[0]);
            return null;
        }
    }




}
