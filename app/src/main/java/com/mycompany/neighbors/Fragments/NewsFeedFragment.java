package com.mycompany.neighbors.Fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mycompany.neighbors.R;
import com.mycompany.neighbors.SinglePost;

import java.util.ArrayList;

/**
 * Created by joshua on 5/25/2016.
 */
public class NewsFeedFragment extends ListFragment implements AdapterView.OnItemClickListener{

    private ListView lv;
    private TextView tvUserName;
    private TextView tvStatus;
    private ArrayList<SinglePost> posts = new ArrayList<>();
    private static final String POSTS_PATH = "https://neighboars.firebaseio.com/posts";
    private Firebase postsRef;

    @Override
    public void onViewCreated(View v, Bundle s){

        lv =  getListView();
        lv.setOnItemClickListener(this);

    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_post_feed_item,parent,false);

        tvUserName = (TextView)v.findViewById(R.id.tvUN);
        tvStatus = (TextView)v.findViewById(R.id.tvStatus);

        postsRef = new Firebase(POSTS_PATH);
        postsRef.addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {

                SinglePost post = dataSnapshot.getValue(SinglePost.class);
                post.setKey(dataSnapshot.getKey());
                posts.add(0, post);

                if(posts.size() > 0) {
                    PostAdapter adapter = new PostAdapter(posts);
                    setListAdapter(adapter);
                }else{
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


        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){

        SinglePost p = ((PostAdapter) getListAdapter()).getItem(position);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private class PostAdapter extends ArrayAdapter<SinglePost>{

       public PostAdapter(ArrayList<SinglePost> singlePost){
           super(getActivity(),android.R.layout.simple_list_item_1,singlePost);
       }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_post_feed_item,null);

            }

            SinglePost p = getItem(position);

            TextView tvUserName = (TextView)convertView.findViewById(R.id.tvUN);
            tvUserName.setText(p.getUserName());

            TextView tvStatus = (TextView)convertView.findViewById(R.id.tvStatus);
            tvStatus.setText(p.getStatus());



            return convertView;
        }

    }
}
