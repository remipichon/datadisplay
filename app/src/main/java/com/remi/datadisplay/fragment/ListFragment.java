package com.remi.datadisplay.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.remi.datadisplay.DummyStorage;
import com.remi.datadisplay.R;
import com.remi.datadisplay.adapter.ReviewAdapter;
import com.remi.datadisplay.model.Review;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment {


    @BindView(R.id.review_list)
    ListView reviewListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.list_fragment, container, false);
        ButterKnife.bind(this, view);

        ArrayList<Review> reviews = DummyStorage.reviews;

        ReviewAdapter reviewAdapter = new ReviewAdapter(this.getActivity(), reviews);

        reviewListView = (ListView) view.findViewById(R.id.review_list);

        reviewListView.setAdapter(reviewAdapter);


        return view;
    }

}
