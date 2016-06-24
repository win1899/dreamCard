package com.dreamcard.app.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.entity.OffersRecordHolder;
import com.dreamcard.app.entity.Voucher;
import com.dreamcard.app.utils.ImageUtil;
import com.dreamcard.app.utils.ImageViewLoader;
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.activity.OfferInvoicePdfActivity;

import java.util.ArrayList;

/**
 * Created by Moayed on 8/15/2014.
 */
public class VoucerListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Voucher> data;
    private static LayoutInflater inflater = null;

    public VoucerListAdapter(Activity a, ArrayList<Voucher> list) {
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
        VoucherItemHolder holder = null;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.voucher_item_list, null);
            holder = new VoucherItemHolder();
            holder.setVoucherImage((ImageView) vi.findViewById(R.id.voucher_image_item));
            vi.setTag(holder);
        } else
            holder = (VoucherItemHolder) convertView.getTag();

        Utils.loadImage(activity, data.get(position).getUrl(), holder.getVoucherImage());
        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return vi;
    }

    private class VoucherItemHolder {
        public ImageView getVoucherImage() {
            return mVoucherImage;
        }

        public void setVoucherImage(ImageView mVoucherImage) {
            this.mVoucherImage = mVoucherImage;
        }

        private ImageView mVoucherImage;
    }
}
