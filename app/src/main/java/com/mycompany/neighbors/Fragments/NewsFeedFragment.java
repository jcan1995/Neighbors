package com.mycompany.neighbors.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycompany.neighbors.R;

/**
 * Created by joshua on 5/25/2016.
 */
public class NewsFeedFragment extends ListFragment {

    private FloatingActionButton bPost;

    public void postFragment(){

        PostFragment postFragment = new PostFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragContainer,postFragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_post_feed,parent,false);

        bPost = (FloatingActionButton)v.findViewById(R.id.bPost);
        bPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //The following should open up a post fragment
                postFragment();
            }
        });

        //TODO: GET POSTS FROM FIREBASE / TIE POSTS WITH LISTFRAGMENT

        return v;


    }


}
