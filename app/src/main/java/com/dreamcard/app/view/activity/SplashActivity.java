package com.dreamcard.app.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dreamcard.app.MainActivity;
import com.dreamcard.app.R;
import com.dreamcard.app.common.DatabaseController;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.UserInfo;
import com.dreamcard.app.services.LoginAsync;
import com.dreamcard.app.view.interfaces.IServiceListener;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends Activity implements IServiceListener {

    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(getApplicationContext());
        }

        setContentView(R.layout.activity_splash);

        UserInfo bean = DatabaseController.getInstance(SplashActivity.this).getLoginInfo();

        if (bean != null) {
            if (!checkFacebookLogin()) {
                email = bean.getEmail();
                password = bean.getPassword();
                LoginAsync loginAsync = new LoginAsync(SplashActivity.this
                        , ServicesConstants.getLoginRequestList(bean.getEmail(), bean.getPassword())
                        , Params.SERVICE_PROCESS_1);
                loginAsync.execute(this);
            }
        } else {
            if (!checkFacebookLogin()) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        }
    }

    private boolean checkFacebookLogin() {
        SharedPreferences pref = getSharedPreferences(Params.APP_DATA, MODE_PRIVATE);
        if (pref.getBoolean(Params.USER_FACEBOOK_LOGIN, false)) {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject jsonResponse, GraphResponse response) {
                    if (jsonResponse == null) {
                        UserInfo userInfo = new UserInfo();
                        userInfo.setStatus(0);
                        userInfo.setIsFacebook(true);

                        checkLogin(userInfo);
                        return;
                    }
                    Log.v("LoginActivity", jsonResponse.toString());
                    try {
                        UserInfo userInfo = new UserInfo();
                        userInfo.setFullName(jsonResponse.getString("name"));
                        userInfo.setGender(jsonResponse.getString("gender"));
                        userInfo.setId(jsonResponse.getString("id"));
                        userInfo.setStatus(1);
                        userInfo.setIsFacebook(true);

                        checkLogin(userInfo);
                    }
                    catch (JSONException e) {
                        Log.e("MainActivty", "Failed to parse json from facebook");
                    }
                }
            });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
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

    private void checkLogin(UserInfo bean) {
        if (bean.getStatus() == 1) {
            SharedPreferences pref = getSharedPreferences(Params.APP_DATA, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(Params.USER_INFO_ID, bean.getId());
            editor.putString(Params.USER_INFO_EMAIL, this.email);
            editor.putString(Params.USER_INFO_NAME, bean.getFullName());
            editor.putString(Params.USER_INFO_PASSWORD, this.password);
            editor.putString(Params.USER_INFO_FIRST_NAME, bean.getFirstName());
            editor.putString(Params.USER_INFO_LAST_NAME, bean.getLastName());
            editor.putString(Params.USER_INFO_MOBILE, bean.getMobile());
            editor.putString(Params.USER_INFO_GENDER, bean.getGender());
            editor.putString(Params.USER_INFO_WORK, bean.getWork());
            editor.putString(Params.USER_INFO_BIRTHDAY, bean.getBirthday());
            editor.putString(Params.USER_INFO_CITY, bean.getCity());

            Intent intent = new Intent(this, NavDrawerActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }

    @Override
    public void onServiceSuccess(Object b, int processType) {
        if (b != null) {
            UserInfo bean = (UserInfo) b;
            checkLogin(bean);
        }  else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}
