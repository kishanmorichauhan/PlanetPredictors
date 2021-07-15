package com.example.adminapp.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adminapp.MainActivity;
import com.example.adminapp.R;
import com.example.adminapp.databinding.ActivityUserProfilrBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UserProfilr_Activity extends AppCompatActivity {

    TextView userFullName;
    ImageView imageView;
    ActivityUserProfilrBinding binding;
    FirebaseFirestore firestore;
    ArrayList<UserData> userDataArrayList;
    UserAdapter userAdapter;
    String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfilrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        userDataArrayList = new ArrayList<>();
        userAdapter = new UserAdapter(UserProfilr_Activity.this, userDataArrayList);

        EventDataChangeListner();


        //fetch Data into UserAdapter
        String name = getIntent().getStringExtra("uName");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String country = getIntent().getStringExtra("country");
        String state = getIntent().getStringExtra("state");
        String city = getIntent().getStringExtra("city");
        String profile = getIntent().getStringExtra("uImage");
        String gender = getIntent().getStringExtra("gender");
        deviceId = getIntent().getStringExtra("deviceId");


        //setData into UserProfile Activity
        binding.userFullName.setText("Name: " + name);
        binding.userDate.setText("Birth Date: " + date);
        binding.userTime.setText("Birth Time: " + time);
        binding.userCountry.setText("Country: " + country);
        binding.userState.setText("State: " + state);
        binding.userCity.setText("City: " + city);
        binding.userGender.setText("Gender: " + gender);
        Glide.with(UserProfilr_Activity.this)
                .load(profile)
                .placeholder(R.drawable.profile_image)
                .into(binding.userProfile);

    }
    private void EventDataChangeListner() {

        //deleting User in firestore
        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilr_Activity.this);
                builder.setTitle("Delete User");
                builder.setIcon(R.drawable.ic_baseline_delete_24);
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firestore.collection("User").document(deviceId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull  Task<Void> task) {
                                Intent intent = new Intent(UserProfilr_Activity.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(UserProfilr_Activity.this, "User delete successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                        userAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.show();

            }
        });

    }
}