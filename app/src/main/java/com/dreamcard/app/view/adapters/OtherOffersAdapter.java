package com.dreamcard.app.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.entity.Comments;
import com.dreamcard.app.entity.Offers;

import java.util.ArrayList;

/**
 * Created by Moayed on 6/25/2014.
 */
public class OtherOffersAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Offers> data;
    private static LayoutInflater inflater=null;

    public OtherOffersAdapter(Activity a,ArrayList<Offers> list) {
        this.activity = a;
        this.data=list;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.category_list_item, null);

        TextView title = (TextView)vi.findViewById(R.id.txt_title);
        Offers bean=this.data.get(position);
        title.setText(bean.getTitle());
        return vi;
    }
}
