package com.example.githinji.fitdudegym.model;

public class Gym {
    private String gymName;
    private String latitude;
    private String longitude;
    private String openingTime;
    private String closingTime;
    private String rating;

    //    Class constructor
    public Gym(String gymName, String latitude, String longitude, String openingTime, String closingTime, String rating){
        this.gymName = gymName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.rating = rating;
    }


    public String getGymName() {
        return gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String id) {
        this.closingTime = closingTime;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
