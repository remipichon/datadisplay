package com.remi.datadisplay.model;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Review {

    private String browserName;
    private String browserVersion;
    private String platform;
    private LatLng location;
    private Integer rating;
    private ArrayList<String> labels;

    public Review(String browserName, String browserVersion, String platform, LatLng location, Integer rating, ArrayList<String> labels) {
        this.browserName = browserName;
        this.browserVersion = browserVersion;
        this.platform = platform;
        this.location = location;
        this.rating = rating;
        this.labels = labels;
    }

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }

    @Override
    public String toString() {
        return "Review{" +
                "browserName='" + browserName + '\'' +
                ", browserVersion='" + browserVersion + '\'' +
                ", platform='" + platform + '\'' +
                ", location=" + location +
                ", rating=" + rating +
                ", labels=" + labels +
                '}';
    }
}
