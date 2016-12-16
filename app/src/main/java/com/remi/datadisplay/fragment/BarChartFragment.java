
package com.remi.datadisplay.fragment;

import android.app.Fragment;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.remi.datadisplay.DummyStorage;
import com.remi.datadisplay.R;
import com.remi.datadisplay.event.BrowserFilterEvent;
import com.remi.datadisplay.event.DataUpdated;
import com.remi.datadisplay.filter.BrowserBarChartFilter;
import com.remi.datadisplay.model.Review;
import com.remi.datadisplay.util.MapUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarChartFragment extends Fragment {

    protected BarChart ratingChart;
    protected BarChart labelChart;
    protected BrowserBarChartFilter browserBarChartFilter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.bart_chart_fragment, container, false);

        ratingChart = (BarChart) view.findViewById(R.id.rating_distribution_bar_chart);
        labelChart = (BarChart) view.findViewById(R.id.label_distribution_bar_chart);

        browserBarChartFilter = new BrowserBarChartFilter(this);

        configBarChart(ratingChart);
        configBarChart(labelChart);

        if(DummyStorage.reviews != null) {
            setRatingData(DummyStorage.reviews);
            setLabelData(DummyStorage.reviews);
        }

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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DataUpdated event) {
        ArrayList<Review> reviews = DummyStorage.reviews;
        setRatingData(DummyStorage.reviews);
        setLabelData(DummyStorage.reviews);
    };

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BrowserFilterEvent event) {
        ArrayList<String> selectedBrowsers = event.getSelectedBrowsers();

        browserBarChartFilter.filter(
                (selectedBrowsers.size() != 0) ? selectedBrowsers.toString() : null);
    };

    private void configBarChart(BarChart chart) {
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);

        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);


        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
    }

    public void setRatingData(ArrayList<Review> reviews) {
        BarChart chart = ratingChart;
        float ratingsCount[] = new float[5];

        for (Review review : reviews) {
                ratingsCount[review.getRating() - 1]++;
        }

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1f, ratingsCount[0]));
        entries.add(new BarEntry(2f,ratingsCount[1]));
        entries.add(new BarEntry(3f,ratingsCount[2]));
        entries.add(new BarEntry(4f,ratingsCount[3]));
        entries.add(new BarEntry(5f,ratingsCount[4]));

        BarDataSet set = new BarDataSet(entries, "");
        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh

    }

    public void setLabelData(ArrayList<Review> reviews) {
        BarChart chart = labelChart;
        Map<String, Integer> labelCount = getLabelCount(reviews);
        final List<String> labels = new ArrayList<>();
        for (String label : labelCount.keySet()) {
            labels.add(label);
            if(labels.size() == 10) break;
        }

        XAxis xAxis = labelChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int) value % labels.size());
            }
        });

        List<BarEntry> entries = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : labelCount.entrySet()) {
            String labe = entry.getKey();
            Integer count = entry.getValue();
            entries.add(new BarEntry(entries.size() + 1, count,labe));
        }


        BarDataSet set = new BarDataSet(entries, "");
        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh

    }

    private Map<String, Integer> getLabelCount(ArrayList<Review> reviews) {
        Map<String,Integer> labelCount = new HashMap<>();
        for (Review review : reviews) {
            ArrayList<String> labels = review.getLabels();
            for (String label : labels) {
                if(!labelCount.containsKey(label)){
                    labelCount.put(label,1);
                } else {
                    labelCount.put(label,labelCount.get(label) + 1);
                }
            }
        }

        labelCount = MapUtil.sortByValue(labelCount);
        return labelCount;
    }

    protected RectF mOnValueSelectedRectF = new RectF();

}
