package com.maymeng.jinkeyulv.ui.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.api.RxBus;
import com.maymeng.jinkeyulv.base.RxBaseActivity;
import com.maymeng.jinkeyulv.bean.RxBusBean;
import com.maymeng.jinkeyulv.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by  leijiaxq
 * Date        2017/4/20 9:39
 * Describe    查询进度页
 */
public class QueryActivity extends RxBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.search_et)
    EditText mSearchEt;
    @BindView(R.id.query_tv)
    TextView mQueryTv;
    @BindView(R.id.query_all_tv)
    TextView mQueryAllTv;

    @Override
    public int getLayoutId() {
        return R.layout.activity_query;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mTitleTv.setText("查询进度");
        mQueryAllTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        mQueryAllTv.getPaint().setAntiAlias(true);//抗锯齿
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void loadData() {
        //用于--填完第五页数据后finish掉页面  ----刷新该页面的数据
        RxBus.getDefault().toObservable(RxBusBean.class)
                .compose(this.<RxBusBean>bindToLifecycle())
                .subscribe(new Action1<RxBusBean>() {
                    @Override
                    public void call(RxBusBean bean) {
                        if (bean.id == Constants.RXBUS_ONE) {
                            finish();
                        }
                    }
                });
    }

    @OnClick(R.id.query_tv)
    void clickQuery(View view) {
        String opition = mSearchEt.getText().toString().trim();
        if (TextUtils.isEmpty(opition)) {
            ToastUtil.showShort("查询信息不能为空");
            return;
        }
        Intent intent = new Intent(this, QueryAllActivity.class);
        intent.putExtra("opition", opition);
        startActivity(intent);
    }

    @OnClick(R.id.query_all_tv)
    void clickQueryAll(View view) {
        Intent intent = new Intent(this, QueryAllActivity.class);
        startActivity(intent);
    }


}
