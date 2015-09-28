package com.dreamcard.app.view.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dreamcard.app.R;

import com.dreamcard.app.common.DatabaseController;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.entity.SearchCriteria;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.services.OffersByFilterAsync;
import com.dreamcard.app.view.adapters.LatestOffersListAdapter;
import com.dreamcard.app.view.interfaces.IServiceListener;
import com.dreamcard.app.view.interfaces.OnFragmentInteractionListener;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {}
 * interface.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LatestOfferListFragment extends Fragment implements View.OnClickListener, IServiceListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static int typeParam;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private GridView grid;
    private LatestOffersListAdapter adapter;
    private ProgressBar progressBar;

    private ArrayList<Offers> list = new ArrayList<Offers>();
    private int count = 0;
    private int process;

    static LatestOfferListFragment fragment = null;

    private OffersByFilterAsync allOffersAsync;

    public static LatestOfferListFragment newInstance(int type, String param2) {

        if (type == Params.TYPE_OFFERS_BY_CAT) {
            fragment = null;
        }
        if (fragment == null) {
            fragment = new LatestOfferListFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_PARAM1, type);
            args.putString(ARG_PARAM2, param2);
            fragment.setArguments(args);
        }

        return fragment;
    }

    public static int getType() {
        if (typeParam == Params.TYPE_OFFERS_BY_CAT) {
            typeParam = Params.TYPE_OFFER;
        }
        return typeParam;
    }

    public LatestOfferListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            typeParam = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latestofferlist, container, false);

        grid = (GridView) view.findViewById(R.id.products_grid);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        referesh();
        return view;
    }

    public void referesh() {
        progressBar.setVisibility(View.VISIBLE);
        grid.setVisibility(View.GONE);
        this.process = Params.SERVICE_PROCESS_1;
        ArrayList<ServiceRequest> list = ServicesConstants.getLatestOfferRequestList("100");
        if (this.typeParam == Params.TYPE_OFFERS_BY_CAT) {
            list = ServicesConstants.getOffersByCategoryRequestList(this.mParam2);
        } else {
            SearchCriteria criteria = DatabaseController.getInstance(getActivity()).getCriteria();
            if (criteria != null) {
                list = ServicesConstants.getOffersByFilterRequestParams(criteria);
            } else {
                list = new ArrayList<ServiceRequest>();
            }
        }
        allOffersAsync = new OffersByFilterAsync(this, list
                , Params.SERVICE_PROCESS_1, this.typeParam);
        allOffersAsync.execute(getActivity());
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
        allOffersAsync.cancel(true);
        mListener = null;
    }

    @Override
    public void onServiceSuccess(Object b, int processType) {
        if (getActivity() == null) {
            Log.e(this.getClass().getName(), "Activity is null, avoid callback");
            return;
        }
        if (processType == Params.SERVICE_PROCESS_1) {
            ArrayList<Offers> list = (ArrayList<Offers>) b;
            this.list = list;
            adapter = new LatestOffersListAdapter(getActivity(), list, this);
            grid.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            grid.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {
        if (getActivity() == null) {
            Log.e(this.getClass().getName(), "Activity is null, avoid callback");
            return;
        }
        if (this.process == Params.SERVICE_PROCESS_1) {
            progressBar.setVisibility(View.GONE);
            grid.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), info.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            this.count++;
            if (this.count == this.list.size()) {
                adapter = new LatestOffersListAdapter(getActivity(), list, this);
                grid.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                grid.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        Offers data = null;
        int x = view.getId();
        for (Offers bean : this.list) {
            if (bean.getPosition() == view.getId()) {
                data = bean;
                break;
            }
        }
        if (data != null) {
            mListener.doAction(data, Params.FRAGMENT_LATEST_OFFERS);
        }
    }
}
