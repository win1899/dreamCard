package com.dreamcard.app.view.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dreamcard.app.MainActivity;
import com.dreamcard.app.R;
import com.dreamcard.app.common.DatabaseController;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.entity.Categories;
import com.dreamcard.app.entity.LocationInfo;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.adapters.NavDrawerItem;
import com.dreamcard.app.view.adapters.NavDrawerListAdapter;
import com.dreamcard.app.view.fragments.CategoriesListFragment;
import com.dreamcard.app.view.fragments.DashboardFragment;
import com.dreamcard.app.view.fragments.FragmentMain;
import com.dreamcard.app.view.fragments.LatestOfferListFragment;
import com.dreamcard.app.view.fragments.LocationFragment;
import com.dreamcard.app.view.fragments.StoresListFragment;
import com.dreamcard.app.view.interfaces.OnFragmentInteractionListener;
import com.dreamcard.app.view.layout.MainLayout;

import java.util.ArrayList;

public class SlidingMenuActivity extends FragmentActivity implements FragmentMain.OnFragmentInteractionListener
    ,View.OnClickListener,OnFragmentInteractionListener{

    // The MainLayout which will hold both the sliding menu and our main content
    // Main content will holds our Fragment respectively
    MainLayout mainLayout;

    // ListView menu
    private ListView lvMenu;
    private String[] lvMenuItems;

    // Menu button
    ImageButton btMenu;
    private ImageButton btnSetting;

    // Title according to fragment
    TextView tvTitle;
    private ImageView imgLogo;
    private String currentMenu;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private int currentFragment=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sliding_menu);
        // Inflate the mainLayout
        mainLayout = (MainLayout)this.getLayoutInflater().inflate(R.layout.activity_sliding_menu, null);
        setContentView(mainLayout);

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

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

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(3, -1)));

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(3, -1)));

        // Init menu

        lvMenuItems = getResources().getStringArray(R.array.nav_drawer_items);
        lvMenu = (ListView) findViewById(R.id.activity_main_menu_listview);
//        lvMenu.setAdapter(new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, lvMenuItems));
        NavDrawerListAdapter adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        lvMenu.setAdapter(adapter);
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onMenuItemClick(parent, view, position, id);
            }

        });


        // Get menu button
        btMenu = (ImageButton) findViewById(R.id.activity_main_content_button_menu);
        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show/hide the menu
                toggleMenu(v);
            }
        });
        btnSetting=(ImageButton)findViewById(R.id.btn_notifications);
        btnSetting.setOnClickListener(this);

        // Get title textview
        tvTitle = (TextView) findViewById(R.id.txt_menu_title);
        imgLogo=(ImageView)findViewById(R.id.img_menu_logo);
        imgLogo.setOnClickListener(this);

        Button btnCategories= (Button) findViewById(R.id.btn_category);
        Button btnLatestOffers=(Button)findViewById(R.id.btn_browse);
        Button btnLocations=(Button)findViewById(R.id.btn_location);
        Button btnStores=(Button)findViewById(R.id.btn_store);

        btnCategories.setOnClickListener(this);
        btnLatestOffers.setOnClickListener(this);
        btnLocations.setOnClickListener(this);
        btnStores.setOnClickListener(this);

        this.currentMenu=navMenuTitles[0];

        loadIcons();
        displayView(0,true);

        // Add FragmentMain as the initial fragment
//        FragmentManager fm = SlidingMenuActivity.this.getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//
//        FragmentMain fragment = new FragmentMain();
//        ft.add(R.id.activity_main_content_fragment, fragment);
//        ft.commit();
    }
    private void loadIcons() {
        Params.FONT = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
    }

    public void toggleMenu(View v){
        mainLayout.toggleMenu();

//        if(this.mainLayout.currentMenuState==MainLayout.MenuState.HIDDEN){
//            lvMenu.setCacheColorHint(getResources().getColor(R.color.list_color_hint));
//        }
    }

    // Perform action when a menu item is clicked
    private void onMenuItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = lvMenuItems[position];
//        String currentItem = tvTitle.getText().toString();

        // Do nothing if selectedItem is currentItem
        if(selectedItem.compareTo(this.currentMenu) == 0) {
            mainLayout.toggleMenu();
            return;
        }

//        FragmentManager fm = SlidingMenuActivity.this.getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
       displayView(position,false);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void displayView(int position,boolean isFirstTime) {
        Fragment fragment = null;
        String fragmentTag=Params.FRAGMENT_DASHBOARD;
        FrameLayout mainFrameLayout=(FrameLayout)findViewById(R.id.activity_main_content_fragment);
        switch (position) {
            case 0:
                fragment = new DashboardFragment();
                break;
            case 1:
                fragment = LatestOfferListFragment.newInstance(Params.TYPE_OFFER,"");
                fragmentTag= Params.FRAGMENT_LATEST_OFFERS;
                mainFrameLayout.setBackgroundColor(getResources().getColor(R.color.latest_offer_bg));
                break;
            case 2:
                fragment = new CategoriesListFragment();
                fragmentTag=Params.FRAGMENT_CATEGORIES;
                mainFrameLayout.setBackgroundColor(getResources().getColor(R.color.categories_bg));
                break;
            case 3:
                fragment = new StoresListFragment();
                fragmentTag=Params.FRAGMENT_STORES;
                mainFrameLayout.setBackgroundColor(getResources().getColor(R.color.stores_bg));
                break;
            case 4:
                fragment = new LocationFragment();
                fragmentTag=Params.FRAGMENT_LOCATIONS;
                mainFrameLayout.setBackgroundColor(getResources().getColor(R.color.location_bg));

//                android.app.FragmentManager fragmentManager = getFragmentManager();
//                android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.activity_main_content_fragment, fragment2,fragmentTag).addToBackStack(fragmentTag);
//                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                fragmentTransaction.commit();
//                android.app.FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.activity_main_content_fragment, fragment2).addToBackStack(fragmentTag).commit();
//
//                lvMenu.setItemChecked(position, true);
//                lvMenu.setSelection(position);
//                this.currentMenu=navMenuTitles[position];
                break;
            case 5:
                DatabaseController.getInstance(this).deleteLogin();
                SharedPreferences pref=getSharedPreferences(Params.APP_DATA,MODE_PRIVATE);
                pref.edit().clear().commit();
                Intent intent=new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition( R.anim.push_down_in, R.anim.push_down_out );
                break;
        }

        if(fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.activity_main_content_fragment, fragment, fragmentTag).addToBackStack(fragmentTag);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commitAllowingStateLoss();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.activity_main_content_fragment, fragment,fragmentTag).addToBackStack(fragmentTag).commit();

            lvMenu.setItemChecked(position, true);
            lvMenu.setSelection(position);
            this.currentMenu=navMenuTitles[position];
//            tvTitle.setText(this.currentMenu);
//            if(position==0){
//                tvTitle.setVisibility(View.GONE);
//                imgLogo.setVisibility(View.VISIBLE);
//            }else{
//                tvTitle.setVisibility(View.VISIBLE);
//                imgLogo.setVisibility(View.GONE);
//            }
        }else{

        }
        // Hide menu anyway
        if(!isFirstTime)
            mainLayout.toggleMenu();
    }

    @Override
    public void onBackPressed() {
        if (mainLayout.isMenuShown()) {
            mainLayout.toggleMenu();
        }
        else {
            FragmentManager manager = getSupportFragmentManager();
            if (manager.getBackStackEntryCount() > 0) {
                int i=manager.getBackStackEntryCount();
                Log.d("",""+i);
            }
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sliding_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(String uri, String type) {
        if(type.equalsIgnoreCase(Params.FRAGMENT_STORES)){
            Intent intent=new Intent(this,StoreDetailsActivity.class);
            startActivityForResult(intent,2);
            overridePendingTransition( R.anim.push_down_in, R.anim.push_down_in );
        }
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

//            OfferDetailsFragment frag=new OfferDetailsFragment().newInstance(bean);
//            android.app.FragmentManager fragmentManager = getFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.activity_main_content_fragment, frag).addToBackStack(Params.FRAGMENT_OFFER_DETAILS).commit();

        }else if(fragment.equalsIgnoreCase(Params.FRAGMENT_CATEGORIES)){
            Categories bean= (Categories) b;
            FrameLayout mainFrameLayout=(FrameLayout)findViewById(R.id.activity_main_content_fragment);
            mainFrameLayout.setBackgroundColor(getResources().getColor(R.color.categories_bg));
            LatestOfferListFragment fragment1=LatestOfferListFragment.newInstance(Params.TYPE_OFFERS_BY_CAT,bean.getId());
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_main_content_fragment, fragment1)
                    .addToBackStack(Params.FRAGMENT_CATEGORIES).commit();

            lvMenu.setItemChecked(1, true);
            lvMenu.setSelection(1);
            this.currentMenu=getResources().getString(R.string.offers)+" "+bean.getTitle();
            tvTitle.setText(this.currentMenu);
            tvTitle.setVisibility(View.VISIBLE);
            imgLogo.setVisibility(View.GONE);
        }else if(fragment.equalsIgnoreCase(Params.FRAGMENT_STORES)){
            Stores bean= (Stores) b;
            Intent intent=new Intent(this,StoreDetailsActivity.class);
            intent.putExtra(Params.DATA,bean);
            intent.putExtra(Params.PICTURE_LIST, bean.getPictures());
            startActivityForResult(intent,2);
            overridePendingTransition( R.anim.push_down_in, R.anim.push_down_in );
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_category) {
            currentFragment=R.id.btn_category;
            displayView(2, true);
        }else if(view.getId()==R.id.btn_browse) {
            currentFragment=R.id.btn_browse;
            displayView(1, true);
        }else if(view.getId()==R.id.btn_location){
            if(currentFragment!=view.getId()) {
                currentFragment=view.getId();
                displayView(4, true);
            }
        }else if(view.getId()==R.id.btn_store){
            currentFragment=R.id.btn_store;
            displayView(3, true);
        }else if(view.getId()==R.id.btn_notifications) {
            callSetting();
        }else if(view.getId()==R.id.img_menu_logo){
            displayView(0, true);
        }
    }

    private void callSetting() {
        if (Utils.promoteActivation(this)) {
            return;
        }
        Intent intent=new Intent(this,SettingActivity.class);
        startActivity(intent);
        overridePendingTransition( R.anim.push_down_in, R.anim.push_down_in );
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        Fragment fragment=new SettingFragment();
//        fragmentTransaction.replace(R.id.activity_main_content_fragment,fragment,Params.FRAGMENT_SETTING).addToBackStack(Params.FRAGMENT_SETTING);
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                final LocationInfo location = data.getParcelableExtra("location");

                new Handler().post(new Runnable() {
                    public void run() {
                LocationFragment fragment = LocationFragment.newInstance(location,"");
                String fragmentTag=Params.FRAGMENT_LOCATIONS;
                FrameLayout mainFrameLayout=(FrameLayout)findViewById(R.id.activity_main_content_fragment);
                mainFrameLayout.setBackgroundColor(getResources().getColor(R.color.location_bg));

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.activity_main_content_fragment, fragment, fragmentTag).addToBackStack(fragmentTag);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
                    }
                });
            }else if(resultCode == 100){
                displayView(1, true);
            }else if(resultCode == Params.STATUS_MOVE_TO_DASHBOARD){
                displayView(0,true);
            }
        }else if(requestCode==2){
            if(resultCode == Params.STATUS_MOVE_TO_DASHBOARD){
                displayView(0,true);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
}
