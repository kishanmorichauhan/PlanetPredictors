package com.droid7technolabs.planetpredictor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.droid7technolabs.planetpredictor.UserDetails.BirthProfileActivity;
import com.droid7technolabs.planetpredictor.databinding.ActivityNavigation3Binding;
import com.droid7technolabs.planetpredictor.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.PaymentResultListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//parent Activity of all the fragment
public class Navigation2Activity extends AppCompatActivity implements PaymentResultListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigation3Binding binding;

    FirebaseAuth auth;
    String userId;
    FirebaseFirestore firestore;
    String lifePrediction;
    String senderUid;
    String senderRoom, receiverRoom;

    TextView tv_Prize;
    String adminid = "RB1AwI7NHZOsg3Y5IePe9yuZabM2";

    Date date = new Date();
    DateFormat df = new SimpleDateFormat("d MMM,h:mm a");
    String date1 = df.format(Calendar.getInstance().getTime());
    FirebaseDatabase database;

    //message for lifePrediction
    Messages paymentMsg2 = new Messages("Payment Received for life prediction", adminid, date.getTime(), "");
    Messages paymentMsg3 = new Messages("We have received your life prediction request! One of our astrologers will soon contact you.", adminid, date.getTime(), "");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        userId = auth.getCurrentUser().getUid();

        //to fetch lifePrediction price
        DocumentReference docRef = firestore.collection("Prices").document("RB1AwI7NHZOsg3Y5IePe9yuZabM2");//admin id
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

        //inbuild Fuction
        binding = ActivityNavigation3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavigation3.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation3);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation3);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_menu,menu);
        return true;
    }

    //for menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int navid=item.getItemId();
        switch (navid){
            case R.id.nav_profile:
                Intent intent = new Intent(Navigation2Activity.this, BirthProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_home:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPaymentSuccess(String s) {
        //dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(Navigation2Activity.this);
//        builder.setTitle("Payment ID");
//        builder.setMessage(s);
        //builder.show();

        //storing paymrnt id in firebase
        userId = auth.getCurrentUser().getUid();
        DocumentReference documentReference = firestore.collection("Payment_lifePrediction").document(userId);
        Map<String,Object> payment = new HashMap<>();
        payment.put("paymentId",s);
        payment.put("userid", userId);


        documentReference.set(payment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent i = new Intent(Navigation2Activity.this, Navigation2Activity.class);
                startActivity(i);
                senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                senderRoom = senderUid + adminid;
                receiverRoom = adminid + senderUid;

                //sending payment messaging to sender and receiver
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(paymentMsg3)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference().child("chats")
                                        .child(receiverRoom)
                                        .child("messages")
                                        .push()
                                        .setValue(paymentMsg2)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(Navigation2Activity.this, "Payment Successful", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }
                        });


            }
        });

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show();
    }
}