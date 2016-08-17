package com.mycompany.neighbors;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;


public class MainActivity extends FragmentActivity {

    private Adapter mAdapter;
    private ViewPager mViewPager;

    private static String mApplicationUserUID;
    private Intent intent;
    private TabLayout mTabLayout;


    public static String getUID() {
        return mApplicationUserUID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
