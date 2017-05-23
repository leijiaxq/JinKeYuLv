package com.maymeng.jinkeyulv.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.maymeng.jinkeyulv.R;


public class ProgressXQ extends Dialog {


    ProgressXQ instance;
    TextView tvMessage;
    ImageView ivProgressSpinner;
    AnimationDrawable adProgressSpinner;
    Context context;


//    public  ProgressXQ getInstance() {
    public  ProgressXQ getInstance(Context context) {
//        if (instance == null) {
//            synchronized (ProgressXQ.class) {
//                if (instance == null) {
//                    instance = new ProgressXQ(context);
//                }
//            }
//        }
//        instance = new ProgressXQ();
        instance = new ProgressXQ(context);
        return instance;
    }

//    private ProgressXQ() {
    public ProgressXQ(Context context) {
        super(context, R.style.DialogTheme);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        this.setCanceledOnTouchOutside(false);
        this.context = context;

        View view = getLayoutInflater().inflate(R.layout.dialog_progress, null);
        tvMessage = (TextView) view.findViewById(R.id.textview_message);

        this.setContentView(view);
    }


    public void setMessage(String message) {

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
