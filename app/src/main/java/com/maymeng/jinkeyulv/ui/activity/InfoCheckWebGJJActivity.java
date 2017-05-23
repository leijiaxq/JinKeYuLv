package com.maymeng.jinkeyulv.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.api.RetrofitHelper;
import com.maymeng.jinkeyulv.api.RxBus;
import com.maymeng.jinkeyulv.base.BaseApplication;
import com.maymeng.jinkeyulv.base.RxBaseActivity;
import com.maymeng.jinkeyulv.bean.BaseBean;
import com.maymeng.jinkeyulv.bean.CheckUserBean;
import com.maymeng.jinkeyulv.bean.PagePathBean;
import com.maymeng.jinkeyulv.bean.RxBusBean;
import com.maymeng.jinkeyulv.utils.ConvertUtil;
import com.maymeng.jinkeyulv.utils.ScreenUtil;
import com.maymeng.jinkeyulv.utils.ToastUtil;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by  leijiaxq
 * Date        2017/4/26 17:29
 * Describe    信息校验webView页面
 */

public class InfoCheckWebGJJActivity extends RxBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    //    @BindView(R.id.web_view)
//    WebView mWebView;
//    @BindView(R.id.frame_layout)
    FrameLayout mFrameLayout;

    ProgressBar mProgressBar;


    //用mCheckFlag来判断是社保1，公积金2，手机验证3页面
//    private int mCheckFlag = 0;
    private String mGJJUrl;  //公积金的url
    private String mPhoneUrl;  //运营商的url
    private WebView mWebView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_infocheck_web;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mTitleTv.setText("信息校验");
//        mCheckFlag = getIntent().getIntExtra("check_flag", 0);
        mGJJUrl = getIntent().getStringExtra("check_gjj_url");
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
                Intent intent = new Intent(InfoCheckWebGJJActivity.this, InfoCheckWebPhoneActivity.class);
                intent.putExtra("check_phone_url", mPhoneUrl);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void loadData() {

        showProgressDialog(" 加载中... ");

        mFrameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        mWebView = new WebView(this);

        mFrameLayout.addView(mWebView);

        WebSettings webSetting = mWebView.getSettings();

        webSetting.setJavaScriptEnabled(true);
        webSetting.setBlockNetworkImage(false);

        webSetting.setSupportMultipleWindows(true);

        webSetting.setDefaultTextEncodingName("UTF-8");
        webSetting.setSupportZoom(true);

// 设置出现缩放工具
        webSetting.setBuiltInZoomControls(true);
//扩大比例的缩放
//        webSetting.setUseWideViewPort(true);
//自适应屏幕
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSetting.setLoadWithOverviewMode(true);
//        webSetting.setUserAgentString(Constant.UA);


        // 设置允许JS弹窗
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (Constants.CHECK_RESUL_COMPLETED_URL.equals(url)) {
                    //用于--完结验证后，finish掉页面
                    RxBus.getDefault().post(new RxBusBean(Constants.TYPE_TWO, new BaseBean()));
                    finish();

                } else if (Constants.CHECK_RESULT_NEXT_URL.equals(url)) {
                    Intent intent = new Intent(InfoCheckWebGJJActivity.this, InfoCheckWebPhoneActivity.class);
                    intent.putExtra("check_phone_url", mPhoneUrl);
                    startActivity(intent);
                } else {

                    view.loadUrl(url);
                }
                return true;
            }

        });

        mProgressBar = new ProgressBar(this, null,
                android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setLayoutParams(new ViewGroup.LayoutParams(ScreenUtil
                .getScreenWidth(this), ConvertUtil.px2dp(this, 20)));
        Drawable drawable = this.getResources().getDrawable(
                R.drawable.progress_bar_states);
        mProgressBar.setProgressDrawable(drawable);
        mWebView.addView(mProgressBar);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    hideProgressDialog();
                } else {
                    if (View.INVISIBLE == mProgressBar.getVisibility()) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        if (!TextUtils.isEmpty(mGJJUrl)) {
            mWebView.loadUrl(mGJJUrl);
        } else {
            getGJJPageNet();
        }

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

        BaseApplication.getInstance().mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getPhonePageNet();

            }
        }, 5000);
    }


    //获取公积金的URL
    private void getGJJPageNet() {
        RetrofitHelper.getInfoCheck2Service()
                .getGJJPageNet(Constants.CHECKINFO_GJJ_APIX_KEY, Constants.GJJ_CALLBACK_URL, Constants.GJJ_SUCCESS_URL, Constants.GJJ_FAILED_URL, false)
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
                            setGJJData(bean);
                        }
                    }
                });
    }

    //获取到了社保的url地址，用webview加载
    private void setGJJData(PagePathBean bean) {
        if (mWebView != null) {
            if (!TextUtils.isEmpty(bean.url)) {
                mWebView.loadUrl(bean.url);
            } else {
                ToastUtil.showShort(Constants.CHECK_ERROR);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();// 返回前一个页面
            } else {
//                clearCookie();
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //获取运营商数据查询H5地址
    private void getPhonePageNet() {
        CheckUserBean bean = BaseApplication.getInstance().getCheckUserBean();

        if (bean != null) {

            RetrofitHelper.getInfoCheck2Service()
                    .getPhonePageNet(Constants.CHECKINFO_PHONE_APIX_KEY, Constants.PHONE_CALLBACK_URL, Constants.PHONE_SUCCESS_URL, Constants.PHONE_FAILED_URL, bean.name, bean.IDCard/*, "", true, ""*/)
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
                                mPhoneUrl = bean.url;
                            }
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
//            mWebView.loadUrl("about:blank");
            mWebView.removeAllViews();
//            mWebView.setVisibility(View.GONE);
            mWebView.destroy();
            mWebView = null;
        }
        if (mFrameLayout != null) {
            mFrameLayout.removeAllViews();
            mFrameLayout = null;
        }

        super.onDestroy();

    }
}
