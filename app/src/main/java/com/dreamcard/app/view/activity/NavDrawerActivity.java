package com.dreamcard.app.view.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamcard.app.MainActivity;
import com.dreamcard.app.R;
import com.dreamcard.app.common.DatabaseController;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.entity.Categories;
import com.dreamcard.app.entity.LocationInfo;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.cloudMessaging.DreamRegistrationIntentService;
import com.dreamcard.app.utils.PreferencesGCM;
import com.dreamcard.app.view.fragments.AboutUsFragment;
import com.dreamcard.app.view.fragments.CategoriesListFragment;
import com.dreamcard.app.view.fragments.DashboardFragment;
import com.dreamcard.app.view.fragments.FAQFragment;
import com.dreamcard.app.view.fragments.FeedbackFragment;
import com.dreamcard.app.view.fragments.LatestOfferListFragment;
import com.dreamcard.app.view.fragments.LeftNavDrawerFragment;
import com.dreamcard.app.view.fragments.LocationFragment;
import com.dreamcard.app.view.fragments.StoresListFragment;
import com.dreamcard.app.view.fragments.SubcategoryFragment;
import com.dreamcard.app.view.interfaces.OnFragmentInteractionListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

public class NavDrawerActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,View.OnClickListener,OnFragmentInteractionListener {

    private static final String APP_ID = "36bf383e50c742b4b3ca7a48bd270b23";

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = NavDrawerActivity.class.getName();

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private LeftNavDrawerFragment leftNavDrawerFragment;
    private SubcategoryFragment subCategoryFragment;

    /**
     * Used to store the last screen title. For use in {@link ()}.
     */
    private CharSequence mTitle;

    private ImageButton btMenu;
    private ImageButton btnSetting;
    private TextView tvTitle;
    private ImageView imgLogo;
//    private RelativeLayout headerPnl;

    private DrawerLayout mDrawer;
    private int currentFragment=0;
    private boolean isOfferListSelected=false;
    private Button btnStores;
    private Button btnCategories;
    private Button btnLatestOffers;
    private Button btnLocations;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer_actiity);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        leftNavDrawerFragment = (LeftNavDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.left_navigation_drawer);


        mDrawer=(DrawerLayout) findViewById(R.id.drawer_layout);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                mDrawer);

        leftNavDrawerFragment.setUp(
                R.id.navigation_drawer,
                mDrawer);


        // Get menu button
        btMenu = (ImageButton) findViewById(R.id.activity_main_content_button_menu);
        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show/hide the menu
                mDrawer.openDrawer(Gravity.RIGHT);
            }
        });
        btnSetting=(ImageButton)findViewById(R.id.btn_notifications);
        btnSetting.setOnClickListener(this);

        // Get title textview
        tvTitle = (TextView) findViewById(R.id.txt_menu_title);
        imgLogo=(ImageView)findViewById(R.id.img_menu_logo);
        imgLogo.setOnClickListener(this);

        btnCategories= (Button) findViewById(R.id.btn_category);
        btnLatestOffers=(Button)findViewById(R.id.btn_browse);
        btnLocations=(Button)findViewById(R.id.btn_location);
        btnStores=(Button)findViewById(R.id.btn_store);

        btnCategories.setOnClickListener(this);
        btnLatestOffers.setOnClickListener(this);
        btnLocations.setOnClickListener(this);
        btnStores.setOnClickListener(this);

        btnStores.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.performClick();
                }
            }
        });

        btnLocations.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.performClick();
                }
            }
        });

        btnLatestOffers.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.performClick();
                }
            }
        });

        btnCategories.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.performClick();
                }
            }
        });


        loadIcons();

        if(btnSetting!=null) {
            btnSetting.setVisibility(View.GONE);
        }
        currentFragment=0;
//        onNavigationDrawerItemSelected(0);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(PreferencesGCM.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Toast.makeText(NavDrawerActivity.this, "Token sent", Toast.LENGTH_LONG);
                } else {
                    Toast.makeText(NavDrawerActivity.this, "Error !!!!!", Toast.LENGTH_LONG);
                }
            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, DreamRegistrationIntentService.class);
            startService(intent);
        }
    }

    private void loadIcons() {
        Params.FONT = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//                .commit();

        Fragment fragment = null;
        String fragmentTag=Params.FRAGMENT_DASHBOARD;
        FrameLayout mainFrameLayout=(FrameLayout)findViewById(R.id.activity_main_content_fragment);
        Intent intent;
        switch (position) {
            case 0:
                fragment = new DashboardFragment();
                this.isOfferListSelected=false;
                if(btnSetting!=null) {
                    btnSetting.setVisibility(View.GONE);
                }
                if(tvTitle != null) {
                    tvTitle.setVisibility(View.GONE);
                }
                if(imgLogo !=null) {
                    imgLogo.setVisibility(View.VISIBLE);
                }
                currentFragment = 0;
                break;
            case 1:
                LatestOfferListFragment.getType();
                fragment = LatestOfferListFragment.newInstance(Params.TYPE_OFFER, "");
                fragmentTag= Params.FRAGMENT_LATEST_OFFERS;
//                mainFrameLayout.setBackgroundColor(getResources().getColor(R.color.latest_offer_bg));
                this.isOfferListSelected=true;
                btnSetting.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.GONE);
                imgLogo.setVisibility(View.VISIBLE);

                btnCategories.setBackground(getResources().getDrawable(R.drawable.categories_bg));
                btnLatestOffers.setBackground(getResources().getDrawable(R.drawable.latest_offers_active_bg));
                btnStores.setBackground(getResources().getDrawable(R.drawable.store_button_bg));
                btnLocations.setBackground(getResources().getDrawable(R.drawable.location_button_bg));
                break;
            case 2:
                fragment = CategoriesListFragment.newInstance(null, "");
                fragmentTag=Params.FRAGMENT_CATEGORIES;
//                mainFrameLayout.setBackgroundColor(getResources().getColor(R.color.categories_bg));
                this.isOfferListSelected=false;
                btnSetting.setVisibility(View.GONE);
                tvTitle.setVisibility(View.GONE);
                imgLogo.setVisibility(View.VISIBLE);

                btnCategories.setBackground(getResources().getDrawable(R.drawable.categories_bg_active));
                btnLatestOffers.setBackground(getResources().getDrawable(R.drawable.latest_offers_bg));
                btnStores.setBackground(getResources().getDrawable(R.drawable.store_button_bg));
                btnLocations.setBackground(getResources().getDrawable(R.drawable.location_button_bg));
                break;
            case 3:
                fragment = new StoresListFragment();
                fragmentTag=Params.FRAGMENT_STORES;
//                mainFrameLayout.setBackgroundColor(getResources().getColor(R.color.stores_bg));
                this.isOfferListSelected=false;
                btnSetting.setVisibility(View.GONE);
                tvTitle.setVisibility(View.GONE);
                imgLogo.setVisibility(View.VISIBLE);
                btnCategories.setBackground(getResources().getDrawable(R.drawable.categories_bg));
                btnLatestOffers.setBackground(getResources().getDrawable(R.drawable.latest_offers_bg));
                btnStores.setBackground(getResources().getDrawable(R.drawable.stores_active_bg));
                btnLocations.setBackground(getResources().getDrawable(R.drawable.location_button_bg));
                break;
            case 4:
                fragment = new LocationFragment();
                fragmentTag=Params.FRAGMENT_LOCATIONS;
//                mainFrameLayout.setBackgroundColor(getResources().getColor(R.color.location_bg));
                this.isOfferListSelected=false;
                btnSetting.setVisibility(View.GONE);
                tvTitle.setVisibility(View.GONE);
                imgLogo.setVisibility(View.VISIBLE);
                btnCategories.setBackground(getResources().getDrawable(R.drawable.categories_bg));
                btnLatestOffers.setBackground(getResources().getDrawable(R.drawable.latest_offers_bg));
                btnStores.setBackground(getResources().getDrawable(R.drawable.store_button_bg));
                btnLocations.setBackground(getResources().getDrawable(R.drawable.location_active));

                break;
            case 5:
                intent=new Intent(this,SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_in);
                this.isOfferListSelected=false;
                break;

            case 6:
                fragment = new FAQFragment();
                fragmentTag=Params.FRAGMENT_FAQ;
                this.isOfferListSelected=false;
                btnSetting.setVisibility(View.GONE);
                break;

            case 7:
                fragment = new AboutUsFragment();
                fragmentTag=Params.FRAGMENT_ABOUTUS;
                this.isOfferListSelected=false;
                btnSetting.setVisibility(View.GONE);
                break;

            case 8:
                fragment = new FeedbackFragment();
                fragmentTag=Params.FRAGMENT_FEEDBACK;
                this.isOfferListSelected=false;
                btnSetting.setVisibility(View.GONE);
                break;

            case 9:
                DatabaseController.getInstance(this).deleteLogin();
                SharedPreferences pref=getSharedPreferences(Params.APP_DATA,MODE_PRIVATE);
                pref.edit().clear().commit();
                intent=new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition( R.anim.push_down_in, R.anim.push_down_out );
                break;
        }

        if(fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.activity_main_content_fragment, fragment, fragmentTag);
            if (fragment instanceof FAQFragment || fragment instanceof AboutUsFragment) {
                fragmentTransaction.addToBackStack(fragmentTag);
            }
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public String getActiveFragment() {

        if (getFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }

        String tag = getFragmentManager()
                .getBackStackEntryAt(getFragmentManager()
                        .getBackStackEntryCount() - 1)
                .getName();

        return tag;
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.nav_drawer_actiity, menu);
//            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String uri, String type) {
        if(type.equalsIgnoreCase(Params.FRAGMENT_STORES)){
            Intent intent=new Intent(this,StoreDetailsActivity.class);
            startActivityForResult(intent,2);
            overridePendingTransition( R.anim.push_down_in, R.anim.push_down_in );
        }else if(type.equalsIgnoreCase(Params.FRAGMENT_FEEDBACK)){
            onNavigationDrawerItemSelected(0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkForCrashes();

        leftNavDrawerFragment.setDrawerMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(PreferencesGCM.REGISTRATION_COMPLETE));
    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        UpdateManager.unregister();
        super.onPause();
    }

    private void checkForCrashes() {
        CrashManager.register(this, APP_ID);
    }
    @Override
    public void doAction(Object b, String fragment) {
        if(fragment.equalsIgnoreCase(Params.FRAGMENT_LATEST_OFFERS)){
            Offers bean= (Offers) b;
            Intent intent=new Intent(this,OfferDetailsActivity.class);
            intent.putExtra(Params.DATA,bean);
            intent.putExtra(Params.PICTURE_LIST, bean.getPicturesList());
            startActivityForResult(intent, 1);
            overridePendingTransition( R.anim.push_right_in, R.anim.push_right_out );

        }else if(fragment.equalsIgnoreCase(Params.FRAGMENT_CATEGORIES)){
            Categories bean= (Categories) b;

            FrameLayout mainFrameLayout=(FrameLayout)findViewById(R.id.activity_main_content_fragment);
            mainFrameLayout.setBackgroundColor(getResources().getColor(R.color.categories_bg));

            //Any dummy number
            currentFragment = 11;
            subCategoryFragment=SubcategoryFragment.newInstance(bean,bean.getId());
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_main_content_fragment, subCategoryFragment)
                    .commit();
            fragmentManager.executePendingTransactions();

        }else if(fragment.equalsIgnoreCase(Params.FRAGMENT_STORES)){
            Stores bean= (Stores) b;
            Intent intent=new Intent(this,StoreDetailsActivity.class);
            intent.putExtra(Params.DATA,bean);
            intent.putExtra(Params.PICTURE_LIST, bean.getPictures());
            startActivityForResult(intent, 2);
            overridePendingTransition( R.anim.push_down_in, R.anim.push_down_in );
        }else if(fragment.equalsIgnoreCase(Params.FRAGMENT_LEFT_DRAWER)){
            if(this.isOfferListSelected){
                LatestOfferListFragment.getType();
                LatestOfferListFragment f= LatestOfferListFragment.newInstance(Params.TYPE_OFFER, "");
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.activity_main_content_fragment, f)
                        .commit();
                fragmentManager.executePendingTransactions();

                f.referesh();

            }


        }else if(fragment.equalsIgnoreCase(Params.FRAGMENT_SUB_CATEGORY)){
            Categories bean = (Categories) b;

            FrameLayout mainFrameLayout=(FrameLayout)findViewById(R.id.activity_main_content_fragment);
            mainFrameLayout.setBackgroundColor(getResources().getColor(R.color.categories_bg));
            currentFragment = R.id.btn_browse;
            LatestOfferListFragment fragment1=LatestOfferListFragment.newInstance(Params.TYPE_OFFERS_BY_CAT,bean.getId());
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_main_content_fragment, fragment1)
                    .commit();
            btnCategories.setBackground(getResources().getDrawable(R.drawable.categories_bg));
            btnLatestOffers.setBackground(getResources().getDrawable(R.drawable.latest_offers_active_bg));

            mNavigationDrawerFragment.selectItem(1);
            String currentMenu=getResources().getString(R.string.offers)+" "+bean.getTitle();
            tvTitle.setText(currentMenu);
            tvTitle.setVisibility(View.VISIBLE);
            imgLogo.setVisibility(View.GONE);
        }else if(fragment.equalsIgnoreCase(Params.FRAGMANT_CATEGORY_EMPTY)){
            Categories bean = (Categories) b;

            FrameLayout mainFrameLayout=(FrameLayout)findViewById(R.id.activity_main_content_fragment);
            mainFrameLayout.setBackgroundColor(getResources().getColor(R.color.categories_bg));
            currentFragment = R.id.btn_browse;
            LatestOfferListFragment fragment1=LatestOfferListFragment.newInstance(Params.TYPE_OFFERS_BY_CAT,bean.getId());
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_main_content_fragment, fragment1)
                    .commit();
            btnCategories.setBackground(getResources().getDrawable(R.drawable.categories_bg));
            btnLatestOffers.setBackground(getResources().getDrawable(R.drawable.latest_offers_active_bg));

            String currentMenu=getResources().getString(R.string.offers)+" "+bean.getTitle();

            tvTitle.setVisibility(View.GONE);
            imgLogo.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_category) {
            if(currentFragment!=R.id.btn_category) {
                currentFragment = R.id.btn_category;
                onNavigationDrawerItemSelected(2);
            }
        }else if(view.getId()==R.id.btn_browse) {
            if(currentFragment!=R.id.btn_browse) {
                currentFragment = R.id.btn_browse;
                onNavigationDrawerItemSelected(1);
            }
        }else if(view.getId()==R.id.btn_location){
            if(currentFragment!=view.getId()) {
                currentFragment=view.getId();
                onNavigationDrawerItemSelected(4);
            }
        }else if(view.getId()==R.id.btn_store){
            if(currentFragment!=R.id.btn_store) {
                currentFragment = R.id.btn_store;
                onNavigationDrawerItemSelected(3);
            }
        }else if(view.getId()==R.id.btn_notifications) {
            callSetting();
        }else if(view.getId()==R.id.img_menu_logo){
            LeftNavDrawerFragment.setDrawerMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            onNavigationDrawerItemSelected(0);
        }
    }

    private void callSetting() {
        mDrawer.openDrawer(Gravity.LEFT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK || resultCode == Params.NAVIGATE) {
                if(data != null) {
                    final LocationInfo location = data.getParcelableExtra("location");
                    currentFragment = R.id.btn_location;
                    new Handler().post(new Runnable() {
                        public void run() {
                            LocationFragment fragment = LocationFragment.newInstance(location, "");
                            String fragmentTag = Params.FRAGMENT_LOCATIONS;
                            FrameLayout mainFrameLayout = (FrameLayout) findViewById(R.id.activity_main_content_fragment);
                            mainFrameLayout.setBackgroundColor(getResources().getColor(R.color.location_bg));

                            btnCategories.setBackground(getResources().getDrawable(R.drawable.categories_bg));
                            btnLatestOffers.setBackground(getResources().getDrawable(R.drawable.latest_offers_bg));
                            btnStores.setBackground(getResources().getDrawable(R.drawable.store_button_bg));
                            btnLocations.setBackground(getResources().getDrawable(R.drawable.location_active));

                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.activity_main_content_fragment, fragment, fragmentTag).addToBackStack(fragmentTag);
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            fragmentTransaction.commit();
                        }
                    });
                }
            }else if(resultCode == 100){
                LatestOfferListFragment f= LatestOfferListFragment.newInstance(Params.TYPE_OFFER, "");
                f.referesh();
            }else if(resultCode == Params.STATUS_MOVE_TO_DASHBOARD){
                onNavigationDrawerItemSelected(0);
            }else if(resultCode == Params.STATUS_NOTHING){
                finish();
            }
        }else if(requestCode==2){
            if(resultCode == Params.STATUS_MOVE_TO_DASHBOARD){
                onNavigationDrawerItemSelected(0);
            }
            if (resultCode == Params.NAVIGATE && data != null) {
                final LocationInfo location = data.getParcelableExtra("location");
                currentFragment = R.id.btn_location;
                new Handler().post(new Runnable() {
                    public void run() {
                        LocationFragment fragment = LocationFragment.newInstance(location, "");
                        String fragmentTag = Params.FRAGMENT_LOCATIONS;
                        FrameLayout mainFrameLayout = (FrameLayout) findViewById(R.id.activity_main_content_fragment);
                        mainFrameLayout.setBackgroundColor(getResources().getColor(R.color.location_bg));

                        btnCategories.setBackground(getResources().getDrawable(R.drawable.categories_bg));
                        btnLatestOffers.setBackground(getResources().getDrawable(R.drawable.latest_offers_bg));
                        btnStores.setBackground(getResources().getDrawable(R.drawable.store_button_bg));
                        btnLocations.setBackground(getResources().getDrawable(R.drawable.location_active));

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.activity_main_content_fragment, fragment, fragmentTag).addToBackStack(fragmentTag);
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        fragmentTransaction.commit();
                    }
                });
            }
        }else if(resultCode == Params.STATUS_NOTHING){
            finish();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_nav_drawer_actiity, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((NavDrawerActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(Gravity.RIGHT)) {
            Log.i(this.getClass().getName(), "Drawer opened ... closing");
            mDrawer.closeDrawer(Gravity.RIGHT);
            return;
        }

        if (mDrawer.isDrawerOpen(Gravity.LEFT)) {
            Log.i(this.getClass().getName(), "Drawer on left is open ... closing");
            mDrawer.closeDrawer(Gravity.LEFT);
            return;
        }

        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            int i=manager.getBackStackEntryCount();
            Log.d("", "" + i);
            manager.popBackStackImmediate();
        }else {
            finish();
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
