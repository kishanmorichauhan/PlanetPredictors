package com.droid7technolabs.planetpredictor.UserDetails;

import android.widget.EditText;
import android.widget.TextView;

public class UserData {
    private String fullname;
    private String dob;
    private String deviceid;
    private String profile_image;
    private String time;
    private String city;
    private String country;

    public UserData()
    {
    }


    public UserData(String fullname, String date, String deviceid,String profile_image,String time,String city,String country)
    {
        this.fullname= fullname;
        this.dob= date;
        this.deviceid= deviceid;
        this.profile_image = profile_image;
        this.time = time;
        this.city = city;
        this.country = country;

    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDate() {
        return dob;
    }

    public void setDate(String date) {
        this.dob = date;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
