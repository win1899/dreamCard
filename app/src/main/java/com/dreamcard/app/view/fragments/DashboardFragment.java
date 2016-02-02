package com.dreamcard.app.view.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
//import android.app.Fragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
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
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.services.ConsumerDiscountAsyncTask;
import com.dreamcard.app.services.TotalSavingAsync;
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.adapters.NotificationGridAdapter;
import com.dreamcard.app.view.interfaces.IServiceListener;
import com.dreamcard.app.view.interfaces.OnFragmentInteractionListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    private TextView txtYouSaved;
    private TextView txtTotalShop;
    private TextView txtLastUse;
    private TextView txtUserName;
    private GridView grid;
    private NotificationGridAdapter adapter;

    private TotalSavingAsync totalSavingAsync;
    private ConsumerDiscountAsyncTask consumerDiscountAsyncTask;
    private ArrayList<Offers> notificationList = new ArrayList<Offers>();
    private Button btnGas;
    private Button btnCashPoints;
    private Button btnCash;
    private Button btnMobile;

    private double totalSave = 0.0;

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


        txtLastUse = (TextView) view.findViewById(R.id.txt_last_used);
        txtTotalShop = (TextView) view.findViewById(R.id.txt_total_shop);
        txtYouSaved = (TextView) view.findViewById(R.id.txt_total_saved);
        txtUserName = (TextView) view.findViewById(R.id.txt_username);
        grid = (GridView) view.findViewById(R.id.notifications_grid);

        btnCash = (Button) view.findViewById(R.id.btn_cash);
        btnCash.setOnClickListener(this);

        btnGas = (Button) view.findViewById(R.id.btn_gas);
        btnGas.setOnClickListener(this);

        btnCashPoints = (Button) view.findViewById(R.id.btn_cash_points);
        btnCashPoints.setOnClickListener(this);

        btnMobile = (Button) view.findViewById(R.id.btn_mobile);
        btnMobile.setOnClickListener(this);

        SharedPreferences prefs = getActivity().getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
        String id = prefs.getString(Params.USER_INFO_ID, "");
        String name = Utils.getUserName(getActivity());

        txtUserName.setText(name);

        totalSavingAsync = new TotalSavingAsync(this, ServicesConstants.getTotalSavingRequestList(id)
                , Params.SERVICE_PROCESS_1);
        totalSavingAsync.execute(getActivity());

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
        if (totalSavingAsync != null && totalSavingAsync.getStatus() == AsyncTask.Status.RUNNING) {
            totalSavingAsync.cancel(true);
        }
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
            btnCashPoints.setBackground(getResources().getDrawable(R.color.button_not_seleted));
            btnMobile.setBackground(getResources().getDrawable(R.color.button_not_seleted));
            btnGas.setBackground(getResources().getDrawable(R.color.button_not_seleted));

            btnCash.setBackgroundColor(getResources().getColor(R.color.button_selected));

            showSavedAmountDialog("Cash saved", totalSave);
        } else if (view.getId() == R.id.btn_cash_points) {
            btnCash.setBackgroundColor(getResources().getColor(R.color.button_not_seleted));
            btnMobile.setBackground(getResources().getDrawable(R.color.button_not_seleted));
            btnGas.setBackground(getResources().getDrawable(R.color.button_not_seleted));

            btnCashPoints.setBackground(getResources().getDrawable(R.color.button_selected));

            comingSoonDialog();
        } else if (view.getId() == R.id.btn_mobile) {
            btnCash.setBackgroundColor(getResources().getColor(R.color.button_not_seleted));
            btnGas.setBackground(getResources().getDrawable(R.color.button_not_seleted));
            btnCashPoints.setBackground(getResources().getDrawable(R.color.button_not_seleted));

            btnMobile.setBackground(getResources().getDrawable(R.color.button_selected));

            comingSoonDialog();
        }
        else if (view.getId() == R.id.btn_gas) {
            btnCash.setBackgroundColor(getResources().getColor(R.color.button_not_seleted));
            btnMobile.setBackground(getResources().getDrawable(R.color.button_not_seleted));
            btnCashPoints.setBackground(getResources().getDrawable(R.color.button_not_seleted));

            btnGas.setBackground(getResources().getDrawable(R.color.button_selected));

            comingSoonDialog();
        }
    }

    private void showSavedAmountDialog(String title, double amount) {
        new AlertDialog.Builder(this.getActivity())
                .setTitle(title)
                .setMessage(amount + getResources().getString(R.string.ils))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();

    }


    private void comingSoonDialog() {
        new AlertDialog.Builder(this.getActivity())
                .setTitle(getResources().getString(R.string.coming_soon))
                .setMessage(getString(R.string.coming_soon))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onServiceSuccess(Object b, int processType) {
        if (getActivity() == null) {
            Log.e(this.getClass().getName(), "Activity is null, avoid callback");
            return;
        }
        if (processType == Params.SERVICE_PROCESS_1) {
            ConsumerInfo bean = (ConsumerInfo) b;
            try {
                totalSave = Double.parseDouble(bean.getTotalSaving());
                txtYouSaved.setText(Integer.toString((int) totalSave) + getResources().getString(R.string.ils));
            }
            catch (Exception e) {
                Log.e(DashboardFragment.class.getName(), "Unable to parse double: " + bean.getTotalSaving());
                txtYouSaved.setText(bean.getTotalSaving() + getResources().getString(R.string.ils));
            }
        } else if (processType == Params.SERVICE_PROCESS_3) {
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

            if (bean.getDate() != null && bean.getDate().length() > 0 && !bean.getDate().equalsIgnoreCase("null")) {
                Date date = new Date(Long.parseLong(bean.getDate().replaceAll(".*?(\\d+).*", "$1")));
                android.text.format.DateFormat df = new android.text.format.DateFormat();
                Calendar c = Calendar.getInstance();
                c.setTime(date);

                String x = df.format("dd/MM", c.getTime()).toString();
                txtLastUse.setText(x);
            }

            if (bean.getAmount() != null && bean.getAmount().length() > 0)
                txtTotalShop.setText(bean.getAmount());
            else
                txtTotalShop.setText("" + 0);

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
