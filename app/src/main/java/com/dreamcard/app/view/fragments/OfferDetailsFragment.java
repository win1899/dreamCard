package com.dreamcard.app.view.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.view.interfaces.OnFragmentInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OfferDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class OfferDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Offers mParam1;

    private OnFragmentInteractionListener mListener;

    private TextView txtOfferPeriod;
    private TextView txtMobile;
    private TextView txtPhone;
    private TextView txtCity;
    private TextView txtOldPrice;
    private TextView txtNewPrice;
    private TextView txtBusinessName;
    private TextView txtPercentage;
    private ImageView imgOfferLogo;
    private TextView txtDescription;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment OfferDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OfferDetailsFragment newInstance(Offers param1) {
        OfferDetailsFragment fragment = new OfferDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }
    public OfferDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_offer_details, container, false);
        txtBusinessName=(TextView)view.findViewById(R.id.txt_business_name);
        txtCity=(TextView)view.findViewById(R.id.txt_city);
        txtDescription=(TextView)view.findViewById(R.id.txt_description);
        txtMobile=(TextView)view.findViewById(R.id.txt_mobile);
        txtOfferPeriod=(TextView)view.findViewById(R.id.txt_offer_period);
        txtPhone=(TextView)view.findViewById(R.id.txt_phone);
        txtPercentage=(TextView)view.findViewById(R.id.txt_percentage_off);
        imgOfferLogo=(ImageView)view.findViewById(R.id.img_offer_logo);
        txtNewPrice=(TextView)view.findViewById(R.id.txt_new_price);
        txtOldPrice=(TextView)view.findViewById(R.id.txt_old_price);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
