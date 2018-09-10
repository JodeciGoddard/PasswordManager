package com.example.jodeci.passwordmanager;

/**
 * Created by jodeci on 10/11/2017.
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
        cleanseInput();
    }

    private String escapeMetaCharacters(String inputString){
        final String[] metaCharacters = {"\\","\0","\'","\"","*","\b","\n","\r","\t","%","_","{","}"};
        String outputString="";
        for (int i = 0 ; i < metaCharacters.length ; i++){
            if(inputString.contains(metaCharacters[i])){
                outputString = inputString.replace(metaCharacters[i],"\\"+metaCharacters[i]);
                inputString = outputString;
            }
        }
        return inputString;
    }

    private void cleanseInput(){
        _applicationName = escapeMetaCharacters(_applicationName);
        _appUsername = escapeMetaCharacters(_appUsername);
        _appPassword = escapeMetaCharacters(_appPassword);
    }

    //removes the backslashed
    public static String uncleanseInput(String input){
        final String[] unclean = {"\\","\0","\'","\"","*","\b","\n","\r","\t","%","_","{","}"};
        final String[] clean = {"\\\\","\\\0","\\\'","\\\"","\\*","\\\b","\\\n","\\\r","\\\t","\\%","\\_","\\{","\\}"};
        String output = "";
        for (int i = 0; i < clean.length; i++){
            if(input.contains(clean[i])){
                output = input.replace(clean[i], unclean[i]);
                input = output;
            }
        }
        return input;
    }


    public int get_id() {
        return _id;
    }

    public String get_applicationName() {
        return uncleanseInput(_applicationName);
    }

    public String get_appUsername() {
        return uncleanseInput(_appUsername);
    }

    public String get_appPassword() {
        return uncleanseInput(_appPassword);
    }

    public String getUncleanAppName() {return _applicationName;}

    public String getUncleanUsername() {return _appUsername;}

    public String getUncleanPass() {return  _appPassword;}

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
