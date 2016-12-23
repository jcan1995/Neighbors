package com.mycompany.neighbors;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;


import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mycompany.neighbors.Fragments.LoginFragment;
import com.mycompany.neighbors.utils.Constants;

/**
 * Created by joshua on 5/25/2016.
 */
public class LoginActivity extends FragmentActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("MethodCheck","onCreate/LoginActivity");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //The user is signed in.
                    Intent i = new Intent(getApplication(), MainActivity.class);
                    startActivity(i);

                    Log.d("LoginActivity","onAuthStateChanged:signed_on");
                }else{
                    Log.d("LoginActivity","onAuthStateChanged:signed_off");

                }
            }
        };

        LoginFragment loginFrag = new LoginFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.Container, loginFrag)
                .commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onRestart(){
        super.onRestart();
    }
}
