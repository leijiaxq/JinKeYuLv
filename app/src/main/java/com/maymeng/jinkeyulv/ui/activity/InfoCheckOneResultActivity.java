package com.maymeng.jinkeyulv.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.api.RetrofitHelper;
import com.maymeng.jinkeyulv.api.RxBus;
import com.maymeng.jinkeyulv.base.RxBaseActivity;
import com.maymeng.jinkeyulv.bean.BaseBean;
import com.maymeng.jinkeyulv.bean.PagePathBean;
import com.maymeng.jinkeyulv.bean.RxBusBean;
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
        //用于--完结验证后，finish掉页面
        RxBus.getDefault().post(new RxBusBean(Constants.TYPE_TWO, new BaseBean()));
        finish();
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
