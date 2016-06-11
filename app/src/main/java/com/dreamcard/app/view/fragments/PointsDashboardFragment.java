package com.dreamcard.app.view.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.CashPointsTransaction;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.services.GetCashPointsAsync;
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.adapters.StoresAdapter;
import com.dreamcard.app.view.interfaces.IServiceListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by WIN on 5/28/2016.
 */
public class PointsDashboardFragment extends Fragment implements IServiceListener, View.OnClickListener {

    private GetCashPointsAsync _getPointsAsync;

    private HashMap<Integer, ArrayList<CashPointsTransaction>> _transactions;
    private HashMap<Integer, Integer> _positionToId;
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
        _positionToId = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard_cashpoints, container, false);

        buildUI(view);

        SharedPreferences prefs = getActivity().getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
        String id = prefs.getString(Params.USER_INFO_ID, "");

        _getPointsAsync = new GetCashPointsAsync(this, ServicesConstants.getPointsRequestList(id), Params.SERVICE_PROCESS_1);
        _getPointsAsync.execute(getActivity());
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

                final StoresAdapter adapter = new StoresAdapter(_transactions, _positionToId, getActivity().getApplicationContext(), _layoutManager, _recycler);

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
                        if (dx > 10 && _transactions != null && _scrollPosition <= _transactions.size() - 3) {
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

    private void filterStores(HashMap<Integer, ArrayList<CashPointsTransaction>> hash) {
        if (_transactions == null) {
            _transactions = new HashMap<>();
        }
        else {
            _transactions.clear();
        }
        _transactions.put(-1, new ArrayList<CashPointsTransaction>());
        _positionToId.put(0, -1);

        Set<Integer> set = hash.keySet();
        int index = 1;
        for (Integer integer : set) {
            _positionToId.put(index, integer);
            _transactions.put(integer, hash.get(integer));
            index++;
        }
        _transactions.put(Integer.MAX_VALUE, new ArrayList<CashPointsTransaction>());
        _positionToId.put(index, Integer.MAX_VALUE);
    }

    @Override
    public void onDetach() {
        if (_getPointsAsync != null && _getPointsAsync.getStatus() == AsyncTask.Status.RUNNING) {
            _getPointsAsync.cancel(true);
        }
        _recycler.clearOnScrollListeners();
        _recycler.removeOnLayoutChangeListener(_layoutChangeListener);
        super.onDestroy();
    }

    @Override
    public void onServiceSuccess(Object b, int processType) {
        if (processType == Params.SERVICE_PROCESS_1) {
            filterStores((HashMap<Integer, ArrayList<CashPointsTransaction>>) b);

            prepareRecyclerView();
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {

    }

    private void estimateSelected() {
        int firstItem = _scrollPosition;

        if (firstItem == _lastEstimation
                || _recycler == null
                || _transactions == null
                || _layoutManager == null) {
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
        int key = _positionToId.get(_scrollPosition + 1);
        int totalPoints = 0;
        for (CashPointsTransaction trans : _transactions.get(key))
        {
            totalPoints += trans.getPointsValue();
        }
        _totalEarnings.setText(Integer.toString(totalPoints));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.right_arrow_cashpoints) {
            if (_transactions == null || _scrollPosition >= _transactions.size() - 3) {
                return;
            }
            _scrollPosition++;
            _layoutManager.scrollToPositionWithOffset(_scrollPosition, 0);
            return;
        }
        if (v.getId() == R.id.left_arrow_cashpoints) {
            if (_transactions == null || _scrollPosition <= 0) {
                return;
            }
            _scrollPosition--;
            _layoutManager.scrollToPositionWithOffset(_scrollPosition, 0);
        }
    }
}