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

import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.Categories;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.GridItem;
import com.dreamcard.app.entity.ItemButton;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.services.CategoriesAsync;
import com.dreamcard.app.view.adapters.CategoriesListAdapter;
import com.dreamcard.app.view.adapters.CustomGridViewAdapterButton;
import com.dreamcard.app.view.interfaces.IServiceListener;
import com.dreamcard.app.view.interfaces.OnFragmentInteractionListener;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link}
 * interface.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CategoriesListFragment extends Fragment implements View.OnClickListener, IServiceListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String parentId;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private GridView grid;
    private CustomGridViewAdapterButton customGridAdapter;
    private CategoriesListAdapter adapter;
    private CategoriesAsync categoriesAsync;
    private ProgressBar progressBar;

    private ArrayList<Categories> list = new ArrayList<Categories>();

    public static CategoriesListFragment newInstance(String param1, String param2) {
        CategoriesListFragment fragment = new CategoriesListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CategoriesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            parentId = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categorieslistfragment, container, false);
        grid = (GridView) view.findViewById(R.id.products_grid);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        grid.setVisibility(View.GONE);
        if (this.parentId == null) {
            categoriesAsync = new CategoriesAsync(this, new ArrayList<ServiceRequest>(), Params.SERVICE_PROCESS_1
                    , Params.TYPE_PARENT_CATEGORY);
            categoriesAsync.execute(getActivity());
        } else {
            ArrayList<ServiceRequest> list = ServicesConstants.getSubCategoryRequestList(this.parentId);
            categoriesAsync = new CategoriesAsync(this, list, Params.SERVICE_PROCESS_1, Params.TYPE_SUB_CATEGORY);
            categoriesAsync.execute(getActivity());
        }
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
        categoriesAsync.cancel(true);
        mListener = null;
    }

    @Override
    public void onServiceSuccess(Object b, int processType) {
        if (getActivity() == null) {
            Log.e(this.getClass().getName(), "Activity is null, avoid callback");
            return;
        }
        ArrayList<Categories> list = (ArrayList<Categories>) b;
        this.list = list;
        ArrayList<GridItem> gridArray = new ArrayList<GridItem>();
        for (Categories trans : this.list) {
            gridArray.add(new ItemButton(trans.getLogo(), trans.getTitle(), trans.getId(), trans.getPosition()));
        }
        if (getActivity() == null) {
            Log.e("Debug", "Activity is null ");
            return;
        }
        if (gridArray == null) {
            Log.e("Debug", "GridArray is null");
        }
        customGridAdapter = new CustomGridViewAdapterButton(getActivity(), R.layout.row_grid_button
                , gridArray, this, null);
        grid.setAdapter(customGridAdapter);
        progressBar.setVisibility(View.GONE);
        grid.setVisibility(View.VISIBLE);
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {
        if (getActivity() == null) {
            Log.e(this.getClass().getName(), "Activity is null, avoid callback");
            return;
        }
        progressBar.setVisibility(View.GONE);
        grid.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), info.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        Categories data = null;
        int x = view.getId();
        for (Categories bean : this.list) {
            if (bean.getPosition() == view.getId()) {
                data = bean;
                break;
            }
        }
        if (data != null) {
            mListener.doAction(data, Params.FRAGMENT_CATEGORIES);
        }
    }

}
