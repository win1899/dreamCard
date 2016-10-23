package com.dreamcard.app.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.entity.CashPointsTransaction;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.view.adapters.PointsDetailsAdapter;
import com.dreamcard.app.view.interfaces.IServiceListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by WIN on 7/1/2016.
 */
public class PointsDetailsActivity extends Activity {

    private ArrayList<CashPointsTransaction> _transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_details);

        Intent intent = getIntent();

        _transactions = new ArrayList<>();

        if (intent != null) {
            HashMap<Integer, ArrayList<CashPointsTransaction>> hash =
                    (HashMap<Integer, ArrayList<CashPointsTransaction>>) intent.getSerializableExtra("Data");
            if (hash == null) {
                finish();
                return;
            }

            Set<Integer> keys = hash.keySet();
            for (Integer i : keys) {
                ArrayList<CashPointsTransaction> transs = hash.get(i);
                if (transs != null) {
                    for (CashPointsTransaction trans : transs) {
                        if (trans.getStatus().equalsIgnoreCase("Earned")) {
                            _transactions.add(trans);
                        }
                    }
                }
            }
        }

        if (_transactions == null || _transactions.size() == 0) {
            TextView noPoints = (TextView) findViewById(R.id.no_points);
            noPoints.setVisibility(View.VISIBLE);
            return;
        }

        ListView list = (ListView) findViewById(R.id.points_list);
        PointsDetailsAdapter adapter = new PointsDetailsAdapter(this, _transactions);
        list.setAdapter(adapter);
    }
}
