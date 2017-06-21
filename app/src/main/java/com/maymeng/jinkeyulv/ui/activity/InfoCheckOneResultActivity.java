package com.maymeng.jinkeyulv.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.api.RetrofitHelper;
import com.maymeng.jinkeyulv.api.RxBus;
import com.maymeng.jinkeyulv.base.BaseApplication;
import com.maymeng.jinkeyulv.base.RxBaseActivity;
import com.maymeng.jinkeyulv.bean.BaseBean;
import com.maymeng.jinkeyulv.bean.BaseNetBean;
import com.maymeng.jinkeyulv.bean.CheckUserBean;
import com.maymeng.jinkeyulv.bean.LoginBean;
import com.maymeng.jinkeyulv.bean.PagePathBean;
import com.maymeng.jinkeyulv.bean.RxBusBean;
import com.maymeng.jinkeyulv.utils.SPUtil;
import com.maymeng.jinkeyulv.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by  leijiaxq
 * Date        2017/4/19 21:50
 * Describe    信息校验--身份验证--结果页
 */
public class InfoCheckOneResultActivity extends RxBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.result_tv)
    TextView mResultTv;
    @BindView(R.id.end_tv)
    TextView mEndTv;
    @BindView(R.id.next_tv)
    TextView mNextTv;

    private String mUrl;
    private long mWaitTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_infocheck_one_result;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mTitleTv.setText("信息校验");
    }

    @Override
    public void initToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.item_skip, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
//            case R.id.action_skip:
//                Intent intent = new Intent(this, InfoCheckWebShebaoActivity.class);
//                intent.putExtra("check_shebao_url", mUrl);
//                startActivity(intent);
//
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void loadData() {
        //用于--完结验证后，finish掉页面
        RxBus.getDefault().toObservable(RxBusBean.class)
                .compose(this.<RxBusBean>bindToLifecycle())
                .subscribe(new Action1<RxBusBean>() {
                    @Override
                    public void call(RxBusBean bean) {
                        if (bean.id == Constants.RXBUS_TWO) {
                            finish();
                        }
                    }
                });

        //在空闲时间获取社保的url地址
        getSheBaoPageNet();

    }

    @OnClick(R.id.next_tv)
    void clickNext(View view) {
//        Intent intent = new Intent(this, InfoCheckThreeActivity.class);
        Intent intent = new Intent(this, InfoCheckWebShebaoActivity.class);
//        intent.putExtra("check_flag", 1);
        intent.putExtra("check_shebao_url", mUrl);
        startActivity(intent);
    }

    @OnClick(R.id.end_tv)
    void clickEnd(View view) {

        endValidNet();

       /* //用于--完结验证后，finish掉页面
        RxBus.getDefault().post(new RxBusBean(Constants.TYPE_TWO, new BaseBean()));
        finish();*/
    }

    //完结验证---通知后台
    private void endValidNet() {
        CheckUserBean checkUserBean = BaseApplication.getInstance().getCheckUserBean();
        if (checkUserBean ==null) {
            ToastUtil.showShort("信息有误，请重新校验");
            return;
        }
        showProgressDialog("提交数据...");

        mWaitTime = System.currentTimeMillis();

        LoginBean.ResponseDataBean bean = BaseApplication.getInstance().getLoginBean();
        if (bean == null) {
            bean = new LoginBean.ResponseDataBean();
            int account_id = (int) SPUtil.get(this, Constants.ACCOUNT_ID, 0);
            String account_name = (String) SPUtil.get(this, Constants.ACCOUNT_NAME, "");
            String account_token = (String) SPUtil.get(this, Constants.ACCOUNT_TOKEN, "");
            bean.AccountId = account_id;
            bean.AccountName = account_name;
            bean.Token = account_token;
            BaseApplication.getInstance().setLoginBean(bean);
        }
        RetrofitHelper.getBaseApi()
                .endValidNet(bean.Token,bean.AccountId+"", bean.AccountId,checkUserBean.CaseId)
                .compose(this.<BaseNetBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseNetBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showNetError();
                    }

                    @Override
                    public void onNext(BaseNetBean bean) {
//                        hideProgressDialog();
//                        ToastUtil.showShort(TextUtils.isEmpty(bean.ResponseMessage) ? Constants.ERROR : bean.ResponseMessage);
                        if (Constants.OK.equals(bean.StateCode)) {
                            if (Constants.TOKEN_ERROR.equals(bean.ResponseMessage)) {
                                hideProgressDialog();
                                ToastUtil.showLong(Constants.TOKEN_RELOGIN);
//                                SPUtil.clear(InfoCheckOneResultActivity.this);
                                SPUtil.put(InfoCheckOneResultActivity.this,Constants.ACCOUNT_LOGIN,false);
                                Intent intent = new Intent(InfoCheckOneResultActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {

//                            finishTask(bean);
                                final String str = bean.ResponseMessage;

                                long l = System.currentTimeMillis();
                                if (l - mWaitTime >= Constants.WAIT_TIME) {
                                    hideProgressDialog();
                                    ToastUtil.showShort(TextUtils.isEmpty(str) ? "提交成功" : str);
                                    //用于--完结验证后，finish掉页面
                                    RxBus.getDefault().post(new RxBusBean(Constants.TYPE_TWO, new BaseBean()));
                                    finish();
                                } else {
                                    BaseApplication.getInstance().mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideProgressDialog();
                                            ToastUtil.showShort(TextUtils.isEmpty(str) ? "提交成功" : str);
                                            //用于--完结验证后，finish掉页面
                                            RxBus.getDefault().post(new RxBusBean(Constants.TYPE_TWO, new BaseBean()));
                                            finish();
                                        }
                                    }, Constants.WAIT_TIME);
                                }

                            }
                        } else {
                            hideProgressDialog();
                            ToastUtil.showShort(TextUtils.isEmpty(bean.ResponseMessage) ? Constants.ERROR : bean.ResponseMessage);
                        }
                    }
                });
    }


    //获取社保的URL
    private void getSheBaoPageNet() {
        RetrofitHelper.getInfoCheck2Service()
                .getSheBaoPageNet(Constants.CHECKINFO_SHEBAO_APIX_KEY, Constants.SHEBAO_CALLBACK_URL, Constants.SHEBAO_SUCCESS_URL, Constants.SHEBAO_FAILED_URL,false)
                .compose(this.<PagePathBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PagePathBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showShort(Constants.CHECK_ERROR);
                    }

                    @Override
                    public void onNext(PagePathBean bean) {
                        if (bean != null) {
                            mUrl = bean.url;
                        }

                    }
                });
    }

}
