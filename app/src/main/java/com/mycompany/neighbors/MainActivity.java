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

    private Adapter mAdapter;
    private ViewPager mViewPager;
    private static FloatingActionButton bButton;
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

        bButton = (FloatingActionButton)findViewById(R.id.bButton);
        bButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int index  = mViewPager.getCurrentItem();
                Adapter adapter = ((Adapter)mViewPager.getAdapter());
                Fragment fragment = adapter.getFragment(index);

                if(fragment instanceof NewsFeedFragment){

                    Log.e("TAG","THIS IS A NEWSFEEDFRAGMENT");
                }
                else if(fragment instanceof MapFragment){
                    Log.e("TAG","THIS IS A MAPFRAGMENT");
                }
                else if(fragment instanceof ProfileFragment){
                    Log.e("TAG","THIS IS A ProfileFragment");

                }

            }

        });
    }





}
