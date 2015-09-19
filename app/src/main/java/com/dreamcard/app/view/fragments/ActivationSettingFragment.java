package com.dreamcard.app.view.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dreamcard.app.R;
import com.dreamcard.app.components.TransparentProgressDialog;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.Categories;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.GridItem;
import com.dreamcard.app.entity.ItemButton;
import com.dreamcard.app.entity.RecordHolder;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.services.AddRemoveInterestCatAsync;
import com.dreamcard.app.services.CategoriesAsync;
import com.dreamcard.app.services.RegisterConsumerAsync;
import com.dreamcard.app.view.adapters.CategoriesListAdapter;
import com.dreamcard.app.view.adapters.CustomGridViewAdapterButton;
import com.dreamcard.app.view.interfaces.IServiceListener;
import com.dreamcard.app.view.interfaces.OnFragmentInteractionListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.dreamcard.app.view.interfaces.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActivationSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivationSettingFragment extends Fragment implements View.OnClickListener, IServiceListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private GridView grid;
    private CustomGridViewAdapterButton customGridAdapter;
    private ProgressBar progress;
    private HashMap<Integer, RecordHolder> recordHolderList = new HashMap<Integer, RecordHolder>();
    private ArrayList<Categories> gridList = new ArrayList<Categories>();
    private CategoriesAsync categoriesAsync;
    private RecordHolder holder = new RecordHolder();
    private Categories selectedCategory = new Categories();

    private TransparentProgressDialog progressDialog;
    private Runnable runnable;
    private Handler handler;

    private RecordListener recordListener = new RecordListener() {
        @Override
        public void setHolder(RecordHolder holder, int position) {
            recordHolderList.put(position, holder);
        }
    };

    public static ActivationSettingFragment newInstance(String param1, String param2) {
        ActivationSettingFragment fragment = new ActivationSettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ActivationSettingFragment() {
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
        View view = inflater.inflate(R.layout.fragment_activation_setting, container, false);

        grid = (GridView) view.findViewById(R.id.products_grid);
        progress = (ProgressBar) view.findViewById(R.id.progress);

        handler = new Handler();
        progressDialog = new TransparentProgressDialog(view.getContext(), R.drawable.loading);
        runnable = new Runnable() {
            @Override
            public void run() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        };

        progress.setVisibility(View.VISIBLE);
        grid.setVisibility(View.GONE);

        categoriesAsync = new CategoriesAsync(this, new ArrayList<ServiceRequest>(), Params.SERVICE_PROCESS_1,
                Params.TYPE_ALL_CATEGORY);
        categoriesAsync.execute(getActivity());

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction("", "");
        }
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
        mListener = null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        Categories data = null;
        int x = view.getId();
        for (Categories bean : this.gridList) {
            if (bean.getPosition() == view.getId()) {
                data = bean;
                break;
            }
        }
        if (data != null) {
            RecordHolder holder = this.recordHolderList.get(data.getPosition());
            if (data.isSelected()) {
                this.holder = holder;
                this.selectedCategory = data;

                progressDialog.show();
                handler.postDelayed(runnable, 5000);

                AddRemoveInterestCatAsync async = new AddRemoveInterestCatAsync(this
                        , ServicesConstants.getAddRemoveCategoryRequestList(data.getId(), mParam1)
                        , Params.SERVICE_PROCESS_2, Params.TYPE_REMOVE_CATEGORY);
                async.execute(getActivity());
            } else {
                this.holder = holder;
                this.selectedCategory = data;

                progressDialog.show();
                handler.postDelayed(runnable, 5000);

                AddRemoveInterestCatAsync async = new AddRemoveInterestCatAsync(this
                        , ServicesConstants.getAddRemoveCategoryRequestList(data.getId(), mParam1)
                        , Params.SERVICE_PROCESS_3, Params.TYPE_ADD_CATEGORY);
                async.execute(getActivity());
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onServiceSuccess(Object b, int processType) {
        if (processType == Params.SERVICE_PROCESS_1) {
            ArrayList<Categories> list = (ArrayList<Categories>) b;
            this.gridList = list;
            ArrayList<GridItem> gridArray = new ArrayList<GridItem>();
            for (Categories trans : this.gridList) {
                gridArray.add(new ItemButton(trans.getLogo(), trans.getTitle(), trans.getId(), trans.getPosition()));
            }
            customGridAdapter = new CustomGridViewAdapterButton(getActivity(), R.layout.category_grid_button
                    , gridArray, this, this.recordListener);
            grid.setAdapter(customGridAdapter);
            progress.setVisibility(View.GONE);
            grid.setVisibility(View.VISIBLE);
        } else if (processType == Params.SERVICE_PROCESS_2) {
            progressDialog.dismiss();
            holder.getImgSelected().setVisibility(View.GONE);
            holder.getPnl().setBackground(getResources().getDrawable(R.drawable.category_btn_bg));
            holder.getPnl2().setBackground(getResources().getDrawable(R.drawable.category_btn_bg));
            this.selectedCategory.setSelected(false);
        } else if (processType == Params.SERVICE_PROCESS_3) {
            progressDialog.dismiss();
            holder.getPnl().setBackground(getResources().getDrawable(R.drawable.category_btn_bg_press));
            holder.getPnl2().setBackground(getResources().getDrawable(R.drawable.category_btn_bg_press));
            holder.getImgSelected().setVisibility(View.VISIBLE);
            this.selectedCategory.setSelected(true);
        }

    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {
        progress.setVisibility(View.GONE);
        grid.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), info.getMessage(), Toast.LENGTH_LONG).show();
    }

    public interface RecordListener {
        public void setHolder(RecordHolder holder, int position);
    }

}
