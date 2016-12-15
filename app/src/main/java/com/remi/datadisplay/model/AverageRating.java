package com.remi.datadisplay.model;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class AverageRating implements ClusterItem {

    private LatLng Position;
    private Float averageRating;
    private String cityName;

    public AverageRating(LatLng position, Float averageRating, String cityName) {
        Position = position;
        this.averageRating = averageRating;
        this.cityName = cityName;
    }

    public LatLng getPosition() {
        return Position;
    }

    public Float getAverageRating() {
        return averageRating;
    }

    public String getCityName() {
        return cityName;
    }
}
