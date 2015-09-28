package com.dreamcard.app.view.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.dreamcard.app.R;

import com.dreamcard.app.components.TransparentProgressDialog;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.GridItem;
import com.dreamcard.app.entity.ItemButton;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.services.AllBusinessAsync;
import com.dreamcard.app.utils.ImageViewLoader;
import com.dreamcard.app.view.adapters.RegularStoresGridAdapter;
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

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private LinearLayout goldLayout;
    private LinearLayout silverLayout;
    private GridView grid;
    private ListAdapter mAdapter;
    private RegularStoresGridAdapter adapter;
    private HorizontalScrollView goldScroll;
    private TextView txtAdv1;
    private TextView txtAdv2;
    private HorizontalScrollView silverScroll;
    private ArrayList<Stores> list;
    private ArrayList<Stores> gridList=new ArrayList<Stores>();
    HashMap<String,Stores> storesMap=new HashMap<String,Stores>();

    private AllBusinessAsync allBusinessAsync;
    private Activity activity;
    private ProgressBar progressBar;
    private LinearLayout storeListMainLayout;

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

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storeslist, container, false);

        goldLayout=(LinearLayout)view.findViewById(R.id.gold_gallery);
        silverLayout=(LinearLayout)view.findViewById(R.id.silver_gallery);
        grid=(GridView)view.findViewById(R.id.regular_stores_grid);
        goldScroll=(HorizontalScrollView)view.findViewById(R.id.gold_scroll);
        txtAdv1=(TextView)view.findViewById(R.id.txt_adv_1);
        txtAdv2=(TextView)view.findViewById(R.id.txt_adv_2);
        progressBar=(ProgressBar)view.findViewById(R.id.progress);
        storeListMainLayout = (LinearLayout) view.findViewById(R.id.store_list_main_layout);
        silverScroll=(HorizontalScrollView)view.findViewById(R.id.silver_scroll);
        progressBar.setVisibility(View.VISIBLE);
        storeListMainLayout.setVisibility(View.GONE);
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
        allBusinessAsync.cancel(true);
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
            ArrayList<GridItem> gridArray = new ArrayList<GridItem>();
            for (Stores trans : this.gridList) {
                gridArray.add(new ItemButton(trans.getLogo(), "", trans.getId(), trans.getPosition()));
            }
            adapter = new RegularStoresGridAdapter(getActivity(), R.layout.stores_grid_button
                    , gridArray, this, null);
            grid.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            storeListMainLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {
        if (getActivity() == null) {
            Log.e(this.getClass().getName(), "Activity is null, avoid callback");
            return;
        }
        progressBar.setVisibility(View.GONE);
        storeListMainLayout.setVisibility(View.VISIBLE);
        Toast.makeText(this.activity, info.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void setGoldList() {
        for(Stores bean:list){
            if(bean.getStoreClass()==Params.STORE_CLASS_GOLD) {
                goldLayout.addView(insertPhotoGold(bean.getLogo(), bean.getPosition(), 220, 200));
                this.storesMap.put(""+bean.getPosition(),bean);
            }else if(bean.getStoreClass()==Params.STORE_CLASS_SILVER){
                silverLayout.addView(insertPhoto(bean.getLogo(), bean.getPosition(),150,150));
                this.storesMap.put(""+bean.getPosition(),bean);
            }else{
                this.gridList.add(bean);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private View insertPhotoGold(String url,int position,int width,int height){
        LinearLayout layout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
        params.setMargins(10,10,10,10);
        layout.setLayoutParams(params);
        layout.setGravity(Gravity.CENTER);
        layout.setId(position);
        layout.setBackground(getResources().getDrawable(R.drawable.other_offer_background));
        layout.setOnClickListener(this);

        ImageView imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setId(position);
        imageView.setOnClickListener(this);
        imageView.setOnClickListener(this);
        layout.addView(imageView);
        AQuery aq=new AQuery(this.activity);
        aq.id(imageView).image(url, true, true
                , imageView.getWidth(), 0, null, AQuery.FADE_IN, AQuery.RATIO_PRESERVE);
        return layout;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private View insertPhoto(String url,int position,int width,int height){
        LinearLayout layout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
        params.setMargins(10,10,10,10);
        layout.setLayoutParams(params);
        layout.setGravity(Gravity.CENTER);
        layout.setBackground(getResources().getDrawable(R.drawable.other_offer_background));

        ImageView imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setId(position);
        imageView.setOnClickListener(this);
        layout.addView(imageView);
        ImageViewLoader imgLoader = new ImageViewLoader(getActivity());
        imgLoader.DisplayImage(url, imageView, getActivity().getResources());
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
