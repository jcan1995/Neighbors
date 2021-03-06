package com.mycompany.neighbors.Dialogs;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mycompany.neighbors.MainActivity;
import com.mycompany.neighbors.R;
import com.mycompany.neighbors.SinglePost;
import com.mycompany.neighbors.utils.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by joshua on 7/28/2016.
 */

//TODO:8/5 Find way to post news statuses under correct cityname according to user.
public class PostDialog extends DialogFragment implements LocationListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener  {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private GoogleApiClient mGoogleApiClient;
    private boolean permissionIsGranted = false;
    private Location usersLocation;

    private String countryName;
    private String stateName;
    private String cityName;

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 101;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.dialog_post,null);

        googleApiBuilder();
        database = FirebaseDatabase.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Say something!");
        builder.setView(view);

        final EditText Post = (EditText) view.findViewById(R.id.etPost);

        builder.setPositiveButton(R.string.Post, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final String status = Post.getText().toString();
                        SinglePost post = new SinglePost("someName",status);
                        myRef = database.getReference(countryName).child(stateName).child(cityName).child("posts");
                        //myRef = database.getReference(countryName + stateName + cityName + "posts");
                        myRef.push().setValue(post);




//                        Firebase ref = new Firebase(FIREBASE_URL+ "users/"+ UID+"/userName");
//                       ref.addValueEventListener(new ValueEventListener() {
//                           @Override
//                           public void onDataChange(DataSnapshot dataSnapshot) {
//                               String userName = (String)dataSnapshot.getValue();
//                               SinglePost post = new SinglePost(userName,status);
//
//                               Firebase fRoot = new Firebase(FIREBASE_URL);
//                               fRoot.child(countryName).child(stateName).child(cityName).child("posts").push().setValue(post);
//                           }
//
//                           @Override
//                           public void onCancelled(FirebaseError firebaseError) {
//
//                           }
//                       }) ;
                    }
                });



               builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();
    }

    private void googleApiBuilder() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("PostDialog","Connected to google api");
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(60000);

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        usersLocation = location;
        Log.d("PostDialog","Latitude:"+location.getLatitude());
        Log.d("PostDialog","Longitude:"+location.getLongitude());

        if(countryName == null && stateName == null && cityName == null) {
            geoCoder();
        }
    }

    private void geoCoder() {

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(usersLocation.getLatitude(),usersLocation.getLongitude(),1);
            countryName = (addresses.get(0).getCountryName()).replaceAll("\\s+","");
            cityName = (addresses.get(0).getLocality()).replaceAll("\\s+","");
            stateName = (addresses.get(0).getAdminArea()).replaceAll("\\s+","");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
    public void onStart(){
        super.onStart();
        Log.d("PostDialog","onStart called");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop(){

        if(permissionIsGranted){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("PostDialog","onResume called");

        if(permissionIsGranted){
            if(mGoogleApiClient.isConnected()){

                requestLocationUpdates();
            }
        }
    }

    @Override
    public void onPause(){
        Log.d("PostDialog","onPause called");

        super.onPause();
    }
}
