package com.dreamcard.app.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamcard.app.R;
import com.dreamcard.app.components.TransparentProgressDialog;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.MessageInfo;
import com.dreamcard.app.services.CommentsAsync;
import com.dreamcard.app.services.RateBusinessAsyncTask;
import com.dreamcard.app.view.interfaces.IServiceListener;

public class RateBusinessActivity extends Activity implements View.OnClickListener, IServiceListener {

    private Button btnSendRating;
    private RatingBar ratingBar;
    private TextView txtRightArrow;
    private TextView txtBack;
    private TextView txtTitle;
    private int type;

    private TransparentProgressDialog progress;
    private Runnable runnable;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_business);

        buildUI();

        Intent intent = getIntent();
        int type = intent.getIntExtra(Params.RATING_TYPE, 0);
        if (type == Params.TYPE_OFFER) {
            txtTitle.setText(getString(R.string.rate_offer));
            this.type = type;
        }
    }

    private void buildUI() {
        txtRightArrow = (TextView) findViewById(R.id.txt_arrow);
        txtBack = (TextView) findViewById(R.id.txt_back);
        btnSendRating = (Button) findViewById(R.id.btn_send_rating);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnSendRating.setOnClickListener(this);
        txtBack.setOnClickListener(this);
        txtRightArrow.setOnClickListener(this);
        txtTitle = (TextView) findViewById(R.id.txt_title);

        handler = new Handler();
        progress = new TransparentProgressDialog(this, R.drawable.loading);
        runnable = new Runnable() {
            @Override
            public void run() {
                if (progress.isShowing()) {
                    progress.dismiss();
                }
            }
        };

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rate_business, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_send_rating) {
            progress.show();
            sendRating();
        } else if (view.getId() == R.id.txt_back || view.getId() == R.id.btn_back)
            finish();
    }

    private void sendRating() {
        SharedPreferences prefs = getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
        String id = prefs.getString(Params.USER_INFO_ID, "");

        Intent intent = getIntent();
        String businessId = intent.getStringExtra(Params.BUSINESS_ID);

        String rating = "" + ratingBar.getRating();
        if (type == Params.TYPE_OFFER) {
            RateBusinessAsyncTask asyncTask = new RateBusinessAsyncTask(this
                    , ServicesConstants.getRateOfferRequestList(id, businessId, rating)
                    , Params.SERVICE_PROCESS_1, Params.TYPE_OFFER);
            asyncTask.execute(this);
        } else {
            RateBusinessAsyncTask asyncTask = new RateBusinessAsyncTask(this
                    , ServicesConstants.getRateBusinessRequestList(id, businessId, rating)
                    , Params.SERVICE_PROCESS_1, Params.TYPE_BUSINESS);
            asyncTask.execute(this);
        }
    }

    @Override
    public void onServiceSuccess(Object b, int processType) {
        MessageInfo info = (MessageInfo) b;
        if (info.isValid()) {
            progress.dismiss();
            Toast.makeText(this, getResources().getString(R.string.rate_business_successfully), Toast.LENGTH_LONG).show();

            Intent returnIntent = new Intent();
            returnIntent.putExtra("item", "" + ratingBar.getRating());
            setResult(Params.STATUS_ADD_RATING, returnIntent);
            finish();
        } else {
            progress.dismiss();
            Toast.makeText(this, getResources().getString(R.string.rate_business_not_sent), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {
        progress.dismiss();
        Toast.makeText(this, info.getMessage(), Toast.LENGTH_LONG).show();
    }
}
