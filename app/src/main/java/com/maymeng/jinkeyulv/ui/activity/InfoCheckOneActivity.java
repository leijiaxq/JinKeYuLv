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
import com.maymeng.jinkeyulv.bean.CheckIDCardBean;
import com.maymeng.jinkeyulv.bean.CheckInfoBean;
import com.maymeng.jinkeyulv.bean.CheckUserBean;
import com.maymeng.jinkeyulv.bean.LoginBean;
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
 * Date        2017/4/19 20:41
 * Describe    信息校验--身份验证
 */
public class InfoCheckOneActivity extends RxBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.identity_tv)
    TextView mIdentityTv;
    @BindView(R.id.confirm_tv)
    TextView mConfirmTv;
    @BindView(R.id.name_tv)
    TextView mNameTv;

    private long mWaitTime;
    private CheckInfoBean.ResponseDataBean mBean;

    private int mCaseId = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_infocheck_one;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mTitleTv.setText("信息校验");

        mBean = getIntent().getParcelableExtra("CheckInfoBean");

        if (mBean != null) {
            mNameTv.setText(TextUtils.isEmpty(mBean.CustomerName) ? "" : mBean.CustomerName);
            mIdentityTv.setText(TextUtils.isEmpty(mBean.IDCard) ? "" : mBean.IDCard);
            mCaseId = mBean.CaseId;
        }
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
//                intent.putExtra("check_shebao_url", "");
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
    }

    @OnClick(R.id.confirm_tv)
    void clickConfirm(View view) {
       /* CheckUserBean checkUserBean = BaseApplication.getInstance().getCheckUserBean();
        if (checkUserBean == null) {
            checkUserBean = new CheckUserBean();
        }
        checkUserBean.IDCard = "431126197812181210";
        checkUserBean.name = "张三";

        BaseApplication.getInstance().setCheckUserBean(checkUserBean);
        Intent intent = new Intent(this, InfoCheckOneResultActivity.class);
        startActivity(intent);*/

        String name = mNameTv.getText().toString().trim();
       /* if (TextUtils.isEmpty(name)) {
            ToastUtil.showShort("姓名不能为空");
            return;
        }*/
        String IDcard = mIdentityTv.getText().toString().trim();

       /* if (TextUtils.isEmpty(IDcard)) {
            ToastUtil.showShort("身份证号码不能为空");
            return;
        }
        if (IDcard.length() == 15) {
            if (!RegexUtil.isIDCard15(IDcard)) {
                ToastUtil.showShort("身份证号码有误，请重新输入");
                return;
            }
        } else if (IDcard.length() == 18) {
            if (!RegexUtil.isIDCard18(IDcard)) {
                ToastUtil.showShort("身份证号码有误，请重新输入");
                return;
            }
        } else {
            ToastUtil.showShort("身份证号码长度不对");
            return;
        }*/

        showProgressDialog("正在验证...");
        checkIDCardNet(name, IDcard);


    }

    //调用接口进行信息核验
    private void checkIDCardNet(String name, String IDcard) {
        RetrofitHelper.getInfoCheckService()
                .checkIDCardNet(Constants.CHECKINFO_IDCARD_APIX_KEY, "idcard", IDcard, name)
                .compose(this.<CheckIDCardBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CheckIDCardBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressDialog();
                        ToastUtil.showShort(Constants.CHECK_ERROR);
//                        showNetError();
                    }

                    @Override
                    public void onNext(CheckIDCardBean bean) {
                        if (bean != null) {
                            finishTask(bean);
                           /* long l = System.currentTimeMillis();
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
                            }*/
                        } else {
                            hideProgressDialog();
                            ToastUtil.showShort(Constants.CHECK_ERROR);
                        }

                    }
                });
    }


    private void submitCheckInfoToServiceNet(int caseID, String address) {
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

        if (caseID == 0) {
            if (mBean != null) {
                caseID = mBean.CaseId;
                CheckUserBean checkUserBean = BaseApplication.getInstance().getCheckUserBean();
                if (checkUserBean !=null) {
                    checkUserBean.CaseId = caseID;
                }
                BaseApplication.getInstance().setCheckUserBean(checkUserBean);
            }

            if (caseID == 0) {
                caseID = mCaseId;
                CheckUserBean checkUserBean = BaseApplication.getInstance().getCheckUserBean();
                if (checkUserBean !=null) {
                    checkUserBean.CaseId = caseID;
                }
                BaseApplication.getInstance().setCheckUserBean(checkUserBean);
            }
        }

        RetrofitHelper.getBaseApi()
                .submitCheckInfoToServiceNet(bean.Token, bean.AccountId + "", 1, caseID, address)
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
                                SPUtil.clear(InfoCheckOneActivity.this);
                                Intent intent = new Intent(InfoCheckOneActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                long l = System.currentTimeMillis();
                                if (l - mWaitTime >= Constants.WAIT_TIME) {
                                    hideProgressDialog();
                                    Intent intent = new Intent(InfoCheckOneActivity.this, InfoCheckOneResultActivity.class);
                                    startActivity(intent);
                                } else {
                                    BaseApplication.getInstance().mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideProgressDialog();
                                            Intent intent = new Intent(InfoCheckOneActivity.this, InfoCheckOneResultActivity.class);
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
        if (bean instanceof CheckIDCardBean) {
            setIDcardBeanDate((CheckIDCardBean) bean);
        }
    }

    private void setIDcardBeanDate(CheckIDCardBean bean) {
        if (bean.code == 0) {
            if (bean.data != null) {
                CheckUserBean checkUserBean = BaseApplication.getInstance().getCheckUserBean();
                if (checkUserBean == null) {
                    checkUserBean = new CheckUserBean();
                }
                checkUserBean.IDCard = bean.data.cardno;
                checkUserBean.name = bean.data.name;
                if (mBean != null) {
                    checkUserBean.CaseId = mBean.CaseId;
                }

                BaseApplication.getInstance().setCheckUserBean(checkUserBean);

                submitCheckInfoToServiceNet(checkUserBean.CaseId, bean.data.address);
            }
//            Intent intent = new Intent(this, InfoCheckOneResultActivity.class);
//            startActivity(intent);
        } else {
            hideProgressDialog();
            ToastUtil.showShort(TextUtils.isEmpty(bean.msg) ? Constants.CHECK_ERROR : bean.msg);
        }
    }
}
