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
import com.mycompany.neighbors.FragmentLifeCycle;
import com.mycompany.neighbors.MainActivity;
import com.mycompany.neighbors.R;
import com.mycompany.neighbors.User;

/**
 * Created by joshua on 5/25/2016.
 */
public class MapFragment extends Fragment implements FragmentLifeCycle,OnMapReadyCallback,LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

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
                final LatLng coordinates = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                //userMarker is Marker for User.
                MarkerOptions userMarker = new MarkerOptions();
                userMarker.position(coordinates);
                userMarker.title("Me");
                maps.addMarker(userMarker);

                //These markers will be the markers from other users.
                getMarkers();
                addMarkersToMap();

                maps.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
                maps.animateCamera(CameraUpdateFactory.zoomTo(20));


                if(currentLocation == previousLocation){
                    return;

                }


               // final Firebase userRef = new Firebase(FIREBASE_URL + "users/" + mApplicationUserUID + "/userName/location");
                final Firebase userRef = new Firebase(FIREBASE_URL + "users/" + mApplicationUserUID);

                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userRef.child("location").setValue(coordinates);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


              this.start();
            }
        }.start();

        return v;

    }

    private void getMarkers() {

        //TODO: Query locations from Firebase and cast into markers. Store markers in ArrayList.
        //In comments, we are requesting all the posts made by users. Adjust to query for locations instead.
/*
  postsRef = new Firebase(POSTS_PATH);
        postsRef.addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
        SinglePost post = dataSnapshot.getValue(SinglePost.class);
        post.setKey(dataSnapshot.getKey());//getKey() probably returns the UID of JSON field
*/

    }

    private void addMarkersToMap() {

        //TODO: With the markers, display them on map.

    }


    ////////////////////////////////////////LIFECYCLE METHODS///////////////////////////////////////////////////////////
    @Override
    public void onStart(){
        super.onStart();
        Log.d("MAPFRAGMENT","onStart called");
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("MAPFRAGMENT","onResume called");

        if(permissionIsGranted){
            if(mGoogleApiClient.isConnected()){
                requestLocationUpdates();

            }
        }
    }

    @Override
    public void onStop(){
        cdt.cancel();
        Log.d("MAPFRAGMENT","onStop called");

        if(permissionIsGranted){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onPause(){
        cdt.cancel();
        Log.d("MAPFRAGMENT","onPause called");

        super.onPause();
    }

    @Override
    public void onDestroy(){

        super.onDestroy();
        Log.d("MAPFRAGMENT","onDestroy called");

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

                            /*
                            Fragment chatFragment = new ChatFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragContainer,chatFragment)
                                    .addToBackStack(null)
                                    .commit();
                                    */
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
        Log.d("TAG_JOSH","Latitude: " +Double.toString(location.getLatitude()));

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

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }


/////////////////////////////////////////OVERRIDE METHODS////////////////////////////////////////////////////////////



}


