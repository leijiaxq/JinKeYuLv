package com.maymeng.jinkeyulv.ui.pop;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.utils.ToastUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;


/**
 * Created by leijiaxq
 * Data       2017/4/21 9:36
 * Describe   数据读取
 */


public class ReadDataPop extends PopupWindow implements View.OnClickListener {

    private Activity mContext;
    private LinearLayout mLayout1;
    private LinearLayout mLayout2;
    private ImageView mPopProgressIv;
    private TextView mPopProgresstv;

    public ReadDataPop(Context context) {
        super(context);
        this.mContext = (Activity) context;
        initView();
    }

    private void initView() {
        View view = View.inflate(mContext, R.layout.pop_read_data, null);
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

        mLayout1 = (LinearLayout) view.findViewById(R.id.pop_layout1);

        mLayout2 = (LinearLayout) view.findViewById(R.id.pop_layout2);

        mPopProgressIv = (ImageView) view.findViewById(R.id.pop_progress_iv);
        mPopProgresstv = (TextView) view.findViewById(R.id.pop_progress_tv);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_confirm_tv:
//                this.dismiss();
//                if (mOnPopListenter != null) {
//                    mOnPopListenter.onConfirm();
//                }
                showProgressLayout();


                break;
            case R.id.pop_cancel_tv:
                this.dismiss();
                break;
        }

    }

    private void showProgressLayout() {
        new RxPermissions(mContext)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            // 所有权限请求被同意
                            changeContent();
                            if (mOnPopListenter !=null) {
                                mOnPopListenter.onConfirm();
                            }

                        } else {
                            ToastUtil.showShort(mContext, "权限拒绝");
                        }
                    }
                });

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


    public void changeContent() {
        if (mLayout2 != null) {
            mLayout2.setVisibility(View.INVISIBLE);
        }
        if (mLayout1 != null) {
            mLayout1.setVisibility(View.VISIBLE);
        }

        //图标旋转起来
        Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.image_rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        mPopProgressIv.startAnimation(operatingAnim);
    }

}
