package com.remi.datadisplay.fragment;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.remi.datadisplay.DummyStorage;
import com.remi.datadisplay.model.Review;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AverageRatingMapsFragment extends MapsFragment {


    @Override
    public void onMapReady(GoogleMap map) {
        super.onMapReady(map);

//        googleMap = map;

        addItem();

//        mMapView.onResume();
    }

    private void addItem() {

        ArrayList<Review> reviews = DummyStorage.reviews;

        //TODO use Java8 and Jack ToolChain (but disable Instant Run)
        //group by city
        HashMap<String, List<Integer>> ratingByCities = new HashMap<String, List<Integer>>();
        HashMap<String, LatLng> citiesApproxLoc = new HashMap<String, LatLng>();
        for (Review review : reviews) {
            if (!ratingByCities.containsKey(review.getCity())) {
                List<Integer> ratings = new ArrayList<>();
                ratings.add(review.getRating());

                ratingByCities.put(review.getCity(), ratings);

                citiesApproxLoc.put(review.getCity(),review.getLocation());
            } else {
                ratingByCities.get(review.getCity()).add(review.getRating());
            }
        }
        //average per city
        HashMap<String, Float> averageRatingByCities = new HashMap<String, Float>();
        for (Map.Entry<String, List<Integer>> entry : ratingByCities.entrySet()) {
            String city = entry.getKey();
            List<Integer> ratings = entry.getValue();
            Double total = 0.0; //overkill uh ?
            for (Integer rating : ratings) {
                total += rating;
            }
            averageRatingByCities.put(city,new Float(total / ratings.size()));
        }

        //display average on maps
        System.out.println("average ratings");
        for (Map.Entry<String, Float> entry : averageRatingByCities.entrySet()) {
            String city = entry.getKey();
            Float average = entry.getValue();
            if(city == null) continue;
            LatLng position = citiesApproxLoc.get(city);

            System.out.println(city + " " + average + " " + position);

            IconGenerator iconFactory = new IconGenerator(this.getActivity());
            iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
            addIcon(iconFactory, city + " " + new DecimalFormat("#.##").format(average) + "/5", position);

        }


    }

    private void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        googleMap.addMarker(markerOptions);
    }


}












