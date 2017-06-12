package com.maymeng.jinkeyulv.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.api.RetrofitHelper;
import com.maymeng.jinkeyulv.api.RxBus;
import com.maymeng.jinkeyulv.base.BaseApplication;
import com.maymeng.jinkeyulv.base.RxBaseActivity;
import com.maymeng.jinkeyulv.bean.BaseBean;
import com.maymeng.jinkeyulv.bean.BaseNetBean;
import com.maymeng.jinkeyulv.bean.LoginBean;
import com.maymeng.jinkeyulv.bean.NewDispatchBean;
import com.maymeng.jinkeyulv.bean.NewDispatchCaseInfoBean;
import com.maymeng.jinkeyulv.bean.RxBusBean;
import com.maymeng.jinkeyulv.bean.WriteInfoBean;
import com.maymeng.jinkeyulv.utils.SPUtil;
import com.maymeng.jinkeyulv.utils.ToastUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by  leijiaxq
 * Date        2017/4/19 11:49
 * Describe    派单详情
 */
public class DispatchDetailActivity extends RxBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.name_tv)
    TextView mNameTv;
    @BindView(R.id.phone_tv)
    TextView mPhoneTv;
    @BindView(R.id.report_tv)
    TextView mReportTv;
    @BindView(R.id.write_info_tv)
    TextView mWriteInfoTv;
    @BindView(R.id.phone_layout)
    LinearLayout mPhoneLayout;
    @BindView(R.id.phone_layout2)
    LinearLayout mPhoneLayout2;
    @BindView(R.id.hospital_tv)
    TextView mHospitalTv;

    private NewDispatchBean.ResponseDataBean mBean;
    private NewDispatchCaseInfoBean.ResponseDataBean mNewDispatchCaseInfoBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_dispatch_detail;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mBean = getIntent().getParcelableExtra("NewDisPatchBean");

        mTitleTv.setText("派单详情");
        if (mBean != null) {
            if (mBean.IsStatus == 2) {
                mPhoneLayout.setVisibility(View.GONE);
                mPhoneLayout2.setVisibility(View.VISIBLE);
                mWriteInfoTv.setSelected(true);
                mWriteInfoTv.setEnabled(false);
            } else {
                mPhoneLayout.setVisibility(View.VISIBLE);
                mPhoneLayout2.setVisibility(View.GONE);
            }


            mNameTv.setText(TextUtils.isEmpty(mBean.CustomerName) ? "" : mBean.CustomerName);

            String phone = "";
            if (!TextUtils.isEmpty(mBean.Phone)) {
                if (mBean.Phone.length() == 11) {

                    StringBuilder sb = new StringBuilder();
                    sb.append(mBean.Phone.substring(0, 3)).append("-");
                    sb.append(mBean.Phone.substring(3, 7)).append("-");
                    sb.append(mBean.Phone.substring(7, 11));

                    phone = sb.toString();

                } else {
                    phone = mBean.Phone;
                }
            }

            mPhoneTv.setText("联系电话：" + phone);
            mReportTv.setText("报案号：" + mBean.ReportNumber);
            mHospitalTv.setText("医院名称：" + (TextUtils.isEmpty(mBean.HospitalName)?"":mBean.HospitalName));
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
        if (mBean != null) {

            getNewDispatchCaseInfoNet(mBean.CaseId);

            if (!mBean.IsRead) {
                setNewDispatchReadNet(mBean.OrderId);
            }
        }

        //用于--填完第五页数据后finish掉页面  ----刷新该页面的数据
        RxBus.getDefault().toObservable(RxBusBean.class)
                .compose(this.<RxBusBean>bindToLifecycle())
                .subscribe(new Action1<RxBusBean>() {
                    @Override
                    public void call(RxBusBean bean) {
                        if (bean.id == Constants.RXBUS_ONE) {
                            finish();
                           /* if (mBean != null) {
                                getNewDispatchCaseInfoNet(mBean.CaseId);
                            }*/
                        }
                    }
                });
    }

    //点击录入资料
    @OnClick(R.id.write_info_tv)
    void clickWriteInfo(View view) {
//表示是从新的派单进入的
        BaseApplication instance = BaseApplication.getInstance();
        instance.setPageFlag(0);

        WriteInfoBean bean = new WriteInfoBean();
        if (mNewDispatchCaseInfoBean != null) {
            bean.ReportNumber = mNewDispatchCaseInfoBean.ReportNumber;
            bean.OutDangerTime = mNewDispatchCaseInfoBean.OutDangerTime;
            bean.OutDangerAddress = mNewDispatchCaseInfoBean.OutDangerAddress;
            bean.CasualtiesType = mNewDispatchCaseInfoBean.CasualtiesType;
            bean.OutDangerDescription = mNewDispatchCaseInfoBean.OutDangerDescription;

            bean.Sex = mNewDispatchCaseInfoBean.Sex;
            bean.Age = mNewDispatchCaseInfoBean.Age;
            bean.HouseholdRegisterAddress = mNewDispatchCaseInfoBean.HouseholdRegisterAddress;
            bean.HouseholdRegisterType = mNewDispatchCaseInfoBean.HouseholdRegisterType;
            bean.IDCard = mNewDispatchCaseInfoBean.IDCard;
            bean.LiveInfo = mNewDispatchCaseInfoBean.LiveInfo;
            bean.LiveStartTime = mNewDispatchCaseInfoBean.LiveStartTime;
            bean.LiveEndTime = mNewDispatchCaseInfoBean.LiveEndTime;

            bean.JobStartTime = mNewDispatchCaseInfoBean.JobStartTime;
            bean.JobEndTime = mNewDispatchCaseInfoBean.JobEndTime;
            bean.SocialSecurity = mNewDispatchCaseInfoBean.SocialSecurity;
            bean.LaborContract = mNewDispatchCaseInfoBean.LaborContract;
            bean.SalaryFrom = mNewDispatchCaseInfoBean.SalaryFrom;

            bean.HospitalName = mNewDispatchCaseInfoBean.HospitalName;
            bean.SectionBed = mNewDispatchCaseInfoBean.SectionBed;
            bean.AuditTime = mNewDispatchCaseInfoBean.AuditTime;

            bean.SurveyImg = mNewDispatchCaseInfoBean.SurveyImg;
            bean.HomesImg = mNewDispatchCaseInfoBean.HomesImg;
            bean.WardImg = mNewDispatchCaseInfoBean.WardImg;
            bean.BedsideCardImg = mNewDispatchCaseInfoBean.BedsideCardImg;
            bean.CaseDataImg = mNewDispatchCaseInfoBean.CaseDataImg;
            bean.XCTImg = mNewDispatchCaseInfoBean.XCTImg;
            bean.CostImg = mNewDispatchCaseInfoBean.CostImg;
            bean.WholeBodyImg = mNewDispatchCaseInfoBean.WholeBodyImg;
            bean.InjuryImg = mNewDispatchCaseInfoBean.InjuryImg;
            bean.IDCardImg = mNewDispatchCaseInfoBean.IDCardImg;
            bean.NoticeLetterImg = mNewDispatchCaseInfoBean.NoticeLetterImg;

        }
        if (mBean != null) {
            bean.CustomerName = mBean.CustomerName;
            bean.Phone = mBean.Phone;
            bean.CaseId = mBean.CaseId;
            bean.OrderId = mBean.OrderId;
            bean.HospitalName = mBean.HospitalName;
        }
        instance.setWriteInfoBean(bean);


        Intent intent = new Intent(this, WriteInfoOneActivity.class);
        startActivity(intent);
    }

    //拨打电话
    @OnClick(R.id.phone_iv)
    void clickPhone(View view) {
        callPhoneByNumber();
    }

    //拨打电话
    @OnClick(R.id.phone_iv2)
    void clickPhone2(View view) {
        callPhoneByNumber();
    }

    private void callPhoneByNumber() {
        if (mBean != null) {
            if (TextUtils.isEmpty(mBean.Phone)) {
                ToastUtil.showShort("手机号码为空");
                return;
            }
            new RxPermissions(this)
                    .request(Manifest.permission.CALL_PHONE)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (aBoolean) {
                                // 所有权限请求被同意

                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                Uri data = Uri.parse("tel:" + mBean.Phone);
                                intent.setData(data);
                                startActivity(intent);

                            } else {
                                ToastUtil.showShort("权限拒绝");
                            }
                        }
                    });
        }
    }

    private void getNewDispatchCaseInfoNet(int caseID) {
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
                .getNewDispatchCaseInfoNet(bean.Token,bean.AccountId+"", caseID)
                .compose(this.<NewDispatchCaseInfoBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NewDispatchCaseInfoBean>() {
                    @Override
                    public void onCompleted() {
                        hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showNetError();
                    }

                    @Override
                    public void onNext(NewDispatchCaseInfoBean bean) {
                        if (Constants.OK.equals(bean.StateCode)) {
                            if (Constants.TOKEN_ERROR.equals(bean.ResponseMessage)) {
                                ToastUtil.showLong(Constants.TOKEN_RELOGIN);
//                                SPUtil.clear(DispatchDetailActivity.this);
                                SPUtil.put(DispatchDetailActivity.this,Constants.ACCOUNT_LOGIN,false);
                                Intent intent = new Intent(DispatchDetailActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                finishTask(bean);
                            }

                        } else {
                            ToastUtil.showShort(TextUtils.isEmpty(bean.ResponseMessage) ? Constants.ERROR : bean.ResponseMessage);
                        }
                    }
                });
    }

    //更改指定派单为已读
    private void setNewDispatchReadNet(int orderID) {
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
                .setNewDispatchReadNet(bean.Token,bean.AccountId+"", orderID)
                .compose(this.<BaseNetBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseNetBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.showShort("设置状态失败");
                    }

                    @Override
                    public void onNext(BaseNetBean baseNetBean) {
//                        ToastUtil.showShort(TextUtils.isEmpty(baseNetBean.ResponseMessage) ? "设置状态成功" : baseNetBean.ResponseMessage);
                    }
                });
    }


    @Override
    public void finishTask(BaseBean bean) {
        if (bean instanceof NewDispatchCaseInfoBean) {
            setNewDispatchCaseInfoBeanData((NewDispatchCaseInfoBean) bean);
        }
    }

    private void setNewDispatchCaseInfoBeanData(NewDispatchCaseInfoBean bean) {
        mNewDispatchCaseInfoBean = bean.ResponseData;

        if (mNewDispatchCaseInfoBean != null) {

            mNameTv.setText(TextUtils.isEmpty(mNewDispatchCaseInfoBean.CustomerName) ? "" : mNewDispatchCaseInfoBean.CustomerName);

            String phone = "";
            if (!TextUtils.isEmpty(mNewDispatchCaseInfoBean.Phone)) {
                if (mNewDispatchCaseInfoBean.Phone.length() == 11) {

                    StringBuilder sb = new StringBuilder();
                    sb.append(mNewDispatchCaseInfoBean.Phone.substring(0, 3)).append("-");
                    sb.append(mNewDispatchCaseInfoBean.Phone.substring(3, 7)).append("-");
                    sb.append(mNewDispatchCaseInfoBean.Phone.substring(7, 11));

                    phone = sb.toString();

                } else {
                    phone = mNewDispatchCaseInfoBean.Phone;
                }
            }

            mPhoneTv.setText("联系电话：" + phone);
        }
    }

}
