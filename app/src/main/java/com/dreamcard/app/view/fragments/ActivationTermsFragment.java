package com.dreamcard.app.view.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.dreamcard.app.R;
import com.dreamcard.app.view.interfaces.OnFragmentInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.dreamcard.app.view.interfaces.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActivationTermsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ActivationTermsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Button btnAgree;
    private boolean isSelected=false;

    public static ActivationTermsFragment newInstance(String param1, String param2) {
        ActivationTermsFragment fragment = new ActivationTermsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public ActivationTermsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_activation_tearms, container, false);
        btnAgree=(Button)view.findViewById(R.id.btn_i_agree);
        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSelected) {
                    btnAgree.setBackgroundColor(getResources().getColor(R.color.sponsored_bg));
                    isSelected=false;
                }
                else {
                    btnAgree.setBackgroundColor(getResources().getColor(R.color.agree_bg));
                    isSelected=true;
                }
            }
        });
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction("","");
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
    public boolean IsValid(){
        return this.isSelected;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
