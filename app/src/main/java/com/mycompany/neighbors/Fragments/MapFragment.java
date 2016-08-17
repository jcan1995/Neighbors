package com.mycompany.neighbors.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.mycompany.neighbors.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by joshua on 5/25/2016.
 */
public class MapFragment extends Fragment implements FragmentLifeCycle,OnMapReadyCallback,LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private final String FIREBASE_URL = Constants.FIREBASE_ROOT_URL;

    private String countryName;
    private String stateName;
    private String cityName;

    private static String mApplicationUserUID;
    private User mApplicationUser;

    private LatLng coordinates;
    private static Location currentLocation;
    private Location previousLocation;

    private GoogleMap maps;
    private GoogleApiClient mGoogleApiClient;
    private SupportMapFragment mSupportMapFragment;


    private FloatingActionButton bRefresh;
    private boolean permissionIsGranted = false;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 101;

    private ArrayList<LatLng> neighborLocations = new ArrayList<>();

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
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        mApplicationUserUID = MainActivity.getUID();
        Firebase applicationUserRef = new Firebase(FIREBASE_URL + "users/"+ mApplicationUserUID);
        applicationUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mApplicationUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_map,container,false);

        googleApiBuilder();
        initializeScreen(v);
        createMap();

        return v;

    }

    private void googleApiBuilder() {

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    private void initializeScreen(View v) {

        bRefresh = (FloatingActionButton)v.findViewById(R.id.bRefresh);
        bRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("FAB","FAB in MapFragment called");
                geoCoder();
                sendLocationUpdate();
                getMarkers();
                updateUI();
            }
        });

    }

    private void geoCoder() {

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {

            List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(),currentLocation.getLongitude(),1);
            countryName = addresses.get(0).getCountryName();
            cityName = addresses.get(0).getLocality();
            stateName = addresses.get(0).getAdminArea();

            Log.d("MapFragment","cityName: " + cityName +" stateName: " + stateName + " countryName: " + countryName);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    private void sendLocationUpdate() {

        final Firebase userRef = new Firebase(FIREBASE_URL);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userRef.child(countryName).child(stateName).child(cityName).child(mApplicationUser.getKey()).child("location").setValue(coordinates);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    private void updateUI(){

        coordinates = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        //TODO: Find Users current city using geocoder. Save location to Firebase based on city.

        //userMarker is Marker for User.
        MarkerOptions userMarker = new MarkerOptions();
        userMarker.position(coordinates);
        userMarker.title("Me");
        maps.clear();
        maps.addMarker(userMarker);

        //TODO: Construct for loop to iterate through array filled of LatLngs

        for(int i = 0; i < neighborLocations.size(); i++){

            MarkerOptions neighborMarker = new MarkerOptions();
            neighborMarker.position(neighborLocations.get(i));


        }

//        MarkerOptions userMarker = new MarkerOptions();
//        userMarker.position(coordinates);
//        userMarker.title("Me");
//        maps.addMarker(userMarker);


        maps.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
        maps.animateCamera(CameraUpdateFactory.zoomTo(20));



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

        Log.d("MAPFRAGMENT","onStop called");

        if(permissionIsGranted){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onPause(){

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
        Log.d("MapFragment","onPauseFragment called");
    }

    @Override
    public void onResumeFragment() {
        Log.d("MapFragment","onResumeFragment called");
}

/////////////////////////////////////////OVERRIDE METHODS////////////////////////////////////////////////////////////
}


