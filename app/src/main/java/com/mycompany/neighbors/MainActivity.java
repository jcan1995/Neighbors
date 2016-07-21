package com.mycompany.neighbors;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.mycompany.neighbors.Fragments.MapFragment;
import com.mycompany.neighbors.Fragments.NewsFeedFragment;
import com.mycompany.neighbors.Fragments.ProfileFragment;


public class MainActivity extends FragmentActivity {


    private final String FIREBASE_URL = "https://neighboars.firebaseio.com/";

    private final String TAG = "MyTestApp";

    private Adapter mAdapter;
    private ViewPager mViewPager;
    private static FloatingActionButton bButton;
    private static String mApplicationUserUID;
    private Intent intent;

    public static String getUID() {
        return mApplicationUserUID;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // mLocationRequest = new LocationRequest();


        mAdapter = new Adapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.vPager);
        mViewPager.setAdapter(mAdapter);

        intent = getIntent();
        mApplicationUserUID = intent.getStringExtra("uid");
        // USER_URL = "https://neighboars.firebaseio.com/users/" + mApplicationUserUID;

        bButton = (FloatingActionButton) findViewById(R.id.bButton);
        bButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int index = mViewPager.getCurrentItem();
                Adapter adapter = ((Adapter) mViewPager.getAdapter());
                Fragment fragment = adapter.getFragment(index);

                if (fragment instanceof NewsFeedFragment) {

                    Log.e("TAG", "THIS IS A NEWSFEEDFRAGMENT");
                } else if (fragment instanceof MapFragment) {


                    sendLocationUpdates();

                    //When user clicks, user will send current location update.

                } else if (fragment instanceof ProfileFragment) {
                    Log.e("TAG", "THIS IS A ProfileFragment");

                }

            }

        });
    }

    public void sendLocationUpdates() {
        //TODO: Obtain and send location to firebase
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("OVERRIDDEN_TAG", "onStart called");


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("OVERRIDDEN_TAG", "onResume called");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("OVERRIDDEN_TAG", "onPause called");

    }

    @Override
    protected void onStop() {
        Log.d("OVERRIDDEN_TAG", "onStop called");

        super.onStop();
    }

}
