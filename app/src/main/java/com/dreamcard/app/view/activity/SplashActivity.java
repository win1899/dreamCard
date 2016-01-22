package com.dreamcard.app.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.facebook.drawee.backends.pipeline.Fresco;

public class SplashActivity extends Activity implements IServiceListener {

    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_splash);

        UserInfo bean = DatabaseController.getInstance(SplashActivity.this).getLoginInfo();
        if (bean != null) {
            email = bean.getEmail();
            password = bean.getPassword();
            LoginAsync loginAsync = new LoginAsync(SplashActivity.this
                    , ServicesConstants.getLoginRequestList(bean.getEmail(), bean.getPassword())
                    , Params.SERVICE_PROCESS_1);
            loginAsync.execute(this);

        } else {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
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

    @Override
    public void onServiceSuccess(Object b, int processType) {
        if (b != null) {
            UserInfo bean = (UserInfo) b;
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
        } else {
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
