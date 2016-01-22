package com.dreamcard.app.view.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.LocationInfo;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.services.AllBusinessAsync;
import com.dreamcard.app.utils.ImageViewLoader;
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.adapters.MarkerInfoWindowAdapter;
import com.dreamcard.app.view.interfaces.ILocationListener;
import com.dreamcard.app.view.interfaces.IServiceListener;
import com.dreamcard.app.view.interfaces.OnFragmentInteractionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LocationFragment extends Fragment implements IServiceListener, View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LocationInfo mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private GoogleMap map;
    //    private ProgressBar progressBar;
    private View view;

    private ArrayList<Stores> list = new ArrayList<Stores>();
    private double latitude;
    private double longitude;
    private Activity activity;
    private AllBusinessAsync allBusinessAsync;
    private TextView storeName;
    private TextView storeAddress;
    private TextView storePhone;
    private ImageView storeLogo;
    private ImageView navigateToStore;
    private Stores selectedStore;
    private RelativeLayout navPanel;
    private Marker selectedMarker;

    public static LocationFragment newInstance(LocationInfo param1, String param2) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getParcelable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_location, container, false);

        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }
        map = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                selectStore(marker);
                return true;
            }
        });

        storeName = (TextView) view.findViewById(R.id.map_store_name);
        storeName.setOnClickListener(this);
        storeAddress = (TextView) view.findViewById(R.id.map_address);
        storeAddress.setOnClickListener(this);
        storeLogo = (ImageView) view.findViewById(R.id.map_store_logo);
        storeLogo.setOnClickListener(this);
        navigateToStore = (ImageView) view.findViewById(R.id.map_gps_button);
        storePhone = (TextView) view.findViewById(R.id.map_phone_num);
        storePhone.setOnClickListener(this);

        navPanel = (RelativeLayout) view.findViewById(R.id.navigation_panel);

        navigateToStore.setOnClickListener(this);

        allBusinessAsync = new AllBusinessAsync(this, new ArrayList<ServiceRequest>()
                , Params.SERVICE_PROCESS_1);
        allBusinessAsync.execute(getActivity());
        return view;
    }

    private void selectStore(Marker marker) {
        for (Stores bean : this.list) {
            if (marker.getTitle().equalsIgnoreCase(bean.getStoreName())) {
                selectedStore = bean;
                storeName.setText(selectedStore.getStoreName());
                storeAddress.setText(selectedStore.getAddress1());
                storePhone.setText(selectedStore.getPhone());
                Utils.loadImage(activity, selectedStore.getLogo(), storeLogo);
                break;
            }
        }
        if (selectedStore != null) {
            navPanel.setVisibility(View.VISIBLE);
        }

        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_ping_selected));
        if (selectedMarker != null) {
            selectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_unselected));
        }
        selectedMarker = marker;
    }

    private LocationInfo getLocationInfo() {
        ILocationListener listener = new ILocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("AdsAdvisor", "Location changes " + location.getLatitude());
            }
        };
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, listener);
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (this.mParam1 != null) {
            if (mParam1.getLatitude() > 0) {
                if (location == null) {
                    location = new Location("Passive");
                }
                location.setLatitude(mParam1.getLatitude());
                location.setLongitude(mParam1.getLongitude());
            }
        }

        LocationInfo bean = new LocationInfo();
        if (location != null) {
            bean.setLatitude(location.getLatitude());
            bean.setLongitude(location.getLongitude());
        } else {
            bean.setLatitude(31.8890726);
            bean.setLongitude(35.2195921);
        }
        return bean;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = activity;
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        allBusinessAsync.cancel(true);
        mListener = null;
    }

    private void getAroundLocation() {
        LocationInfo info = getLocationInfo();
        this.latitude = info.getLatitude();
        this.longitude = info.getLongitude();
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(info.getLatitude(), info.getLongitude())).zoom(11).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void addStoresToMap() {
        map.setMyLocationEnabled(true);
        int count = 0;
        for (Stores bean : this.list) {
            if (bean.getLongitude() != null && !bean.getLongitude().equalsIgnoreCase("null")
                    && bean.getLatitude() != null && !bean.getLatitude().equalsIgnoreCase("null")) {
                if (bean.getLatitude().length() > 0 && bean.getLongitude().length() > 0) {
                    double latitude = Double.parseDouble(bean.getLatitude());
                    double longitude = Double.parseDouble(bean.getLongitude());
                    View markerx = ((LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker_layout, null);

                    ImageView markerIcon = (ImageView) markerx.findViewById(R.id.marker_icon);
                    TextView markerLabel = (TextView) markerx.findViewById(R.id.marker_label);

                    markerLabel.setText(bean.getStoreName());
                    ImageViewLoader imgLoader = new ImageViewLoader(this.activity);
                    imgLoader.DisplayImage(bean.getLogo(), markerIcon, this.activity.getResources());


                    MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude))
                            .title(bean.getStoreName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_unselected));

                    Marker mark = map.addMarker(marker);
                    try {
                        if (getLocationInfo().getLatitude() == Double.parseDouble(bean.getLatitude())
                                && getLocationInfo().getLongitude() == Double.parseDouble(bean.getLongitude())) {
                            selectStore(mark);
                        }
                    } catch (Exception e) {

                    }
                    count++;

                }

            }
        }

        if (count == 0) {
            Stores store = new Stores();
            store.setId("1");
            store.setStoreName("Store 1");
            store.setPosition(-1);
            this.list.add(store);
            ArrayList<LocationInfo> list2 = dummyLocations();
            for (LocationInfo bean : list2) {
                MarkerOptions marker = new MarkerOptions().position(new LatLng(bean.getLatitude(), bean.getLongitude()))
                        .title(bean.getName());
                Marker mark = map.addMarker(marker);
                map.setInfoWindowAdapter(new MarkerInfoWindowAdapter(getActivity(), null, this));
                selectStore(mark);
            }
        }

    }

    private ArrayList<LocationInfo> dummyLocations() {
        ArrayList<LocationInfo> list = new ArrayList<LocationInfo>();
        LocationInfo bean = new LocationInfo();
        bean.setName("Store 1");
        bean.setLatitude(31.9062642);
        bean.setLongitude(35.2122460);
        list.add(bean);

        return list;
    }

    @Override
    public void onServiceSuccess(Object b, int processType) {
        if (getActivity() == null) {
            Log.e(this.getClass().getName(), "Activity is null, avoid callback");
            return;
        }
        if (processType == Params.SERVICE_PROCESS_1) {
            ArrayList<Stores> list = (ArrayList<Stores>) b;

            this.list = list;
            getAroundLocation();
            addStoresToMap();
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment f = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.map);
        if (f != null)
            getActivity().getFragmentManager().beginTransaction().remove(f).commit();
    }

    @Override
    public void onClick(View view) {
        Stores info = null;
        for (Stores bean : this.list) {
            if (view.getId() == bean.getPosition()) {
                info = bean;
                break;
            }
        }
        if (info != null) {
            mListener.doAction(info, Params.FRAGMENT_STORES);
        }

        if (view.getId() == R.id.map_store_logo
                || view.getId() == R.id.map_store_name
                || view.getId() == R.id.map_address) {
            loadStoreInfo();
        }

        if (view.getId() == R.id.map_gps_button) {
            if (selectedStore == null) {
                return;
            }

            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(String.format("google.navigation:ll=%s,%s&mode=c", selectedStore.getLatitude(), selectedStore.getLongitude())));
            startActivity(intent);
        }
    }

    private void loadStoreInfo() {
        if (selectedStore != null) {
            mListener.doAction(selectedStore, Params.FRAGMENT_STORES);
        }
    }
}
