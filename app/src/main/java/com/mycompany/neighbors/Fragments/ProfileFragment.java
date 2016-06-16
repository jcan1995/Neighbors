package com.mycompany.neighbors.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by joshua on 5/25/2016.
 */
public class ProfileFragment extends Fragment {



    public static ProfileFragment newInstance(int index){
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt("index",index);
        profileFragment.setArguments(args);
        return profileFragment;
    }


}
