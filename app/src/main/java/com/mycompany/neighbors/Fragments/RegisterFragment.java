package com.mycompany.neighbors.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mycompany.neighbors.R;
import com.mycompany.neighbors.User;
import com.mycompany.neighbors.utils.Constants;

import java.util.Map;

/**
 * Created by joshua on 5/25/2016.
 */


public class RegisterFragment extends Fragment {

    private final String FIREBASE_URL = Constants.FIREBASE_ROOT_URL;
    private Firebase fRef = new Firebase(FIREBASE_URL);

    private EditText etUserName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button bSubmit;



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

             /*  if(!confirmPass(Password, ConfirmPassword)){


               }*/


        //TODO: Check if email/username is already taken.
        //TODO: Check if email is valid "@"
        //TODO: Check if password is valid. (strong)

        final User user = new User(UserName, Email, Password);//new
        fRef.createUser(Email,Password, new Firebase.ValueResultHandler<Map<String, Object>>(){

            @Override
            public void onSuccess(Map<String, Object> result) {

                fRef.child("users").child(result.get("uid").toString()).setValue(user);

                Toast toast = Toast.makeText(getActivity(),"Registration Successful!", Toast.LENGTH_LONG);
                toast.show();

                LoginFragment loginFrag = new LoginFragment();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.Container, loginFrag)
                        .commit();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Toast toast = Toast.makeText(getActivity(),"Sorry. Email is already registered.", Toast.LENGTH_LONG);
                toast.show();
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
