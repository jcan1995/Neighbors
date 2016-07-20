package com.mycompany.neighbors;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by joshua on 6/9/2016.
 */
public class LocationUpdates {
    public int getLatitude() {
        return Latitude;
    }

    public void setLatitude(int latitude) {
        Latitude = latitude;
    }

    public int getLongitude() {
        return Longitude;
    }

    public void setLongitude(int longitude) {
        Longitude = longitude;
    }

    private int Latitude;
    private int Longitude;


    @JsonIgnore
    private String key;

    public LocationUpdates(){}

    public LocationUpdates(int lat, int lon){
        Latitude = lat;
        Longitude = lon;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
