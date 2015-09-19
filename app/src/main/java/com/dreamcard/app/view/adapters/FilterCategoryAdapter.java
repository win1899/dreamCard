package com.dreamcard.app.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.entity.Categories;
import com.dreamcard.app.entity.Comments;
import com.dreamcard.app.entity.RecordHolder;
import com.dreamcard.app.view.fragments.ActivationSettingFragment;

import java.util.ArrayList;

/**
 * Created by Moayed on 6/14/2015.
 */
public class FilterCategoryAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Categories> data;
    private static LayoutInflater inflater = null;
    private ActivationSettingFragment.RecordListener recordListener;

    public FilterCategoryAdapter(Activity a, ArrayList<Categories> list, ActivationSettingFragment.RecordListener recordListener) {
        this.activity = a;
        this.data = list;
        this.recordListener = recordListener;
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
//        Categories bean=this.data.get(position);
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        RecordHolder holder = null;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.filter_category_list_item, null);
            holder = new RecordHolder();
            holder.setTxtTitle((TextView) vi.findViewById(R.id.txt_category_name));
            holder.setImageItem((ImageView) vi.findViewById(R.id.img_selected));
            vi.setTag(holder);
        } else
            holder = (RecordHolder) convertView.getTag();
        Categories bean = this.data.get(position);
        if (bean.getTitle().length() > 16) {
            holder.getTxtTitle().setText(bean.getTitle().substring(0, 16) + "...");
        } else {
            holder.getTxtTitle().setText(bean.getTitle());
        }
        holder.getImageItem().setId(Integer.parseInt(bean.getId()));
        if (bean.isSelected()) {
            holder.getImageItem().setVisibility(View.VISIBLE);
        }
        if (recordListener != null) {
            this.recordListener.setHolder(holder, Integer.parseInt(bean.getId()));
        }
        return vi;
    }
}
