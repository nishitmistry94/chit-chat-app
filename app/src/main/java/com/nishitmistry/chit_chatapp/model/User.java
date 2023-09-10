package com.nishitmistry.chit_chatapp.model;


import androidx.annotation.NonNull;

public class User {
    private String name;
    private String email;
    private String password;
    private String uid;
    private String pfplink;

    User(){}

    public User(String name, @NonNull String email, @NonNull String password, @NonNull String uid, String pfplink) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.uid = uid;
        this.pfplink = pfplink;
    }

    public String getName()
    {
        return name;
    }
    public String getUid()
    {
        return uid;
    }
    public void setPfplink(String pfplink){
        this.pfplink = pfplink;
    }

    public String getPfplink() {
        return pfplink;
    }
}
