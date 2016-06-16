package com.mycompany.neighbors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by joshua on 6/9/2016.
 */
public class LocationUpdates {

    private String mUserName;
    private LatLng mLatLng;

    @JsonIgnore
    private String key;

    public LocationUpdates(){}

    public LocationUpdates(String userName, LatLng latLng){

        mUserName = userName;
        mLatLng = latLng;
    }


    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }



    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }
}
