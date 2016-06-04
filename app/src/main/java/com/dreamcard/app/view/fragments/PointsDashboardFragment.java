package com.dreamcard.app.view.fragments;

import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.services.AllBusinessAsync;
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.adapters.StoresAdapter;
import com.dreamcard.app.view.interfaces.IServiceListener;

import java.util.ArrayList;

/**
 * Created by WIN on 5/28/2016.
 */
public class PointsDashboardFragment extends Fragment implements IServiceListener, View.OnClickListener {

    private AllBusinessAsync _allBusinessAsync;

    private static ArrayList<Stores> _storesList;
    private RecyclerView _recycler;
    private LinearLayoutManager _layoutManager;
    private View.OnLayoutChangeListener _layoutChangeListener;
    private TextView _youEarnedText;
    private TextView _totalEarnings;

    private int _scrollPosition = 0;
    private int _lastEstimation = -1;

    public static PointsDashboardFragment newInstance() {
        PointsDashboardFragment fragment = new PointsDashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PointsDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard_cashpoints, container, false);

        buildUI(view);

        _allBusinessAsync = new AllBusinessAsync(this, new ArrayList<ServiceRequest>(), Params.SERVICE_PROCESS_1);
        _allBusinessAsync.execute(getActivity());
        return view;
    }

    private void buildUI(View view) {
        _recycler = (RecyclerView) view.findViewById(R.id.recycler_cashpoints_dashboard);

        ImageView rightArrow = (ImageView) view.findViewById(R.id.right_arrow_cashpoints);
        ImageView leftArrow = (ImageView) view.findViewById(R.id.left_arrow_cashpoints);
        rightArrow.setOnClickListener(this);
        leftArrow.setOnClickListener(this);

        _youEarnedText = (TextView) view.findViewById(R.id.you_earned_cashponts);
        _totalEarnings = (TextView) view.findViewById(R.id.store_earnings_cashpoints);

        _youEarnedText.setVisibility(View.GONE);
        _totalEarnings.setVisibility(View.GONE);
    }

    private void prepareRecyclerView() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _layoutManager = new LinearLayoutManager(getActivity());
                _layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

                final StoresAdapter adapter = new StoresAdapter(_storesList, getActivity().getApplicationContext(), _layoutManager, _recycler);

                _recycler.setLayoutManager(_layoutManager);
                _recycler.setAdapter(adapter);
                _recycler.scrollToPosition(_scrollPosition);

                _recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (dx > 10 && _scrollPosition <= _storesList.size() - 3) {
                            _scrollPosition ++;
                        }
                        if (dx < -10 && _scrollPosition > 0) {
                            _scrollPosition--;
                        }
                        _layoutManager.scrollToPositionWithOffset(_scrollPosition, 0);
                    }
                });
            }
        });

        _layoutChangeListener = new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                estimateSelected();
            }
        };
        _recycler.addOnLayoutChangeListener(_layoutChangeListener);
    }

    private void filterStores(ArrayList<Stores> stores) {
        if (_storesList == null) {
            _storesList = new ArrayList<>();
        }
        else {
            _storesList.clear();
        }
        _storesList.add(new Stores());

        for (Stores store : stores) {
            if (store.getCashPoints() != null && !"".equalsIgnoreCase(store.getCashPoints())
                    && !"null".equalsIgnoreCase(store.getCashPoints()) && !"0".equalsIgnoreCase(store.getCashPoints())) {
                _storesList.add(store);
            }
        }
        _storesList.add(new Stores());
    }

    @Override
    public void onDetach() {
        if (_allBusinessAsync != null && _allBusinessAsync.getStatus() == AsyncTask.Status.RUNNING) {
            _allBusinessAsync.cancel(true);
        }
        _recycler.clearOnScrollListeners();
        _recycler.removeOnLayoutChangeListener(_layoutChangeListener);
        super.onDestroy();
    }

    @Override
    public void onServiceSuccess(Object b, int processType) {
        if (processType == Params.SERVICE_PROCESS_1) {
            filterStores((ArrayList<Stores>) b);

            prepareRecyclerView();
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {

    }

    private void estimateSelected() {
        int firstItem = _scrollPosition;

        if (firstItem == _lastEstimation) {
            return;
        }

        _lastEstimation = firstItem;
        StoresAdapter.ViewHolder firstHolder = (StoresAdapter.ViewHolder) _recycler.findViewHolderForLayoutPosition(firstItem);
        if (firstHolder != null) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) firstHolder.storeImageView.getLayoutParams();
            params.width = Utils.dpToPx(60, getActivity().getResources().getDisplayMetrics());
            params.height = Utils.dpToPx(62, getActivity().getResources().getDisplayMetrics());
            params.gravity = Gravity.CENTER;

            firstHolder.storeImageView.setLayoutParams(params);
            firstHolder.storeImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        StoresAdapter.ViewHolder middleItem = (StoresAdapter.ViewHolder) _recycler.findViewHolderForLayoutPosition(firstItem + 1);
        if (middleItem != null) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) middleItem.storeImageView.getLayoutParams();
            params.width = Utils.dpToPx(100, getActivity().getResources().getDisplayMetrics());
            params.height = Utils.dpToPx(102, getActivity().getResources().getDisplayMetrics());
            params.gravity = Gravity.CENTER;

            middleItem.storeImageView.setLayoutParams(params);
            middleItem.storeImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        StoresAdapter.ViewHolder lastItem = (StoresAdapter.ViewHolder) _recycler.findViewHolderForLayoutPosition(firstItem + 2);
        if (lastItem != null) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lastItem.storeImageView.getLayoutParams();
            params.width = Utils.dpToPx(60, getActivity().getResources().getDisplayMetrics());
            params.height = Utils.dpToPx(62, getActivity().getResources().getDisplayMetrics());
            params.gravity = Gravity.CENTER;

            lastItem.storeImageView.setLayoutParams(params);
            lastItem.storeImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        _youEarnedText.setVisibility(View.VISIBLE);
        _totalEarnings.setVisibility(View.VISIBLE);
        _totalEarnings.setText(_storesList.get(firstItem + 1).getCashPoints());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.right_arrow_cashpoints) {
            if (_scrollPosition >= _storesList.size() - 3) {
                return;
            }
            _scrollPosition++;
            _layoutManager.scrollToPositionWithOffset(_scrollPosition, 0);
            return;
        }
        if (v.getId() == R.id.left_arrow_cashpoints) {
            if (_scrollPosition <= 0) {
                return;
            }
            _scrollPosition--;
            _layoutManager.scrollToPositionWithOffset(_scrollPosition, 0);
        }
    }
}