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
import com.dreamcard.app.entity.RecordHolder;

import java.util.ArrayList;

/**
 * Created by Moayed on 6/25/2014.
 */
public class CommentsAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Comments> data;
    private static LayoutInflater inflater = null;

    public CommentsAdapter(Activity a, ArrayList<Comments> list) {
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
        RecordHolder holder = null;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.category_list_item, null);
            holder = new RecordHolder();
            holder.setTxtTitle((TextView) vi.findViewById(R.id.txt_title));
            holder.setTxtUser((TextView) vi.findViewById(R.id.txt_username));
            vi.setTag(holder);
        } else
            holder = (RecordHolder) convertView.getTag();

        Comments bean = this.data.get(position);
        holder.getTxtTitle().setText(bean.getComment());
        if (bean.getConsumer() != null) {
            if (!bean.getConsumer().equalsIgnoreCase("null")) {
                holder.getTxtUser().setText(bean.getConsumer());
            }
        }
        return vi;
    }
}
