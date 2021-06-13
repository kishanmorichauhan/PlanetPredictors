package com.droid7technolabs.planetpredictor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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
    //EditText email,phoneNo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Checkout.preload(getApplicationContext());
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        payment_btn = findViewById(R.id.payment_btn);
        //email = findViewById(R.id.email);
        //phoneNo = findViewById(R.id.phoneno);
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
                    object.put("prefill.contact", null);
                    //email
                    object.put("prefill.email", null);
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
        //builder.show();
        //String e = email.getText().toString();
        //String p = phoneNo.getText().toString();
        userId = auth.getCurrentUser().getUid();
        DocumentReference documentReference = firestore.collection("Payment").document(userId);
        Map<String,Object> payment = new HashMap<>();
        payment.put("paymentId",s);
        payment.put("userid", userId);
        //payment.put("EmailId",e);
        //payment.put("PhoneNo",p);
        documentReference.set(payment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(PaymentActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(i);
                SharedPreferences preferences = getSharedPreferences("com.example.counter", 0);
                preferences.edit().remove("pref_total_key").commit();


            }
        });

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}