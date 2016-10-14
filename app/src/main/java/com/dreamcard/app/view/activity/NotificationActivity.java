package com.dreamcard.app.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.common.DatabaseController;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.NotificationDB;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.services.AllBusinessAsync;
import com.dreamcard.app.services.SellingStoresAsync;
import com.dreamcard.app.view.adapters.NotificationsAdapter;
import com.dreamcard.app.view.interfaces.IServiceListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by WIN on 3/2/2016.
 */
public class NotificationActivity extends Activity implements IServiceListener {

    private static final String LOG_TAG = NotificationActivity.class.getName();

    private ListView list;

    private ProgressBar progressBar;
    private TextView noMatchs;
    private ArrayList<Stores> stores;

    private AllBusinessAsync allBusinessAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        list = (ListView) findViewById(R.id.notification_list);
        progressBar = (ProgressBar) findViewById(R.id.notification_activity_progress);
        progressBar.setVisibility(View.VISIBLE);
        noMatchs = (TextView) findViewById(R.id.notifications_no_data);

        allBusinessAsync = new AllBusinessAsync(this, new ArrayList<ServiceRequest>(), Params.SERVICE_PROCESS_1);
        allBusinessAsync.execute(this);
    }

    private void filterStores() {
        ArrayList<NotificationDB> storesReviewdId = DatabaseController.getInstance(this).getAllNotifications();
        if (storesReviewdId == null || storesReviewdId.size() == 0) {
            noMatchsFound();
            return;
        }

        HashMap<Integer, NotificationDB> notifications = new HashMap<Integer, NotificationDB>();
        int position = 0;
        for (NotificationDB notification : storesReviewdId) {
            if (notification.type.equalsIgnoreCase(NotificationDB.OFFER_TYPE)) {
                notifications.put(position, notification);
                position++;
                continue;
            }

            for (int i = 0; i < stores.size(); i++) {
                if (notification.id.equalsIgnoreCase(stores.get(i).getId())) {
                    notification.store = stores.get(i);
                    notifications.put(position, notification);
                    break;
                }
            }
            position++;
        }

        if (notifications.size() == 0) {
            noMatchsFound();
            return;
        }

        NotificationsAdapter adapter = new NotificationsAdapter(NotificationActivity.this, notifications);
        list.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);
    }

    private void noMatchsFound() {
        progressBar.setVisibility(View.GONE);
        noMatchs.setVisibility(View.VISIBLE);
        list.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        if (allBusinessAsync != null && allBusinessAsync.getStatus() == AsyncTask.Status.RUNNING) {
            allBusinessAsync.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    public void onServiceSuccess(Object b, int processType) {
        if (processType == Params.SERVICE_PROCESS_1) {
            stores = (ArrayList<Stores>) b;
            if (stores == null || stores.size() == 0) {
                noMatchsFound();
                return;
            }

            filterStores();
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {
        noMatchsFound();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setResult(resultCode, data);
        finish();
    }
}