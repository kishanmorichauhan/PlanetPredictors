package com.droid7technolabs.planetpredictor.UserDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.droid7technolabs.planetpredictor.MainActivity;
import com.droid7technolabs.planetpredictor.PaymentActivity;
import com.droid7technolabs.planetpredictor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;


import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class BirthProfileActivity extends AppCompatActivity {
    private CircleImageView profile_img;
    private EditText Full_name,city, country;
    private TextView date;
    private String D_ate;
    Uri selectedImage;

    private  DatePickerDialog.OnDateSetListener setListener;
    private  String set_time;
    private  TextView Time;
    private int hour,min;

    private FirebaseFirestore fstore;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;


    Button save_details;

    private TextView DEVICE_ID;
    private String str_device_id;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birth_profile);

        mAuth = FirebaseAuth.getInstance();
        fstore =FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();


        profile_img = (CircleImageView)findViewById(R.id.profile_img);
        date = (TextView)findViewById(R.id.date);
        Time= (TextView)findViewById(R.id.time);
        Full_name = (EditText) findViewById(R.id.full_name);
        save_details = (Button) findViewById(R.id.button);
        country = (EditText)findViewById(R.id.country);
        city = (EditText) findViewById(R.id.city);

        //   Device ID Number
        DEVICE_ID = findViewById(R.id.device_id);
        str_device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        // DEVICE_ID.setText("Device ID: "+str_device_id); for showing device id

        // Setting date
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(BirthProfileActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 45);
            }
        });

        Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(BirthProfileActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Time.setText( selectedHour + ":" + selectedMinute);
                        set_time = selectedHour + ":" + selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog( BirthProfileActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth , setListener, year, month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month =month + 1;
                D_ate = day +"/"+month+"/"+year;
                date.setText(D_ate);
            }
        };

        // saving data Button
        save_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signInAnonymously()
                        .addOnCompleteListener(BirthProfileActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInAnonymously:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // User data
                                    //String fullname = Full_name.getText().toString();
                                    //String uid = mAuth.getCurrentUser().getUid();
                                    if (user.isAnonymous()) {
                                        // store data in firestore
                                        /*Map<String,Object> user_details = new HashMap<>();
                                        user_details.put("Full Name :",fullname);
                                        user_details.put("Dob :",D_ate);
                                        user_details.put("Time :",set_time);
                                        user_details.put("Device Id:",str_device_id);*/

                                        if (selectedImage != null) {
                                            StorageReference reference = storage.getReference().child("UserImage").child(mAuth.getUid());
                                            reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            String imageUrl = uri.toString();
                                                            String fullname = Full_name.getText().toString();
                                                            String uid = mAuth.getCurrentUser().getUid();
                                                            String City = city.getText().toString();
                                                            String Country = country.getText().toString();
                                                            UserData userData = new UserData(fullname, D_ate, str_device_id, imageUrl,set_time,City,Country);
                                                            fstore.collection("User").document(uid).set(userData)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Intent main = new Intent(BirthProfileActivity.this, MainActivity.class);
                                                                            startActivity(main);

                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(BirthProfileActivity.this, " Failure:" + e.getMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            });


                                                        }
                                                    });
                                                }
                                            });
                                        }else {
                                            String fullname = Full_name.getText().toString();
                                            String uid = mAuth.getCurrentUser().getUid();
                                            String City = city.getText().toString();
                                            String Country = country.getText().toString();
                                            UserData userData = new UserData(fullname, D_ate, str_device_id, "No Image",set_time,City,Country);
                                            fstore.collection("User").document(uid).set(userData)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(BirthProfileActivity.this, "Profile Updated", Toast.LENGTH_LONG).show();
                                                            Intent main = new Intent(BirthProfileActivity.this, MainActivity.class);
                                                            startActivity(main);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(BirthProfileActivity.this, " Failure:" + e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInAnonymously:failure", task.getException());
                                    Toast.makeText(BirthProfileActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
            }
        });

    } // end of OnCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (data.getData() != null) {
                profile_img.setImageURI(data.getData());
                selectedImage = data.getData();
            }
        }
    }
} // end of AppCompatActivity