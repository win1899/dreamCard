package com.dreamcard.app.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.services.GetInvoiceForOfferAsync;
import com.dreamcard.app.view.fragments.LeftNavDrawerFragment;

import org.w3c.dom.Text;

/**
 * Created by WIN on 11/12/2015.
 */
public class FAQViewer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_item_activity);

        Intent data = getIntent();
        if (data == null) {
            return;
        }

        String question = data.getStringExtra(Params.QUESTION_EXTRA);
        if (question == null || question.equalsIgnoreCase("")) {
            return;
        }
        String answer = data.getStringExtra(Params.ANSWER_EXTRA);
        if (answer == null || answer.equalsIgnoreCase("")) {
            return;
        }

        TextView questionText = (TextView) findViewById(R.id.question_faq);
        TextView answerText = (TextView) findViewById(R.id.answer_faq);

        questionText.setText(question);
        answerText.setText(answer);

    }

}
