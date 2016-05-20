package com.dreamcard.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
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
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.activity.BuyDreamCardActivity;
import com.dreamcard.app.view.activity.MainActivationFormActivity;
import com.dreamcard.app.view.activity.NavDrawerActivity;
import com.dreamcard.app.view.interfaces.IServiceListener;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class MainActivity extends Activity implements View.OnClickListener,IServiceListener{

    private static final String APP_ID = "36bf383e50c742b4b3ca7a48bd270b23";

    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin;
    private Button btnForgotPass;
    private Button whereToBuyDreamCard;
    private Button activationPnl;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private LoginAsync loginAsync;

    private TransparentProgressDialog progress;
    private Runnable runnable;
    private Handler handler;
    private JSONObject _jsonResponse;
    private boolean _facebookClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(getApplicationContext());
        }

        LoginManager.getInstance().logOut();

        setContentView(R.layout.activity_main);

        buildUI();

        UserInfo bean= DatabaseController.getInstance(this).getLoginInfo();
        if(bean != null){
            if (!bean.getEmail().endsWith("@dreamcarduser.com")) {
                txtUsername.setText(bean.getEmail());
                txtPassword.setText(bean.getPassword());
            }
            else {
                DatabaseController.getInstance(this).deleteLogin();
                SharedPreferences pref=getSharedPreferences(Params.APP_DATA,MODE_PRIVATE);
                pref.edit().clear().commit();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        UpdateManager.unregister();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForCrashes();
    }

    private void checkForCrashes() {
        CrashManager.register(this, APP_ID);
    }

    private void buildUI() {
        txtPassword=(EditText)findViewById(R.id.txt_password);
        txtUsername=(EditText)findViewById(R.id.txt_username);
        btnForgotPass=(Button)findViewById(R.id.btn_forgot_pass);
        whereToBuyDreamCard = (Button) findViewById(R.id.where_to_buy);
        btnLogin=(Button)findViewById(R.id.btn_login);
        activationPnl=(Button)findViewById(R.id.activation_pnl);
        btnLogin.setOnClickListener(this);
        btnForgotPass.setOnClickListener(this);
        whereToBuyDreamCard.setOnClickListener(this);
        activationPnl.setOnClickListener(this);
        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile"));
        loginButton.setReadPermissions("email");

        // If using in a fragment
        callbackManager = CallbackManager.Factory.create();
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                _facebookClicked = true;
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonResponse, GraphResponse response) {
                        if (jsonResponse == null) {
                            loginAsync=new LoginAsync(MainActivity.this, ServicesConstants.getLoginRequestList(""
                                    , "")
                                    , Params.SERVICE_PROCESS_9);
                            loginAsync.execute(MainActivity.this);
                            return;
                        }
                        Log.v("LoginActivity", jsonResponse.toString());

                        String userName = Utils.getFacebookUserName(jsonResponse.optString("id"));

                        _jsonResponse = jsonResponse;

                        progress = new TransparentProgressDialog(MainActivity.this, R.drawable.loading);
                        progress.show();
                        loginAsync=new LoginAsync(MainActivity.this, ServicesConstants.getLoginRequestList(userName
                                , jsonResponse.optString("id"))
                                , Params.SERVICE_PROCESS_9);
                        loginAsync.execute(MainActivity.this);
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e("FacebookLogin", "Cancelled");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("FacebookLogin", "error: " + exception.getMessage());

            }
        });

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
                    txtUsername.setGravity(Gravity.LEFT);
                } else {
                    txtUsername.setHint(getResources().getString(R.string.username));
                    if ("".equalsIgnoreCase(txtUsername.getText().toString())) {
                        txtUsername.setGravity(Gravity.RIGHT);
                    }
                }
            }
        });

        txtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    txtPassword.setHint("");
                    txtPassword.setGravity(Gravity.LEFT);
                } else {
                    txtPassword.setHint(getResources().getString(R.string.password));
                    if ("".equalsIgnoreCase(txtPassword.getText().toString())) {
                        txtPassword.setGravity(Gravity.RIGHT);
                    }
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
        }else if(view.getId()==R.id.where_to_buy) {
            Intent intent = new Intent(MainActivity.this, BuyDreamCardActivity.class);
            startActivity(intent);

        }
    }

    private void activationForm() {
        Intent intent=new Intent(this, MainActivationFormActivity.class);
        startActivity(intent);
        overridePendingTransition( R.anim.push_right_in, R.anim.push_right_out );
        finish();
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

    private void loginSuccessful(UserInfo userInfo) {
        if(userInfo.getStatus() == 1) {
            SharedPreferences pref = getSharedPreferences(Params.APP_DATA, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(Params.USER_INFO_ID, userInfo.getId());
            editor.putString(Params.USER_INFO_EMAIL, txtUsername.getText().toString());
            editor.putString(Params.USER_INFO_NAME, userInfo.getFullName());
            editor.putString(Params.USER_INFO_PASSWORD, txtPassword.getText().toString());
            editor.putString(Params.USER_INFO_FIRST_NAME, userInfo.getFirstName());
            editor.putString(Params.USER_INFO_LAST_NAME, userInfo.getLastName());
            editor.putString(Params.USER_INFO_MOBILE, userInfo.getMobile());
            editor.putString(Params.USER_INFO_GENDER, userInfo.getGender());
            editor.putString(Params.USER_INFO_WORK, userInfo.getWork());
            editor.putString(Params.USER_INFO_BIRTHDAY, userInfo.getBirthday());
            editor.putString(Params.USER_INFO_CITY, userInfo.getCity());
            editor.putString(Params.USER_INFO_COUNTRY, userInfo.getCountry());
            editor.putString(Params.USER_INFO_PHONE, userInfo.getPhone());
            editor.putString(Params.USER_INFO_ID_NUM, userInfo.getIdNum());
            editor.putString(Params.USER_INFO_ADDRESS, userInfo.getAddress());
            editor.putString(Params.USER_INFO_EDUCATION, userInfo.getEducation());
            editor.putString(Params.USER_INFO_FULL_NAME, userInfo.getFullName());
            editor.putString(Params.USER_INFO_CARD_NUMBER, userInfo.getCardNumber());
            editor.putBoolean(Params.USER_FACEBOOK_LOGIN, userInfo.getIsFacebook());
            editor.commit();

            if (!userInfo.getIsFacebook() && !_facebookClicked) {
                DatabaseController.getInstance(this).saveLogin(txtUsername.getText().toString()
                        , txtPassword.getText().toString());
            }
            else if (_jsonResponse != null) {
                String userName = Utils.getFacebookUserName(Utils.getFacebookUserName(_jsonResponse.optString("id")));
                DatabaseController.getInstance(this).saveLogin(userName, _jsonResponse.optString("id"));
            }
            _facebookClicked = false;
            Intent intent = new Intent(this, NavDrawerActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            finish();
        }
        else if (_jsonResponse != null) {
            UserInfo fbUserInfo = new UserInfo();
            fbUserInfo.setFullName(_jsonResponse.optString("name"));
            fbUserInfo.setFirstName(_jsonResponse.optString("first_name"));
            fbUserInfo.setLastName(_jsonResponse.optString("last_name"));
            fbUserInfo.setGender(_jsonResponse.optString("gender"));
            fbUserInfo.setId(_jsonResponse.optString("id"));
            fbUserInfo.setEmail(_jsonResponse.optString("email"));
            fbUserInfo.setStatus(1);
            fbUserInfo.setIsFacebook(true);

            _jsonResponse = null;
            _facebookClicked = false;

            loginSuccessful(fbUserInfo);
        }
        else {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.error_in_login))
                    .setMessage(getString(R.string.please_insert_username_password_valid))
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            _facebookClicked = false;

        }
    }

    @Override
    public void onServiceSuccess(Object b, int processType) {
        progress.dismiss();
        if(b!=null){
            ((UserInfo)b).setIsFacebook(false);
            loginSuccessful((UserInfo) b);
            return;
        }
        _facebookClicked = false;
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
        _facebookClicked = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
