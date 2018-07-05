package com.example.githinji.fitdudegym.model;

public class Workout {
    private String workoutType;
    private String location;
    private String date;
    private String time;
    private Integer userID;
    private Integer reps;
    private Integer sets;

    public Workout(String workoutType, String location, String date, String time,
                   Integer userID, Integer reps, Integer sets){
        this.workoutType = workoutType;
        this.location = location;
        this.date = date;
        this.time = time;
        this.userID = userID;
        this.reps = reps;
        this.reps = reps;
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

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Integer getSets() {
        return sets;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }
}
