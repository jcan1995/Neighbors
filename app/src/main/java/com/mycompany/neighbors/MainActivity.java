package com.mycompany.neighbors;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;


public class MainActivity extends FragmentActivity {

    private Adapter mAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton bPost;
    private static String UID;
     private Intent intent;

    public static String getUID(){
        return UID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new Adapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.vPager);
        mViewPager.setAdapter(mAdapter);

        intent = getIntent();
        UID = intent.getStringExtra("uid");

        Log.d("UID:", UID);
    }



}
