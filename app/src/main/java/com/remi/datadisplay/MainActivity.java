package com.remi.datadisplay;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;

import com.remi.datadisplay.event.BrowserFilterEvent;
import com.remi.datadisplay.event.DataUpdated;
import com.remi.datadisplay.fragment.BarChartFragment;
import com.remi.datadisplay.fragment.DoubleMapsFragment;
import com.remi.datadisplay.fragment.HomeFragment;
import com.remi.datadisplay.fragment.PieChartFragment;
import com.remi.datadisplay.model.Review;
import com.remi.datadisplay.service.ServerDataIntentService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import eu.fiskur.chipcloud.ChipCloud;
import eu.fiskur.chipcloud.ChipListener;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private PopupWindow popWindow;
    private ArrayList<String> selectedBrowsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpFloatingButton();

        setUpNavigationDrawer(toolbar);

        setupNavBarLink();

        //get data from server
        Intent serverDataIntent = new Intent(this,ServerDataIntentService.class);
        startService(serverDataIntent);

        startFragment(HomeFragment.class);

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DataUpdated event) {

        setupPopup(findViewById(R.id.content_frame));

        Snackbar.make(findViewById(R.id.content_frame), "Data are ready, you can now use the xfilter", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    };

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_maps) {
            this.startFragment(DoubleMapsFragment.class);
        } else if (id == R.id.nav_bar_chart) {
            this.startFragment(BarChartFragment.class);
        } else if (id == R.id.nav_pie_chart) {
            this.startFragment(PieChartFragment.class);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void startFragment(Class fragmentClassName) {
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


    private void setUpFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show the popup at bottom of the screen and set some margin at bottom ie,
                popWindow.showAtLocation(findViewById(R.id.content_frame), Gravity.BOTTOM, 0,200);            }
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

    public void setupPopup(View v){
        selectedBrowsers = new ArrayList<>();

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

        //TODO add transition

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
                        selectedBrowsers.add(browser);
                        EventBus.getDefault().postSticky(new BrowserFilterEvent(selectedBrowsers));

                    }
                    @Override
                    public void chipDeselected(int index) {
                        String browser = browsers[index];
                        selectedBrowsers.remove(browser);
                        EventBus.getDefault().postSticky(new BrowserFilterEvent(selectedBrowsers));

                    }
                })
                .build();


    }

    private void setupNavBarLink() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_github).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/remipichon/datadisplay"));
                startActivity(browserIntent);
                return false;
            }
        });
        navigationView.getMenu().findItem(R.id.nav_linkedin).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/remipichon"));
                startActivity(browserIntent);
                return false;
            }
        });
        navigationView.getMenu().findItem(R.id.nav_feedback).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"pichon.remi.pr@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "An awesome app !");
                i.putExtra(Intent.EXTRA_TEXT   , "You are hired !");
                startActivity(Intent.createChooser(i, "Send mail..."));
                return false;
            }
        });
    }


}
