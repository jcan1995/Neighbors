package com.mycompany.neighbors;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        ImageButton imageButton = (ImageButton)toolbar.findViewById(R.id.ibSettings);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast toast = Toast.makeText(getApplicationContext(),"imagebuttonset", Toast.LENGTH_LONG);
//                toast.show();
//            }
//        });


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
                Toast toast = Toast.makeText(getApplicationContext(),"imagebuttonset", Toast.LENGTH_LONG);
                toast.show();
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
