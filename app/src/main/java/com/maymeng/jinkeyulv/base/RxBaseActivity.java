package com.maymeng.jinkeyulv.base;

import android.os.Bundle;
import android.text.TextUtils;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.bean.BaseBean;
import com.maymeng.jinkeyulv.utils.ActivityStackUtil;
import com.maymeng.jinkeyulv.utils.ToastUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Create by  leijiaxq
 * Date       2017/3/2 14:31
 * Describe
 */
public abstract class RxBaseActivity extends RxAppCompatActivity {

    private Unbinder bind;
    private KProgressHUD mKProgressHUD;

//    private ProgressXQ mProgressXQ;
//    private LoadXQAnim mLoadXQAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //设置布局内容
        setContentView(getLayoutId());
        ActivityStackUtil.addActivity(this);

        //初始化控件绑定框架
        bind = ButterKnife.bind(this);
        //初始化控件
        initViews(savedInstanceState);
        //初始化ToolBar
        initToolBar();

        loadData();
    }


    public abstract int getLayoutId();

    public abstract void initViews(Bundle savedInstanceState);

    public abstract void initToolBar();

    public void loadData() {
    }


    public void initRecyclerView() {
    }

    public void initRefreshLayout() {
    }

    public void finishTask(BaseBean bean) {
    }


    public void showProgressDialog(String message) {
       /* if (mProgressXQ == null) {
            mProgressXQ = new ProgressXQ(RxBaseActivity.this);
        }
        mProgressXQ.setMessage(message);
        mProgressXQ.show();*/

       /* mLoadXQAnim = new LoadXQAnim(RxBaseActivity.this);
        mLoadXQAnim.setMessage(message);
        mLoadXQAnim.show();*/
        if (TextUtils.isEmpty(message)) {
            mKProgressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(true);
        } else {
            mKProgressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(message)
                    .setCancellable(true);
        }
        mKProgressHUD.show();
    }

    public void hideProgressDialog() {
       /* if (mProgressXQ != null && mProgressXQ.isShowing()) {
            mProgressXQ.dismiss();
        }*/
       /* if (mLoadXQAnim != null && mLoadXQAnim.isShowing()) {
            mLoadXQAnim.dismiss();
        }*/
        if (mKProgressHUD !=null && mKProgressHUD.isShowing()) {
            mKProgressHUD.dismiss();
        }

    }

    public void showNetError() {
        hideProgressDialog();
        ToastUtil.showShort(this, Constants.FAILURE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        ActivityStackUtil.removeActivity(this);
    }
}
