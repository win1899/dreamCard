package com.dreamcard.app.view.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.services.ConsumerDiscountAsyncTask;
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.adapters.NotificationGridAdapter;
import com.dreamcard.app.view.interfaces.IServiceListener;
import com.dreamcard.app.view.interfaces.OnFragmentInteractionListener;

import java.util.ArrayList;

/**
 * A simple {@link} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DashboardFragment extends Fragment implements View.OnClickListener, IServiceListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private GridView grid;
    private NotificationGridAdapter adapter;

    private ConsumerDiscountAsyncTask consumerDiscountAsyncTask;
    private ArrayList<Offers> notificationList = new ArrayList<Offers>();
    private ImageButton btnGas;
    private ImageButton btnCashPoints;
    private ImageButton btnCash;
    private ImageButton btnMobile;

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        showCashFragment();

        grid = (GridView) view.findViewById(R.id.notifications_grid);

        btnCash = (ImageButton) view.findViewById(R.id.btn_cash);
        btnCash.setOnClickListener(this);

        btnGas = (ImageButton) view.findViewById(R.id.btn_gas);
        btnGas.setOnClickListener(this);

        btnCashPoints = (ImageButton) view.findViewById(R.id.btn_cash_points);
        btnCashPoints.setOnClickListener(this);

        btnMobile = (ImageButton) view.findViewById(R.id.btn_mobile);
        btnMobile.setOnClickListener(this);

        SharedPreferences prefs = getActivity().getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
        String id = prefs.getString(Params.USER_INFO_ID, "");
        String name = Utils.getUserName(getActivity());

        consumerDiscountAsyncTask = new ConsumerDiscountAsyncTask(this
                , ServicesConstants.getTotalSavingRequestList(id)
                , Params.SERVICE_PROCESS_3);
        consumerDiscountAsyncTask.execute(getActivity());

        ArrayList<ServiceRequest> list = ServicesConstants.getLatestOfferRequestList("100");
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (consumerDiscountAsyncTask != null && consumerDiscountAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            consumerDiscountAsyncTask.cancel(true);
        }

        mListener = null;
    }

    @Override
    public void onClick(View view) {
        Offers info = null;
        for (Offers bean : this.notificationList) {
            if (view.getId() == bean.getPosition()) {
                info = bean;
                break;
            }
        }
        if (info != null) {
            mListener.doAction(info, Params.FRAGMENT_LATEST_OFFERS);
        }

        if (view.getId() == R.id.btn_cash) {
            btnCashPoints.setImageDrawable(getResources().getDrawable(R.drawable.points_icon_inactive));
            btnMobile.setImageDrawable(getResources().getDrawable(R.drawable.mobile_icon_inactive));
            btnGas.setImageDrawable(getResources().getDrawable(R.drawable.gas_inactive));

            btnCash.setImageDrawable(getResources().getDrawable(R.drawable.cash_icon_active));

            if (!Utils.promoteActivation(getActivity())) {
                showCashFragment();
            }
        }
        else if (view.getId() == R.id.btn_cash_points) {
            btnCash.setImageDrawable(getResources().getDrawable(R.drawable.cash_icon_inactive));
            btnMobile.setImageDrawable(getResources().getDrawable(R.drawable.mobile_icon_inactive));
            btnGas.setImageDrawable(getResources().getDrawable(R.drawable.gas_inactive));

            btnCashPoints.setImageDrawable(getResources().getDrawable(R.drawable.points_icon));

            if (!Utils.promoteActivation(getActivity())) {
                showCashPointsFragment();
            }
        }
        else if (view.getId() == R.id.btn_mobile) {
            btnCash.setImageDrawable(getResources().getDrawable(R.drawable.cash_icon_inactive));
            btnGas.setImageDrawable(getResources().getDrawable(R.drawable.gas_inactive));
            btnCashPoints.setImageDrawable(getResources().getDrawable(R.drawable.points_icon_inactive));

            btnMobile.setImageDrawable(getResources().getDrawable(R.drawable.mobile_icon_active));

            if (!Utils.promoteActivation(getActivity())) {
                showMobileFragment();
            }
        }
        else if (view.getId() == R.id.btn_gas) {
            btnCash.setImageDrawable(getResources().getDrawable(R.drawable.cash_icon_inactive));
            btnMobile.setImageDrawable(getResources().getDrawable(R.drawable.mobile_icon_inactive));
            btnCashPoints.setImageDrawable(getResources().getDrawable(R.drawable.points_icon_inactive));

            btnGas.setImageDrawable(getResources().getDrawable(R.drawable.gas_active));

            if (!Utils.promoteActivation(getActivity())) {
                showGasFragment();
            }
        }

        btnCash.setScaleType(ImageView.ScaleType.FIT_CENTER);
        btnMobile.setScaleType(ImageView.ScaleType.FIT_CENTER);
        btnCashPoints.setScaleType(ImageView.ScaleType.FIT_CENTER);
        btnGas.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    private void showCashFragment() {
        CashDashboardFragment fragment = CashDashboardFragment.newInstance();
        if (fragment == null) {
            return;
        }

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.dashboard_fragment_holder, fragment);
        ft.commit();
    }

    private void showGasFragment() {
        GasDashboardFragment fragment = GasDashboardFragment.newInstance();
        if (fragment == null) {
            return;
        }

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.dashboard_fragment_holder, fragment);
        ft.commit();
    }

    private void showMobileFragment() {
        MobileDashboardFragment fragment = MobileDashboardFragment.newInstance();
        if (fragment == null) {
            return;
        }

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.dashboard_fragment_holder, fragment);
        ft.commit();
    }

    private void showCashPointsFragment() {
        PointsDashboardFragment fragment = PointsDashboardFragment.newInstance();
        if (fragment == null) {
            return;
        }

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.dashboard_fragment_holder, fragment);
        ft.commit();
    }


    @Override
    public void onServiceSuccess(Object b, int processType) {
        if (getActivity() == null) {
            Log.e(this.getClass().getName(), "Activity is null, avoid callback");
            return;
        }
        if (processType == Params.SERVICE_PROCESS_3) {
            ArrayList<Offers> list = (ArrayList<Offers>) b;
            Params.DISCOUNT_LIST = list;
            if (list.size() > 0) {
                setDiscountInfo(list);
            }
        }
    }

    private void setDiscountInfo(ArrayList<Offers> list) {
        if (list != null && !list.isEmpty()) {
            Offers bean = list.get(list.size() - 1);

            //build notification list
            notificationList = new ArrayList<Offers>();
            if (list.size() > 0) {
                int i = 0;
                for (int index = list.size() - 1; index >= 0; index--) {
                    Offers offer = list.get(index);
                    notificationList.add(offer);
                    i++;
                    if (i == 3)
                        break;
                }
                adapter = null;
                adapter = new NotificationGridAdapter(getActivity(), this.notificationList);
                grid.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {

    }

}
