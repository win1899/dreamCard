package com.dreamcard.app.view.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.services.AllBusinessAsync;
import com.dreamcard.app.services.SellingStoresAsync;
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

    private SellingStoresAsync sellingBusinessAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_dream_card);

        list = (ListView) findViewById(R.id.where_to_buy_list);
        progressBar = (ProgressBar) findViewById(R.id.buy_dream_progress);
        progressBar.setVisibility(View.VISIBLE);
        noMatchs = (TextView) findViewById(R.id.buy_dream_no_matches);

        sellingBusinessAsync = new SellingStoresAsync(this, new ArrayList<ServiceRequest>(), Params.SERVICE_PROCESS_1);
        sellingBusinessAsync.execute(this);
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
        if (sellingBusinessAsync != null && sellingBusinessAsync.getStatus() == AsyncTask.Status.RUNNING) {
            sellingBusinessAsync.cancel(true);
        }
        super.onDestroy();
    }
}

