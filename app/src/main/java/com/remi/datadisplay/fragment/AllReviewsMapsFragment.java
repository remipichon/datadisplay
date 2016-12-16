package com.remi.datadisplay.fragment;


import com.google.android.gms.maps.GoogleMap;
import com.remi.datadisplay.DummyStorage;
import com.remi.datadisplay.model.Review;

import java.util.ArrayList;


public class AllReviewsMapsFragment extends MapsFragment<Review> {


    @Override
    public void onMapReady(GoogleMap map) {
        super.onMapReady(map);

        setUpCluster();
        if(DummyStorage.reviews != null)addItems(DummyStorage.reviews);
    }

    public void addItems(ArrayList<Review> reviews) {

        mClusterManager.clearItems();
        for (Review review : reviews) {
            mClusterManager.addItem(review);
        }
        mClusterManager.cluster();
    }

}
