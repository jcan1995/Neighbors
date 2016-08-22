package com.mycompany.neighbors;
import android.location.Location;

/**
 * Created by joshua on 8/17/2016.
 */
public class UserMarker {

    private String userName;
    private Location userLocation;

    public UserMarker(String userName, Location userLocation){
        this.userName = userName;
        this.userLocation = userLocation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }
}
