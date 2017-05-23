package com.maymeng.jinkeyulv.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.api.RxBus;
import com.maymeng.jinkeyulv.base.RxBaseActivity;
import com.maymeng.jinkeyulv.bean.BaseBean;
import com.maymeng.jinkeyulv.bean.RxBusBean;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by  leijiaxq
 * Date        2017/4/19 22:40
 * Describe    信息校验--手机、银行卡验证--结果页
 */
public class InfoCheckThreeResultActivity extends RxBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.result_tv)
    TextView mResultTv;

    @Override
    public int getLayoutId() {
        return R.layout.activity_infocheck_three_result;
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_skip, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_skip:
                Intent intent= new Intent(this, InfoCheckFourActivity.class);
                startActivity(intent);

                return true;
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
    }

    @OnClick(R.id.next_tv)
    void clickNext(View view) {
        Intent intent= new Intent(this, InfoCheckFourActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.end_tv)
    void clickEnd(View view) {
        //用于--完结验证后，finish掉页面
        RxBus.getDefault().post(new RxBusBean(Constants.TYPE_TWO,new BaseBean()));
        finish();
    }
}
