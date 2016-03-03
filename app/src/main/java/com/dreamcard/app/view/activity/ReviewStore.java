package com.dreamcard.app.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.MessageInfo;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.services.AddBusinessCommentAsync;
import com.dreamcard.app.services.GetBussinesByIdAsync;
import com.dreamcard.app.services.RateBusinessAsyncTask;
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.interfaces.IServiceListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by WIN on 2/19/2016.
 */
public class ReviewStore extends Activity implements View.OnClickListener, IServiceListener {
    private static final String LOG = ReviewStore.class.getName();

    public static final String BUSINESS_ID_EXTRA = "businessIdExtra";

    private String _storeId;
    private GetBussinesByIdAsync _getBussinesByIdAsync;
    private RateBusinessAsyncTask _rateStoreAsync;
    private AddBusinessCommentAsync _addBussinessComment;
    private Stores _store;

    private RatingBar _ratingBar;
    private EditText _reviewText;

    private volatile AtomicInteger _count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) {
            //TODO: fallback ...
            finish();
        }

        _storeId = intent.getExtras().getString(BUSINESS_ID_EXTRA);
        if (_storeId == null || _storeId.isEmpty()) {
            Log.e(LOG, "Store id is empty ... finishing");
            finish();
        }

        _count = new AtomicInteger(0);

        _getBussinesByIdAsync = new GetBussinesByIdAsync(this, ServicesConstants.getBusinessById(_storeId),
                Params.SERVICE_PROCESS_9);
        _getBussinesByIdAsync.execute(this);
    }

    private void buildUI() {
        setContentView(R.layout.activity_review_store);

        TextView thanksText = (TextView) findViewById(R.id.thanks_text);
        thanksText.setText(getResources().getString(R.string.review_store_thanks) + " " + _store.getStoreName() + ".");

        ImageView storeIcon = (ImageView) findViewById(R.id.review_store_logo);
        Utils.loadImage(this, _store.getLogo(), storeIcon);

        TextView storeName = (TextView) findViewById(R.id.review_store_name);
        storeName.setText(_store.getStoreName());

        TextView storeAddr = (TextView) findViewById(R.id.review_store_address);
        storeAddr.setText(_store.getAddress1());

        TextView storePhone = (TextView) findViewById(R.id.review_store_phone_num);
        storePhone.setText(_store.getPhone());

        Button finishButton = (Button) findViewById(R.id.finish_review);
        finishButton.setOnClickListener(this);

        _ratingBar = (RatingBar) findViewById(R.id.review_ratingBar);
        _reviewText = (EditText) findViewById(R.id.review_edit_text);
    }


    @Override
    protected void onPause() {
        if (_getBussinesByIdAsync != null && _getBussinesByIdAsync.getStatus() == AsyncTask.Status.RUNNING) {
            _getBussinesByIdAsync.cancel(true);
        }
        if (_rateStoreAsync != null && _rateStoreAsync.getStatus() == AsyncTask.Status.RUNNING) {
            _rateStoreAsync.cancel(true);
        }
        if (_addBussinessComment != null && _addBussinessComment.getStatus() == AsyncTask.Status.RUNNING) {
            _addBussinessComment.cancel(true);
        }
        super.onPause();
    }

    @Override
    public void onServiceSuccess(Object b, int processType) {
        if (processType == Params.SERVICE_PROCESS_9) {
            ArrayList<Stores> list = (ArrayList<Stores>) b;
            if (list != null && list.size() == 1) {
                _store = list.get(0);
            } else {
                for (Stores s : list) {
                    if (s.getId().equalsIgnoreCase(_storeId)) {
                        _store = s;
                        return;
                    }
                }
            }

            if (_store != null) {
                Log.d(LOG, "Found store, Building UI");
                buildUI();
            }
            else {
                Log.e(LOG, "No store found, finishing");
                finish();
            }
        }

        else if (processType == Params.SERVICE_PROCESS_1) {
            MessageInfo info = (MessageInfo) b;
            if (!info.isValid()) {
                Toast.makeText(this, getResources().getString(R.string.rate_business_not_sent), Toast.LENGTH_LONG).show();
                return;
            }
            if (_count.incrementAndGet() == 2) {
                removeStore();
                finish();
            }
        }
        else if (processType == Params.SERVICE_PROCESS_3) {
            MessageInfo bean = (MessageInfo) b;

            if (!bean.isValid()) {
                Toast.makeText(this, getResources().getString(R.string.comment_not_added), Toast.LENGTH_LONG).show();
                return;
            }
            if (_count.incrementAndGet() == 2) {
                removeStore();
                finish();
            }
        }
    }

    private void removeStore() {
        SharedPreferences prefs = getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);

        HashSet<String> set = (HashSet<String>) prefs.getStringSet(Params.STORES_TO_REVIEW_KEY, new HashSet<String>());
        if (set != null && set.size() > 0) {
            HashSet<String> newStores = new HashSet<String>();
            for (String storeId : set) {
                if (!storeId.equalsIgnoreCase(_store.getId())) {
                    newStores.add(storeId);
                }
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(Params.STORES_TO_REVIEW_KEY);
            editor.putStringSet(Params.STORES_TO_REVIEW_KEY, newStores);
            editor.commit();

            Utils.updateNotificationBadge(ReviewStore.this, -1);
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {
        Toast.makeText(this, "حدث خلل اثناء الاتصال", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.finish_review) {

            SharedPreferences prefs = getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
            String id = prefs.getString(Params.USER_INFO_ID, "");

            int rating = (int) _ratingBar.getRating();
            String comment = _reviewText.getText().toString();

            _rateStoreAsync = new RateBusinessAsyncTask(this
                    , ServicesConstants.getRateOfferRequestList(id, _store.getId(), Integer.toString(rating))
                    , Params.SERVICE_PROCESS_1, Params.TYPE_OFFER);
            _rateStoreAsync.execute(ReviewStore.this);

            _addBussinessComment = new AddBusinessCommentAsync(this
                    , ServicesConstants.getAddBusinessCommentRequestList(id, _store.getId(), comment)
                    , Params.SERVICE_PROCESS_3, Params.TYPE_BUSINESS);
            _addBussinessComment.execute(this);
        }
    }
}
