package com.mycompany.neighbors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.mycompany.neighbors.Fragments.MapFragment;
import com.mycompany.neighbors.Fragments.NewsFeedFragment;
import com.mycompany.neighbors.Fragments.ProfileFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by joshua on 5/25/2016.
 */
public class Adapter extends FragmentPagerAdapter {


        private String Fragment[] = {"Posts" , "Map" , "Profile"};
        public Map<Integer,Fragment> mPageReferenceMap = new HashMap();


    public Adapter(FragmentManager fm){
        super (fm);
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        super.destroyItem(container,position,object);
        mPageReferenceMap.remove(position);


    }


    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                NewsFeedFragment nfFragment = NewsFeedFragment.newInstance(position);
                mPageReferenceMap.put(position, nfFragment);
                return nfFragment;
            case 1:
                MapFragment mapFragment = MapFragment.newInstance(position);
                mPageReferenceMap.put(position, mapFragment);
                return mapFragment;
            case 2:
                ProfileFragment profileFragment = ProfileFragment.newInstance(position);
                mPageReferenceMap.put(position, profileFragment);
                return profileFragment;

            default:
                return null;
        }

    }
        @Override
        public int getCount(){return 3;}//Fragment.length;}

        @Override
        public CharSequence getPageTitle(int position) {
            return Fragment[position];
        }

    public Fragment getFragment(int key){
        return mPageReferenceMap.get(key);
    }

    }

