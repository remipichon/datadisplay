package com.remi.datadisplay.filter;


import com.remi.datadisplay.fragment.PieChartFragment;
import com.remi.datadisplay.model.Review;

import java.util.ArrayList;

public class BrowserPieChartFilter extends BrowserFilter {


    private final PieChartFragment pieChartFragment;

    public BrowserPieChartFilter(PieChartFragment pieChartFragment) {
        this.pieChartFragment = pieChartFragment;

    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        ArrayList<Review> reviews = (ArrayList<Review>) filterResults.values;

        this.pieChartFragment.setBrowserData(reviews);
        this.pieChartFragment.setPlatformData(reviews);
    }
}
