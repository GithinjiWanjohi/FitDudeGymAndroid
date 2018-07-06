package com.example.githinji.fitdudegym.model;

public class Workout {
    private String workoutType;
    private String location;
    private String date;
    private String time;
    private String userID;
    private String reps;
    private String sets;

    public Workout(String workoutType, String location, String date, String time,
                   String userID, String reps, String sets){
        this.workoutType = workoutType;
        this.location = location;
        this.date = date;
        this.time = time;
        this.userID = userID;
        this.reps = reps;
        this.sets = sets;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }
}
