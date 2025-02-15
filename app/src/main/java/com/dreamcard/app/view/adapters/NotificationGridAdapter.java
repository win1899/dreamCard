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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.entity.OffersRecordHolder;
import com.dreamcard.app.utils.ImageUtil;
import com.dreamcard.app.utils.ImageViewLoader;

import java.util.ArrayList;

/**
 * Created by Moayed on 8/15/2014.
 */
public class NotificationGridAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Offers> data;
    private static LayoutInflater inflater = null;
    private View.OnClickListener listener;

    public NotificationGridAdapter(Activity a, ArrayList<Offers> list, View.OnClickListener listener) {
        this.activity = a;
        this.data = list;
        this.listener = listener;
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
        OffersRecordHolder holder = null;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.notification_row_layout, null);
            holder = new OffersRecordHolder();
            holder.setImgOffer((ImageView) vi.findViewById(R.id.img_notification_icon));
            holder.setTxtName((TextView) vi.findViewById(R.id.txt_notification_detail));
            holder.setRelativeLayout((RelativeLayout) vi.findViewById(R.id.notification_pnl));
            vi.setTag(holder);
        } else
            holder = (OffersRecordHolder) convertView.getTag();


        Offers bean = this.data.get(position);
        holder.getRelativeLayout().setId(bean.getPosition());
        holder.getRelativeLayout().setOnClickListener(this.listener);
        holder.getImgOffer().setOnClickListener(this.listener);

        double discount = bean.getAmountBeforeDicount() - bean.getAmountAfterDiscount();
        String discSt = String.valueOf(discount);
        if (String.valueOf(discount).lastIndexOf(".") != -1) {
            discSt = String.valueOf(discount).substring(0, String.valueOf(discount).lastIndexOf("."));
        }

        String title = this.activity.getString(R.string.notification_desc_1) + " " + discSt + " "
                + this.activity.getString(R.string.notification_desc_2) + " " + bean.getBusinessName();
        if (title.length() > 35) {
            title = title.substring(0, 35) + "...";
        }
        holder.getTxtName().setText(title);

        ImageViewLoader imgLoader = new ImageViewLoader(this.activity);
        imgLoader.DisplayImage(bean.getOfferMainPhoto(), holder.getImgOffer(), this.activity.getResources());

        return vi;
    }
}
