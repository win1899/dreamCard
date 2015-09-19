package com.dreamcard.app.components;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.dreamcard.app.R;
import com.dreamcard.app.view.interfaces.AddCommentListener;

/**
 * Created by Moayed on 8/1/2014.
 */
public class AddCommentDialog extends Dialog implements View.OnClickListener{

    private Context context;
    private AddCommentListener listener;
    private EditText txtComment;

    public AddCommentDialog(Context context,AddCommentListener listener) {
        super(context);
        this.context=context;
        this.listener=listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.add_comment_dialog_layput, null);
        setContentView(view);

        Button btnAdd=(Button)view.findViewById(R.id.btn_add);
        Button btnCancel=(Button)view.findViewById(R.id.btn_cancel);
        txtComment=(EditText)view.findViewById(R.id.txt_comment);
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_add)
            listener.addComment(txtComment.getText().toString());
        else if(view.getId()==R.id.btn_cancel)
            listener.cancel();
    }
}
