package com.mycompany.neighbors.Fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mycompany.neighbors.R;

/**
 * Created by joshua on 5/25/2016.
 */
public class ChatFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View withPussy = inflater.inflate(R.layout.fragment_chat,container,false);
        //lol....cute
        TextView tvMessageBox = (TextView) withPussy.findViewById(R.id.tvMessageBox);

        return withPussy;
    }
}
