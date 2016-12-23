package com.mycompany.neighbors.Fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mycompany.neighbors.MainActivity;
import com.mycompany.neighbors.R;

import static android.content.ContentValues.TAG;

/**
 * Created by joshua on 5/25/2016.
 */
public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText etEmail;
    private EditText etPassword;
    private Button bLogin;
    private Button bRegister;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_login,container,false);
        Log.d("MethodCheck","onCreateView/LoginFragment");

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

                mAuth.signInWithEmailAndPassword(Email,Password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        Toast toast = Toast.makeText(getActivity(),"Successfully logged user in",Toast.LENGTH_LONG);
                        toast.show();
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(getActivity(), "Login Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
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


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
