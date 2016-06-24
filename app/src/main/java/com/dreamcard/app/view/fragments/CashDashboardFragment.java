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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.MessageInfo;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.services.ConsumerDiscountAsyncTask;
import com.dreamcard.app.services.TotalCashPointsAsync;
import com.dreamcard.app.view.interfaces.IServiceListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by WIN on 5/27/2016.
 */
public class CashDashboardFragment extends Fragment implements IServiceListener {
    private String _totalSavings = "0";

    private TextView txtYouSaved;
    private TextView _totalItems;
    private TextView _differnetStores;
    private Button _detailsButton;
    private TotalCashPointsAsync _totalCashPointsAsync;
    private ConsumerDiscountAsyncTask consumerDiscountAsyncTask;

    public static CashDashboardFragment newInstance() {
        CashDashboardFragment fragment = new CashDashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CashDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard_cash, container, false);

        txtYouSaved = (TextView) view.findViewById(R.id.txt_total_saved);
        _totalItems = (TextView) view.findViewById(R.id.total_items_value);
        _differnetStores = (TextView) view.findViewById(R.id.different_stores_value);
        _detailsButton = (Button) view.findViewById(R.id.more_details_cash);
        _detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Coming soon ...", Toast.LENGTH_SHORT).show();
            }
        });

        SharedPreferences prefs = getActivity().getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
        String id = prefs.getString(Params.USER_INFO_ID, "");

        consumerDiscountAsyncTask = new ConsumerDiscountAsyncTask(this
                , ServicesConstants.getTotalSavingRequestList(id)
                , Params.SERVICE_PROCESS_3);
        consumerDiscountAsyncTask.execute(getActivity());

        _totalCashPointsAsync = new TotalCashPointsAsync(this, ServicesConstants.getTotalPointsSaved(id), Params.SERVICE_PROCESS_4);
        _totalCashPointsAsync.execute(getActivity());

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (consumerDiscountAsyncTask != null && consumerDiscountAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            consumerDiscountAsyncTask.cancel(true);
        }
        if (_totalCashPointsAsync != null && _totalCashPointsAsync.getStatus() == AsyncTask.Status.RUNNING) {
            _totalCashPointsAsync.cancel(true);
        }
    }

    private void setDiscountInfo(ArrayList<Offers> list) {
        if (list != null && !list.isEmpty()) {
            double totalSavings = 0.0;
            int diffStores = 0;
            int totalItems = 0;
            HashMap<String, Boolean> storesCounted = new HashMap<>();

            for (Offers offer : list) {
                totalSavings += (offer.getAmountBeforeDicount() - offer.getAmountAfterDiscount());
                totalItems++;
                if (storesCounted.get(offer.getBusinessId()) == null) {
                    diffStores++;
                    storesCounted.put(offer.getBusinessId(), true);
                }
            }
            txtYouSaved.setText((int)totalSavings + getResources().getString(R.string.ils));
            _differnetStores.setText(Integer.toString(diffStores));
            _totalItems.setText(Integer.toString(totalItems));
        }
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
        else if (processType == Params.SERVICE_PROCESS_4) {
            _totalSavings = ((MessageInfo) b).getValue();
            _totalSavings = _totalSavings.replaceAll("\"", "");
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {

    }

}
