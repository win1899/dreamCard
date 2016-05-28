package com.dreamcard.app.view.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.ConsumerInfo;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.MessageInfo;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.services.ConsumerDiscountAsyncTask;
import com.dreamcard.app.services.TotalCashPointsAsync;
import com.dreamcard.app.services.TotalSavingAsync;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by WIN on 5/27/2016.
 */
public class MobileDashboardFragment extends Fragment {

    public static MobileDashboardFragment newInstance() {
        MobileDashboardFragment fragment = new MobileDashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MobileDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard_mobile, container, false);

        return view;
    }

}
