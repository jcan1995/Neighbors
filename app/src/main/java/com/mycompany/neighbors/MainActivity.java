package com.mycompany.neighbors;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mycompany.neighbors.Fragments.MapFragment;
import com.mycompany.neighbors.Fragments.NewsFeedFragment;
import com.mycompany.neighbors.Fragments.ProfileFragment;


public class MainActivity extends FragmentActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final String FIREBASE_URL = "https://neighboars.firebaseio.com/";

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 101;
    private final String TAG = "MyTestApp";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    private Adapter mAdapter;
    private ViewPager mViewPager;
    private boolean permissionIsGranted = false;
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

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        mAdapter = new Adapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.vPager);
        mViewPager.setAdapter(mAdapter);

        intent = getIntent();
        mApplicationUserUID = intent.getStringExtra("uid");


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
        mGoogleApiClient.connect();

    }

    @Override
    protected void onResume(){
        super.onResume();
        if(permissionIsGranted){
            if(mGoogleApiClient.isConnected()){
                requestLocationUpdates();
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (permissionIsGranted) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
        }
    }
    @Override
    protected void onStop() {
        if(permissionIsGranted)
            mGoogleApiClient.disconnect();

        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.d("TAG_JOSH","onConnected");
        requestLocationUpdates();

    }

    private void requestLocationUpdates(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.d("TAG_JOSH","Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("TAG_JOSH","Connection failed");

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("TAG_JOSH",Double.toString(location.getLatitude()));

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permission granted
                    permissionIsGranted = true;
                }else{
                    //Permission denied
                    permissionIsGranted = false;
                    Toast.makeText(getApplicationContext(),"This app requires location permissions",Toast.LENGTH_SHORT).show();


                }
                break;
        }

    }
}
