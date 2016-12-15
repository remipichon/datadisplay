package com.remi.datadisplay.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.remi.datadisplay.R;


public class DoubleMapsFragment extends Fragment  {

    ViewPager viewPager;
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.double_maps_fragment, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.double_maps_view_pager);
        tabLayout = (TabLayout) view.findViewById(R.id.double_maps_tab_layout);

        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                if(position == 0){
                    fragment = new MapsFragment();
                } else if(position == 1){
                    fragment = new MapsFragment();
                }
                return fragment;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getResources().getString(R.string.allReviewsMapsTab);
                    case 1:
                        return getResources().getString(R.string.averageRatingMapsTab);
                }
                return null;
            }

        });
        tabLayout.setupWithViewPager(viewPager);
        //tabLayout.getTabAt(1).setIcon(R.drawable.ic_menu_camera);

        return view;
    }

}
