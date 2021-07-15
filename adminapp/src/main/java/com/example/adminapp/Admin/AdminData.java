package com.example.adminapp.Admin;

public class AdminData {

    private String AdminImage;
    private String AdminName;

    public AdminData() {
    }

    public AdminData(String adminImage, String adminName) {
        this.AdminImage = adminImage;
        this.AdminName = adminName;
    }

    public String getAdminImage() {
        return AdminImage;
    }

    public void setAdminImage(String adminImage) {
        AdminImage = adminImage;
    }

    public String getAdminName() {
        return AdminName;
    }

    public void setAdminName(String adminName) {
        AdminName = adminName;
    }
}
