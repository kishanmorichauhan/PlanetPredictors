package com.droid7technolabs.planetpredictor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.droid7technolabs.planetpredictor.UserDetails.BirthProfileActivity;

public class Joinas extends AppCompatActivity {

    Button admin, user, astrologer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_as);


        user = findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent user = new Intent(com.droid7technolabs.planetpredictor.Joinas.this, BirthProfileActivity.class);
                startActivity(user);
            }
        });


    }
}
