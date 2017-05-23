package com.maymeng.jinkeyulv.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.view.LoadingView;


public class LoadXQAnim extends Dialog {


    LoadXQAnim instance;
    LoadingView mLoadingView;
    Context context;


    public LoadXQAnim getInstance(Context context) {

        instance = new LoadXQAnim(context);
        return instance;
    }

    public LoadXQAnim(Context context) {
        super(context, R.style.DialogTheme);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        this.setCanceledOnTouchOutside(true);
        this.context = context;

        View view = getLayoutInflater().inflate(R.layout.dialog_loading, null);
        mLoadingView = (LoadingView) view.findViewById(R.id.loadView);

        this.setContentView(view);
    }


    public void setMessage(String message) {
        mLoadingView.setLoadingText(TextUtils.isEmpty(message) ? "" : message);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
