package com.maymeng.jinkeyulv.ui.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.maymeng.jinkeyulv.R;


/**
 * Created by leijiaxq
 * Data       2017/4/21 9:36
 * Describe   注销pop
 */


public class ExitPop extends PopupWindow implements View.OnClickListener {

    private Activity mContext;

    public ExitPop(Context context) {
        super(context);
        this.mContext = (Activity) context;
        initView();
    }

    private void initView() {
        View view = View.inflate(mContext, R.layout.pop_exit, null);
        this.setContentView(view);
        this.setFocusable(true);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.pop_center_anim);


//        ColorDrawable colorDrawable = new ColorDrawable(0x00000000);
        ColorDrawable colorDrawable = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(colorDrawable);
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        WindowManager.LayoutParams params = mContext.getWindow().getAttributes();
        params.alpha = 0.5f;
        mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mContext.getWindow().setAttributes(params);

        this.setOnDismissListener(new poponDismissListener());


        view.findViewById(R.id.pop_confirm_tv).setOnClickListener(this);
        view.findViewById(R.id.pop_cancel_tv).setOnClickListener(this);
//        view.findViewById(R.id.pop_layout).setOnClickListener(this);

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_confirm_tv:
                this.dismiss();
                if (mOnPopListenter != null) {
                    mOnPopListenter.onConfirm();
                }
                break;
            case R.id.pop_cancel_tv:
                this.dismiss();
                break;
           /* case R.id.pop_layout:
                this.dismiss();
                break;*/
        }

    }


    class poponDismissListener implements OnDismissListener {

        @Override
        public void onDismiss() {
            WindowManager.LayoutParams params = mContext.getWindow().getAttributes();
            params.alpha = 1f;
            mContext.getWindow().setAttributes(params);
        }

    }

    public interface OnPopListenter {
        void onConfirm();
    }

    private OnPopListenter mOnPopListenter;

    public void setOnPopListenter(OnPopListenter l) {
        mOnPopListenter = l;
    }


}
