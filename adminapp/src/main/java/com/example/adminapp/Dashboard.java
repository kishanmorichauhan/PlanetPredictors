package com.example.adminapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.adminapp.Astrologers.AstrologerDetail;
import com.example.adminapp.Astrologers.AstrologersActivity;

public class Dashboard extends AppCompatActivity {

    private CardView usercard, astro, addastro, updateprice;

//Dashboard screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getSupportActionBar().setTitle("Dashboard");

        usercard=findViewById(R.id.usercard);
        astro=findViewById(R.id.astrocard);
        addastro=findViewById(R.id.addastrocard);
        updateprice=findViewById(R.id.updateprice);


        usercard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, MainActivity.class));
            }
        });

        astro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, AstrologerDetail.class));
            }
        });

        addastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, AstrologersActivity.class));
            }
        });

        updateprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, PrizeUpdateActivity.class));
            }
        });
    }
}