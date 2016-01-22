package com.dreamcard.app.view.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dreamcard.app.R;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.utils.Utils;

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

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_offer_image);

        if (mStore.getPictures() == null || mStore.getPictures().length <= 0) {
            if (mStore.getLogo() == null) {
                itemView.setVisibility(View.GONE);
                return itemView;
            }
            Utils.loadImage(mContext, mStore.getLogo(), imageView);
        }
        else {
            Utils.loadImage(mContext, mStore.getPictures()[position], imageView);
        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
