package com.remi.datadisplay.fragment;


import android.support.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.remi.datadisplay.DummyStorage;
import com.remi.datadisplay.model.AverageRating;
import com.remi.datadisplay.model.Review;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AverageRatingMapsFragment extends MapsFragment {

    ClusterManager<AverageRating> mClusterManager;


    @Override
    public void onMapReady(GoogleMap map) {
        super.onMapReady(map);

        ArrayList<Review> reviews = DummyStorage.reviews;
        setUpCluster(reviews);
    }

    private void setUpCluster(ArrayList<Review> reviews) {
        // Initialize the manager with the context and the map.
        mClusterManager = new ClusterManager<AverageRating>(this.getActivity(), googleMap);

        mClusterManager.setRenderer(new AverageRatingRendered());

        // Point the map's listeners at the listeners implemented by the cluster manager.
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems(reviews);
    }

    public void addItems(ArrayList<Review> reviews) {

        //TODO use Java8 and Jack ToolChain (but disable Instant Run)
        //group by city
        HashMap<String, List<Integer>> ratingByCities = new HashMap<String, List<Integer>>();
        HashMap<String, LatLng> citiesApproxLoc = new HashMap<String, LatLng>();
        for (Review review : reviews) {
            if (!ratingByCities.containsKey(review.getCity())) {
                List<Integer> ratings = new ArrayList<>();
                ratings.add(review.getRating());

                ratingByCities.put(review.getCity(), ratings);

                citiesApproxLoc.put(review.getCity(), review.getLocation());
            } else {
                ratingByCities.get(review.getCity()).add(review.getRating());
            }
        }
        //average per city
        HashMap<String, Float> averageRatingByCities = new HashMap<String, Float>();
        for (Map.Entry<String, List<Integer>> entry : ratingByCities.entrySet()) {
            String city = entry.getKey();
            List<Integer> ratings = entry.getValue();
            Float average = getAverage(ratings);
            averageRatingByCities.put(city, average);
        }

        //display average on maps
        for (Map.Entry<String, Float> entry : averageRatingByCities.entrySet()) {
            String city = entry.getKey();
            Float average = entry.getValue();
            if (city == null) continue;
            LatLng position = citiesApproxLoc.get(city);

            mClusterManager.addItem(new AverageRating(position, average, city));
        }
    }

    @NonNull
    private Float getAverage(List<Integer> ratings) {
        Double total = 0.0; //overkill uh ?
        for (Integer rating : ratings) {
            total += rating;
        }
        return new Float(total / ratings.size());
    }


    private class AverageRatingRendered extends DefaultClusterRenderer<AverageRating> {
        IconGenerator iconFactory = new IconGenerator(getActivity());

        public AverageRatingRendered() {
            super(getActivity(), googleMap, mClusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(AverageRating averageRating, MarkerOptions markerOptions) {
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(new DecimalFormat("#.##").format(averageRating.getAverageRating()))));
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<AverageRating> cluster, MarkerOptions markerOptions) {
            Double sum  = 0.0;
            for (AverageRating averageRating : cluster.getItems()) {
                sum += averageRating.getAverageRating();
            }
            Double wrapperAverage = sum / cluster.getItems().size();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(new DecimalFormat("#.##").format(wrapperAverage))));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }
}












