package com.dreamcard.app.view.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dreamcard.app.R;
import com.dreamcard.app.common.InputValidator;
import com.dreamcard.app.components.TransparentProgressDialog;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.MessageInfo;
import com.dreamcard.app.services.FeedbackAsyncTask;
import com.dreamcard.app.view.interfaces.IServiceListener;
import com.dreamcard.app.view.interfaces.OnFragmentInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedbackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedbackFragment extends Fragment implements View.OnClickListener,IServiceListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EditText txtFeedback;
    private Button btnSend;

    private TransparentProgressDialog progress;
    private Runnable runnable;
    private Handler handler;

    private Activity activity;

    private FeedbackAsyncTask feedbackAsyncTask;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedbackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedbackFragment newInstance(String param1, String param2) {
        FeedbackFragment fragment = new FeedbackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FeedbackFragment() {
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
        View view=inflater.inflate(R.layout.fragment_feedback, container, false);

        txtFeedback=(EditText)view.findViewById(R.id.txt_feedback);
        btnSend=(Button)view.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);

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
        if (feedbackAsyncTask != null && feedbackAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            feedbackAsyncTask.cancel(true);
        }
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_send){
            if (InputValidator.isNotEmpty(txtFeedback, getString(R.string.please_insert_your_feedback))) {
                progress.show();
                handler.postDelayed(runnable, 5000);
                SharedPreferences prefs = this.activity.getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
                String id = prefs.getString(Params.USER_INFO_ID, "");
                feedbackAsyncTask = new FeedbackAsyncTask(this
                        , ServicesConstants.getFeedbackRequestParams(id, txtFeedback.getText().toString())
                        , Params.SERVICE_PROCESS_1);
                feedbackAsyncTask.execute(this.activity);
            }
        }
    }

    @Override
    public void onServiceSuccess(Object b, int processType) {
        if (getActivity() == null) {
            Log.e(this.getClass().getName(), "Activity is null, avoid callback");
            return;
        }
        if(processType==Params.SERVICE_PROCESS_1){
            progress.dismiss();
            MessageInfo info= (MessageInfo) b;
            if(info.isSuccess()) {
                Toast.makeText(this.activity, getResources().getString(R.string.feedback_sent_successfully), Toast.LENGTH_LONG).show();
                mListener.onFragmentInteraction("", Params.FRAGMENT_FEEDBACK);
            }
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {
        if (getActivity() == null) {
            Log.e(this.getClass().getName(), "Activity is null, avoid callback");
            return;
        }
        progress.dismiss();
        Toast.makeText(this.activity, getResources().getString(R.string.feedback_not_sent), Toast.LENGTH_LONG).show();
    }
}
