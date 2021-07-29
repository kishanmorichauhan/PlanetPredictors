package com.droid7technolabs.planetpredictor.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.droid7technolabs.planetpredictor.Navigation2Activity;
import com.droid7technolabs.planetpredictor.R;
import com.droid7technolabs.planetpredictor.databinding.FragmentCustomerSupportBinding;


public class CustomerSupport extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button email_btn;

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
        View v = inflater.inflate(R.layout.fragment_customer_support, container, false);
        email_btn = v.findViewById(R.id.email);


        email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Email intent
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "planetspredictors@gmail.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));

            }
        });
        return v;
    }


    //when user come to this activity user redirect the GMAIL
    @Override
    public void onViewStateRestored(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        //Email intent
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "planetspredictors@gmail.com", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }

}