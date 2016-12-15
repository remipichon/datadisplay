package com.remi.datadisplay.fragment;


import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.ClusterManager;
import com.remi.datadisplay.DummyStorage;
import com.remi.datadisplay.model.Review;

import java.util.ArrayList;


public class AllReviewsMapsFragment extends MapsFragment  {

    ClusterManager<Review> mClusterManager;

    @Override
    public void onMapReady(GoogleMap map) {
        super.onMapReady(map);

        setUpCluster();
    }

    private void setUpCluster() {

        // Initialize the manager with the context and the map.
        mClusterManager = new ClusterManager<Review>(this.getActivity(), googleMap);

        // Point the map's listeners at the listeners implemented by the cluster manager.
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {

        ArrayList<Review> reviews = DummyStorage.reviews;
        for (Review review : reviews) {
            mClusterManager.addItem(review);
        }
    }
}
