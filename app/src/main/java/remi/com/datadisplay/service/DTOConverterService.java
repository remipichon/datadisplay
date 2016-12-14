package remi.com.datadisplay.service;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import remi.com.datadisplay.model.Review;
import remi.com.datadisplay.model.ReviewDTO;

public class DTOConverterService {

    public static ArrayList<Review> fromDTO(List<ReviewDTO> reviewsDTO) {
        ArrayList<Review> result = new ArrayList<>();

        for (ReviewDTO reviewDTO : reviewsDTO) {
            result.add(new Review(
                    reviewDTO.getComputed_browser().getBrowser(),
                    reviewDTO.getComputed_browser().getVersion(),
                    reviewDTO.getComputed_browser().getPlatform(),
                    new LatLng(reviewDTO.getGeo().getLat(), reviewDTO.getGeo().getLon()),
                    reviewDTO.getRating(),
                    reviewDTO.getLabels()
            ));
        }

        return result;
    }
}
