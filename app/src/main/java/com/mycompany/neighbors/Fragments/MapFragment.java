package com.mycompany.neighbors.Fragments;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.mycompany.neighbors.LocationUpdates;
import com.mycompany.neighbors.MainActivity;
import com.mycompany.neighbors.R;
import com.mycompany.neighbors.User;

/**
 * Created by joshua on 5/25/2016.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{

    private final String FIREBASE_URL = "https://neighboars.firebaseio.com/";

    SupportMapFragment mSupportMapFragment;
    private GoogleMap maps;
    private LatLng mLatLng;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private User mApplicationUser;
    private String mApplicationUserUID;


    public static MapFragment newInstance(int index){
        MapFragment mapFragment = new MapFragment();
        Bundle args = new Bundle();
        args.putInt("index",index);
        mapFragment.setArguments(args);
        return mapFragment;

    }

    private void sendLocationUpdates(){

        //LocationListener
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Firebase ref1 = new Firebase(FIREBASE_URL + "/users/" + mApplicationUserUID + "/userName");

                final LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());//assign latlng
                ref1.addValueEventListener(new ValueEventListener() {//Set Value event listener to obtain users userName
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Firebase root = new Firebase(FIREBASE_URL);
                        String userName = (String) dataSnapshot.getValue();//get userName;
                        LocationUpdates mLocationupdate = new LocationUpdates(userName,latLng);
                        root.child("locationUpdates").push().setValue(mLocationupdate);
                        updateUI();//function to receive locatio nupdates and update google map
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });




            }
        };

        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


    }

    public void updateUI(){


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_map,container,false);
        mApplicationUserUID = MainActivity.getUID();

        createMap();
        sendLocationUpdates();

/*//This may be a part of updateUI
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

        return v;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        maps = googleMap;

    }

}
