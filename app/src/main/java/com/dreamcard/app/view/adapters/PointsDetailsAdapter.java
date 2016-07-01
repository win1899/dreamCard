package com.dreamcard.app.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.entity.CashPointsTransaction;

import java.util.ArrayList;

/**
 * Created by WIN on 7/1/2016.
 */
public class PointsDetailsAdapter extends BaseAdapter {
    private Context _context;
    private ArrayList<CashPointsTransaction> _transactions;

    public PointsDetailsAdapter(Context context, ArrayList<CashPointsTransaction> transactions) {
        _context = context;
        _transactions = transactions;
        if (_transactions == null) {
            _transactions = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return _transactions.size();
    }

    @Override
    public Object getItem(int position) {
        return _transactions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vi = convertView;
        PointsHolder holder = null;

        if(convertView == null) {
            vi = inflater.inflate(R.layout.points_item, null);
            holder = new PointsHolder();
            holder.storeName = (TextView) vi.findViewById(R.id.points_store_name);
            holder.pointsValue = (TextView) vi.findViewById(R.id.points_value);
            holder.transDate = (TextView) vi.findViewById(R.id.points_date);

            vi.setTag(holder);
        } else {
            holder = (PointsHolder) convertView.getTag();
        }

        CashPointsTransaction bean = _transactions.get(position);
        holder.storeName.setText(bean.getStoreName());
        if (bean.getDate() != null && !bean.getDate().equalsIgnoreCase("null")) {
            holder.transDate.setText(bean.getDate());
        }
        else {
            holder.transDate.setText("");
        }
        holder.pointsValue.setText("" + (int) bean.getPointsValue());

        return vi;
    }

    private class PointsHolder {
        public TextView storeName;
        public TextView transDate;
        public TextView pointsValue;
    }
}
