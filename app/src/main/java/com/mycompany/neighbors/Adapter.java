package com.mycompany.neighbors;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mycompany.neighbors.Fragments.MapFragment;
import com.mycompany.neighbors.Fragments.NewsFeedFragment;
import com.mycompany.neighbors.Fragments.ProfileFragment;

/**
 * Created by joshua on 5/25/2016.
 */
public class Adapter extends FragmentPagerAdapter {


        private String Fragment[] = {"Posts" , "Map" , "Profile"};
        public Adapter(FragmentManager fm){
            super (fm);
        }


        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return new NewsFeedFragment();
                case 1:
                    return new MapFragment();
                case 2:
                    return new ProfileFragment();

                default:
                    return null;
            }

        }

        @Override
        public int getCount(){return Fragment.length;}

        @Override
        public CharSequence getPageTitle(int position) {
            return Fragment[position];
        }

    }

