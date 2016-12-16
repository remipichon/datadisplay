
package com.remi.datadisplay.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.remi.datadisplay.DummyStorage;
import com.remi.datadisplay.R;
import com.remi.datadisplay.event.BrowserFilterEvent;
import com.remi.datadisplay.event.DataUpdated;
import com.remi.datadisplay.filter.BrowserPieChartFilter;
import com.remi.datadisplay.model.Review;
import com.remi.datadisplay.util.MapUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieChartFragment extends Fragment {

    protected PieChart browserChart;
    protected PieChart platformChart;
    private BrowserPieChartFilter browserFilter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.pie_chart_fragment, container, false);

        browserChart = (PieChart) view.findViewById(R.id.brower_pie_chart);
        platformChart = (PieChart) view.findViewById(R.id.platform_pie_chart);

        configPieChart(browserChart);
        configPieChart(platformChart);

        if(DummyStorage.reviews != null) {
            setBrowserData(DummyStorage.reviews);
            setPlatformData(DummyStorage.reviews);
        }

        browserFilter = new BrowserPieChartFilter(this);

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DataUpdated event) {
        ArrayList<Review> reviews = DummyStorage.reviews;
        setBrowserData(reviews);
        setPlatformData(reviews);
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BrowserFilterEvent event) {
        ArrayList<String> selectedBrowsers = event.getSelectedBrowsers();

        browserFilter.filter(
                (selectedBrowsers.size() != 0) ? selectedBrowsers.toString() : null);
    };

    private void configPieChart(PieChart chart) {
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);
        chart.setHoleRadius(40f);
        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        chart.getLegend().setEnabled(false);
        chart.setEntryLabelColor(Color.BLACK);
        chart.setEntryLabelTextSize(12f);
    }

    public void setBrowserData(ArrayList<Review> reviews) {
        PieChart chart = browserChart;
        Map<String,Integer> browsersCount = new HashMap<>();
        for (Review review : reviews) {
            if(!browsersCount.containsKey(review.getBrowserName())){
                browsersCount.put(review.getBrowserName(),1);
            } else {
                browsersCount.put(review.getBrowserName(),browsersCount.get(review.getBrowserName()) + 1);
            }
        }

        Map<String,Float> percentageBrowsersCount = new HashMap<>();
        for (Map.Entry<String, Integer> entry : browsersCount.entrySet()) {
            String browser = entry.getKey();
            Integer count = entry.getValue();
            Float percentage = new Float(count) / browsersCount.size();
            percentageBrowsersCount.put(browser, percentage);
        }
        percentageBrowsersCount =  MapUtil.sortByValue(percentageBrowsersCount);

        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : percentageBrowsersCount.entrySet()) {
            String browser = entry.getKey();
            Float percentage = entry.getValue();
            entries.add(new PieEntry(percentage, browser));
        }

        PieDataSet set = new PieDataSet(entries, "");
        PieData data = new PieData(set);
        data.setValueTextSize(10f);
        data.setValueFormatter(new PercentFormatter());
        set.setSliceSpace(3f);
        set.setSelectionShift(5f);
        set.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set.setValueLineWidth(30f);
        chart.setData(data);
        chart.invalidate(); // refresh

    }

    public void setPlatformData(ArrayList<Review> reviews) {
        PieChart chart = platformChart;
        Map<String,Integer> platformsCount = new HashMap<>();
        for (Review review : reviews) {
            if(!platformsCount.containsKey(review.getPlatform())){
                platformsCount.put(review.getPlatform(),1);
            } else {
                platformsCount.put(review.getPlatform(),platformsCount.get(review.getPlatform()) + 1);
            }
        }

        Map<String,Float> percentagePlatformCount = new HashMap<>();
        for (Map.Entry<String, Integer> entry : platformsCount.entrySet()) {
            String platform = entry.getKey();
            Integer count = entry.getValue();
            Float percentage = new Float(count) / reviews.size();
            System.out.println(percentage);
            if(percentage <= 0.03f){
                if(!percentagePlatformCount.containsKey("Other"))
                    percentagePlatformCount.put("Other",percentage);
                else{
                    percentagePlatformCount.put("Other",percentagePlatformCount.get("Other")+percentage);
                }
            } else
                percentagePlatformCount.put(platform, percentage);        }
        percentagePlatformCount =  MapUtil.sortByValue(percentagePlatformCount);

        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : percentagePlatformCount.entrySet()) {
            String platform = entry.getKey();
            Float percentage = entry.getValue();
            entries.add(new PieEntry(percentage, platform));
        }
        PieDataSet set = new PieDataSet(entries, "");
        PieData data = new PieData(set);
        data.setValueTextSize(10f);
        data.setValueFormatter(new PercentFormatter());
        set.setSliceSpace(3f);
        set.setSelectionShift(5f);
        set.setColors(ColorTemplate.VORDIPLOM_COLORS);
        chart.setData(data);
        chart.invalidate(); // refresh


    }


}
