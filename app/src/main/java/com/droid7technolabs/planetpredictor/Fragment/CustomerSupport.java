package com.droid7technolabs.planetpredictor.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droid7technolabs.planetpredictor.R;


public class CustomerSupport extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomerSupport() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CustomerSupport newInstance(String param1, String param2) {
        CustomerSupport fragment = new CustomerSupport();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_support, container, false);
    }


    //when user come to this activity user redirect the GMAIL
    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "predictorplanet@gmail.com", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }
}