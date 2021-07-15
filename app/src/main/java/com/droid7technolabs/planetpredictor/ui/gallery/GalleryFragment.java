package com.droid7technolabs.planetpredictor.ui.gallery;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.droid7technolabs.planetpredictor.R;
import com.droid7technolabs.planetpredictor.databinding.FragmentGalleryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;

import org.json.JSONException;
import org.json.JSONObject;

public class GalleryFragment extends Fragment {

    //This is for lifePrediction

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String userId;
    String lifePrediction;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        //fetch lifePrediction prize in fireStore
        DocumentReference docRef = firestore.collection("Prices").document("RB1AwI7NHZOsg3Y5IePe9yuZabM2");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        lifePrediction = document.getString("lifePrediction");

                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });

        //This is for lifePrediction Razorpay
        binding.lifePredictPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checkout checkout = new Checkout();//inbuid class for razorPay calling
                //razorPay KEY
                checkout.setKeyID("rzp_test_4QEnVTWbIy6SPS");
                checkout.setImage(R.drawable.horoscope);//icon
                JSONObject object = new JSONObject();

                //sending this data to razorPay
                try {
                    //name
                    object.put("name","Planets Predictors");
                    //description
                    object.put("description","Planets Predictors Payment");
                    //theme color
                    object.put("theme.color","#0093DD");
                    //current unit
                    object.put("currency","INR");
                    //amount
                    object.put("amount",lifePrediction);
                    //mobile no
                    object.put("prefill.contact", null);
                    //email
                    object.put("prefill.email", null);
                    //open razor checkout activity
                    checkout.open((Activity) GalleryFragment.this.getContext(), object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return root;
    }


}