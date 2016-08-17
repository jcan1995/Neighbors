package com.mycompany.neighbors;

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
