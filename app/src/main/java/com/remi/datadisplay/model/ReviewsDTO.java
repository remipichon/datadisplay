package com.remi.datadisplay.model;


import java.io.Serializable;
import java.util.ArrayList;

public class ReviewsDTO implements Serializable {


    ArrayList<ReviewDTO> items;

    public ArrayList<ReviewDTO> getReviews() {
        return items;
    }
}
