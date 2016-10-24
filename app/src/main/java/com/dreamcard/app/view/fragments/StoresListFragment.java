package com.dreamcard.app.view.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import android.widget.Toast;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.services.AllBusinessAsync;
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.adapters.StoresListAdapter;
import com.dreamcard.app.view.fragments.dummy.DummyContent;
import com.dreamcard.app.view.interfaces.IServiceListener;
import com.dreamcard.app.view.interfaces.OnFragmentInteractionListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {}
 * interface.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class StoresListFragment extends Fragment implements AbsListView.OnItemClickListener,IServiceListener
        ,View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    private ArrayList<Stores> list;
    private ArrayList<Stores> gridList=new ArrayList<Stores>();
    HashMap<String,Stores> storesMap=new HashMap<String,Stores>();

    private AllBusinessAsync allBusinessAsync;
    private Activity activity;
    private ProgressBar progressBar;
    private ListView listView;

    public static StoresListFragment newInstance(String param1, String param2) {
        StoresListFragment fragment = new StoresListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public StoresListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storeslist, container, false);

        progressBar=(ProgressBar)view.findViewById(R.id.progress);
        listView = (ListView) view.findViewById(R.id.stores_list_view);

        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        allBusinessAsync=new AllBusinessAsync(this, new ArrayList<ServiceRequest>(), Params.SERVICE_PROCESS_1);
        allBusinessAsync.execute(this.activity);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity=activity;
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (allBusinessAsync != null && allBusinessAsync.getStatus() == AsyncTask.Status.RUNNING) {
            allBusinessAsync.cancel(true);
        }
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id, Params.FRAGMENT_STORES);
        }
    }

    @Override
    public void onServiceSuccess(Object b, int processType) {
        if (getActivity() == null) {
            Log.e(this.getClass().getName(), "Activity is null, avoid callback");
            return;
        }
        if(processType == Params.SERVICE_PROCESS_1) {
            ArrayList<Stores> list = (ArrayList<Stores>) b;
            this.list = list;
            setGoldList();

            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {
        if (getActivity() == null) {
            Log.e(this.getClass().getName(), "Activity is null, avoid callback");
            return;
        }
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        Toast.makeText(this.activity, info.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void setGoldList() {
        ArrayList<Stores> gold = new ArrayList<>();
        ArrayList<Stores> silver = new ArrayList<>();
        ArrayList<Stores> other = new ArrayList<>();

        for(Stores bean:list){
            if(bean.getStoreClass()==Params.STORE_CLASS_GOLD) {
                gold.add(bean);
            }else if(bean.getStoreClass()==Params.STORE_CLASS_SILVER){
                silver.add(bean);
                this.storesMap.put(""+bean.getPosition(),bean);
            }else{
                other.add(bean);
                this.gridList.add(bean);
            }
        }

        this.list.clear();
        this.list.addAll(gold);
        list.addAll(silver);
        list.addAll(other);

        StoresListAdapter adapter = new StoresListAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.doAction(list.get(position), Params.FRAGMENT_STORES);
            }
        });
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private View insertPhotoGold(String url,int position, int width, int height){
        LinearLayout layout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
        params.setMargins(10,10,10,10);
        layout.setLayoutParams(params);
        layout.setGravity(Gravity.CENTER);
        layout.setId(position);
        layout.setBackground(getResources().getDrawable(R.drawable.other_offer_background));
        layout.setOnClickListener(this);

        ImageView imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(width - dpToPx(10),height - dpToPx(10)));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setId(position);
        imageView.setOnClickListener(this);
        imageView.setOnClickListener(this);
        layout.addView(imageView);

        Utils.loadImage(activity, url, imageView, width - dpToPx(10), height - dpToPx(10), false);
        return layout;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private View insertPhoto(String url,int position,int width,int height){
        LinearLayout layout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(10, 10, 10, 10);
        layout.setLayoutParams(params);
        layout.setGravity(Gravity.CENTER);
        layout.setBackground(getResources().getDrawable(R.drawable.other_offer_background));

        ImageView imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(width - dpToPx(10),height - dpToPx(10)));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setId(position);
        imageView.setOnClickListener(this);
        layout.addView(imageView);
        Utils.loadImage(activity, url, imageView, width - dpToPx(10), height - dpToPx(10), false);
        return layout;
    }

    @Override
    public void onClick(View view) {
        Stores info=null;
        for(Stores bean:this.list){
            int id=view.getId();
            if(view.getId()==bean.getPosition()){
                info=bean;
                break;
            }
        }
        if(info!=null){
            mListener.doAction(info, Params.FRAGMENT_STORES);
        }
    }
}
