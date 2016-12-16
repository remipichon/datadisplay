package com.remi.datadisplay;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;

import com.remi.datadisplay.fragment.AllReviewsMapsFragment;
import com.remi.datadisplay.fragment.BarChartFragment;
import com.remi.datadisplay.fragment.DoubleMapsFragment;
import com.remi.datadisplay.fragment.ListFragment;
import com.remi.datadisplay.fragment.PieChartFragment;
import com.remi.datadisplay.model.Review;
import com.remi.datadisplay.service.ServerDataIntentService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import eu.fiskur.chipcloud.ChipCloud;
import eu.fiskur.chipcloud.ChipListener;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private PopupWindow popWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpFloatingButton();

        setUpNavigationDrawer(toolbar);


        //get data from server
        Intent serverDataIntent = new Intent(this,ServerDataIntentService.class);
        startService(serverDataIntent);

    }

    private void setUpFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowPopup(findViewById(R.id.content_frame));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setUpNavigationDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void onShowPopup(View v){

        final View popupView = getLayoutInflater().inflate(R.layout.common_filter_popup, null,false);
        // get device size
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        // set height depends on the device size
        popWindow = new PopupWindow(popupView, size.x - 50,size.y - 350, true );
        // set a background drawable with rounders corners
        popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_bg));
        // make it focusable to show the keyboard to enter in `EditText`
        popWindow.setFocusable(true);
        // make it outside touchable to dismiss the popup window
        popWindow.setOutsideTouchable(true);

        // show the popup at bottom of the screen and set some margin at bottom ie,
        popWindow.showAtLocation(v, Gravity.BOTTOM, 0,200);

        //TODO add transition



        //configure popup
        ChipCloud chipCloud = (ChipCloud) popupView.findViewById(R.id.chip_cloud);

        final Set<String> labels = new HashSet<>();

        ArrayList<Review> reviews = DummyStorage.reviews;
        for (Review review : reviews) {
            labels.add(review.getBrowserName());
        }

        final String[] browsers = labels.toArray(new String[labels.size()]);
        new ChipCloud.Configure()
                .chipCloud(chipCloud)
                .selectedColor(Color.parseColor("#ff00cc"))
                .selectedFontColor(Color.parseColor("#ffffff"))
                .deselectedColor(Color.parseColor("#e1e1e1"))
                .deselectedFontColor(Color.parseColor("#333333"))
                .selectTransitionMS(500)
                .labels(browsers)
                .deselectTransitionMS(250)
                .mode(ChipCloud.Mode.MULTI)
                .chipListener(new ChipListener() {
                        @Override
                        public void chipSelected(int index) {
                            String browser = browsers[index];
                            System.out.println("browser selected "+browser);
                        }
                        @Override
                        public void chipDeselected(int index) {
                            String browser = browsers[index];
                            System.out.println("browser deselected "+browser);
                        }
                })
                .build();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_maps) {
            this.startFragment(AllReviewsMapsFragment.class);
        } else if (id == R.id.nav_list) {
            this.startFragment(ListFragment.class);
        } else if (id == R.id.nav_slideshow) {
            this.startFragment(DoubleMapsFragment.class);
        } else if (id == R.id.nav_manage) {
            this.startFragment(BarChartFragment.class);
        } else if (id == R.id.nav_share) {
            this.startFragment(PieChartFragment.class);
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    void startFragment(Class fragmentClassName) {
        System.out.println("start fragment "+fragmentClassName.getName());
        Fragment fragment = null;
        try {
            Constructor<?> ctor = fragmentClassName.getConstructor();
            fragment = (Fragment) ctor.newInstance(new Object[]{});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment).commit();
    }
}
