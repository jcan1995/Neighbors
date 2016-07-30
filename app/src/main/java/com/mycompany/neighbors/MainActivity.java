package com.mycompany.neighbors;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.firebase.client.Firebase;
import com.mycompany.neighbors.Dialogs.PostDialog;
import com.mycompany.neighbors.Fragments.MapFragment;
import com.mycompany.neighbors.Fragments.NewsFeedFragment;
import com.mycompany.neighbors.Fragments.ProfileFragment;


public class MainActivity extends FragmentActivity {

    private Adapter mAdapter;
    private ViewPager mViewPager;
    private static FloatingActionButton bButton;
    private static String mApplicationUserUID;
    private Intent intent;
    private TabLayout mTabLayout;

    public static String getUID() {
        return mApplicationUserUID;
    }


    private void setFAB(){
        bButton = (FloatingActionButton) findViewById(R.id.bButton);
        bButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int index = mViewPager.getCurrentItem();
                Adapter adapter = ((Adapter) mViewPager.getAdapter());
                Fragment fragment = adapter.getFragment(index);

                if (fragment instanceof NewsFeedFragment) {

                    Log.d("TAG", "THIS IS A NEWSFEEDFRAGMENT");

                    PostDialog post = new PostDialog();
                    post.show(getSupportFragmentManager(),"TAG");

                    /*
                    PostFragment postFragment = new PostFragment();
                    FragmentActivity activity = (FragmentActivity)view.getContext();
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragContainer,postFragment)
                            .addToBackStack(null)
                            .commit();
*/

                } else if (fragment instanceof MapFragment) {

                    Log.d("TAG", "THIS IS A MapFragment");

                    sendLocationUpdates();
                    //When user clicks, user will send current location update.

                } else if (fragment instanceof ProfileFragment) {
                    Log.d("TAG", "THIS IS A ProfileFragment");

                }

            }

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

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
        intent = getIntent();
        mApplicationUserUID = intent.getStringExtra("uid");

        setFAB();
    }

    public void sendLocationUpdates() {
        //TODO: Obtain and send location to firebase
    }


    /////////////////////////////////////////OVERRIDEN METHODS//////////////////////////////////////////////////////////
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MAINACTIVITY", "onStart called");


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
        Log.d("MAINACTIVITY", "onStop called");
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    /////////////////////////////////////////OVERRIDEN METHODS//////////////////////////////////////////////////////////

}
