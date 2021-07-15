package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.example.adminapp.User.UserAdapter;
import com.example.adminapp.User.UserData;
import com.example.adminapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    ArrayList<UserData> userDataArrayList;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("User Chats");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.recyclerView.setAdapter(userAdapter);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userDataArrayList = new ArrayList<>();
        userAdapter = new UserAdapter(MainActivity.this, userDataArrayList);

        binding.recyclerView.setAdapter(userAdapter);

        EventDataChangeListner();


        //Token for the notification
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

                    //generate FCM token
                    private void GenerateToken(String token) {

                        String userId = auth.getCurrentUser().getUid();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("token", token);

                        //Store Token in firebase firestore
                        firestore.collection("Tokens").document(userId).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });
    }

    private void EventDataChangeListner() {


        //Showing user in recycleView
        firestore.collection("User")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        userDataArrayList.clear();

                        if (error != null) {
                            Log.e("firebase error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) ;

                            userDataArrayList.add(dc.getDocument().toObject(UserData.class));
                        }
                        userAdapter.notifyDataSetChanged();
                    }
                });
    }
}