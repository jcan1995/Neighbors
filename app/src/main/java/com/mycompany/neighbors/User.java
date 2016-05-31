package com.mycompany.neighbors;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by joshua on 5/25/2016.
 */
public class User {


    private String UserName;
    private String Email;
    private String Password;

    @JsonIgnore
    private String key;

    public User(){
    }

    public User(String UN, String E, String P){

        UserName = UN;
        Email = E;
        Password = P;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }



}
