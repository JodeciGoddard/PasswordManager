package com.example.jodeci.passwordmanager;

public class Users {
    private int _id;
    private String _username;
    private String _password;

    public Users(){}

    public Users(String user, String password){
        this._username = user;
        this._password = password;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public int get_id() {
        return _id;
    }

    public String get_username() {
        return _username;
    }

    public String get_password() {
        return _password;
    }
}
