package com.maymeng.jinkeyulv.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.bean.BaseBean;
import com.maymeng.jinkeyulv.utils.ToastUtil;
import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Create by  leijiaxq
 * Date       2017/3/2 14:31
 * Describe   Fragment基类
 */
public abstract class RxLazyFragment extends RxFragment {

    public FragmentActivity mActivity;

    // 标志位 标志已经初始化完成。
    protected boolean isPrepared;

    //标志位 fragment是否可见
    protected boolean isVisible;

    private Unbinder bind;

    //    private ProgressXQ mProgressXQ;
    private KProgressHUD mKProgressHUD;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        mActivity = getActivity();
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = ButterKnife.bind(this, view);
        initView(savedInstanceState);
        isPrepared = true;
        loadData();
    }

    @LayoutRes
    public abstract int getLayoutResId();

    public abstract void initView(Bundle state);

    public abstract void loadData();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
    }


    /**
     * Fragment数据的懒加载
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    protected void onVisible() {
        if (!isPrepared || !isVisible) {
            return;
        }
        lazyLoad();
    }


    protected void lazyLoad() {
    }


    protected void onInvisible() {

    }


    public void initRecyclerView() {
    }


    public void initRefreshLayout() {
    }

    protected void finishTask(BaseBean bean) {
    }

    public void showProgressDialog(String message) {
       /* if (mProgressXQ == null) {
            mProgressXQ = new ProgressXQ(mActivity);
        }
        mProgressXQ.setMessage(message);
        mProgressXQ.show();*/
        if (TextUtils.isEmpty(message)) {
            mKProgressHUD = KProgressHUD.create(mActivity)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(true);
        } else {
            mKProgressHUD = KProgressHUD.create(mActivity)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(message)
                    .setCancellable(true);
        }
    }

    public void hideProgressDialog() {
       /* if (mProgressXQ != null && mProgressXQ.isShowing()) {
            mProgressXQ.dismiss();
        }*/
        if (mKProgressHUD != null && mKProgressHUD.isShowing()) {
            mKProgressHUD.dismiss();
        }
    }

    public void showNetError() {
        hideProgressDialog();
        ToastUtil.showShort(mActivity, Constants.FAILURE);
    }

}
