package com.mycompany.neighbors.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.mycompany.neighbors.MainActivity;
import com.mycompany.neighbors.R;
import com.mycompany.neighbors.User;
import com.mycompany.neighbors.utils.Constants;

import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by joshua on 5/25/2016.
 */


public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText etUserName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button bSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_register,container,false);
        initializeScreen(v);

        return v;
    }



    private void initializeScreen(View v) {

        etUserName = (EditText)v.findViewById(R.id.etUserName);
        etEmail = (EditText)v.findViewById(R.id.etEmail);
        etPassword = (EditText)v.findViewById(R.id.etPassword);
        etConfirmPassword = (EditText)v.findViewById(R.id.etConfirmPassword);
        bSubmit = (Button)v.findViewById(R.id.bSubmit);

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerUser();
            }
        });

    }

    public void registerUser(){

        final String UserName = etUserName.getText().toString();
        final String Email = etEmail.getText().toString();
        final String Password = etPassword.getText().toString();
        final String ConfirmPassword = etConfirmPassword.getText().toString();

        //TODO: Check if email/username is already taken.
        //TODO: Check if email is valid "@"
        //TODO: Check if password is valid. (strong)

        //final User user = new User(Email, 0.0, 0.0, Password, UserName);

        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("TAG","createUserWithEmail:onComplete:" + task.isSuccessful());

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(UserName)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                }
                            }
                        });

                Toast toast = Toast.makeText(getActivity(),"Registration Successful!", Toast.LENGTH_LONG);
                toast.show();

                Intent i = new Intent(getActivity(), MainActivity.class);
               // i.putExtra("displayName",UserName);
                startActivity(i);

                if(!task.isSuccessful()){
                    Toast.makeText(getActivity(),"Registration failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    @Override
    public void onStart(){
        super.onStart();
        Log.d("RegisterFragment","Google connected");

    }

    @Override
    public void onStop(){
        super.onStop();

    }

    @Override
    public void onResume(){
        super.onResume();

    }

}
