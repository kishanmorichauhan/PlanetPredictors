package com.droid7technolabs.planetpredictor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    Button payment_btn;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        payment_btn = findViewById(R.id.payment_btn);
        String sAmount = "295";

        int amount = Math.round(Float.parseFloat(sAmount)*100);


        payment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Checkout checkout = new Checkout();
                checkout.setKeyID("rzp_test_4QEnVTWbIy6SPS");
                checkout.setImage(R.drawable.ic_launcher_background);
                JSONObject object = new JSONObject();

                try {
                    //name
                    object.put("name","Planet Predictor");
                    //description
                    object.put("description","Planet Predictor Payment");
                    //theme color
                    object.put("theme.color","#0093DD");
                    //current unit
                    object.put("currency","INR");
                    //amount
                    object.put("amount",amount);
                    //mobile no
                    object.put("prefill.contact","9090909090");
                    //email
                    object.put("prefill.email","predictorplanet@gmail.com");
                    //open razor checkout activity
                    checkout.open(PaymentActivity.this,object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onPaymentSuccess(String s) {
        //dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment ID");
        builder.setMessage(s);
        builder.show();
        userId = auth.getCurrentUser().getUid();
        DocumentReference documentReference = firestore.collection("Payment").document(userId);
        Map<String,Object> payment = new HashMap<>();
        payment.put("paymentId",s);
        documentReference.set(payment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(PaymentActivity.this, "Payment Success !", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}