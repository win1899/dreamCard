package com.dreamcard.app.view.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.Categories;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.GridItem;
import com.dreamcard.app.entity.ItemButton;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.services.CategoriesAsync;
import com.dreamcard.app.view.adapters.CategoriesListAdapter;
import com.dreamcard.app.view.adapters.CustomGridViewAdapterButton;
import com.dreamcard.app.view.interfaces.IServiceListener;
import com.dreamcard.app.view.interfaces.OnFragmentInteractionListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubcategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubcategoryFragment extends Fragment implements View.OnClickListener,IServiceListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Categories categories;
    private String mParam2;

    private GridView grid;
    private CustomGridViewAdapterButton customGridAdapter;
    private CategoriesListAdapter adapter;
    private CategoriesAsync categoriesAsync;
    private ProgressBar progressBar;

    private ArrayList<Categories> list=new ArrayList<Categories>();

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubcategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubcategoryFragment newInstance(Categories param1, String param2) {
        SubcategoryFragment fragment = new SubcategoryFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SubcategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categories = getArguments().getParcelable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subcategory, container, false);
        grid=(GridView) view.findViewById(R.id.products_grid);
        progressBar=(ProgressBar)view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        grid.setVisibility(View.GONE);

        ArrayList<ServiceRequest> list= ServicesConstants.getSubCategoryRequestList(this.categories.getId());
        categoriesAsync = new CategoriesAsync(this, list, Params.SERVICE_PROCESS_1, Params.TYPE_SUB_CATEGORY);
        categoriesAsync.execute(getActivity());

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        categoriesAsync.cancel(true);
        mListener = null;
    }

    @Override
    public void onServiceSuccess(Object b, int processType) {
        if (getActivity() == null) {
            Log.e(this.getClass().getName(), "Activity is null, avoid callback");
            return;
        }
        ArrayList<Categories> list= (ArrayList<Categories>) b;
        if(list.size() > 0) {
            this.list = list;
            ArrayList<GridItem> gridArray = new ArrayList<GridItem>();
            for (Categories trans : this.list) {
                gridArray.add(new ItemButton(trans.getLogo(), trans.getTitle(), trans.getId(), trans.getPosition()));
            }
            customGridAdapter = new CustomGridViewAdapterButton(getActivity(), R.layout.row_grid_button
                    , gridArray, this, null);
            grid.setAdapter(customGridAdapter);
            progressBar.setVisibility(View.GONE);
            grid.setVisibility(View.VISIBLE);
        }else{
            mListener.doAction(this.categories,Params.FRAGMANT_CATEGORY_EMPTY);
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {
        if (getActivity() == null) {
            Log.e(this.getClass().getName(), "Activity is null, avoid callback");
            return;
        }
        progressBar.setVisibility(View.GONE);
        grid.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), info.getMessage(), Toast.LENGTH_LONG).show();

        mListener.doAction(this.categories, Params.FRAGMANT_CATEGORY_EMPTY);
    }

    @Override
    public void onClick(View view) {
        Categories data=null;
        int x=view.getId();
        for(Categories bean:this.list){
            if(bean.getPosition()==view.getId()){
                data=bean;
                break;
            }
        }
        if(data!=null){
            mListener.doAction(data,Params.FRAGMENT_SUB_CATEGORY);
        }
    }
}
