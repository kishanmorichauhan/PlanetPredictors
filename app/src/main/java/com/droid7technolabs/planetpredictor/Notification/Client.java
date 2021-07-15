package com.droid7technolabs.planetpredictor.Notification;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//use retrofit libreay to convert json file
public class Client {
    private static Retrofit retrofit = null;


    //Retrofit - it easy to Restfull web service
    public static Retrofit getRetrofit(String url) {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


        }


        return retrofit;

    }
}
