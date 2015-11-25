package com.dreamcard.app.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.dreamcard.app.R;
import com.dreamcard.app.entity.Stores;

import java.util.ArrayList;

/**
 * Created by WIN on 11/25/2015.
 */
public class SellingAreasAdapter  extends BaseAdapter {

    private Activity activity;
    private ArrayList<Stores> data;
    private static LayoutInflater inflater = null;

    public SellingAreasAdapter(Activity a, ArrayList<Stores> list) {
        this.activity = a;
        this.data = list;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View vi = convertView;
        StoreHolder holder;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.where_to_buy_list_item, null);
            holder = new StoreHolder();
            holder.storeLogo = (ImageView) vi.findViewById(R.id.buy_areas_store_logo);
            holder.storeName = (TextView) vi.findViewById(R.id.buy_areas_store_name);
            holder.storeAddress = (TextView) vi.findViewById(R.id.buy_areas_store_address);
            holder.storePhone = (TextView) vi.findViewById(R.id.buy_areas_store_phone);

            vi.setTag(holder);
        }
        else {
            holder = (StoreHolder) vi.getTag();
        }

        Stores store = data.get(position);

        holder.storeName.setText(store.getStoreName());
        holder.storeAddress.setText(store.getAddress1());
        holder.storePhone.setText(store.getPhone());

        AQuery aq = new AQuery(activity);
        aq.id(holder.storeLogo).image(store.getLogo(), true, true
                , holder.storeLogo.getWidth(), 0, null, AQuery.FADE_IN, AQuery.RATIO_PRESERVE);

        return vi;
    }

    private class StoreHolder {
        public ImageView storeLogo;
        public TextView storeName;
        public TextView storeAddress;
        public TextView storePhone;
    }
}