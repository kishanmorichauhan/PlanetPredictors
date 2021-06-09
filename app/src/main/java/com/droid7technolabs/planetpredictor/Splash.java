package com.droid7technolabs.planetpredictor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.droid7technolabs.planetpredictor.UserDetails.BirthProfileActivity;

public class Splash extends AppCompatActivity {

    TextView appname;
    LottieAnimationView lottie;

    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appname= findViewById(R.id.appname);
        lottie=findViewById(R.id.lottie);


        lottie.animate().translationY(2000).setDuration(2000).setStartDelay(2900);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onBoardingScreen = getSharedPreferences("onBoradingScreen",MODE_PRIVATE);

                boolean isFirstTime=onBoardingScreen.getBoolean("firstTime",true);
                if(isFirstTime){

                    SharedPreferences.Editor editor=onBoardingScreen.edit();
                    editor.putBoolean("firstTime",false);
                    editor.commit();

                    Intent i = new Intent(getApplicationContext(),OnBoarding.class);
                    startActivity(i);
                    finish();


                }
                else{
                    Intent i = new Intent(getApplicationContext(), BirthProfileActivity.class);
                    startActivity(i);
                    finish();

                }


            }
        }, 5000);
    }
}
