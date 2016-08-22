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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mycompany.neighbors.Dialogs.PostDialog;
import com.mycompany.neighbors.FragmentLifeCycle;
import com.mycompany.neighbors.PostsAdapter;
import com.mycompany.neighbors.R;
import com.mycompany.neighbors.SinglePost;
import com.mycompany.neighbors.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by joshua on 7/25/2016.
 */
public class NewsFeedFragment extends Fragment implements FragmentLifeCycle,LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean permissionIsGranted = false;

    private ArrayList<SinglePost> posts = new ArrayList<>();
    private FloatingActionButton bPost;
    private final String FIREBASE_URL = Constants.FIREBASE_ROOT_URL;

    private GoogleApiClient mGoogleApiClient;
    private static Location currentLocation;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 101;

    private String countryName;
    private String stateName;
    private String cityName;

    public static NewsFeedFragment newInstance(int index) {
        NewsFeedFragment nfFragment = new NewsFeedFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        nfFragment.setArguments(args);
        return nfFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_news_feed,container,false);

        googleApiBuilder();
        initializeScreen(v);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        return v;

    }

    private void googleApiBuilder() {

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    private void geoCoder() {

        Log.d("NewsFeedFragment","Inside geoCoder");

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {

            List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(),currentLocation.getLongitude(),1);

            countryName = (addresses.get(0).getCountryName()).replaceAll("\\s+","");
            cityName = (addresses.get(0).getLocality()).replaceAll("\\s+","");
            stateName = (addresses.get(0).getAdminArea()).replaceAll("\\s+","");

            Log.d("MapFragment","cityName: " + cityName +" stateName: " + stateName + " countryName: " + countryName);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void queryPosts() {

        Log.d("NewsFeedFragment","Inside queryPosts");

        posts.clear();

        Firebase postsRef = new Firebase(FIREBASE_URL + countryName +"/"+ stateName + "/"+ cityName + "/posts");
        postsRef.addChildEventListener(new com.firebase.client.ChildEventListener() {

            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {

                SinglePost post = dataSnapshot.getValue(SinglePost.class);
                post.setKey(dataSnapshot.getKey());//getKey() probably returns the UID of JSON field

                posts.add(0, post);

                if(posts.size() > 0) {
                   mAdapter = new PostsAdapter(posts);
                   mRecyclerView.setAdapter(mAdapter);
                   } else{
                          Toast toast = Toast.makeText(getActivity(),"No data", Toast.LENGTH_SHORT);
                          toast.show();
                   }
            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void initializeScreen(View v){

        bPost = (FloatingActionButton)v.findViewById(R.id.bPost);
        bPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PostDialog postDialog = new PostDialog();
                postDialog.show(getFragmentManager(),"TAG");

                Log.d("FAB","FAB In NewsFeedFragment clicked");

            }
        });
    }

    @Override
    public void onStart(){

        super.onStart();
        Log.d("NewsFeedFragment","onStart called");

        mGoogleApiClient.connect();
    }

    @Override
    public void onPause(){

        super.onPause();
        Log.d("NewsFeedFragment","onPause called");

    }
    @Override
    public void onResume(){

        super.onPause();
        Log.d("NewsFeedFragment","onResume called");

    }

    @Override
    public void onStop(){

        if(permissionIsGranted){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
            mGoogleApiClient.disconnect();
        }

        super.onStop();
        Log.d("NewsFeedFragment","onStop called");

    }

    @Override
    public void onPauseFragment() {

        Log.d("NewsFeedFragment","onPauseFragment called");

    }


    @Override
    public void onResumeFragment() {

        Log.d("NewsFeedFragment","onResumeFragment called");

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

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("NewsFeedFragment", "inside onLocationChanged");
        currentLocation = location;

            geoCoder();

        if(posts.size() == 0) {
            queryPosts();
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

}
