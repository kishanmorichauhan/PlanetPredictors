package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adminapp.Notification.Token;
import com.example.adminapp.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase database;
    String senderUid;
    String receiverUid;
    ArrayList<Messages> messages;
    MessagesAdapter adapter;
    private String str_device_id;
    private  String adminid;
    String title="You have received a message";
    String token;
    boolean notify = false;
    String userIdforToken;
    String senderRoom, receiverRoom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("User Chats");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseMessaging.getInstance().subscribeToTopic("all");


        str_device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        messages = new ArrayList<>();
        adapter = new MessagesAdapter(this, messages, senderRoom, receiverRoom);
        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView2.setAdapter(adapter);

        //get user name and user image
        String name = getIntent().getStringExtra("name");
        String profile = getIntent().getStringExtra("image");

        //set user name and user image
        binding.name.setText(name);
        Glide.with(ChatActivity.this)
                .load(profile)
                .placeholder(R.drawable.profile_image)
                .into(binding.profile);


        //Instance
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        // fetch user uid from the UserAdapter
        receiverUid = getIntent().getStringExtra("uid");
        senderUid = "RB1AwI7NHZOsg3Y5IePe9yuZabM2";

        //Create sender and receiver room for the chat conversation
        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;


        //Database reference
        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
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
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        //send message in realtime database
        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageTxt = binding.messageBox.getText().toString();
                // Date dateFormat = Calendar.getInstance().getTime();
                DateFormat df = new SimpleDateFormat("d MMM,h:mm a");
                String date1 = df.format(Calendar.getInstance().getTime());
                Date date = new Date();
                Messages message = new Messages(messageTxt, senderUid, date.getTime(), date1);
                binding.messageBox.setText("");

                notify = true;

                //For the Notification
                firebaseFirestore.collection("Admin").document(senderUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(notify){
                            sendNotification(receiverUid,messageTxt);

                        }
                        notify = false;
                    }

                    //send Notification to user app
                    private void sendNotification(String receiverUid, String messageTxt) {

                        userIdforToken = firebaseAuth.getCurrentUser().getUid();
                        firebaseFirestore.collection("Tokens").document(receiverUid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable  DocumentSnapshot value, @Nullable  FirebaseFirestoreException error) {

                                Token objecttoken = value.toObject(Token.class);
                                token = objecttoken.getToken();

                            }
                        });
                    }
                });

                if(!messageTxt.isEmpty()){
                    FcmNotificationsSender fcmNotificationsSender= new FcmNotificationsSender(token, title, messageTxt, getApplicationContext(), ChatActivity.this);
                    fcmNotificationsSender.SendNotifications();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Enter message", Toast.LENGTH_SHORT).show();
                    return;
                }


                String randomKey = database.getReference().push().getKey();
                database.getReference().child("chats").child(senderRoom);
                database.getReference().child("chats").child(receiverRoom);


                //store message in senderRoom and receiverRoom
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .child(randomKey)
                        .setValue(message)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference().child("chats")
                                        .child(receiverRoom)
                                        .child("messages")
                                        .child(randomKey)
                                        .setValue(message)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }
                        });
            }

        });

    }

}
