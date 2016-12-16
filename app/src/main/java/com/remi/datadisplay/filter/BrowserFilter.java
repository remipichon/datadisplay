package com.remi.datadisplay.filter;


import android.widget.Filter;

import com.remi.datadisplay.DummyStorage;
import com.remi.datadisplay.model.Review;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class BrowserFilter extends Filter {


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        ArrayList<Review> reviews = DummyStorage.reviews;

        FilterResults result = new FilterResults();
        if (constraint != null && constraint.toString().length() > 0) {

            ArrayList<String> selectedBrowsers = new ArrayList<>(Arrays.asList(
                    ((String) constraint).substring(1, constraint.length() - 1).split(", "))
            );


            constraint = constraint.toString().toLowerCase();
            ArrayList<Review> filteredItems = new ArrayList<>();

            for (int i = 0, l = reviews.size(); i < l; i++) {
                Review review = reviews.get(i);
                //effective search pattern
                String browserName = review.getBrowserName();
                for (String selectedBrowser : selectedBrowsers) {
                    if (browserName.equals(selectedBrowser))
                        filteredItems.add(review);
                }
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

}
