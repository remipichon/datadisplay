package com.remi.datadisplay.model;

import java.io.Serializable;

public class LocationDTO  implements Serializable {
    Float lat;
    Float lon;
    String city;

    public String getCity() {
        return city;
    }

    public Float getLat() {
        return lat;
    }

    public Float getLon() {
        return lon;
    }
}
