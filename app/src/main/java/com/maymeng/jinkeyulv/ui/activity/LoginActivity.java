package com.maymeng.jinkeyulv.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.api.RetrofitHelper;
import com.maymeng.jinkeyulv.base.BaseApplication;
import com.maymeng.jinkeyulv.base.RxBaseActivity;
import com.maymeng.jinkeyulv.bean.BaseBean;
import com.maymeng.jinkeyulv.bean.LoginBean;
import com.maymeng.jinkeyulv.bean.VerificationCodeBean;
import com.maymeng.jinkeyulv.utils.ActivityStackUtil;
import com.maymeng.jinkeyulv.utils.CommonUtil;
import com.maymeng.jinkeyulv.utils.RegexUtil;
import com.maymeng.jinkeyulv.utils.SPUtil;
import com.maymeng.jinkeyulv.utils.ToastUtil;
import com.maymeng.jinkeyulv.utils.function.CountDownTimer;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by  leijiaxq
 * Date        2017/4/20 11:23
 * Describe    登录也
 */

public class LoginActivity extends RxBaseActivity {
    @BindView(R.id.phone_et)
    EditText mPhoneEt;
    @BindView(R.id.password_et)
    EditText mPasswordEt;
    @BindView(R.id.password_tv)
    TextView mPasswordTv;
    @BindView(R.id.clause_layout)
    LinearLayout mClauseLayout;
    @BindView(R.id.login_tv)
    TextView mLoginTv;
    private CountDownTimer mTimer;

    //验证码
    private String mVerificationCode;

    //获取验证码时输入的手机号码，用于登录时匹配是不是同一个号码
    private String mPhoneNumber;

    private long mWaitTime;

    @Override
    public int getLayoutId() {
//        jude2MainPage();
        return R.layout.activity_login;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

    }

    @Override
    public void initToolBar() {

    }

    boolean isBlack = true;

    @Override
    public void loadData() {
        mTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isBlack) {
                    isBlack = false;
                    mPasswordTv.setTextColor(getResources().getColor(R.color.gray_ccc));
                }
                long l = millisUntilFinished / 1000;

                mPasswordTv.setText("重新获取(" + l+ "S)");
            }

            @Override
            public void onFinish() {
                isBlack = true;
                mPasswordTv.setTextColor(getResources().getColor(R.color.text_gray999));
                mPasswordTv.setEnabled(true);
                mPasswordTv.setText("获取验证码");
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();

        }
    }

    //发送验证码
    @OnClick(R.id.password_tv)
    void clickPasswordTv(View view) {
        String phone = mPhoneEt.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showShort("手机号码不能为空");
            return;
        }
        if (!RegexUtil.isMobileExact(phone)) {
            ToastUtil.showShort("手机号码格式不正确");
            return;
        }
        view.setEnabled(false);

        if (mTimer != null) {
            mTimer.start();
        }

        mPhoneNumber = phone;
        mVerificationCode = "";

        getVerificationCodeNet(phone);
    }

    //点击了条款
    @OnClick(R.id.clause_layout)
    void clickClause(View view) {
        ToastUtil.showShort(this, "进入服务条款页");
    }

    @OnClick(R.id.login_tv)
    void clickLogin(View view) {

        /*Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();*/
        String phoneNumber = mPhoneEt.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtil.showShort("手机号码不能为空");
            return;
        }
        if (!RegexUtil.isMobileExact(phoneNumber)) {
            ToastUtil.showShort("手机号码格式不正确");
            return;
        }
        if (TextUtils.isEmpty(mVerificationCode)) {
            ToastUtil.showShort("请先获取验证码");
            return;
        }
        if (!phoneNumber.equals(mPhoneNumber)) {
            ToastUtil.showShort("两次输入的手机号码不相同，请重新输入");
            return;
        }

        String passwordNumber = mPasswordEt.getText().toString().trim();
        if (TextUtils.isEmpty(passwordNumber)) {
            ToastUtil.showShort("密码不能为空");
            return;
        }
        if (!passwordNumber.equals(mVerificationCode)) {
            ToastUtil.showShort("验证码错误，请重新输入");
            return;
        }

//        phoneNumber = "13833333333";
//        passwordNumber = "13833333333";

        showProgressDialog("正在登陆中...");
        mWaitTime = System.currentTimeMillis();

        loginUser(phoneNumber, passwordNumber);

        /*final String finalPhoneNumber = phoneNumber;

        final String finalPasswordNumber = passwordNumber;
        BaseApplication.getInstance().mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loginUser(finalPhoneNumber, finalPasswordNumber);

            }
        }, 1000);*/
    }


  /*  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            // 不退出程序，进入后台
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/


    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(),
                        "再按一次退出",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ActivityStackUtil.AppExit();
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getVerificationCodeNet(String phone) {
        RetrofitHelper.getBaseApi()
                .getVerificationCodeNet(phone)
                .compose(this.<VerificationCodeBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VerificationCodeBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showNetError();
                    }

                    @Override
                    public void onNext(VerificationCodeBean bean) {
                        if (Constants.OK.equals(bean.StateCode)) {
                            finishTask(bean);
                        } else {
                            ToastUtil.showShort(TextUtils.isEmpty(bean.ResponseMessage) ? Constants.ERROR : bean.ResponseMessage);
                        }
                    }
                });
    }

    private void loginUser(String phoneNumber, String AccountPwd) {
        String ipAddress = CommonUtil.getIPAddress(true);

        RetrofitHelper.getBaseApi()
                .loginUser(phoneNumber, AccountPwd, ipAddress)
                .compose(this.<LoginBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showNetError();
                    }

                    @Override
                    public void onNext(final LoginBean bean) {
                        if (Constants.OK.equals(bean.StateCode)) {
                            long l = System.currentTimeMillis();
                            if (l - mWaitTime >= Constants.WAIT_TIME) {
                                hideProgressDialog();
                                finishTask(bean);
                            } else {
                                BaseApplication.getInstance().mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideProgressDialog();
                                        finishTask(bean);
                                    }
                                }, Constants.WAIT_TIME);
                            }

                        } else {
                            hideProgressDialog();
                            ToastUtil.showShort(TextUtils.isEmpty(bean.ResponseMessage) ? Constants.ERROR : bean.ResponseMessage);
                        }
                    }
                });
    }


    @Override
    public void finishTask(BaseBean bean) {
        if (bean instanceof VerificationCodeBean) {
            setVerificationCodeBeanData((VerificationCodeBean) bean);
        } else if (bean instanceof LoginBean) {
            setLoginBeanData((LoginBean) bean);

        }
    }


    //设置验证码
    private void setVerificationCodeBeanData(VerificationCodeBean bean) {
        mVerificationCode = bean.ResponseData;
    }

    //用户登录
    private void setLoginBeanData(LoginBean bean) {

        if (bean.ResponseData != null) {
            SPUtil.put(this, Constants.ACCOUNT_ID, bean.ResponseData.AccountId);
            SPUtil.put(this, Constants.ACCOUNT_NAME, bean.ResponseData.AccountName);
            SPUtil.put(this, Constants.ACCOUNT_TOKEN, bean.ResponseData.Token);

            BaseApplication.getInstance().setLoginBean(bean.ResponseData);
        }

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

//    //判断用户是否登录过，若是登录过，进入首页
//    private void jude2MainPage() {
//        int account_id = (int) SPUtil.get(this, Constants.ACCOUNT_ID, 0);
//        if (account_id != 0) {
//            String account_name = (String) SPUtil.get(this, Constants.ACCOUNT_NAME, "");
//            LoginBean.ResponseDataBean bean = new LoginBean.ResponseDataBean();
//            bean.AccountId = account_id;
//            bean.AccountName = account_name;
//            BaseApplication.getInstance().setLoginBean(bean);
//
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }
}
