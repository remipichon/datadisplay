package com.remi.datadisplay.filter;


import com.google.maps.android.clustering.ClusterItem;
import com.remi.datadisplay.fragment.MapsFragment;
import com.remi.datadisplay.model.Review;

import java.util.ArrayList;

public class BrowserMapsFilter<A extends ClusterItem> extends BrowserFilter {


    private final MapsFragment<A> mapsFragment;

    public BrowserMapsFilter(MapsFragment<A> mapsFragment) {
        this.mapsFragment = mapsFragment;

    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        ArrayList<Review> reviews = (ArrayList<Review>) filterResults.values;

        this.mapsFragment.addItems(reviews);
    }
}
