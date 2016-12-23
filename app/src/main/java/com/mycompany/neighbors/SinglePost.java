package com.mycompany.neighbors;


/**
 * Created by joshua on 5/29/2016.
 */

//@JsonIgnoreProperties(ignoreUnknown = true)
public class SinglePost {

    private String userName;
    private String status;

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
