package com.droid7technolabs.planetpredictor.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.droid7technolabs.planetpredictor.Dialoge;
import com.droid7technolabs.planetpredictor.FcmNotificationsSender;
import com.droid7technolabs.planetpredictor.Messages;
import com.droid7technolabs.planetpredictor.MessagesAdapter;
import com.droid7technolabs.planetpredictor.Notification.Token;
import com.droid7technolabs.planetpredictor.UserDetails.UserData;
import com.droid7technolabs.planetpredictor.databinding.FragmentHomeBinding;
import com.droid7technolabs.planetpredictor.preconfig;
import com.droid7technolabs.planetpredictor.preconfig2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
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
import java.util.HashMap;

public class HomeFragment extends Fragment {

    //This is for Chat
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    int counter = 0;//for razorPay
    int counter1 = 0;//welcome message
    ArrayList<UserData> userDataArrayList;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase database;

    String senderUid;
    String adminid = "RB1AwI7NHZOsg3Y5IePe9yuZabM2";

    ArrayList<Messages> messages;
    MessagesAdapter adapter;

    String senderRoom, receiverRoom;

    String str_device_id;

    public static String rImage;
    String receiverImage;

    Date date = new Date();
    DateFormat df = new SimpleDateFormat("d MMM,h:mm a");
    String date1 = df.format(Calendar.getInstance().getTime());
    Messages wlcMsg = new Messages("Welcome to Planets Predictors!  Thanks for using the application. Now you can ask your question. 1st question will be free.", adminid, date.getTime(), "");
    Messages wlcMsg2 = new Messages("Thank you! One of our astrologers will contact you within 24 hours.", adminid, date.getTime(), "");

    //for notification
    String title = "You have received a message";
    String token;
    boolean notify = false;
    String userIdforToken;

    //for chat message
    String messageTxt;
    String randomKey;


    //welcome message 1
    //when user enter this activity then welcome message show admin side
    @Override
    public void onStart() {
        super.onStart();
        if (counter1 == 0) {
            //User side welcome message
            database.getReference().child("chats")
                    .child(senderRoom)
                    .child("messages")
                    .child(randomKey)
                    .setValue(wlcMsg)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //Admin side welcome message
                            database.getReference().child("chats")
                                    .child(receiverRoom)
                                    .child("messages")
                                    .child(randomKey)
                                    .setValue(wlcMsg)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            //user counter and store that's value
                                            ++counter1;
                                            preconfig2.saveTotalInPref(getContext().getApplicationContext(), counter1);

                                        }
                                    });
                        }
                    });
        }


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //get Device id
        str_device_id = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        //storing counter value
        counter = preconfig.loadTotalFromPref(getActivity());
        counter1 = preconfig2.loadTotalFromPref(getActivity());


        FirebaseMessaging.getInstance().subscribeToTopic("all");

        //getToken
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Token", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        GenerateToken(token);
                    }

                    //Storing Token in Firestore
                    private void GenerateToken(String token) {

                        String userId = firebaseAuth.getCurrentUser().getUid();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("token", token);

                        firebaseFirestore.collection("Tokens").document(userId).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });


        senderUid = FirebaseAuth.getInstance().getUid();

        messages = new ArrayList<>();
        adapter = new MessagesAdapter(getActivity(), messages, senderRoom, receiverRoom);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        binding.recyclerView.setAdapter(adapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        //ID
        senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        adminid = "RB1AwI7NHZOsg3Y5IePe9yuZabM2";

        randomKey = database.getReference().push().getKey();

        //Room
        senderRoom = senderUid + adminid;
        receiverRoom = adminid + senderUid;

        //admin image
        DocumentReference docRef = firebaseFirestore.collection("Admin").document("RB1AwI7NHZOsg3Y5IePe9yuZabM2");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        receiverImage = document.getString("adminImage");
                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                rImage = receiverImage;
            }
        });

        //show message in recyclerview
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

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageTxt = binding.messageBox.getText().toString();

                //checking wether user has type any message
                if (!messageTxt.isEmpty()) {
                    counter++;
                } else {
                    //validation
                    Toast.makeText(getContext(), "Please type your question", Toast.LENGTH_SHORT).show();
                    return;
                }


                //send user notification to admin
                firebaseFirestore.collection("User").document(str_device_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (notify) {
                            sendNotification(adminid, messageTxt);

                        }
                        notify = false;
                    }

                    //sending notification
                    private void sendNotification(String adminid, String messageTxt) {

                        userIdforToken = firebaseAuth.getCurrentUser().getUid();
                        firebaseFirestore.collection("Tokens").document(adminid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                                Token objecttoken = value.toObject(Token.class);
                                token = objecttoken.getToken();

                            }
                        });
                    }
                });

                //messageBox value store in sharePreference
                preconfig.saveTotalInPref(getActivity().getApplicationContext(), counter);
                String randomKey = database.getReference().push().getKey();
                SharedPreferences sp = getActivity().getSharedPreferences("mypref", 0);
                SharedPreferences.Editor ed = sp.edit();
                ed.putString("name", binding.messageBox.getText().toString());
                ed.apply();
                binding.messageBox.setText(binding.messageBox.getText().toString());


                if (counter >= 2) {
                    //open payment dialogBox
                    openDiloge();
                } else {
                    //for normal message
                    messageTxt = binding.messageBox.getText().toString();
                    DateFormat df = new SimpleDateFormat("d MMM,h:mm a");
                    String date1 = df.format(Calendar.getInstance().getTime());
                    Date date = new Date();
                    Messages message = new Messages(messageTxt, senderUid, date.getTime(), date1);
                    notify = true;

                    //message send userSide and adminSide
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
                                                    //sending notification to admin
                                                    FcmNotificationsSender fcmNotificationsSender = new FcmNotificationsSender(token, title, messageTxt, getActivity(), HomeFragment.this.getActivity());
                                                    fcmNotificationsSender.SendNotifications();
                                                    //clear message in messageBox
                                                    binding.messageBox.setText("");
                                                    //clear message in sharePreference
                                                    SharedPreferences preferences = getActivity().getSharedPreferences("mypref", 0);
                                                    SharedPreferences.Editor rm = preferences.edit().clear();
                                                    rm.clear();
                                                    rm.apply();
                                                    preferences.edit().remove("name").commit();

                                                    //for welcome message 2
                                                    String randomKey2 = database.getReference().push().getKey();

                                                        database.getReference().child("chats")
                                                                .child(senderRoom)
                                                                .child("messages")
                                                                .child(randomKey2)
                                                                .setValue(wlcMsg2)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        database.getReference().child("chats")
                                                                                .child(receiverRoom)
                                                                                .child("messages")
                                                                                .child(randomKey2)
                                                                                .setValue(wlcMsg2)
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
                            });

                }

            }
        });
        //setting the message value to sharePreferences
        SharedPreferences sp1 = getActivity().getSharedPreferences("mypref", 0);
        String editvalue = sp1.getString("name", "");
        binding.messageBox.setText(editvalue);


        return root;
    }
//payment dialoge
    public void openDiloge() {
        Dialoge exampleDialog = new Dialoge(binding.sendBtn.getContext());
        exampleDialog.show(getFragmentManager(), "example dialoge");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}