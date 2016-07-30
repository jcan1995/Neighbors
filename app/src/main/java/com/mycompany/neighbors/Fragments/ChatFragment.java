package com.mycompany.neighbors.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycompany.neighbors.R;

/**
 * Created by joshua on 5/25/2016.
 */
public class ChatFragment extends Fragment {


    public ChatFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_post,parent,false);





        return v;
    }



}
