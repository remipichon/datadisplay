package com.remi.datadisplay.service;

import com.google.android.gms.maps.model.LatLng;
import com.remi.datadisplay.model.Review;
import com.remi.datadisplay.model.ReviewDTO;

import java.util.ArrayList;
import java.util.List;


public class DTOConverterService {

    public static ArrayList<Review> fromDTO(List<ReviewDTO> reviewsDTO) {
        ArrayList<Review> result = new ArrayList<>();

        for (ReviewDTO reviewDTO : reviewsDTO) {
            result.add(new Review(
                    reviewDTO.getComputed_browser().getBrowser(),
                    reviewDTO.getComputed_browser().getVersion(),
                    reviewDTO.getComputed_browser().getPlatform(),
                    new LatLng(reviewDTO.getGeo().getLat(), reviewDTO.getGeo().getLon()),
                    reviewDTO.getGeo().getCity(),
                    reviewDTO.getRating(),
                    reviewDTO.getLabels()
            ));
        }

        return result;
    }
}
