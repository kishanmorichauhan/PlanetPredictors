package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PrizeUpdateActivity extends AppCompatActivity {


    EditText mprize,mlifePrediction;
    Button btn_prize;
    FirebaseFirestore firestore;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prize_update);

        getSupportActionBar().setTitle("Update Prices");

        //Id
        mprize = findViewById(R.id.prize);
        mlifePrediction = findViewById(R.id.lifePrediction);
        btn_prize = findViewById(R.id.btn_prizeUpdate);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        //store normal prize and lifeprediction prize in firestore
        btn_prize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String prize = mprize.getText().toString();
                int amount = Math.round(Float.parseFloat(prize)*100);
                String p = String.valueOf(amount);

                String lifePrediction = mlifePrediction.getText().toString();
                int lifePrediction_Amount = Math.round(Float.parseFloat(lifePrediction)*100);
                String lp = String.valueOf(lifePrediction_Amount);

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DocumentReference documentReference = firestore.collection("Prices").document(uid);
                Map<String,Object> a = new HashMap<>();
                a.put("normal_price",p);
                a.put("lifePrediction", lp);
                documentReference.set(a).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(PrizeUpdateActivity.this, "Price Update", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}