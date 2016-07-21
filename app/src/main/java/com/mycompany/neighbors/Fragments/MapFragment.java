package com.mycompany.neighbors.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mycompany.neighbors.MainActivity;
import com.mycompany.neighbors.R;
import com.mycompany.neighbors.User;

/**
 * Created by joshua on 5/25/2016.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;
    private final String FIREBASE_URL = "https://neighboars.firebaseio.com/";
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 101;

    SupportMapFragment mSupportMapFragment;
    private GoogleMap maps;
    private boolean permissionIsGranted = false;

    private LatLng mLatLng;
    private Location currentLocation;
    private Location previousLocation;

    private User mApplicationUser;
    private static String mApplicationUserUID;
    private CountDownTimer cdt;

    public static MapFragment newInstance(int index){
        MapFragment mapFragment = new MapFragment();
        Bundle args = new Bundle();
        args.putInt("index",index);
        mapFragment.setArguments(args);
        return mapFragment;

    }

    private void createMap(){
        mSupportMapFragment = SupportMapFragment.newInstance();
        FragmentManager fm = getFragmentManager();
        mSupportMapFragment.getMapAsync(this);
        if(!mSupportMapFragment.isAdded())
            fm.beginTransaction().add(R.id.map_frag,mSupportMapFragment).commit();

        else if(mSupportMapFragment.isAdded())
            fm.beginTransaction().hide(mSupportMapFragment).commit();
        else
            fm.beginTransaction().show(mSupportMapFragment).commit();

    }

    private void requestLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(60000);

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

/////////////////////////////////////////OVERRIDE METHODS////////////////////////////////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_map,container,false);
        mApplicationUserUID = MainActivity.getUID();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        createMap();

        cdt = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                Log.d("TAG_JOSH", "onFinish");

                // previousLocation = currentLocation;
                final LatLng coordinates = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

                MarkerOptions userMarker = new MarkerOptions();
                userMarker.position(coordinates);
                userMarker.title("Me");
              //  userMarker
                maps.addMarker(userMarker);
                maps.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
                maps.animateCamera(CameraUpdateFactory.zoomTo(20));


                if(currentLocation == previousLocation){
                    return;

                }


                final Firebase userRef = new Firebase(FIREBASE_URL + "users/" + mApplicationUserUID + "/userName/location");

                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userRef.setValue(coordinates);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


              this.start();
            }
        }.start();

       // createMap();
        return v;

    }
////////////////////////////////////////LIFECYCLE METHODS///////////////////////////////////////////////////////////
    @Override
    public void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(permissionIsGranted){
            if(mGoogleApiClient.isConnected()){
                requestLocationUpdates();

            }
        }
    }

    @Override
    public void onStop(){
        cdt.cancel();
        if(permissionIsGranted){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onPause(){
        cdt.cancel();
        super.onPause();
    }
///////////////////////LIFECYCLE METHODS//////////////////////////////////////////////

    @Override
    public void onMapReady(GoogleMap googleMap) {
        maps = googleMap;

        if(maps != null){
            maps.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    View v = getActivity().getLayoutInflater().inflate(R.layout.info_window,null);

                    TextView tvUserName = (TextView)v.findViewById(R.id.tvuserName);
                    TextView tvMessage = (TextView)v.findViewById(R.id.tvMessage);

                    tvMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //TODO: Switch to chat fragment
                        }
                    });

                    return v;
                }
            });
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("TAG_JOSH", "onConnected");

        requestLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.d("TAG_JOSH", "Connection suspended");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.d("TAG_JOSH", "Connection failed");

    }

    @Override
    public void onLocationChanged(final Location location) {

        currentLocation = location;
       // previousLocation = location;
        Log.d("TAG_JOSH","Latitude: " +Double.toString(location.getLatitude()));
        /*
        final LatLng coordinates = new LatLng(location.getLatitude(),location.getLongitude());
        final Firebase userRef = new Firebase(FIREBASE_URL + "users/" + mApplicationUserUID + "/userName/location");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userRef.setValue(coordinates);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch(requestCode){
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permission granted
                    permissionIsGranted = true;

                } else{
                    //Permission denied
                    permissionIsGranted = false;
                    Toast.makeText(getContext(),"This app requires location permissions", Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }


/////////////////////////////////////////OVERRIDE METHODS////////////////////////////////////////////////////////////



}


/*
    public static void sendLocationUpdates(){

        //LocationListener
       LocationListener mLocationListener = new LocationListener() {

           @Override
            public void onLocationChanged(Location location) {
               //Store location updates under a certain user's tab

                //Firebase ref1 = new Firebase(FIREBASE_URL + "/users/" + mApplicationUserUID + "/userName");
               Firebase ref1 = new Firebase(FIREBASE_URL + "/users/" + mApplicationUserUID + "/location");



               final LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());//assign latlng
                ref1.addValueEventListener(new ValueEventListener() {//Set Value event listener to obtain users userName
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Firebase root = new Firebase(FIREBASE_URL);
                        String userName = (String) dataSnapshot.getValue();//get userName;
                        LocationUpdates mLocationupdate = new LocationUpdates(userName,latLng);
                        root.child("locationUpdates").push().setValue(mLocationupdate);
                        //updateUI();//function to receive locatio nupdates and update google map
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });




            }
        };

        LocationManager mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


    }

        //sendLocationUpdates();

/*
//This may be a part of updateUI

        mLocationListener = new LocationListener() {
@Override
public void onLocationChanged(Location location) {
        if(location != null && maps != null){

        maps.clear();
        mLatLng = new LatLng(location.getLatitude(),location.getLongitude());
        //ReceiveUpdates();

        //getLatLng
        //Make new LocationUpdate object...
        //Post LocationUpdate object to firebase

        MarkerOptions mp1 = new MarkerOptions();
        mp1.position(mLatLng);
        // mp1.title();
        maps.addMarker(mp1);
        maps.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));
        maps.animateCamera(CameraUpdateFactory.zoomTo(20));
        }

        }
        };*/

