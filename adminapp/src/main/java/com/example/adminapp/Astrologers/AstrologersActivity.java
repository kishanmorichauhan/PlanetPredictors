package com.example.adminapp.Astrologers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adminapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AstrologersActivity extends AppCompatActivity {

    private EditText mName, mSpecialization, mRating;
    private ImageView mAstrologer_img;
    private Button mCreate;
    Uri selectedImage;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    String id = UUID.randomUUID().toString();
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astrologers);
        getSupportActionBar().setTitle("Add astrologer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Instance
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        //ID
        mName = findViewById(R.id.name_astrologer);
        mSpecialization = findViewById(R.id.specialisation);
        mRating =  findViewById(R.id.rating);
        mAstrologer_img = findViewById(R.id.astrologer_img);
        mCreate = findViewById(R.id.create);

        //open gallery
        mAstrologer_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 45);
            }
        });

        //Store all the data in firebase firestore
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if  image is selected

                if(selectedImage!=null){
                    StorageReference reference = storage.getReference().child("Astrologer_Image").child(id);
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull  Task<UploadTask.TaskSnapshot> task) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    //String randomKey = firestore.document("").getId();
                                    String name = mName.getText().toString();
                                    String rating = mRating.getText().toString();
                                    String specification = mSpecialization.getText().toString();

                                    Astrologer astrologer = new Astrologer(name , imageUrl , rating ,specification);
                                    firestore.collection("Astrologers").document(id).set(astrologer)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(AstrologersActivity.this, "Profile Updated", Toast.LENGTH_LONG).show();
                                                    Intent main = new Intent(AstrologersActivity.this, AstrologerDetail.class);
                                                    startActivity(main);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull  Exception e) {
                                            Toast.makeText(AstrologersActivity.this, " Failure:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    });
                    //image is not selected
                }else {
                    String name = mName.getText().toString();
                    String specification = mSpecialization.getText().toString();
                    String rating = mRating.getText().toString();

                    Astrologer astrologer = new Astrologer(name , "No image" , rating ,specification);
                    firestore.collection("Astrologers").document(id).set(astrologer)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AstrologersActivity.this, "Profile Updated", Toast.LENGTH_LONG).show();
                                    Intent main = new Intent(AstrologersActivity.this, AstrologerDetail.class);
                                    startActivity(main);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull  Exception e) {
                            Toast.makeText(AstrologersActivity.this, " Failure:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    //set URL
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (data.getData() != null) {
                mAstrologer_img.setImageURI(data.getData());
                selectedImage = data.getData();
            }
        }
    }

}