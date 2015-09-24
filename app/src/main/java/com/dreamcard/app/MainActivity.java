package com.dreamcard.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dreamcard.app.common.DatabaseController;
import com.dreamcard.app.common.InputValidator;
import com.dreamcard.app.components.TransparentProgressDialog;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.UserInfo;
import com.dreamcard.app.services.LoginAsync;
import com.dreamcard.app.view.activity.MainActivationFormActivity;
import com.dreamcard.app.view.activity.NavDrawerActiity;
import com.dreamcard.app.view.activity.SlidingMenuActivity;
import com.dreamcard.app.view.interfaces.IServiceListener;


public class MainActivity extends Activity implements View.OnClickListener,IServiceListener{

    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin;
    private Button btnForgotPass;
    private LinearLayout activationPnl;

    private LoginAsync loginAsync;

    private TransparentProgressDialog progress;
    private Runnable runnable;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildUI();

        UserInfo bean= DatabaseController.getInstance(this).getLoginInfo();
        if(bean != null){
            txtUsername.setText(bean.getEmail());
            txtPassword.setText(bean.getPassword());
        }
    }

    private void buildUI() {
        txtPassword=(EditText)findViewById(R.id.txt_password);
        txtUsername=(EditText)findViewById(R.id.txt_username);
        btnForgotPass=(Button)findViewById(R.id.btn_forgot_pass);
        btnLogin=(Button)findViewById(R.id.btn_login);
        activationPnl=(LinearLayout)findViewById(R.id.activation_pnl);
        btnLogin.setOnClickListener(this);
        btnForgotPass.setOnClickListener(this);
        activationPnl.setOnClickListener(this);

        handler = new Handler();
        progress = new TransparentProgressDialog(this, R.drawable.loading);
        runnable =new Runnable() {
            @Override
            public void run() {
                if (progress.isShowing()) {
                    progress.dismiss();
                }
            }
        };

        txtUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    txtUsername.setHint("");
                    txtUsername.setGravity(Gravity.END);
                } else {
                    txtUsername.setHint(getResources().getString(R.string.username));
                    txtUsername.setGravity(Gravity.START);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        if(view.getId()==R.id.btn_login) {
            handleLogin();
        }else if(view.getId()==R.id.btn_forgot_pass) {
            forgotPass();
        }else if(view.getId()==R.id.activation_pnl) {
            activationForm();
        }
    }

    private void activationForm() {
        Intent intent=new Intent(this, MainActivationFormActivity.class);
        startActivity(intent);
        overridePendingTransition( R.anim.push_right_in, R.anim.push_right_out );
    }

    private void forgotPass() {
        Uri uri = Uri.parse(Params.DREAMCARD_URL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void handleLogin() {
        if (InputValidator.isNotEmpty(txtUsername, getString(R.string.username_not_valid))
                && InputValidator.isNotEmpty(txtPassword, getString(R.string.password_not_valid))){
            progress.show();
            handler.postDelayed(runnable, 5000);

            loginAsync=new LoginAsync(this, ServicesConstants.getLoginRequestList(txtUsername.getText().toString()
                    , txtPassword.getText().toString())
                    , Params.SERVICE_PROCESS_1);
            loginAsync.execute(this);

        }
    }

    @Override
    public void onServiceSuccess(Object b, int processType) {
        progress.dismiss();
        if(b!=null){
            UserInfo bean= (UserInfo) b;
            if(bean.getStatus()==1) {
                SharedPreferences pref = getSharedPreferences(Params.APP_DATA, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(Params.USER_INFO_ID, bean.getId());
                editor.putString(Params.USER_INFO_EMAIL, txtUsername.getText().toString());
                editor.putString(Params.USER_INFO_NAME,bean.getFullName());
                editor.putString(Params.USER_INFO_PASSWORD,txtPassword.getText().toString());
                editor.putString(Params.USER_INFO_FIRST_NAME,bean.getFirstName());
                editor.putString(Params.USER_INFO_LAST_NAME,bean.getLastName());
                editor.putString(Params.USER_INFO_MOBILE,bean.getMobile());
                editor.putString(Params.USER_INFO_GENDER,bean.getGender());
                editor.putString(Params.USER_INFO_WORK,bean.getWork());
                editor.putString(Params.USER_INFO_BIRTHDAY,bean.getBirthday());
                editor.putString(Params.USER_INFO_CITY,bean.getCity());
                editor.putString(Params.USER_INFO_COUNTRY,bean.getCountry());
                editor.putString(Params.USER_INFO_PHONE,bean.getPhone());
                editor.putString(Params.USER_INFO_ID_NUM,bean.getIdNum());
                editor.putString(Params.USER_INFO_ADDRESS,bean.getAddress());
                editor.putString(Params.USER_INFO_EDUCATION,bean.getEducation());
                editor.putString(Params.USER_INFO_FULL_NAME,bean.getFullName());
                editor.putString(Params.USER_INFO_CARD_NUMBER,bean.getCardNumber());
                editor.commit();

                DatabaseController.getInstance(this).saveLogin(txtUsername.getText().toString()
                        ,txtPassword.getText().toString());

                Intent intent = new Intent(this, NavDrawerActiity.class);
                startActivity(intent);
                overridePendingTransition( R.anim.push_right_in, R.anim.push_right_out );
            } else {
                new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.error_in_login))
                        .setMessage(getString(R.string.please_insert_username_password_valid))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
        else {
            SharedPreferences pref = getSharedPreferences(Params.APP_DATA, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(Params.USER_INFO_ID, "1");
            editor.putString(Params.USER_INFO_EMAIL, "walid511@hotmail.com");
            editor.putString(Params.USER_INFO_NAME, "Walid");
            editor.putString(Params.USER_INFO_PASSWORD,"1234");
            editor.putString(Params.USER_INFO_FIRST_NAME,"WIN");
            editor.putString(Params.USER_INFO_LAST_NAME,"EID");
            editor.putString(Params.USER_INFO_MOBILE,"0598576421");
            editor.putString(Params.USER_INFO_GENDER,"Male");
            editor.putString(Params.USER_INFO_WORK,"dsada");
            editor.putString(Params.USER_INFO_BIRTHDAY,"05/11/2014");
            editor.putString(Params.USER_INFO_CITY,"Nablus");
            editor.putString(Params.USER_INFO_COUNTRY,"Palestine");
            editor.putString(Params.USER_INFO_PHONE,"ddd");
            editor.putString(Params.USER_INFO_ID_NUM, "1");
            editor.putString(Params.USER_INFO_ADDRESS, "2231");
            editor.putString(Params.USER_INFO_EDUCATION, "Master");
            editor.putString(Params.USER_INFO_FULL_NAME, "InfoFullName");
            editor.putString(Params.USER_INFO_CARD_NUMBER, "512");
            editor.commit();

            DatabaseController.getInstance(this).saveLogin(txtUsername.getText().toString()
                    ,txtPassword.getText().toString());

            Intent intent = new Intent(this, NavDrawerActiity.class);
            startActivity(intent);
            overridePendingTransition( R.anim.push_right_in, R.anim.push_right_out );
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {
        progress.dismiss();
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.not_connected))
                .setMessage(info.getMessage())
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
