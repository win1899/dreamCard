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
import com.dreamcard.app.entity.NotificationDB;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.activity.OfferDetailsActivity;
import com.dreamcard.app.view.activity.ReviewStore;
import com.dreamcard.app.view.activity.StoreDetailsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by WIN on 3/2/2016.
 */
public class NotificationsAdapter extends BaseAdapter {

    private Activity activity;
    private HashMap<Integer, NotificationDB> data;
    private static LayoutInflater inflater = null;

    public NotificationsAdapter(Activity a, HashMap<Integer, NotificationDB> list) {
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
            holder.notificationDetails = (TextView) vi.findViewById(R.id.notification_action_name);
            vi.setTag(holder);
        } else {
            holder = (NotificationHolder) vi.getTag();
            holder.notificationLogo.setImageBitmap(null);
        }

        final NotificationDB notification = data.get(position);

        if (notification.type.equalsIgnoreCase(NotificationDB.REVIEW_TYPE)) {
            holder.notificationText.setText("شكرا لاستخدامك كرت الاحلام في " + notification.store.getStoreName() + ".");
            //Utils.loadImage(activity, store.getLogo(), holder.notificationLogo);
            holder.notificationLogo.setVisibility(View.GONE);
            holder.notificationDetails.setText("اضغط هنا لتقييم تعاملك");

            vi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ReviewStore.class);
                    intent.putExtra(ReviewStore.BUSINESS_ID_EXTRA, notification.store.getId());
                    activity.startActivity(intent);
                    activity.finish();
                }
            });
        }
        else if (notification.type.equalsIgnoreCase(NotificationDB.STORE_TYPE)) {
            holder.notificationText.setText(" انضم لعائلة كرت الاحلام"+ " " + notification.store.getStoreName() + ".");
            //Utils.loadImage(activity, store.getLogo(), holder.notificationLogo);
            holder.notificationLogo.setVisibility(View.GONE);
            holder.notificationDetails.setText("اضغط هنا لزيارة المحل");

            vi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, StoreDetailsActivity.class);
                    intent.putExtra(Stores.EXTRA_STORE_ID, notification.id);
                    activity.startActivityForResult(intent, 2);
                }
            });
        }
        else if (notification.type.equalsIgnoreCase(NotificationDB.OFFER_TYPE)) {
            holder.notificationText.setText("تم اضافة عرض جديد.");
            //Utils.loadImage(activity, store.getLogo(), holder.notificationLogo);
            holder.notificationLogo.setVisibility(View.GONE);
            holder.notificationDetails.setText("اضغط هنا لمشاهدة العرض");

            vi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, OfferDetailsActivity.class);
                    intent.putExtra(Offers.EXTRA_OFFER_ID, notification.id);
                    activity.startActivityForResult(intent, 1);
                }
            });
        }
        return vi;
    }

    private class NotificationHolder {
        public ImageView notificationLogo;
        public TextView notificationText;
        public TextView notificationDetails;
    }
}