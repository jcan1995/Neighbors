package com.mycompany.neighbors.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mycompany.neighbors.MainActivity;
import com.mycompany.neighbors.R;
import com.mycompany.neighbors.SinglePost;

/**
 * Created by joshua on 7/28/2016.
 */
public class PostDialog extends DialogFragment {

    private final String FIREBASE_URL = "https://neighboars.firebaseio.com/";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.dialog_post,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Say something!");
        builder.setView(view);

        final EditText Post = (EditText) view.findViewById(R.id.etPost);
     //   final EditText Post2 = (EditText) builder.getView().

        builder.setPositiveButton(R.string.Post, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        String UID = MainActivity.getUID();
                        final String status = Post.getText().toString();

                        Firebase fRef = new Firebase(FIREBASE_URL+"/users/"+ UID+"/userName");//to obtain username

                        fRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String userName = (String)dataSnapshot.getValue();
                                SinglePost newPost = new SinglePost(userName,status);

                                Firebase fRoot = new Firebase(FIREBASE_URL);
                                fRoot.child("posts").push().setValue(newPost);

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                            }
                        });

                    }
                });



               builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();
    }

}
