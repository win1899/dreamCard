package com.dreamcard.app.view.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.ConsumerInfo;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.MessageInfo;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.services.ConsumerDiscountAsyncTask;
import com.dreamcard.app.services.TotalCashPointsAsync;
import com.dreamcard.app.services.TotalSavingAsync;
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.adapters.NotificationGridAdapter;
import com.dreamcard.app.view.interfaces.IServiceListener;
import com.dreamcard.app.view.interfaces.OnFragmentInteractionListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by WIN on 5/27/2016.
 */
public class CashDashboardFragment extends Fragment implements IServiceListener {
    private String _totalSavings = "0";

    private TextView txtYouSaved;
    private TextView txtTotalShop;
    private TextView txtLastUse;

    private TotalCashPointsAsync _totalCashPointsAsync;
    private ConsumerDiscountAsyncTask consumerDiscountAsyncTask;

    private double totalSave = 0.0;

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

        txtLastUse = (TextView) view.findViewById(R.id.txt_last_used);
        txtTotalShop = (TextView) view.findViewById(R.id.txt_total_shop);
        txtYouSaved = (TextView) view.findViewById(R.id.txt_total_saved);

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
            Offers bean = list.get(list.size() - 1);

            if (bean.getDate() != null && bean.getDate().length() > 0 && !bean.getDate().equalsIgnoreCase("null")) {
                Date date = new Date(Long.parseLong(bean.getDate().replaceAll(".*?(\\d+).*", "$1")));
                android.text.format.DateFormat df = new android.text.format.DateFormat();
                Calendar c = Calendar.getInstance();
                c.setTime(date);

                String x = df.format("dd/MM", c.getTime()).toString();
                txtLastUse.setText(x);
            }

            double totalShoppings = 0.0;
            double totalSavings = 0.0;

            for (Offers offer : list) {
                try {
                    totalShoppings += Double.parseDouble(offer.getAmount());
                    totalSavings += offer.getAmountAfterDiscount();
                } catch (Exception e){

                }
            }

            txtTotalShop.setText(Integer.toString((int)totalShoppings));
            txtYouSaved.setText((int)totalSavings + getResources().getString(R.string.ils));

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
