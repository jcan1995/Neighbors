package com.mycompany.neighbors;


/**
 * Created by joshua on 5/25/2016.
 */


public class User {

    private String UserName;
    private String Email;
    private String Password;
    private Double Latitude;
    private Double Longitude;

   // @JsonIgnore
    private String key;

    public User(){
    }

//    public User(String E, String P, String UN){
//
//        this.Email = E;
//        this.Password = P;
//        this.UserName = UN;
//
//    }

    public User(String E, Double latitude, Double longitude, String P, String UN){

        this.UserName = UN;
        this.Email = E;
        this.Password = P;
        this.Latitude = latitude;
        this.Longitude = longitude;

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

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }
}
