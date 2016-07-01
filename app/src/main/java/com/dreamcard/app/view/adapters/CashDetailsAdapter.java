package com.dreamcard.app.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.entity.Offers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by WIN on 7/1/2016.
 */
public class CashDetailsAdapter extends BaseAdapter {
    private Context _context;
    private ArrayList<Offers> _transactions;

    public CashDetailsAdapter(Context context, ArrayList<Offers> transactions) {
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
        CashHolder holder = null;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.cash_item, null);
            holder = new CashHolder();
            holder.storeName = (TextView) vi.findViewById(R.id.cash_store_name);
            holder.pointsValue = (TextView) vi.findViewById(R.id.cash_value);
            holder.transDate = (TextView) vi.findViewById(R.id.cash_date);

            vi.setTag(holder);
        } else {
            holder = (CashHolder) convertView.getTag();
        }

        Offers offer = _transactions.get(position);
        holder.storeName.setText(offer.getBusinessName());
        if (offer.getDate() != null && !offer.getDate().equalsIgnoreCase("null")) {
            try {
                SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(Integer.parseInt(offer.getDate()));

                holder.transDate.setText(formater.format(c));
            } catch (Exception e) {
                holder.transDate.setText("");
            }
        } else {
            holder.transDate.setText("");
        }
        holder.pointsValue.setText("" + (offer.getAmountBeforeDicount() - offer.getAmountAfterDiscount()));

        return vi;
    }

    private class CashHolder {
        public TextView storeName;
        public TextView transDate;
        public TextView pointsValue;
    }
}