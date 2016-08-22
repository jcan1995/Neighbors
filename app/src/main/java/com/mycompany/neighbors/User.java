package com.mycompany.neighbors;

import android.location.Location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by joshua on 5/25/2016.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class User {

    private String UserName;
    private String Email;
    private String Password;
    private Location UserLocation;

    @JsonIgnore
    private String key;

    public User(){
    }

    public User(String userName){

        this.UserName = userName;

    }

    public User(String userName, String Email){
        this.UserName = userName;
        this.Email = Email;

    }
    public User(String UN, String E, String P){

        this.UserName = UN;
        this.Email = E;
        this.Password = P;
        this.UserLocation = null;
    }

    public User(String UN, String E, String P, Location location){

        this.UserName = UN;
        this.Email = E;
        this.Password = P;
        this.UserLocation = location;
    }


    public Location getUserLocation() {
        return UserLocation;
    }

    public void setUserLocation(Location userLocation) {
        UserLocation = userLocation;
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
