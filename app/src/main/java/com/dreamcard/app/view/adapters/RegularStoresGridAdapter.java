package com.dreamcard.app.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.dreamcard.app.R;
import com.dreamcard.app.entity.GridItem;
import com.dreamcard.app.entity.ItemButton;
import com.dreamcard.app.entity.RecordHolder;
import com.dreamcard.app.utils.ButtonImageLoader;
import com.dreamcard.app.utils.ImageViewLoader;
import com.dreamcard.app.view.fragments.ActivationSettingFragment;

import java.util.ArrayList;

/**
 * Created by Moayed on 7/24/2014.
 */
public class RegularStoresGridAdapter extends ArrayAdapter<GridItem> {

    View.OnClickListener listener;
    private ActivationSettingFragment.RecordListener recordListener;
    private boolean withHashCode = false;
    private Context context;
    private int layoutResourceId;
    private static LayoutInflater inflater = null;
    private ArrayList<GridItem> data = new ArrayList<GridItem>();

    public RegularStoresGridAdapter(Context context, int layoutResourceId,
                                    ArrayList<GridItem> data, View.OnClickListener listener
            , ActivationSettingFragment.RecordListener recordListener) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.listener = listener;
        this.layoutResourceId = layoutResourceId;
        this.recordListener = recordListener;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            row = inflater.inflate(layoutResourceId, null);
            holder = new RecordHolder();
            holder.setImageItem((ImageView) row.findViewById(R.id.gl_button));
            row.setTag(holder);

        } else
            holder = (RecordHolder) convertView.getTag();

        holder.getImageItem().setOnClickListener(listener);
        ItemButton item = (ItemButton) data.get(position);
        holder.getImageItem().setId(item.getBtnImage());
        ImageViewLoader imgLoader = new ImageViewLoader(this.context);
        imgLoader.DisplayImage(item.getUrl(), holder.getImageItem(), this.context.getResources());
        return row;

    }
}
