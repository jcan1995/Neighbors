package com.mycompany.neighbors;

import android.*;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.firebase.client.Firebase;

/**
 * Created by joshua on 8/4/2016.
 */
public class NeighborsApplication extends android.app.Application {

    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);


    }


}
