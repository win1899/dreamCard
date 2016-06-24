package com.dreamcard.app.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.activity.BuyDreamCardActivity;
import com.dreamcard.app.view.activity.MainActivationFormActivity;

/**
 * Created by WIN on 6/24/2016.
 */
public class NoUserDashboardFragment  extends Fragment implements View.OnClickListener{

    public static NoUserDashboardFragment newInstance() {
        NoUserDashboardFragment fragment = new NoUserDashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public NoUserDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard_no_user, container, false);

        TextView welcome = (TextView) view.findViewById(R.id.welcome_no_user);
        welcome.setText("Welcome " + Utils.getUserName(getActivity()));

        Button buy = (Button) view.findViewById(R.id.buy_card_button);
        buy.setOnClickListener(this);
        Button add = (Button) view.findViewById(R.id.add_card_button);
        add.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buy_card_button) {
            Intent intent = new Intent(getActivity(), BuyDreamCardActivity.class);
            startActivity(intent);
        }
        else if (v.getId() == R.id.add_card_button) {
            Intent activationIntent=new Intent(getActivity(), MainActivationFormActivity.class);
            activationIntent.putExtra(Params.FACEBOOK_EXTRA, true);
            startActivity(activationIntent);
            getActivity().overridePendingTransition( R.anim.push_right_in, R.anim.push_right_out );
            getActivity().finish();
        }
    }
}