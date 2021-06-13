package com.example.adminapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AdminDetails extends AppCompatActivity {

    private ImageView profile_img;
    private EditText admin_name;
    private Button done;
    Uri selectedImage;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    FirebaseStorage storage;

    String userId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_details);

        profile_img = findViewById(R.id.profile_img);
        admin_name = findViewById(R.id.admin_name);
        done = findViewById(R.id.done);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        userId = mAuth.getCurrentUser().getUid();

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 45);
            }
        });






        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedImage!=null){
                    StorageReference reference = storage.getReference().child("AdminImage").child(mAuth.getUid());
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();

                                    String uid = mAuth.getUid();
                                    String name = admin_name.getText().toString();

                                    AdminData adminData = new AdminData(imageUrl,name);
                                    firestore.collection("Admin").document(uid).set(adminData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(AdminDetails.this, "Profile Updated", Toast.LENGTH_LONG).show();
                                                    Intent main = new Intent(AdminDetails.this, MainActivity.class);
                                                    startActivity(main);

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AdminDetails.this, " Failure:" + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                            });
                        }
                    });

                }else {
                    String name = admin_name.getText().toString();
                    String uid = mAuth.getCurrentUser().getUid();


                    AdminData adminData = new AdminData("No Image",name);
                    firestore.collection("Admin").document(uid).set(adminData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AdminDetails.this, "Profile Updated", Toast.LENGTH_LONG).show();
                                    Intent main = new Intent(AdminDetails.this, MainActivity.class);
                                    startActivity(main);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminDetails.this, " Failure:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }
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

}
