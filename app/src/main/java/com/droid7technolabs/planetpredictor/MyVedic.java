package com.droid7technolabs.planetpredictor;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

//for myvedic page
public class MyVedic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vedic);

        getSupportActionBar().setTitle("Why Vedic?");   //page title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //back button
    }

    @Override     //back button
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}