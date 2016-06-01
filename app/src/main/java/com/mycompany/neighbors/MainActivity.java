package com.mycompany.neighbors;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.mycompany.neighbors.Fragments.PostFragment;


public class MainActivity extends FragmentActivity {

    private Adapter mAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton bPost;
    private static String UID;

    private Intent intent;
    public static String getUID(){
        return UID;
    }

    public void postFragment(){

        PostFragment postFragment = new PostFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragContainer,postFragment)
                .addToBackStack(null)
                .commit();
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

        bPost = (FloatingActionButton)findViewById(R.id.bPost);
        bPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //The following should open up a post fragment
                postFragment();
            }
        });
        Log.d("UID:", UID);
    }



}
