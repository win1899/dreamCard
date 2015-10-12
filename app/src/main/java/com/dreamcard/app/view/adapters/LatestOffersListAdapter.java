package com.dreamcard.app.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.entity.OffersRecordHolder;

import java.util.ArrayList;

/**
 * Created by Moayed on 6/11/2014.
 */
public class LatestOffersListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Offers> data;
    private static LayoutInflater inflater = null;
    private View.OnClickListener listener;

    public LatestOffersListAdapter(Activity a, ArrayList<Offers> list, View.OnClickListener listener) {
        this.activity = a;
        this.data = list;
        this.listener = listener;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getViewTypeCount() {

        int count = 1;
        if (data.size() > 0) {
            count = data.size();
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
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
            vi = inflater.inflate(R.layout.offer_row_grid_layout, null);
            holder = new OffersRecordHolder();
            holder.setImgOffer((ImageView) vi.findViewById(R.id.gl_button));
            holder.setTxtPrice((TextView) vi.findViewById(R.id.txt_price));
            holder.setTxtOldPrice((TextView) vi.findViewById(R.id.txt_old_price));
            holder.setTxtDesc((TextView) vi.findViewById(R.id.txt_description));
            holder.setPnl((RelativeLayout) vi.findViewById(R.id.offer_grid_item_layout));
            holder.setRatingBar((LinearLayout) vi.findViewById(R.id.rating_pnl));
            holder.setRelativeLayout((RelativeLayout) vi.findViewById(R.id.main_offer_btn_layout));
            holder.setView(vi);
            vi.setTag(holder);
        } else
            holder = (OffersRecordHolder) convertView.getTag();


        Offers bean = this.data.get(position);

        if (bean.getDescription() != null && !bean.getDescription().equalsIgnoreCase("null")) {
            if (bean.getTitle().length() > 30) {
                bean.setTitle(bean.getTitle().substring(0, 30) + "...");
            }
            holder.getTxtDesc().setText(bean.getTitle());
        }

        if (bean.getSaleOldPrice().contains(".")) {
            String fraction = bean.getSaleOldPrice().substring(bean.getSaleOldPrice().indexOf("."));
            if (fraction.length() > 2) {
                bean.setSaleOldPrice(bean.getSaleOldPrice().substring(0, (bean.getSaleOldPrice().indexOf(".") + 3))
                        + bean.getSaleOldPrice().substring(bean.getSaleOldPrice().length() - 1));
            }
        }

        if (bean.getSaleOldPrice() != null && !bean.getSaleOldPrice().equalsIgnoreCase("null")) {
            holder.getTxtOldPrice().setText(bean.getSaleOldPrice());
            holder.getTxtOldPrice().setPaintFlags(holder.getTxtOldPrice().getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (bean.getSaleNewPrice().contains(".")) {
            String fraction = bean.getSaleNewPrice().substring(bean.getSaleNewPrice().indexOf("."));
            if (fraction.length() > 2) {
                bean.setSaleNewPrice(bean.getSaleNewPrice().substring(0, (bean.getSaleNewPrice().indexOf(".") + 3))
                        + bean.getSaleNewPrice().substring(bean.getSaleNewPrice().length() - 1));
            }
        }

        holder.getTxtPrice().setText(bean.getSaleNewPrice());

        if (bean.getCurrency() != null) {
            if (bean.getCurrency().equalsIgnoreCase(Params.CURRENCY.ILS)) {
                holder.getTxtPrice().setText(holder.getTxtPrice().getText() + this.activity.getString(R.string.ils));
                holder.getTxtOldPrice().setText(holder.getTxtOldPrice().getText() + this.activity.getString(R.string.ils));
            } else if (bean.getCurrency().equalsIgnoreCase(Params.CURRENCY.USD)) {
                holder.getTxtPrice().setText(holder.getTxtPrice().getText() + this.activity.getString(R.string.usd));
                holder.getTxtOldPrice().setText(holder.getTxtOldPrice().getText() + this.activity.getString(R.string.usd));
            }
        }


        holder.getPnl().setId(bean.getPosition());
        holder.getPnl().setOnClickListener(this.listener);
        holder.getView().setId(bean.getPosition());
        holder.getView().setOnClickListener(this.listener);
        holder.getImgOffer().setId(bean.getPosition());
        holder.getImgOffer().setOnClickListener(this.listener);

        if (bean.getOfferMainPhoto() != null && !bean.getOfferMainPhoto().equalsIgnoreCase("null")
                && bean.getOfferMainPhoto().length() > 0) {
            AQuery aq = new AQuery(this.activity);
            aq.id(holder.getImgOffer()).progress(R.id.offer_detail_progress).image(bean.getOfferMainPhoto(), true, true
                    , holder.getImgOffer().getWidth(), R.drawable.offer_item_bg, null, AQuery.FADE_IN, AQuery.RATIO_PRESERVE);
        }
        holder.getRatingBar().removeAllViews();
        setRating(bean.getOfferRating(), holder.getRatingBar());
        return vi;
    }

    private void setRating(int rating, LinearLayout ratingPnl) {
        for (int index = 0; index < rating; index++) {
            ratingPnl.addView(insertRating(index, 20, 20, activity.getResources().getDrawable(R.drawable.stars_active)));
        }
        for (int index = 0; index < 5 - rating; index++) {
            ratingPnl.addView(insertRating(index, 20, 20, activity.getResources().getDrawable(R.drawable.stars)));
        }
    }

    private View insertRating(int position, int width, int height, Drawable image) {
        Bitmap bm = drawableToBitmap(image);
        LinearLayout layout = new LinearLayout(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(5, 0, 0, 0);
        layout.setLayoutParams(params);
        layout.setGravity(Gravity.CENTER);
        ImageView imageView = new ImageView(activity);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setId(position);
        imageView.setImageBitmap(bm);
        layout.addView(imageView);
        return layout;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
