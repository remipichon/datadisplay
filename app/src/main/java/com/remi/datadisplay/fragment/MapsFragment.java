package com.remi.datadisplay.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.remi.datadisplay.DummyStorage;
import com.remi.datadisplay.R;
import com.remi.datadisplay.event.BrowserFilterEvent;
import com.remi.datadisplay.event.DataUpdated;
import com.remi.datadisplay.filter.BrowserMapsFilter;
import com.remi.datadisplay.model.Review;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import static com.remi.datadisplay.R.id.mapView;

public abstract class MapsFragment<A extends ClusterItem> extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    protected GoogleMap googleMap;
    protected MapView mMapView;
    protected BrowserMapsFilter browserFilter;
    ClusterManager<A> mClusterManager;

    abstract public void addItems(ArrayList<Review> reviews);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.maps_fragment, container, false);

        mMapView = (MapView) view.findViewById(mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        browserFilter = new BrowserMapsFilter(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.379189, 4.899431), 10));
        mMapView.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DataUpdated event) {
        ArrayList<Review> reviews = DummyStorage.reviews;
        addItems(reviews);
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BrowserFilterEvent event) {
        ArrayList<String> selectedBrowsers = event.getSelectedBrowsers();

        browserFilter.filter(
                (selectedBrowsers.size() != 0) ? selectedBrowsers.toString() : null);
    };

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    protected void setUpCluster() {
        // Initialize the manager with the context and the map.
        mClusterManager = new ClusterManager<>(this.getActivity(), googleMap);

        // Point the map's listeners at the listeners implemented by the cluster manager.
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
    }

}
