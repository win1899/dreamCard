package com.dreamcard.app.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.entity.PurchasesHolder;

import java.util.ArrayList;

/**
 * Created by Moayed on 6/24/2015.
 */
public class PurchasesListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Offers> data;
    private static LayoutInflater inflater=null;

    public PurchasesListAdapter(Activity a,ArrayList<Offers> list) {
        this.activity = a;
        this.data=list;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return this.data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        PurchasesHolder holder=null;
        if(convertView==null) {
            vi = inflater.inflate(R.layout.purchases_list_row_layout, null);
            holder=new PurchasesHolder();
            holder.setTxtStoreName((TextView)vi.findViewById(R.id.txt_store_name));
            holder.setTxtCity((TextView) vi.findViewById(R.id.txt_city));
            holder.setTxtAmount((TextView) vi.findViewById(R.id.txt_amount));
            vi.setTag(holder);
        }else
            holder=(PurchasesHolder)convertView.getTag();

        Offers bean=this.data.get(position);
        holder.getTxtStoreName().setText(bean.getBusinessName());
        holder.getTxtAmount().setText(String.valueOf(bean.getAmountAfterDiscount()));
        return vi;
    }
}
