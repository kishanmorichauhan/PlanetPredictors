package com.droid7technolabs.planetpredictor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.droid7technolabs.planetpredictor.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    int counter = 0;
    String sAmount = "295";
    int amount = Math.round(Float.parseFloat(sAmount)*100);

    //TabLayout tabLayout;
    //TabItem mchat,mcall,mstatus;
    //ViewPager viewPager;
    //PagerAdapter pagerAdapter;
    androidx.appcompat.widget.Toolbar mtoolbar;


    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase database;
    String senderUid;
    ArrayList<Messages> messages;
    MessagesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        counter = preconfig.loadTotalFromPref(this);


        senderUid = FirebaseAuth.getInstance().getUid();

        messages = new ArrayList<>();
        adapter  = new MessagesAdapter(this,messages);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);


        /*tabLayout=findViewById(R.id.include);
        mchat=findViewById(R.id.chat);
        mcall=findViewById(R.id.calls);
        mstatus=findViewById(R.id.status);
        viewPager=findViewById(R.id.fragmentcontainer);*/

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        mtoolbar=findViewById(R.id.toolbar);
        //setSupportActionBar(mtoolbar);

        database.getReference().child("chats")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Messages message = snapshot1.getValue(Messages.class);
                            message.setMessageId(snapshot1.getKey());
                            messages.add(message);
                        }
                            adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
               preconfig.saveTotalInPref(getApplicationContext(),counter);
                if (counter >= 2) {
                    openDiloge();
                    binding.sendBtn.setEnabled(false);
                }else {
                    String messageTxt = binding.messageBox.getText().toString();
                    // Date dateFormat = Calendar.getInstance().getTime();
                    DateFormat df = new SimpleDateFormat("h:mm a");
                    String date1 = df.format(Calendar.getInstance().getTime());
                    Date date = new Date();
                    Messages message = new Messages(messageTxt, senderUid, date.getTime(), date1);
                    binding.messageBox.setText("");

                    database.getReference().child("chats")
                            .push()
                            .setValue(message);
                }
            }
        });
    }

    public void openDiloge(){
        Dialoge  exampleDialog = new Dialoge(binding.sendBtn.getContext());
        exampleDialog.show(getSupportFragmentManager(),"example dialoge");
    }
    @Override
    protected void onStop() {
        super.onStop();
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.update("status","Offline").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Now User is Offline",Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.update("status","Online").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Now User is Online",Toast.LENGTH_SHORT).show();
            }
        });

    }
}