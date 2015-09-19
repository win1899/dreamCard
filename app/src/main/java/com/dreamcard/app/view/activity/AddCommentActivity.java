package com.dreamcard.app.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dreamcard.app.R;
import com.dreamcard.app.components.TransparentProgressDialog;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.BusinessComment;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.MessageInfo;
import com.dreamcard.app.services.AddBusinessComment;
import com.dreamcard.app.services.CommentsAsync;
import com.dreamcard.app.view.interfaces.AddCommentListener;
import com.dreamcard.app.view.interfaces.IServiceListener;

public class AddCommentActivity extends Activity implements View.OnClickListener, IServiceListener {

    private AddCommentListener listener;
    private EditText txtComment;

    private String offerId;
    private int type;

    private TransparentProgressDialog progress;
    private Runnable runnable;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        buildUI();

        this.offerId = getIntent().getStringExtra(Params.OFFER_ID);
        this.type = getIntent().getIntExtra(Params.TYPE, 0);
    }

    private void buildUI() {
        Button btnAdd = (Button) findViewById(R.id.btn_add);
        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        txtComment = (EditText) findViewById(R.id.txt_comment);
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_comment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_add) {
            progress.show();
            handler.postDelayed(runnable, 5000);

            SharedPreferences prefs = getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
            String id = prefs.getString(Params.USER_INFO_ID, "");

            if (this.type == Params.TYPE_OFFER) {
                AddBusinessComment asyncTask = new AddBusinessComment(this
                        , ServicesConstants.getAddOfferCommentRequestList(id, this.offerId, txtComment.getText().toString())
                        , Params.SERVICE_PROCESS_1, Params.TYPE_OFFER);
                asyncTask.execute(this);
            } else if (this.type == Params.TYPE_BUSINESS) {
                AddBusinessComment asyncTask = new AddBusinessComment(this
                        , ServicesConstants.getAddBusinessCommentRequestList(id, this.offerId, txtComment.getText().toString())
                        , Params.SERVICE_PROCESS_1, Params.TYPE_BUSINESS);
                asyncTask.execute(this);
            }
        } else if (view.getId() == R.id.btn_cancel) {
            finish();
        }
    }

    @Override
    public void onServiceSuccess(Object b, int processType) {
        if (processType == Params.SERVICE_PROCESS_1) {
            progress.dismiss();
            MessageInfo bean = (MessageInfo) b;
            if (bean.isValid()) {
                Toast.makeText(this, getResources().getString(R.string.comment_added_successfully), Toast.LENGTH_LONG).show();
                Intent returnIntent = new Intent();
                setResult(Params.STATUS_ADD_COMMENT, returnIntent);
                finish();
            } else {
                Toast.makeText(this, getResources().getString(R.string.comment_not_added), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {
        progress.dismiss();
        Toast.makeText(this, getResources().getString(R.string.comment_not_added), Toast.LENGTH_LONG).show();
    }
}
