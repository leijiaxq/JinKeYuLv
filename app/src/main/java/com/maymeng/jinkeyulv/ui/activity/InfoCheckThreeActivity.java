package com.maymeng.jinkeyulv.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.api.RetrofitHelper;
import com.maymeng.jinkeyulv.api.RxBus;
import com.maymeng.jinkeyulv.base.BaseApplication;
import com.maymeng.jinkeyulv.base.RxBaseActivity;
import com.maymeng.jinkeyulv.bean.BaseBean;
import com.maymeng.jinkeyulv.bean.BaseNetBean;
import com.maymeng.jinkeyulv.bean.CheckBankCardBean;
import com.maymeng.jinkeyulv.bean.CheckUserBean;
import com.maymeng.jinkeyulv.bean.LoginBean;
import com.maymeng.jinkeyulv.bean.RxBusBean;
import com.maymeng.jinkeyulv.utils.RegexUtil;
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
 * Date        2017/4/19 22:36
 * Describe    信息校验--手机、银行卡验证
 */
public class InfoCheckThreeActivity extends RxBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.phone_number_et)
    EditText mPhoneNumberEt;
    @BindView(R.id.bank_et)
    EditText mBankEt;
    private long mWaitTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_infocheck_three;
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
                Intent intent = new Intent(this, InfoCheckFourActivity.class);
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

    @OnClick(R.id.confirm_tv)
    void clickConfirm(View view) {
       /* Intent intent = new Intent(this,InfoCheckThreeResultActivity.class);
        startActivity(intent);*/

        String phoneNumber = mPhoneNumberEt.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtil.showShort("请输入手机号码");
            return;
        }
        if (!RegexUtil.isMobileExact(phoneNumber)) {
            ToastUtil.showShort("手机号码格式不正确");
            return;
        }

        String bankNumber = mBankEt.getText().toString().trim();
        if (TextUtils.isEmpty(bankNumber)) {
            ToastUtil.showShort("请输入银行卡号");
            return;
        }
        if (bankNumber.length() < 16 || bankNumber.length() > 19) {
            ToastUtil.showShort("银行卡号长度必须在16到19之间");
            return;
        }
        if (!RegexUtil.isBankNo(bankNumber)) {
            ToastUtil.showShort("银行卡号不符合规则");
            return;
        }
        showProgressDialog("正在验证...");
        mWaitTime = System.currentTimeMillis();
        checkBankNumberNet(phoneNumber, bankNumber);

    }

    //    银行卡实名认证，type类型：
//    bankcard_name：银行卡+姓名（需要传递参数：bankcardno + name）
//    bankcard_phone：银行卡+手机号（需要传递参数：bankcardno +phone）
//    bankcard_three：银行卡+姓名+身份证号（需要传递参数：bankcardno + name + idcardno）
//    bankcard_four：银行卡+姓名+身份证号+手机号（需要传递参数：bankcardno + name + idcardno + phone）
    private void checkBankNumberNet(String phone, String bankno) {
        final CheckUserBean bean = BaseApplication.getInstance().getCheckUserBean();

        if (bean != null) {
            String type = "";
            if (TextUtils.isEmpty(bean.name)) {
                type = "bankcard_phone";
                bean.name = "";
                bean.IDCard = "";
            } else {
                type = "bankcard_four";
            }

            RetrofitHelper.getInfoCheckService()
                    .checkBankCardNet(Constants.CHECKINFO_IDCARD_APIX_KEY, type, bankno, bean.name, bean.IDCard, phone)
                    .compose(this.<CheckBankCardBean>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CheckBankCardBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            hideProgressDialog();
                            ToastUtil.showShort(Constants.CHECK_ERROR);
                        }

                        @Override
                        public void onNext(CheckBankCardBean checkBankCardBean) {
                            if (checkBankCardBean != null) {
                                hideProgressDialog();
                                finishTask(checkBankCardBean);
                               /* long l = System.currentTimeMillis();
                                if (l - mWaitTime >= Constants.WAIT_TIME) {
                                    hideProgressDialog();
                                    finishTask(checkBankCardBean);
                                } else {
                                    BaseApplication.getInstance().mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideProgressDialog();
                                            finishTask(checkBankCardBean);
                                        }
                                    }, Constants.WAIT_TIME);
                                }*/

                            } else {
                                ToastUtil.showShort(Constants.ERROR);
                            }
                        }
                    });
        }
    }

    private void submitCheckInfoToServiceNet(String idCard) {
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
                .submitCheckInfoToServiceNet(bean.Token,bean.AccountId+"",2, idCard, "")
                .compose(this.<BaseNetBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseNetBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressDialog();
                        ToastUtil.showShort(Constants.CHECK_ERROR);
                    }

                    @Override
                    public void onNext(BaseNetBean baseNetBean) {
                        if (Constants.OK.equals(baseNetBean.StateCode)) {
                            if (Constants.TOKEN_ERROR.equals(baseNetBean.ResponseMessage)) {
                                hideProgressDialog();
                                ToastUtil.showLong(Constants.TOKEN_RELOGIN);
                                SPUtil.clear(InfoCheckThreeActivity.this);
                                Intent intent = new Intent(InfoCheckThreeActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {

                                long l = System.currentTimeMillis();
                                if (l - mWaitTime >= Constants.WAIT_TIME) {
                                    hideProgressDialog();
                                    Intent intent = new Intent(InfoCheckThreeActivity.this, InfoCheckThreeResultActivity.class);
                                    startActivity(intent);
                                } else {
                                    BaseApplication.getInstance().mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideProgressDialog();
                                            Intent intent = new Intent(InfoCheckThreeActivity.this, InfoCheckThreeResultActivity.class);
                                            startActivity(intent);
                                        }
                                    }, Constants.WAIT_TIME);
                                }
                            }
//                            hideProgressDialog();
//                            Intent intent = new Intent(InfoCheckOneActivity.this, InfoCheckOneResultActivity.class);
//                            startActivity(intent);
                        } else {
                            hideProgressDialog();
                            ToastUtil.showShort(TextUtils.isEmpty(baseNetBean.ResponseMessage) ? Constants.CHECK_ERROR : baseNetBean.ResponseMessage);
                        }
                    }
                });
    }

    @Override
    public void finishTask(BaseBean bean) {
        if (bean instanceof CheckBankCardBean) {
            setBankCardBeanDate((CheckBankCardBean) bean);
        }
    }

    private void setBankCardBeanDate(CheckBankCardBean bean) {
        if (bean.code == 0) {
            CheckUserBean checkUserBean = BaseApplication.getInstance().getCheckUserBean();
            if (checkUserBean == null) {
                checkUserBean = new CheckUserBean();
            }
            if (bean.data != null) {
                checkUserBean.bankNo = bean.data.bankcardno;

//                BaseApplication.getInstance().setCheckUserBean(checkUserBean);
            }

            if (bean.phone_data != null) {
//                CheckUserBean checkUserBean = BaseApplication.getInstance().getCheckUserBean();
//                if (checkUserBean == null) {
//                    checkUserBean = new CheckUserBean();
//                }
                checkUserBean.phone = bean.phone_data.phone;
            }
            BaseApplication.getInstance().setCheckUserBean(checkUserBean);
            submitCheckInfoToServiceNet(checkUserBean.IDCard);

           /* Intent intent = new Intent(this, InfoCheckThreeResultActivity.class);
            startActivity(intent);*/
        } else {
            hideProgressDialog();
            ToastUtil.showShort(TextUtils.isEmpty(bean.msg) ? Constants.CHECK_ERROR : bean.msg);
        }
    }
}
