package com.dreamcard.app.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.entity.OffersRecordHolder;
import com.dreamcard.app.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Moayed on 8/4/2014.
 */
public class StoreOffersGridAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Offers> data;
    private static LayoutInflater inflater = null;
    private View.OnClickListener listener;

    public StoreOffersGridAdapter(Activity a, ArrayList<Offers> list, View.OnClickListener listener) {
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
            vi = inflater.inflate(R.layout.store_offer_row_layout, null);
            holder = new OffersRecordHolder();
            holder.setImgOffer((ImageView) vi.findViewById(R.id.img_offer_logo));
            holder.setTxtPrice((TextView) vi.findViewById(R.id.txt_offer_price));
            holder.setPnl((RelativeLayout) vi.findViewById(R.id.store_offer_pnl));
            holder.setTxtName((TextView) vi.findViewById(R.id.txt_offer_name));
            vi.setTag(holder);
        } else
            holder = (OffersRecordHolder) convertView.getTag();


        Offers bean = this.data.get(position);
        holder.getTxtPrice().setText(bean.getSaleNewPrice());
        if (bean.getCurrency() != null) {
            if (bean.getCurrency().equalsIgnoreCase(Params.CURRENCY.ILS)) {
                holder.getTxtPrice().setText(holder.getTxtPrice().getText() + this.activity.getString(R.string.ils));
            } else if (bean.getCurrency().equalsIgnoreCase(Params.CURRENCY.USD)) {
                holder.getTxtPrice().setText(holder.getTxtPrice().getText() + this.activity.getString(R.string.usd));
            }
        }
        holder.getPnl().setId(bean.getPosition());
        holder.getPnl().setOnClickListener(this.listener);
        String title = bean.getTitle();
        holder.getTxtName().setText(title);

        if (bean.getOfferMainPhoto() != null && !bean.getOfferMainPhoto().equalsIgnoreCase("null")
                && bean.getOfferMainPhoto().length() > 0) {
            Utils.loadImage(this.activity, bean.getOfferMainPhoto(), holder.getImgOffer());
        }
        return vi;
    }

}
