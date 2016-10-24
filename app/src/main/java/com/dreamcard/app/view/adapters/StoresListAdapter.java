package com.dreamcard.app.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.utils.ImageUtil;
import com.dreamcard.app.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Moayed on 6/24/2014.
 */
public class StoresListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Stores> data;
    private static LayoutInflater inflater = null;

    public StoresListAdapter(Activity a, ArrayList<Stores> list) {
        this.activity = a;
        this.data = list;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return this.data.size();
    }

    public Object getItem(int position) {
        return this.data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        StoresHolder holder;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.stores_list_item, null);
            holder = new StoresHolder();
            holder.storeName = (TextView) vi.findViewById(R.id.store_name);
            holder.storeIcon = (ImageView) vi.findViewById(R.id.store_icon);
            vi.setTag(holder);
        } else {
            holder = (StoresHolder) vi.getTag();
            holder.storeIcon.setImageBitmap(null);
        }

        Stores bean = this.data.get(position);
        holder.storeName.setText(bean.getStoreName());
        Utils.loadImage(activity, bean.getLogo(), holder.storeIcon);
        return vi;
    }

    private class StoresHolder {
        public ImageView storeIcon;
        public TextView storeName;
    }
}
