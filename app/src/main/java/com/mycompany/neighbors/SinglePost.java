package com.mycompany.neighbors;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by joshua on 5/29/2016.
 */
public class SinglePost {

    private String userName;
    private User user;
    private String status;
    @JsonIgnore
    private String key;


    public SinglePost(){
    }

    public SinglePost(String userName, String status){

        this.userName = userName;
        this.status = status;

    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
