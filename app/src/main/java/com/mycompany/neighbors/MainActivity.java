package com.mycompany.neighbors;

import android.*;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.mycompany.neighbors.Dialogs.PostDialog;
import com.mycompany.neighbors.Dialogs.ProfileInitDialog;
import com.mycompany.neighbors.Fragments.LoginFragment;

import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity {

    private String userName;
    private Adapter mAdapter;
    private ViewPager mViewPager;

    private FirebaseUser currentUser;
    private Intent intent;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser.getDisplayName() == null && currentUser.getPhotoUrl() == null){
            //Create dialog fragment to set username and photo
//            PostDialog postDialog = new PostDialog();
//            postDialog.show(getFragmentManager(),"TAG");
            ProfileInitDialog initDialog = new ProfileInitDialog();
            initDialog.show(getFragmentManager(),"TAG");
        }

//        intent = getIntent();
//        if(intent == null){
//
//            Toast toast = Toast.makeText(this,"intent is null",Toast.LENGTH_LONG);
//            toast.show();
//        }
//        userName = getIntent().getStringExtra("displayName");
//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName(userName)//get intent packgage
//                .build();
//
//        currentUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    Log.d(TAG, "User profile updated.");
//                }
//            }
//        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }

        };

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTabLayout = (TabLayout)findViewById(R.id.tab_layout);
        mAdapter = new Adapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.vPager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            int currentPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                FragmentLifeCycle fragmentToShow = (FragmentLifeCycle)mAdapter.getItem(position);
                fragmentToShow.onResumeFragment();

                FragmentLifeCycle fragmentToHide = (FragmentLifeCycle)mAdapter.getItem(currentPosition);
                fragmentToHide.onPauseFragment();

                currentPosition = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_main,menu);
        menu.add(Menu.NONE,R.id.ibSettings,Menu.NONE,"Logout");

        return true;
    }

    @Override
    public  boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.ibSettings:
                Toast toast = Toast.makeText(getApplicationContext(),"Logging out", Toast.LENGTH_SHORT);
                toast.show();
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    /////////////////////////////////////////OVERRIDEN METHODS//////////////////////////////////////////////////////////
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MAINACTIVITY", "onStart called");
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MAINACTIVITY", "onResume called");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MAINACTIVITY", "onPause called");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MAINACTIVITY", "onStop called");

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    /////////////////////////////////////////OVERRIDEN METHODS//////////////////////////////////////////////////////////

}
