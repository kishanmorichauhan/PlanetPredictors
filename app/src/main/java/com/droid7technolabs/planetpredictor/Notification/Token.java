package com.droid7technolabs.planetpredictor.Notification;

public class Token {

   //geating and seting token
    String token;

    public Token() {
    }

    public Token(String token) {
        this.token = token;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    //Tacking token
    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                '}';
    }
}
