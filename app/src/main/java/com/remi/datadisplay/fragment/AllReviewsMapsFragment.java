package com.remi.datadisplay.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;
import com.remi.datadisplay.DummyStorage;
import com.remi.datadisplay.model.Review;

import java.util.ArrayList;


public class AllReviewsMapsFragment extends MapsFragment  {

    ClusterManager<Review> mClusterManager;

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        setUpCluster();

        mMapView.onResume();
    }

    private void setUpCluster() {

        // Position the map.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.379189, 4.899431), 10));

        //display zoom button
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        //display my location button
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
        }

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
