package com.remi.datadisplay.filter;


import com.remi.datadisplay.fragment.MapsFragment;
import com.remi.datadisplay.model.Review;

import java.util.ArrayList;

public class BrowserMapsFilter extends BrowserFilter {


    private final MapsFragment mapsFragment;

    public BrowserMapsFilter(MapsFragment mapsFragment) {
        this.mapsFragment = mapsFragment;

    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        ArrayList<Review> reviews = (ArrayList<Review>) filterResults.values;

        this.mapsFragment.addItems(reviews);
    }
}
