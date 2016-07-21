package com.mycompany.neighbors.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mycompany.neighbors.MainActivity;
import com.mycompany.neighbors.R;

/**
 * Created by joshua on 5/25/2016.
 */
public class LoginFragment extends Fragment {

    private final String FIREBASE_URL = "https://neighboars.firebaseio.com/";
    private Firebase fRef;

    private EditText etEmail;
    private EditText etPassword;
    private static String mApplicationUID;
    private Button bLogin;
    private Button bRegister;


    public static String getApplicationUID(){
        return mApplicationUID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_login,container,false);
        etEmail = (EditText)v.findViewById(R.id.etEmail);
        etEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_NEXT){
                    etPassword.requestFocus();
                    return true;
                }
                return false;
            }
        });
        etPassword = (EditText)v.findViewById(R.id.etPassword);

        bLogin = (Button)v.findViewById(R.id.bLogin);
        bRegister = (Button)v.findViewById(R.id.bRegister);

        bLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                String Email;
                String Password;
                Email = etEmail.getText().toString();
                Password = etPassword.getText().toString();

                fRef = new Firebase(FIREBASE_URL);//New
                fRef.authWithPassword(Email, Password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {

                        Toast toast = Toast.makeText(getActivity(),"Successfully logged user in",Toast.LENGTH_LONG);
                        toast.show();

                        mApplicationUID = authData.getUid();
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        i.putExtra("uid",authData.getUid());//Send UID to Mainactivity to be used there. Display user's information.
                        startActivity(i);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Toast toast = Toast.makeText(getActivity(),"There was an error",Toast.LENGTH_LONG);
                        toast.show();

                    }
                });
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterFragment regisFrag = new RegisterFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.Container, regisFrag)
                        .commit();
            }
        });


        return v;

    }

}
