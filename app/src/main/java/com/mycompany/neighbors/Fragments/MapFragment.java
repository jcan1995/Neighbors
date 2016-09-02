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
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
    private ViewPager v;

    private boolean assigned = false;
    private String countryName;
    private String stateName;
    private String cityName;

    private static String mApplicationUserUID;
    private User mApplicationUser;

    private LatLng mApplicationUserCoordinates;
    private static Location currentLocation;
    private Location previousLocation;

    private GoogleMap maps;
    private GoogleApiClient mGoogleApiClient;
    private SupportMapFragment mSupportMapFragment;


    private FloatingActionButton bRefresh;
    private ImageButton mImageButton;


    private boolean permissionIsGranted = false;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 101;

    private ArrayList<User> mUsers = new ArrayList<>();

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
        mLocationRequest.setInterval(5000);

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
        Log.d("MapFragment","inside onCreate");//<-----------8/17

        //////////////////NEW 8/24/////////////////////

//      v = (ViewPager) getActivity().findViewById(R.id.vPager);

        //////////////////NEW 8/24/////////////////////


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

        ///////////NEW 8/24/////////////////
        mImageButton = (ImageButton)v.findViewById(R.id.left_nav);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              ViewPager v = (ViewPager) getActivity().findViewById(R.id.vPager);

                int tab = v.getCurrentItem();
                if(tab > 0){
                    tab--;
                    v.setCurrentItem(tab);
                }else if(tab == 0){
                    v.setCurrentItem(tab);
                }
            }
        });

        ///////////NEW 8/24/////////////////


        bRefresh = (FloatingActionButton)v.findViewById(R.id.bRefresh);
        bRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("FAB","FAB in MapFragment called");
              if(currentLocation != null && mApplicationUser != null) {

                  mUsers.clear();
                  geoCoder();// < ---- DONE
                  sendUserUpdate();// < ---- DONE
                  getUsers();
                 // updateUI();

              }else{
                  Toast toast = Toast.makeText(getActivity(),"Initializing Location...Please wait.", Toast.LENGTH_LONG);
                  toast.show();
              }//TODO: Check if location services are turned on. If not display toast
            }
        });

    }

    private void geoCoder() {
        Log.d("MAPFRAGMENT","inside geoCoder");

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try{

            List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(),currentLocation.getLongitude(),1);

            countryName = (addresses.get(0).getCountryName()).replaceAll("\\s+","");
            cityName = (addresses.get(0).getLocality()).replaceAll("\\s+","");
            stateName = (addresses.get(0).getAdminArea()).replaceAll("\\s+","");

            Log.d("MapFragment","cityName: " + cityName +" stateName: " + stateName + " countryName: " + countryName);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    private void sendUserUpdate() {
        Log.d("MAPFRAGMENT","inside sendUserUpdate");

        final Firebase userRef = new Firebase(FIREBASE_URL);
        final User newUser = new User(mApplicationUser.getEmail(),currentLocation.getLatitude(),currentLocation.getLongitude(),mApplicationUser.getPassword(),mApplicationUser.getUserName());

        userRef.child(countryName).child(stateName).child(cityName).child("currentUsers").child(mApplicationUserUID).setValue(newUser);

    }


    private void getUsers() {
        Log.d("MAPFRAGMENT","getUsers called");

        //  Iterate through users in firebase and store in arraylist
        mUsers.clear();//First clear ArrayList in case it already consists of UserMarkers objects

        Firebase currentUsersRef = new Firebase(FIREBASE_URL + countryName +"/"+ stateName + "/"+ cityName + "/currentUsers");

        Log.d("usersfilepath",FIREBASE_URL + countryName +"/"+ stateName + "/"+ cityName + "/currentUsers");
        currentUsersRef.addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {

                User user = dataSnapshot.getValue(User.class);
                user.setKey(dataSnapshot.getKey());
                mUsers.add(user);// < ---- OR mUsers.add(0, post);
                Log.d("USERARRAYSIZE","In onChildAdded: " + mUsers.size());

                updateUI(mUsers);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        Log.d("USERARRAYSIZE","Size of mUsers in getUsers(): " + mUsers.size());

    }

    private void updateUI(ArrayList<User> users){
        Log.d("MAPFRAGMENT","updateUI called");

        Log.d("USERARRAYSIZE","Size of mUsers in updateUI(): " + users.size());
        maps.clear();

        for(int i = 0; i < users.size(); i++){

            LatLng userLatLng = new LatLng(users.get(i).getLatitude(),users.get(i).getLongitude());
            Log.d("UserDetails","UserName: " + users.get(i));

            MarkerOptions userMarker = new MarkerOptions();
            userMarker.position(userLatLng);
            userMarker.title(users.get(i).getUserName());
            maps.addMarker(userMarker);

        }

        maps.moveCamera(CameraUpdateFactory.newLatLng(mApplicationUserCoordinates));
        maps.animateCamera(CameraUpdateFactory.zoomTo(20));
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

//        if(maps != null){
//
//            maps.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//                @Override
//                public View getInfoWindow(Marker marker) {
//                    return null;
//                }
//
//                @Override
//                public View getInfoContents(Marker marker) {
//
//                    View v = getActivity().getLayoutInflater().inflate(R.layout.info_window,null);
//
//                    TextView tvUserName = (TextView)v.findViewById(R.id.tvuserName);
//                    TextView tvMessage = (TextView)v.findViewById(R.id.tvMessage);
//
//                    tvMessage.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            //TODO: Switch to chat fragment
//
//                            /*
//                            Fragment chatFragment = new ChatFragment();
//                            FragmentManager fragmentManager = getFragmentManager();
//                            fragmentManager.beginTransaction()
//                                    .replace(R.id.fragContainer,chatFragment)
//                                    .addToBackStack(null)
//                                    .commit();
//                                    */
//                        }
//                    });
//
//                    return v;
//                }
//            });
//        }

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

        Log.d("MapFragment","inside onLocationChanged");

        currentLocation = location;

        mApplicationUserCoordinates = null;
        mApplicationUserCoordinates = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

//        if(mApplicationUser != null) {
//            mApplicationUser.setUserLocation(location);//Set the location attribute of the current user to its current location.
//        }


        Log.d("TAG_JOSH","Latitude: " +Double.toString(location.getLatitude()));

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        Log.d("grantResults"," " + grantResults.toString());


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


