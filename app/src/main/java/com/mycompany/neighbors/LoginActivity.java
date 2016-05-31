package com.mycompany.neighbors;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.mycompany.neighbors.Fragments.LoginFragment;

/**
 * Created by joshua on 5/25/2016.
 */
public class LoginActivity extends FragmentActivity {
    private final String FIREBASE_URL = "https://neighboars.firebaseio.com/";


    @Override
    public void onStart(){
        super.onStart();
       // Firebase.setAndroidContext(this);

    }

    public boolean isExpired(AuthData authData){
        return (System.currentTimeMillis() / 1000) >= authData.getExpires();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);

        ////////////////////NEW////////////////////////////////////////
        Firebase firebase = new Firebase(FIREBASE_URL);
        if(firebase.getAuth() == null || isExpired(firebase.getAuth())){
            LoginFragment loginFrag = new LoginFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.Container, loginFrag)
                    .commit();

        }else{
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("uid",firebase.getAuth().getUid());

            // i.putExtra("userURL", FIREBASE_URL + "/users" + firebase.getAuth().getUid());
            startActivity(i);

        }
////////////////////NEW////////////////////////////////////////
        if(findViewById(R.id.Container) != null) {
            // if we are being restored from a previous state, then we dont need to do anything and should
            // return or else we could end up with overlapping fragments.
            if (savedInstanceState != null)
                return;

            LoginFragment loginFrag = new LoginFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.Container, loginFrag)
                    .commit();
        }


    }


}
