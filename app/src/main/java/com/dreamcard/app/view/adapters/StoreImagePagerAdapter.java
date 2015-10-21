package com.dreamcard.app.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.dreamcard.app.R;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.view.activity.ImageViewerActivity;

/**
 * Created by WIN on 10/21/2015.
 */
public class StoreImagePagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private Stores mStore;


    public StoreImagePagerAdapter(Context context, Stores store) {
        mContext = context;
        mStore = store;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (mStore.getPictures() == null) {
            return 1;
        }
        return mStore.getPictures().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.activity_image_viewer, container, false);
        AQuery aq = new AQuery(mContext);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_offer_image);

        if (mStore.getPictures() == null || mStore.getPictures().length <= 0) {
            if (mStore.getLogo() == null) {
                itemView.setVisibility(View.GONE);
                return itemView;
            }
            aq.id(imageView).image(mStore.getLogo(), true, true
                    , imageView.getWidth(), 0, null, AQuery.FADE_IN, AQuery.RATIO_PRESERVE);
        }
        else {
            aq.id(imageView).image(mStore.getPictures()[position], true, true
                    , imageView.getWidth(), 0, null, AQuery.FADE_IN, AQuery.RATIO_PRESERVE);
        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
