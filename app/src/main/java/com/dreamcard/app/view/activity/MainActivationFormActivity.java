package com.dreamcard.app.view.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dreamcard.app.MainActivity;
import com.dreamcard.app.R;
import com.dreamcard.app.components.TransparentProgressDialog;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.MessageInfo;
import com.dreamcard.app.entity.PersonalInfo;
import com.dreamcard.app.services.CheckActivationAsync;
import com.dreamcard.app.services.RegisterConsumerAsync;
import com.dreamcard.app.view.fragments.ActivationCardNumFragment;
import com.dreamcard.app.view.fragments.ActivationFinalFragment;
import com.dreamcard.app.view.fragments.ActivationInformationFragment;
import com.dreamcard.app.view.fragments.ActivationSettingFragment;
import com.dreamcard.app.view.fragments.ActivationTermsFragment;
import com.dreamcard.app.view.interfaces.IServiceListener;
import com.dreamcard.app.view.interfaces.OnFragmentInteractionListener;

public class MainActivationFormActivity extends FragmentActivity implements View.OnClickListener
        , OnFragmentInteractionListener, IServiceListener {

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btnNext;
    private Button btnBack;

    private ImageView imgStage1;
    private ImageView imgStage2;
    private ImageView imgStage3;
    private ImageView imgStage4;
    private ImageView imgStage5;

    private TransparentProgressDialog progress;
    private Runnable runnable;
    private Handler handler;

    private int currentPage = 0;
    private PersonalInfo personalInfo = new PersonalInfo();
    private String cardNum;
    private String userId = null;

    private ActivationCardNumFragment cardNumFragment = new ActivationCardNumFragment();
    private ActivationInformationFragment informationFragment = new ActivationInformationFragment();
    private ActivationSettingFragment settingFragment = new ActivationSettingFragment();
    private ActivationTermsFragment termsFragment = new ActivationTermsFragment();
    private ActivationFinalFragment finalFragment = new ActivationFinalFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activation_form);

        buildUI();
        initFragment(0);
    }

    private void buildUI() {
        btn1 = (Button) findViewById(R.id.btn_activation_1);
        btn2 = (Button) findViewById(R.id.btn_activation_2);
        btn3 = (Button) findViewById(R.id.btn_activation_3);
        btn4 = (Button) findViewById(R.id.btn_activation_4);
        btn5 = (Button) findViewById(R.id.btn_activation_5);
        btnBack = (Button) findViewById(R.id.btn_back);
        btnNext = (Button) findViewById(R.id.btn_next);

        imgStage1 = (ImageView) findViewById(R.id.img_stage_1);
        imgStage2 = (ImageView) findViewById(R.id.img_stage_2);
        imgStage3 = (ImageView) findViewById(R.id.img_stage_3);
        imgStage4 = (ImageView) findViewById(R.id.img_stage_4);
        imgStage5 = (ImageView) findViewById(R.id.img_stage_5);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        handler = new Handler();
        progress = new TransparentProgressDialog(this, R.drawable.loading);
        runnable = new Runnable() {
            @Override
            public void run() {
                if (progress.isShowing()) {
                    progress.dismiss();
                }
            }
        };
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void initFragment(int position) {
        Fragment fragment = null;
        String tag = Params.FRAGMENT_ACTIVATION_1;
        switch (position) {
            case 0:
                fragment = cardNumFragment;
                tag = Params.FRAGMENT_ACTIVATION_1;
                btn2.setBackground(null);
                btnBack.setVisibility(View.GONE);
                break;
            case 1:
                fragment = informationFragment;
                tag = Params.FRAGMENT_ACTIVATION_2;
                btn2.setBackgroundColor(getResources().getColor(R.color.list_sliding_item));
                btn1.setBackground(null);
                btn3.setBackground(null);
                imgStage1.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.VISIBLE);
                break;
            case 2:
                settingFragment = ActivationSettingFragment.newInstance(this.userId, "");
                fragment = settingFragment;
                tag = Params.FRAGMENT_ACTIVATION_3;
                btn3.setBackgroundColor(getResources().getColor(R.color.list_sliding_item));
                btn2.setBackground(null);
                btn4.setBackground(null);
                imgStage2.setVisibility(View.VISIBLE);
                break;
            case 3:
                fragment = termsFragment;
                tag = Params.FRAGMENT_ACTIVATION_4;
                btn4.setBackgroundColor(getResources().getColor(R.color.list_sliding_item));
                btn3.setBackground(null);
                btn5.setBackground(null);
                imgStage3.setVisibility(View.VISIBLE);
                break;
            case 4:
                String fullName = this.personalInfo.getFullName();
                if ("null".equalsIgnoreCase(fullName) || "".equalsIgnoreCase(fullName)) {
                    fullName = personalInfo.getFirstName() + " " + personalInfo.getLastName();
                    if (fullName.equalsIgnoreCase("null ") || fullName.equalsIgnoreCase("null null") || fullName.equalsIgnoreCase("null")) {
                        fullName = "";
                    }
                }
                finalFragment = ActivationFinalFragment.newInstance(fullName, personalInfo.getUsername(), personalInfo.getPassword());
                fragment = finalFragment;
                tag = Params.FRAGMENT_ACTIVATION_4;
                btn5.setBackgroundColor(getResources().getColor(R.color.list_sliding_item));
                btn4.setBackground(null);
                imgStage4.setVisibility(View.VISIBLE);
                btnNext.setText(getResources().getString(R.string.start));
                btnBack.setVisibility(View.GONE);
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_pnl, fragment, tag);
            fragmentTransaction.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activation_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_back)
            backPressed();
        else if (view.getId() == R.id.btn_next)
            nextPressed();
    }

    private void nextPressed() {
        boolean isValid = true;
        if (this.currentPage == 4) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (currentPage == 0) {
                isValid = cardNumFragment.isValidInput();
                if (isValid) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(cardNumFragment.getCard().getWindowToken(), 0);

                    progress.show();
                    handler.postDelayed(runnable, 5000);

                    this.cardNum = cardNumFragment.getCardNum();
                    CheckActivationAsync async = new CheckActivationAsync(this
                            , ServicesConstants.getActivationCardRequestList(cardNumFragment.getCardNum())
                            , Params.SERVICE_PROCESS_1);
                    async.execute(this);
                }
            } else if (this.currentPage == 1) {
                isValid = informationFragment.isValidInput();
                if (isValid) {
                    this.personalInfo = informationFragment.getData();
                    if (this.userId != null)
                        this.personalInfo.setId(userId);
                    else
                        this.personalInfo.setId(null);
                    progress.show();
                    handler.postDelayed(runnable, 5000);

                    RegisterConsumerAsync async = new RegisterConsumerAsync(this
                            , ServicesConstants.getActivationInformationList(this.cardNum, this.personalInfo)
                            , Params.SERVICE_PROCESS_2);
                    async.execute(this);
                }
            } else if (this.currentPage == 2) {
                currentPage++;
                initFragment(currentPage);
            } else if (this.currentPage == 3) {
                if (termsFragment.IsValid()) {
                    currentPage++;
                    initFragment(currentPage);
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle(getResources().getString(R.string.tearms_of_condition))
                            .setMessage(getResources().getString(R.string.please_approve_terms))
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        }

    }

    private void backPressed() {
        currentPage--;
        initFragment(currentPage);
    }

    @Override
    public void onFragmentInteraction(String uri, String type) {
    }

    @Override
    public void doAction(Object b, String fragment) {

    }

    @Override
    public void onServiceSuccess(Object b, int processType) {
        if (processType == Params.SERVICE_PROCESS_1) {
            progress.dismiss();
            MessageInfo info = (MessageInfo) b;
            if (info.isValid()) {
                currentPage++;
                initFragment(currentPage);
            } else {
                new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.card_not_valid_title))
                        .setMessage(getResources().getString(R.string.activation_card_not_valid))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        } else if (processType == Params.SERVICE_PROCESS_2) {
            MessageInfo bean = (MessageInfo) b;
            this.userId = bean.getValue();
            currentPage++;
            progress.dismiss();
            initFragment(currentPage);
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {
        progress.dismiss();
        Toast.makeText(this, info.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
