package com.droid7technolabs.planetpredictor;

public class Astrologer {

    //astrologer model class
    private String Name;
    private String Astrologer_image;
    private String Ratings;
    private String Specialisation;


    public Astrologer(String name, String astrologer_image, String ratings, String specialisation) {
        this.Name = name;
        this.Astrologer_image = astrologer_image;
        this.Ratings = ratings;
        this.Specialisation = specialisation;
    }

    public Astrologer() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getAstrologer_image() {
        return Astrologer_image;
    }

    public void setAstrologer_image(String astrologer_image) {
        this.Astrologer_image = astrologer_image;
    }

    public String getRatings() {
        return Ratings;
    }

    public void setRatings(String ratings) {
        this.Ratings = ratings;
    }

    public String getSpecialisation() {
        return Specialisation;
    }

    public void setSpecialisation(String specialisation) {
        this.Specialisation = specialisation;
    }
}