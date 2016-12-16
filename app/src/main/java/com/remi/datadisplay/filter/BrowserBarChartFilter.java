package com.remi.datadisplay.filter;


import com.remi.datadisplay.fragment.BarChartFragment;
import com.remi.datadisplay.model.Review;

import java.util.ArrayList;

public class BrowserBarChartFilter extends BrowserFilter {


    private final BarChartFragment barChartFragment;

    public BrowserBarChartFilter(BarChartFragment barChartFragment) {
        this.barChartFragment = barChartFragment;

    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        ArrayList<Review> reviews = (ArrayList<Review>) filterResults.values;

        this.barChartFragment.setRatingData(reviews);
        this.barChartFragment.setLabelData(reviews);
    }
}
