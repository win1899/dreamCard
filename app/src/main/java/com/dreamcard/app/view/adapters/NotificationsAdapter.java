package com.dreamcard.app.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.activity.ReviewStore;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by WIN on 3/2/2016.
 */
public class NotificationsAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Stores> data;
    private static LayoutInflater inflater = null;

    public NotificationsAdapter(Activity a, ArrayList<Stores> list) {
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
        NotificationHolder holder;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.notification_list_item, null);
            holder = new NotificationHolder();
            holder.notificationText = (TextView) vi.findViewById(R.id.notification_text);
            holder.notificationLogo = (ImageView) vi.findViewById(R.id.notification_logo);
            vi.setTag(holder);
        } else {
            holder = (NotificationHolder) vi.getTag();
        }

        final Stores store = data.get(position);

        holder.notificationText.setText("شكرا لاستخدامك كرت الاحلام في " + store.getStoreName() + ".");
        Utils.loadImage(activity, store.getLogo(), holder.notificationLogo);

        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ReviewStore.class);
                intent.putExtra(ReviewStore.BUSINESS_ID_EXTRA, store.getId());
                activity.startActivity(intent);
                activity.finish();
            }
        });
        return vi;
    }

    private class NotificationHolder {
        public ImageView notificationLogo;
        public TextView notificationText;
    }
}