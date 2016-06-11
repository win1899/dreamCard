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
import com.dreamcard.app.common.DatabaseController;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.MessageInfo;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.services.AddBusinessCommentAsync;
import com.dreamcard.app.services.ConsumerDiscountAsyncTask;
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
    private ConsumerDiscountAsyncTask _consumerDiscountAsyncTask;
    private Stores _store;
    private String _discount;

    private RatingBar _ratingBar;
    private EditText _reviewText;
    private TextView _youSaved;
    private TextView _youSavedVal;

    private volatile AtomicInteger _count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) {
            //TODO: fallback ...
            finish();
            return;
        }

        _storeId = intent.getExtras().getString(BUSINESS_ID_EXTRA);
        if (_storeId == null || _storeId.isEmpty()) {
            Log.e(LOG, "Store id is empty ... finishing");
            finish();
            return;
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

        _youSaved = (TextView) findViewById(R.id.you_saved);
        _youSavedVal = (TextView) findViewById(R.id.you_saved_val);

        if (_discount != null && !_discount.equalsIgnoreCase("")) {
            _youSaved.setVisibility(View.VISIBLE);
            _youSavedVal.setVisibility(View.VISIBLE);
            _youSavedVal.setText(_discount);
        }
        else {
            _youSaved.setVisibility(View.GONE);
            _youSavedVal.setVisibility(View.GONE);
        }

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
        if (_consumerDiscountAsyncTask != null && _consumerDiscountAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            _consumerDiscountAsyncTask.cancel(true);
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
                        break;
                    }
                }
            }

            if (_store != null) {
                Log.d(LOG, "Found store, Building UI");
                SharedPreferences prefs = getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
                String id = prefs.getString(Params.USER_INFO_ID, "");

                _consumerDiscountAsyncTask = new ConsumerDiscountAsyncTask(this
                        , ServicesConstants.getTotalSavingRequestList(id)
                        , Params.SERVICE_PROCESS_2);
                _consumerDiscountAsyncTask.execute(this);
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
        else if (processType == Params.SERVICE_PROCESS_2) {
            ArrayList<Offers> list = (ArrayList<Offers>) b;
            if (list.size() > 0) {
                for (Offers offer : list) {
                    if ((offer.getBusinessName() != null && offer.getBusinessName().equalsIgnoreCase(_store.getStoreName()))
                            || (offer.getBusinessId() != null && offer.getBusinessId().equalsIgnoreCase(_store.getId()))) {
                        double discount = offer.getAmountBeforeDicount() - offer.getAmountAfterDiscount();
                        String disTxt = String.valueOf(discount);
                        if (String.valueOf(discount).lastIndexOf(".") != -1) {
                            disTxt = String.valueOf(discount).substring(0, String.valueOf(discount).lastIndexOf("."));
                        }

                        _discount = String.format("%s%s", disTxt, getString(R.string.ils));
                        _youSaved.setVisibility(View.VISIBLE);
                        _youSavedVal.setVisibility(View.VISIBLE);
                        _youSavedVal.setText(_discount);
                        break;
                    }
                }
            }
        }
    }

    private void removeStore() {
        DatabaseController.getInstance(this).deleteNotification(_store.getId());
        Utils.updateNotificationBadge(ReviewStore.this, -1);
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
