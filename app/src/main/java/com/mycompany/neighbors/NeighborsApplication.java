package com.mycompany.neighbors;

import android.*;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.firebase.FirebaseApp;


/**
 * Created by joshua on 8/4/2016.
 */
public class NeighborsApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("MethodCheck","onCreate/Neighbors onCreate");
        FirebaseApp.initializeApp(this);

    }


}
