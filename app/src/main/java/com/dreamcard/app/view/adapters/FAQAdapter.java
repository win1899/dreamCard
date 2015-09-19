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
import com.dreamcard.app.entity.FAQ;
import com.dreamcard.app.entity.FAQRecordHolder;
import com.dreamcard.app.entity.RecordHolder;

import java.util.ArrayList;

/**
 * Created by Moayed on 6/20/2015.
 */
public class FAQAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<FAQ> data;
    private static LayoutInflater inflater = null;

    public FAQAdapter(Activity a, ArrayList<FAQ> list) {
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
        FAQRecordHolder holder = null;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.faq_item_layout, null);
            holder = new FAQRecordHolder();
            holder.setQuastion((TextView) vi.findViewById(R.id.txt_quastion));
            holder.setAnswer((TextView) vi.findViewById(R.id.txt_answer));
            vi.setTag(holder);
        } else
            holder = (FAQRecordHolder) convertView.getTag();

        FAQ bean = this.data.get(position);
        holder.getQuastion().setText(bean.getQuastion());
        holder.getAnswer().setText(bean.getAnswer());
        return vi;
    }
}
