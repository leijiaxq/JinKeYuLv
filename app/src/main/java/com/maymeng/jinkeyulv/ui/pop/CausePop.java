package com.maymeng.jinkeyulv.ui.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.bean.QueryBean;


/**
 * Created by leijiaxq
 * Data       2017/4/21 9:36
 * Describe   审核失败原因
 */


public class CausePop extends PopupWindow implements View.OnClickListener {

    private Activity mContext;

    public CausePop(Context context, QueryBean.ResponseDataBean bean/*String info,String sign*/) {
        super(context);
        this.mContext = (Activity) context;
        initView(bean);
    }

    private void initView(QueryBean.ResponseDataBean bean) {
        View view = View.inflate(mContext, R.layout.pop_cause, null);
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
//        view.findViewById(R.id.pop_cancel_tv).setOnClickListener(this);

       TextView infoTv = (TextView) view.findViewById(R.id.pop_info_tv);
       TextView signTv = (TextView) view.findViewById(R.id.pop_sign_tv);

        if (!TextUtils.isEmpty(bean.SignRemake)&&bean.SignState == 0) {
            signTv.setVisibility(View.VISIBLE);
            signTv.setText("签约审核驳回原因：\n\t\t\t"+bean.SignRemake);
        } else {
            signTv.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(bean.UserInfoRemake)&&bean.UserInfoState == 0) {
            infoTv.setVisibility(View.VISIBLE);
            infoTv.setText("资料审核驳回原因：\n\t\t\t"+bean.UserInfoRemake);
        } else {
            infoTv.setVisibility(View.GONE);
        }

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
            /*case R.id.pop_cancel_tv:
                this.dismiss();
                break;*/
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
