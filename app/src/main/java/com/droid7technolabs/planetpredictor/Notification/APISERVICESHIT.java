package com.droid7technolabs.planetpredictor.Notification;

import com.android.volley.Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

//Using API to send the notification

public interface APISERVICESHIT {


    //FCM key
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAARkT_2sc:APA91bHoIuLDULHxhZzQQN_GgKK4uZY354Ncbp3l1p_BDdLpvXi4-TUJHGmzEYmAq6HrYi-pheSYwiU4X0Mt3cTofvSIYY5KUgkspEhLKLh17K82WBmv9RLfyX6qGQyWgUqbUOa-S_EJ"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);

}
