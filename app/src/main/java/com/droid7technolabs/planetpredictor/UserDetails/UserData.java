package com.droid7technolabs.planetpredictor.UserDetails;

import android.widget.EditText;
import android.widget.TextView;

public class UserData {
    private String fullname;
    private String dob;
    private String deviceid;

    public UserData()
    {

    }

    public UserData(String fullname, String date, String deviceid)
    {
         this.fullname= fullname;
         this.dob= date;
        this.deviceid= deviceid;

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
}
