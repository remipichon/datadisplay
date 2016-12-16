package com.remi.datadisplay.filter;


import android.widget.Filter;

import com.remi.datadisplay.DummyStorage;
import com.remi.datadisplay.fragment.MapsFragment;
import com.remi.datadisplay.model.Review;

import java.util.ArrayList;

public class BrowserMapsFilter extends Filter {


    private final MapsFragment mapsFragment;

    public BrowserMapsFilter(MapsFragment mapsFragment) {
        this.mapsFragment = mapsFragment;

    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        ArrayList<Review> reviews = DummyStorage.reviews;

        FilterResults result = new FilterResults();
        if (constraint != null && constraint.toString().length() > 0) {
            constraint = constraint.toString().toLowerCase();
            ArrayList<Review> filteredItems = new ArrayList<>();

            for (int i = 0, l = reviews.size(); i < l; i++) {
                Review review = reviews.get(i);
                //effective search pattern
                if (review.getBrowserName().toLowerCase().equals(constraint))
                    filteredItems.add(review);
            }
            result.count = filteredItems.size();
            result.values = filteredItems;
        } else {
            synchronized (this) {
                result.values = new ArrayList<>(reviews);
                result.count = reviews.size();
            }
        }
        return result;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        ArrayList<Review> reviews = (ArrayList<Review>) filterResults.values;

        this.mapsFragment.setUpCluster(reviews);
    }
}
