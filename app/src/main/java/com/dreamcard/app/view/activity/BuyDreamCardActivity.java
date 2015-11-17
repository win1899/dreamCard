package com.dreamcard.app.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.MessageInfo;
import com.dreamcard.app.services.ContentBySlagAsync;
import com.dreamcard.app.services.GetInvoiceForOfferAsync;
import com.dreamcard.app.view.interfaces.IServiceListener;

/**
 * Created by WIN on 11/17/2015.
 */
public class BuyDreamCardActivity extends Activity
    implements IServiceListener {

    private ContentBySlagAsync sellingLocationAsync;
    private TextView sellingLocationTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_dream_card);

        sellingLocationTxt = (TextView) findViewById(R.id.txt_selling_locations);
        sellingLocationAsync = new ContentBySlagAsync(this
                , ServicesConstants.getContentSlagRequestParams(Params.ABOUT_US_SLAG)
                , Params.SERVICE_PROCESS_1);
        sellingLocationAsync.execute(this);
    }


    @Override
    public void onServiceSuccess(Object b, int processType) {
        Log.i(OfferInvoicePdfActivity.class.getName(), "OnServiceSucceeded with: " + b.toString());
        if (b != null) {
            MessageInfo info= (MessageInfo) b;
            String toShow = info.getValue();
            toShow = toShow.replaceAll("(\\\\r\\\\n|\\\\n)", "<br />");
            sellingLocationTxt.setText(Html.fromHtml(toShow));
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {
    }

    @Override
    protected void onPause() {
        sellingLocationAsync.cancel(true);
        super.onPause();
    }
}

