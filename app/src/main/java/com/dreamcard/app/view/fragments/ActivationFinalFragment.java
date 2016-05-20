package com.dreamcard.app.view.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.view.interfaces.OnFragmentInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActivationFinalFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ActivationFinalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    // TODO: Rename and change types of parameters
    private String fullName;
    private String userName;
    private String password;

    private boolean isFacebook = false;

    private OnFragmentInteractionListener mListener;

    private TextView txtName;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ActivationFinalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivationFinalFragment newInstance(String fullName, String userName, String password, boolean isFacebook) {
        ActivationFinalFragment fragment = new ActivationFinalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, fullName);
        args.putString(ARG_PARAM2, userName);
        args.putString(ARG_PARAM3, password);
        args.putBoolean(ARG_PARAM4, isFacebook);
        fragment.setArguments(args);
        return fragment;
    }

    public ActivationFinalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fullName = getArguments().getString(ARG_PARAM1);
            userName = getArguments().getString(ARG_PARAM2);
            password = getArguments().getString(ARG_PARAM3);
            isFacebook = getArguments().getBoolean(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_activation_final, container, false);
        txtName=(TextView)view.findViewById(R.id.txt_name);
        txtName.setText(fullName);

        TextView user = (TextView) view.findViewById(R.id.confirm_email_login_txt);
        if (isFacebook) {
            user.setText("تسجيل الدخول عبر فيسبوك");
        }
        else {
            user.setText(userName);
        }
        TextView pass = (TextView) view.findViewById(R.id.confirm_password_login_txt);
        if (isFacebook) {
            pass.setText("تسجيل الدخول عبر فيسبوك");
        }
        else {
            pass.setText(password);
        }
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
