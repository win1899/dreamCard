package com.dreamcard.app.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.webkit.WebView;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.services.GetInvoiceForOfferAsync;
import com.dreamcard.app.view.fragments.LeftNavDrawerFragment;
import com.dreamcard.app.view.interfaces.IServiceListener;


/**
 * Created by WIN on 10/24/2015.
 */
public class OfferInvoicePdfActivity extends Activity
        implements IServiceListener, View.OnClickListener {

    private Offers bean;
    private GetInvoiceForOfferAsync invoiceAsync;

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_invoice_pdf);
        buildUI();
        setData();

        invoiceAsync = new GetInvoiceForOfferAsync(this, ServicesConstants.getOfferInvoicePdfRequestList(this.bean.getOfferId()));
        invoiceAsync.execute(this);
    }

    private void buildUI() {
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://www.dream-card.net/");
    }

    public void setData() {
        Intent intent = getIntent();
        Offers bean = intent.getParcelableExtra(Params.DATA);
        this.bean = bean;
    }

    @Override
    public void onServiceSuccess(Object b, int processType) {

    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txt_back || view.getId() == R.id.txt_arrow)
            finish();
        else if (view.getId() == R.id.img_menu_logo) {
            Intent intent = new Intent();
            setResult(Params.STATUS_MOVE_TO_DASHBOARD, intent);
            LeftNavDrawerFragment.setDrawerMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onPause() {
        invoiceAsync.cancel(true);
        super.onPause();
    }
}

