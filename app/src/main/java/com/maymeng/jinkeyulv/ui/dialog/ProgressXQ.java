package com.maymeng.jinkeyulv.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.view.SpinView;


public class ProgressXQ extends Dialog {


    ProgressXQ instance;
    TextView tvMessage;
    SpinView ivProgressSpinner;
    Context context;



    public ProgressXQ getInstance(Context context) {
        instance = new ProgressXQ(context);
        return instance;
    }

    public ProgressXQ(Context context) {
        super(context, R.style.DialogTheme);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        this.setCanceledOnTouchOutside(false);
        this.context = context;

        View view = getLayoutInflater().inflate(R.layout.dialog_progress, null);
        tvMessage = (TextView) view.findViewById(R.id.textview_message);
        ivProgressSpinner = (SpinView) view.findViewById(R.id.imageview_progress_spinner);

        this.setContentView(view);

    /*    Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.image_rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        ivProgressSpinner.startAnimation(operatingAnim);*/


    }


    public void setMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            tvMessage.setVisibility(View.GONE);
        } else {
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(message);
        }
    }

    @Override
    public void dismiss() {
       /* if (ivProgressSpinner != null) {
            ivProgressSpinner.clearAnimation();
        }*/
        super.dismiss();
    }
}
