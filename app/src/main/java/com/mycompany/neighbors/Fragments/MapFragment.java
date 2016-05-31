package com.mycompany.neighbors.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.mycompany.neighbors.R;

/**
 * Created by joshua on 5/25/2016.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{

    SupportMapFragment mSupportMapFragment;
    private GoogleMap maps;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_map,container,false);

        mSupportMapFragment = SupportMapFragment.newInstance();
        FragmentManager fm = getFragmentManager();
        mSupportMapFragment.getMapAsync(this);
        if(!mSupportMapFragment.isAdded())
            fm.beginTransaction().add(R.id.map_frag,mSupportMapFragment).commit();

        else if(mSupportMapFragment.isAdded())
            fm.beginTransaction().hide(mSupportMapFragment).commit();
        else
            fm.beginTransaction().show(mSupportMapFragment).commit();

        return v;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        maps = googleMap;

    }

}
