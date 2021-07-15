package com.droid7technolabs.planetpredictor;

//message model class
public class Messages {

    String message,messageId;
    String senderId;
    long timestamp;
    String currenttime;


    public Messages() {
    }


    public Messages(String message, String senderId, long timestamp,String currenttime) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.currenttime = currenttime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(String currentTime) {
        this.currenttime = currentTime;
    }


}
