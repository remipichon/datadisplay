package com.remi.datadisplay.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.remi.datadisplay.DummyStorage;
import com.remi.datadisplay.R;
import com.remi.datadisplay.model.Review;

import java.util.ArrayList;

import static com.remi.datadisplay.R.id.mapView;


public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private MapView mMapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.maps_fragment, container, false);

        mMapView = (MapView) view.findViewById(mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        return view;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap map) {

        ArrayList<Review> reviews = DummyStorage.reviews;
        for (Review review : reviews) {
            map.addMarker(new MarkerOptions().position(review.getLocation()));
        }


        mMapView.onResume();
    }
}
