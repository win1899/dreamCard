package com.dreamcard.app.view.activity;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.view.adapters.NavDrawerItem;
import com.dreamcard.app.view.adapters.NavDrawerListAdapter;
import com.dreamcard.app.view.fragments.CategoriesListFragment;
import com.dreamcard.app.view.fragments.DashboardFragment;
import com.dreamcard.app.view.fragments.LatestOfferListFragment;

import java.util.ArrayList;

public class SliderMenuActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    private int adType;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_menu);

        buildUI();

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void buildUI() {
        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        // Pages
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
//        // What's hot, We  will add a counter here
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));


        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_title_bar,null);
//        getActionBar().setDisplayShowCustomEnabled(true);
//        getActionBar().setCustomView(actionBarLayout);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.slider_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        String fragmentTag=Params.FRAGMENT_DASHBOARD;
        switch (position) {
//            case 0:
//                fragment = new DashboardFragment();
//                break;
//            case 1:
//                fragment = new LatestOfferListFragment();
//                fragmentTag=Params.FRAGMENT_LATEST_OFFERS;
//                break;
//            case 2:
////                Intent intent=getIntent();
////                intent.putExtra(IntentConstants.AD_TYPE, this.adType);
//                fragment = new CategoriesListFragment();
//                fragmentTag=Params.FRAGMENT_CATEGORIES;
//                break;
            case 3:
//                intent=getIntent();
//                intent.putExtra(IntentConstants.AD_TYPE, Constants.ADS_ALL_ADS);
//                fragment = new AdsListFragment(Constants.ADS_ALL_ADS);
                break;
            case 100:
//                fragment = new AdsListFragment(Constants.ADS_AD_BY_STORE);
                break;
//            case 4:
//                fragment = new PagesFragment();
//                break;
//            case 5:
//                fragment = new WhatsHotFragment();
//                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).addToBackStack(fragmentTag).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Category fragment
     */


    public void onFragmentInteraction(String uri, String type) {
        if(uri.equalsIgnoreCase(Params.FRAGMENT_CATEGORIES)){
            displayView(2);
        }else if(uri.equalsIgnoreCase(Params.FRAGMENT_LATEST_OFFERS))
            displayView(1);
        else if(uri.equalsIgnoreCase(Params.FRAGMENT_LOCATIONS))
            displayView(1);
        else if(uri.equalsIgnoreCase(Params.FRAGMENT_STORES))
            displayView(1);
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }
}
