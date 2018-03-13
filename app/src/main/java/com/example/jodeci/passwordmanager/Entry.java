package com.example.jodeci.passwordmanager;

/**
 * Created by jodec on 10/11/2017.
 */

public class Entry {
    private int _id;
    private String _applicationName;
    private String _appUsername;
    private String _appPassword;

    public Entry(){}

    public Entry(String applicationName, String appUsername, String appPassword) {
        this._applicationName = applicationName;
        this._appUsername = appUsername;
        this._appPassword = appPassword;
    }

    private String escapeMetaCharacters(String inputString){
        final String[] metaCharacters = {"\\","\0","\'","\"","*","\b","\n","\r","\t","%","_"};
        String outputString="";
        for (int i = 0 ; i < metaCharacters.length ; i++){
            if(inputString.contains(metaCharacters[i])){
                outputString = inputString.replace(metaCharacters[i],"\\"+metaCharacters[i]);
                inputString = outputString;
            }
        }
        return inputString;
    }

    public void cleanseInput(){
        _applicationName = escapeMetaCharacters(_applicationName);
        _appUsername = escapeMetaCharacters(_appUsername);
        _appPassword = escapeMetaCharacters(_appPassword);
    }

    public int get_id() {
        return _id;
    }

    public String get_applicationName() {
        return _applicationName;
    }

    public String get_appUsername() {
        return _appUsername;
    }

    public String get_appPassword() {
        return _appPassword;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_applicationName(String _applicationName) {
        this._applicationName = _applicationName;
    }

    public void set_appUsername(String _appUsername) {
        this._appUsername = _appUsername;
    }

    public void set_appPassword(String _appPassword) {
        this._appPassword = _appPassword;
    }
}
