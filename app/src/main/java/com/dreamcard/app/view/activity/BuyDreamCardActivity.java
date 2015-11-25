package com.dreamcard.app.view.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.MessageInfo;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.services.AllBusinessAsync;
import com.dreamcard.app.services.ContentBySlagAsync;
import com.dreamcard.app.services.GetInvoiceForOfferAsync;
import com.dreamcard.app.view.adapters.SellingAreasAdapter;
import com.dreamcard.app.view.interfaces.IServiceListener;

import java.util.ArrayList;

/**
 * Created by WIN on 11/17/2015.
 */
public class BuyDreamCardActivity extends Activity
    implements IServiceListener {

    private static final String LOG_TAG = BuyDreamCardActivity.class.getName();

    private ListView list;
    private ProgressBar progressBar;
    private TextView noMatchs;

    private AllBusinessAsync businessAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_dream_card);

        list = (ListView) findViewById(R.id.where_to_buy_list);
        progressBar = (ProgressBar) findViewById(R.id.buy_dream_progress);
        progressBar.setVisibility(View.VISIBLE);
        noMatchs = (TextView) findViewById(R.id.buy_dream_no_matches);

        businessAsync = new AllBusinessAsync(this, new ArrayList<ServiceRequest>(), Params.SERVICE_PROCESS_1);
        businessAsync.execute(this);
    }


    @Override
    public void onServiceSuccess(Object b, int processType) {
        Log.i(LOG_TAG, "OnServiceSucceeded with: " + b.toString());
        if (b != null) {
            SellingAreasAdapter adapter = new SellingAreasAdapter(this, (ArrayList<Stores>) b);
            list.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            return;
        }

        progressBar.setVisibility(View.GONE);
        noMatchs.setVisibility(View.VISIBLE);
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {
        progressBar.setVisibility(View.GONE);
        noMatchs.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        if (businessAsync != null && businessAsync.getStatus() == AsyncTask.Status.RUNNING) {
            businessAsync.cancel(true);
        }
        super.onDestroy();
    }
}

