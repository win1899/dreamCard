package com.dreamcard.app.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.utils.ImageViewLoader;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Moayed on 9/19/2014.
 */
public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
{
    private  Activity context;
    private Stores store;
    private View.OnClickListener listener;

    public MarkerInfoWindowAdapter(Activity context,Stores store,View.OnClickListener listener){
        this.context=context;
        this.store=store;
        this.listener=listener;
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker)
    {
        View v  = this.context.getLayoutInflater().inflate(R.layout.map_marker_layout, null);
        ImageView markerIcon = (ImageView) v.findViewById(R.id.marker_icon);
        TextView markerLabel = (TextView)v.findViewById(R.id.marker_label);
        LinearLayout layout=(LinearLayout)v.findViewById(R.id.map_layout);
        layout.setOnClickListener(this.listener);
        markerIcon.setOnClickListener(this.listener);

        if(this.store!=null) {
            markerLabel.setText(this.store.getStoreName());
            ImageViewLoader imgLoader = new ImageViewLoader(this.context);
            imgLoader.DisplayImage(store.getLogo(), markerIcon, this.context.getResources());
            layout.setId(store.getPosition());
            markerIcon.setId(store.getPosition());
        }else{
            markerLabel.setText("Store 1");
            markerIcon.setImageResource(R.drawable.categories);
            layout.setId(-1);
            markerIcon.setId(-1);
        }

        return v;
    }
}
