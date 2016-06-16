package com.mycompany.neighbors.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mycompany.neighbors.MainActivity;
import com.mycompany.neighbors.R;
import com.mycompany.neighbors.SinglePost;

/**
 * Created by joshua on 5/28/2016.
 */
public class PostFragment extends Fragment {
    private final String FIREBASE_URL = "https://neighboars.firebaseio.com/";
    private EditText etStatus;
    private Button bPost;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_post,parent,false);

        etStatus = (EditText)v.findViewById(R.id.etStatus);
        bPost = (Button)v.findViewById(R.id.bPost);
        bPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String UID = MainActivity.getUID();
                final String status = etStatus.getText().toString();
                Firebase fRef = new Firebase(FIREBASE_URL+"/users/"+ UID+"/userName");//to obtain username

                fRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userName = (String)dataSnapshot.getValue();
                        SinglePost post = new SinglePost(userName,status);

                        Firebase fRoot = new Firebase(FIREBASE_URL);
                        fRoot.child("posts").push().setValue(post);
                        //fRoot.child("posts").setValue(post);

                        Toast toast = Toast.makeText(getActivity(),"userName:" + userName, Toast.LENGTH_LONG);
                        toast.show();


                        }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });

            }
        });




        return v;


    }

}
