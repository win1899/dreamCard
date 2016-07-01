package com.dreamcard.app.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.entity.CashPointsTransaction;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.view.adapters.CashDetailsAdapter;
import com.dreamcard.app.view.adapters.PointsDetailsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by WIN on 7/1/2016.
 */
public class CashDetailsActivity extends Activity {

    private ArrayList<Offers> _transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_details);

        Intent intent = getIntent();

        _transactions = new ArrayList<>();

        if (intent != null) {
            _transactions = (ArrayList<Offers>) intent.getSerializableExtra("Data");
        }

        if (_transactions == null || _transactions.size() == 0) {
            TextView noPoints = (TextView) findViewById(R.id.no_cash);
            noPoints.setVisibility(View.VISIBLE);
            return;
        }

        ListView list = (ListView) findViewById(R.id.cash_list);
        CashDetailsAdapter adapter = new CashDetailsAdapter(this, _transactions);
        list.setAdapter(adapter);
    }
}
