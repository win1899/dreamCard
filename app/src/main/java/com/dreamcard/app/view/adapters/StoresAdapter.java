package com.dreamcard.app.view.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dreamcard.app.R;
import com.dreamcard.app.entity.CashPointsTransaction;
import com.dreamcard.app.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by WIN on 5/28/2016.
 */
public class StoresAdapter extends
        RecyclerView.Adapter<StoresAdapter.ViewHolder> {

    private HashMap<Integer, ArrayList<CashPointsTransaction>> _transactions;
    private HashMap<Integer, Integer> _positionToId;
    private List<ImageView> _images;
    private Context _context;
    private LinearLayoutManager _layoutManager;
    private RecyclerView _recyclerView;

    // Pass in the contact array into the constructor
    public StoresAdapter(HashMap<Integer, ArrayList<CashPointsTransaction>> transactions, HashMap<Integer, Integer> idToPosition, Context context, LinearLayoutManager layoutManager, RecyclerView recyclerView) {
        _transactions = transactions;
        _positionToId = idToPosition;
        _context = context;
        _images = new ArrayList<>(_transactions.size());
        _layoutManager = layoutManager;
        _recyclerView = recyclerView;
    }

    public ImageView getImage(int position) {
        if (position >= _images.size() || position < 0) {
            return null;
        }

        return _images.get(position);
    }

    @Override
    public StoresAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View storeView = inflater.inflate(R.layout.recyceler_dashboard_points_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(storeView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(StoresAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        ArrayList<CashPointsTransaction> trans = _transactions.get(_positionToId.get(position));

       // _images.add(position, viewHolder.storeImageView);
        // Set item views based on the data model
        ImageView image = viewHolder.storeImageView;
        CashPointsTransaction transaction = new CashPointsTransaction();
        if (trans.size() > 0) {
            transaction = trans.get(0);
        }

        if (transaction == null || transaction.getStoreLogo() == null || transaction.getStoreLogo().equalsIgnoreCase("")) {
            viewHolder.storeImageView.setImageDrawable(null);
            viewHolder.storeImageView.setVisibility(View.INVISIBLE);
            return;
        }
        Utils.loadImage(_context, transaction.getStoreLogo(), image);
        viewHolder.storeImageView.setVisibility(View.VISIBLE);
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return _transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView storeImageView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            storeImageView = (ImageView) itemView.findViewById(R.id.store_image_dashboard);
        }
    }
}
