package com.remi.datadisplay.fragment;


import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.ClusterManager;
import com.remi.datadisplay.DummyStorage;
import com.remi.datadisplay.filter.BrowserFilter;
import com.remi.datadisplay.filter.BrowserMapsFilter;
import com.remi.datadisplay.model.Review;

import java.util.ArrayList;


public class AllReviewsMapsFragment extends MapsFragment  {

    ClusterManager<Review> mClusterManager;
    private BrowserFilter browserFilter;

    @Override
    public void onMapReady(GoogleMap map) {
        super.onMapReady(map);


        browserFilter = new BrowserMapsFilter(this);


        ArrayList<Review> reviews = DummyStorage.reviews;
        setUpCluster(reviews);

        browserFilter.filter("IE");
    }

     public void setUpCluster(ArrayList<Review> reviews) {

        // Initialize the manager with the context and the map.
        mClusterManager = new ClusterManager<Review>(this.getActivity(), googleMap);

        // Point the map's listeners at the listeners implemented by the cluster manager.
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems(reviews);
    }

    private void addItems(ArrayList<Review> reviews) {

        mClusterManager.clearItems();
        for (Review review : reviews) {
            mClusterManager.addItem(review);
        }
    }
}
