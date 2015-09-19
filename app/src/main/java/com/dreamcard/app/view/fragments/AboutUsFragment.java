package com.dreamcard.app.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamcard.app.R;
import com.dreamcard.app.components.TransparentProgressDialog;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.MessageInfo;
import com.dreamcard.app.services.ContentBySlagAsync;
import com.dreamcard.app.services.FeedbackAsyncTask;
import com.dreamcard.app.view.interfaces.IServiceListener;
import com.dreamcard.app.view.interfaces.OnFragmentInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AboutUsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutUsFragment extends Fragment implements IServiceListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView txtAboutUs;
    private Activity activity;

    private TransparentProgressDialog progress;
    private Runnable runnable;
    private Handler handler;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutUsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutUsFragment newInstance(String param1, String param2) {
        AboutUsFragment fragment = new AboutUsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AboutUsFragment() {
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
        View view=inflater.inflate(R.layout.fragment_about_us, container, false);
        txtAboutUs=(TextView)view.findViewById(R.id.txt_about_us);

        handler = new Handler();
        progress = new TransparentProgressDialog(this.activity, R.drawable.loading);
        runnable =new Runnable() {
            @Override
            public void run() {
                if (progress.isShowing()) {
                    progress.dismiss();
                }
            }
        };

        progress.show();
        handler.postDelayed(runnable, 5000);
        ContentBySlagAsync async = new ContentBySlagAsync(this
                , ServicesConstants.getContentSlagRequestParams(Params.ABOUT_US_SLAG)
                , Params.SERVICE_PROCESS_1);
        async.execute(this.activity);

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
        mListener = null;
    }

    @Override
    public void onServiceSuccess(Object b, int processType) {
        if(processType==Params.SERVICE_PROCESS_1){
            progress.dismiss();
            MessageInfo info= (MessageInfo) b;
            txtAboutUs.setText(info.getValue());
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {
        progress.dismiss();
        Toast.makeText(this.activity, getResources().getString(R.string.feedback_not_sent), Toast.LENGTH_LONG).show();
    }
}
