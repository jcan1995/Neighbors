package com.mycompany.neighbors.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mycompany.neighbors.FragmentLifeCycle;
import com.mycompany.neighbors.PostsAdapter;
import com.mycompany.neighbors.R;
import com.mycompany.neighbors.SinglePost;

import java.util.ArrayList;

/**
 * Created by joshua on 7/25/2016.
 */
public class NewsFeedFragment extends Fragment implements FragmentLifeCycle{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<SinglePost> posts = new ArrayList<>();

    private static final String POSTS_PATH = "https://neighboars.firebaseio.com/posts";

    public static NewsFeedFragment newInstance(int index){
        NewsFeedFragment nfFragment = new NewsFeedFragment();
        Bundle args = new Bundle();
        args.putInt("index",index);
        nfFragment.setArguments(args);
        return nfFragment;

    }
    @Override
    public void onStart(){
        super.onStart();
        queryPosts();
        Log.d("NFFRAGMENT2","onStart called");

    }

    @Override
    public void onPause(){
        super.onPause();
        posts.clear();
        Log.d("NFFRAGMENT2","onPause called");

    }
    @Override
    public void onResume(){
        super.onPause();
        posts.clear();
        Log.d("NFFRAGMENT2","onResume called");

    }

    @Override
    public void onStop(){
        super.onStop();
        posts.clear();
        Log.d("NFFRAGMENT2","onStop called");

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
       // queryPosts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_news_feed,container,false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
       // queryPosts();
        mAdapter = new PostsAdapter(posts);
        //mRecyclerView.setAdapter(mAdapter);

        return v;


        }


    private void queryPosts() {

        Firebase postsRef = new Firebase(POSTS_PATH);
        postsRef.addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {

                SinglePost post = dataSnapshot.getValue(SinglePost.class);
                post.setKey(dataSnapshot.getKey());//getKey() probably returns the UID of JSON field

                posts.add(0, post);


              if(posts.size() > 0) {
                  mAdapter = new PostsAdapter(posts);
                  mRecyclerView.setAdapter(mAdapter);
              }
//                  setListAdapter(adapter);
//                }else{
//                    Toast toast = Toast.makeText(getActivity(),"No data", Toast.LENGTH_SHORT);
//                    toast.show();
//                }

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

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }
}
