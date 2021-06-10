package com.droid7technolabs.planetpredictor;

import android.content.Context;
import android.content.SharedPreferences;

public class preconfig {

    private static final String MY_PREFERENCE_NAME = "com.example.counter";
    private static final String PREF_TOTAL_KEY = "pref_total_key";

    public static void saveTotalInPref(Context context , int totle){
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(PREF_TOTAL_KEY,totle);
        editor.apply();
    }
    public static int loadTotalFromPref(Context context){
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME,Context.MODE_PRIVATE);
        return pref.getInt(PREF_TOTAL_KEY,0);
    }
}
