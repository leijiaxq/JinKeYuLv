package com.maymeng.jinkeyulv.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.api.RetrofitHelper;
import com.maymeng.jinkeyulv.api.RxBus;
import com.maymeng.jinkeyulv.base.BaseApplication;
import com.maymeng.jinkeyulv.base.RxBaseActivity;
import com.maymeng.jinkeyulv.bean.BaseBean;
import com.maymeng.jinkeyulv.bean.LoginBean;
import com.maymeng.jinkeyulv.bean.NewDispatchCaseInfoBean;
import com.maymeng.jinkeyulv.bean.QueryBean;
import com.maymeng.jinkeyulv.bean.RxBusBean;
import com.maymeng.jinkeyulv.bean.WriteInfoBean;
import com.maymeng.jinkeyulv.ui.pop.CausePop;
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
 * Date        2017/4/20 10:20
 * Describe    查询结果页
 */
public class QueryResultActivity extends RxBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.name_tv)
    TextView mNameTv;
    @BindView(R.id.phone_tv)
    TextView mPhoneTv;
    @BindView(R.id.supplement_tv)
    TextView mSupplementTv;
    @BindView(R.id.number_tv1)
    TextView mNumberTv1;
    @BindView(R.id.number_tv2)
    TextView mNumberTv2;
    @BindView(R.id.status_tv)
    TextView mStatusTv;
    @BindView(R.id.progress_iv)
    ImageView mProgressIv;
    @BindView(R.id.line11_v)
    View mLine11V;
    @BindView(R.id.line12_v)
    View mLine12V;
    @BindView(R.id.line21_v)
    View mLine21V;
    @BindView(R.id.round1_v)
    View mRound1V;
    @BindView(R.id.icon1_iv)
    ImageView mIcon1Iv;
    @BindView(R.id.text11_tv)
    TextView mText11Tv;
    @BindView(R.id.text12_tv)
    TextView mText12Tv;
    @BindView(R.id.line22_v)
    View mLine22V;
    @BindView(R.id.line31_v)
    View mLine31V;
    @BindView(R.id.round2_v)
    View mRound2V;
    @BindView(R.id.icon2_iv)
    ImageView mIcon2Iv;
    @BindView(R.id.text21_tv)
    TextView mText21Tv;
    @BindView(R.id.text22_tv)
    TextView mText22Tv;
    @BindView(R.id.line32_v)
    View mLine32V;
    @BindView(R.id.line41_v)
    View mLine41V;
    @BindView(R.id.round3_v)
    View mRound3V;
    @BindView(R.id.icon3_iv)
    ImageView mIcon3Iv;
    @BindView(R.id.text31_tv)
    TextView mText31Tv;
    @BindView(R.id.text32_tv)
    TextView mText32Tv;
    @BindView(R.id.line42_v)
    View mLine42V;
    @BindView(R.id.line51_v)
    View mLine51V;
    @BindView(R.id.round4_v)
    View mRound4V;
    @BindView(R.id.icon4_iv)
    ImageView mIcon4Iv;
    @BindView(R.id.text41_tv)
    TextView mText41Tv;
    @BindView(R.id.text42_tv)
    TextView mText42Tv;
    @BindView(R.id.line52_v)
    View mLine52V;
    @BindView(R.id.line61_v)
    View mLine61V;
    @BindView(R.id.round5_v)
    View mRound5V;
    @BindView(R.id.icon5_iv)
    ImageView mIcon5Iv;
    @BindView(R.id.text51_tv)
    TextView mText51Tv;
    @BindView(R.id.text52_tv)
    TextView mText52Tv;
    @BindView(R.id.line62_v)
    View mLine62V;
    @BindView(R.id.line71_v)
    View mLine71V;
    @BindView(R.id.round6_v)
    View mRound6V;
    @BindView(R.id.icon6_iv)
    ImageView mIcon6Iv;
    @BindView(R.id.text61_tv)
    TextView mText61Tv;
    @BindView(R.id.text62_tv)
    TextView mText62Tv;
    @BindView(R.id.line72_v)
    View mLine72V;
    @BindView(R.id.line81_v)
    View mLine81V;
    @BindView(R.id.round7_v)
    View mRound7V;
    @BindView(R.id.icon7_iv)
    ImageView mIcon7Iv;
    @BindView(R.id.text71_tv)
    TextView mText71Tv;
    @BindView(R.id.text72_tv)
    TextView mText72Tv;
    @BindView(R.id.line82_v)
    View mLine82V;
    @BindView(R.id.line91_v)
    View mLine91V;
    @BindView(R.id.round8_v)
    View mRound8V;
    @BindView(R.id.icon8_iv)
    ImageView mIcon8Iv;
    @BindView(R.id.text81_tv)
    TextView mText81Tv;
    @BindView(R.id.text82_tv)
    TextView mText82Tv;
    @BindView(R.id.line92_v)
    View mLine92V;
    @BindView(R.id.round9_v)
    View mRound9V;
    @BindView(R.id.cause_tv)
    TextView mCauseTv;  //驳回原因


    private QueryBean.ResponseDataBean mBean;
    private NewDispatchCaseInfoBean.ResponseDataBean mNewDispatchCaseInfoBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_query_result;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mBean = getIntent().getParcelableExtra("QueryBean");
        mTitleTv.setText("查询结果");
        if (mBean != null) {
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

            setImageProgressByStatus();


            if (mBean.UserInfoState == 0) {
            } else {
                mSupplementTv.setBackgroundResource(R.drawable.shape_query_gray);
                mSupplementTv.setEnabled(false);
            }

            if ((mBean.UserInfoState == 0 && !TextUtils.isEmpty(mBean.UserInfoRemake)) || (mBean.SignState == 0 && !TextUtils.isEmpty(mBean.SignRemake))) {
                mCauseTv.setVisibility(View.VISIBLE);
            } else {
                mCauseTv.setVisibility(View.GONE);
            }

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
        super.loadData();

        if (mBean != null && mBean.UserInfoState == 0) {
            getNewDispatchCaseInfoNet(mBean.CaseId);
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

    //补充资料
    @OnClick(R.id.supplement_tv)
    void clickSupplement(View view) {


        //表示是点击补充资料进来的
        BaseApplication instance = BaseApplication.getInstance();
        instance.setPageFlag(2);

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
        }
        instance.setWriteInfoBean(bean);

        Intent intent = new Intent(this, WriteInfoOneActivity.class);
        startActivity(intent);
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
                .getNewDispatchCaseInfoNet(bean.Token, bean.AccountId + "", caseID)
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
                                hideProgressDialog();
                                ToastUtil.showLong(Constants.TOKEN_RELOGIN);
//                                SPUtil.clear(QueryResultActivity.this);
                                SPUtil.put(QueryResultActivity.this, Constants.ACCOUNT_LOGIN, false);
                                Intent intent = new Intent(QueryResultActivity.this, LoginActivity.class);
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

    private void setImageProgressByStatus() {
        if (mBean != null) {
            int i = 1;
            if (mBean.UserInfoState == 2) {
                i++;
            }
            if (mBean.SignState == 2) {
                i++;
            }
            if (mBean.InJuryState == 2) {
                i++;
            }
            if (mBean.CheckState == 2) {
                i++;
            }
            if (mBean.MedicaState == 2) {
                i++;
            }
            if (mBean.ClaimState == 2) {
                i++;
            }
            if (mBean.PleteState == 2) {
                i++;
            }


            setImageProgress(i);

            setStatus1();
            setStatus2(mBean.UserInfoState);
            setStatus3(mBean.SignState);
            setStatus4(mBean.InJuryState);
            setStatus5(mBean.CheckState);
            setStatus6(mBean.MedicaState);
            setStatus7(mBean.ClaimState);
            setStatus8(mBean.PleteState);
        }

    }

    private void setImageProgress(int status) {
        if (status == 0) { //未开始
            mProgressIv.setImageResource(R.drawable.icon_pro0);
            mNumberTv1.setText("00");
            mStatusTv.setText("未开始");
        } else if (status == 1) {
            mProgressIv.setImageResource(R.drawable.icon_pro1);
            mNumberTv1.setText("12.5");
            mStatusTv.setText("进行中");

        } else if (status == 2) {
            mProgressIv.setImageResource(R.drawable.icon_pro2);
            mNumberTv1.setText("25");
            mStatusTv.setText("进行中");

        } else if (status == 3) {

            mProgressIv.setImageResource(R.drawable.icon_pro3);
            mNumberTv1.setText("37.5");


            mStatusTv.setText("进行中");


        } else if (status == 4) {
            mProgressIv.setImageResource(R.drawable.icon_pro4);
            mNumberTv1.setText("50");
            mStatusTv.setText("进行中");

        } else if (status == 5) {
            mProgressIv.setImageResource(R.drawable.icon_pro5);
            mNumberTv1.setText("62.5");
            mStatusTv.setText("进行中");


        } else if (status == 6) {
            mProgressIv.setImageResource(R.drawable.icon_pro6);
            mNumberTv1.setText("75");
            mStatusTv.setText("进行中");


        } else if (status == 7) {
            mProgressIv.setImageResource(R.drawable.icon_pro7);
            mNumberTv1.setText("87.5");
            mStatusTv.setText("进行中");

        } else if (status == 8) {
            mProgressIv.setImageResource(R.drawable.icon_pro8);
            mNumberTv1.setText("100");
            mStatusTv.setText("已完成");
        }
    }

    private void setStatus1() {

        mIcon1Iv.setSelected(true);
        mText12Tv.setText("已完成");
        mLine11V.setSelected(true);
        mLine12V.setSelected(true);
        mRound1V.setSelected(true);
    }

    private void setStatus2(int flag) {

        if (flag == 0) {
            mIcon2Iv.setSelected(false);
            mText22Tv.setText("未进行");
            mLine21V.setSelected(false);
            mLine22V.setSelected(false);
            mRound2V.setSelected(false);
        } else {
            mIcon2Iv.setSelected(true);
            if (flag == 1) {
                mText22Tv.setText("进行中");
            } else {
                mText22Tv.setText("已完成");
            }
            mLine21V.setSelected(true);
            mLine22V.setSelected(true);
            mRound2V.setSelected(true);
        }
    }

    private void setStatus3(int flag) {
        if (flag == 0) {
            mIcon3Iv.setSelected(false);

            mText32Tv.setText("未进行");

            mLine31V.setSelected(false);
            mLine32V.setSelected(false);
            mRound3V.setSelected(false);

        } else {

            mIcon3Iv.setSelected(true);
            if (flag == 1) {
                mText32Tv.setText("进行中");
            } else {
                mText32Tv.setText("已完成");
            }
            mLine31V.setSelected(true);
            mLine32V.setSelected(true);
            mRound3V.setSelected(true);
        }
    }

    private void setStatus4(int flag) {
        if (flag == 0) {
            mIcon4Iv.setSelected(false);
            mText42Tv.setText("未进行");
            mLine41V.setSelected(false);
            mLine42V.setSelected(false);
            mRound4V.setSelected(false);
        } else {
            mIcon4Iv.setSelected(true);
            if (flag == 1) {
                mText42Tv.setText("进行中");
            } else {
                mText42Tv.setText("已完成");
            }

            mLine41V.setSelected(true);
            mLine42V.setSelected(true);
            mRound4V.setSelected(true);
        }

    }

    private void setStatus5(int flag) {
        if (flag == 0) {
            mIcon5Iv.setSelected(false);

            mText52Tv.setText("未进行");

            mLine51V.setSelected(false);
            mLine52V.setSelected(false);
            mRound5V.setSelected(false);
        } else {

            mIcon5Iv.setSelected(true);
            if (flag == 1) {
                mText52Tv.setText("进行中");
            } else {
                mText52Tv.setText("已完成");
            }

            mLine51V.setSelected(true);
            mLine52V.setSelected(true);
            mRound5V.setSelected(true);
        }
    }

    private void setStatus6(int flag) {
        if (flag == 0) {
            mIcon6Iv.setSelected(false);
            mText62Tv.setText("未进行");

            mLine61V.setSelected(false);
            mLine62V.setSelected(false);
            mRound6V.setSelected(false);
        } else {

            mIcon6Iv.setSelected(true);
            if (flag == 1) {
                mText62Tv.setText("进行中");
            } else {
                mText62Tv.setText("已完成");

            }
            mLine61V.setSelected(true);
            mLine62V.setSelected(true);
            mRound6V.setSelected(true);
        }

    }

    private void setStatus7(int flag) {
        if (flag == 0) {
            mIcon7Iv.setSelected(false);

            mText72Tv.setText("未进行");

            mLine71V.setSelected(false);
            mLine72V.setSelected(false);
            mRound7V.setSelected(false);
        } else {

            mIcon7Iv.setSelected(true);
            if (flag == 1) {
                mText72Tv.setText("进行中");
            } else {
                mText72Tv.setText("已完成");
            }
            mLine71V.setSelected(true);
            mLine72V.setSelected(true);
            mRound7V.setSelected(true);
        }
    }

    private void setStatus8(int flag) {
        if (flag == 0) {
            mIcon8Iv.setSelected(false);
            mText82Tv.setText("未进行");
            mLine81V.setSelected(false);
            mLine82V.setSelected(false);
            mRound8V.setSelected(false);
            mLine91V.setSelected(false);
            mLine92V.setSelected(false);
            mRound9V.setSelected(false);
        } else {

            mIcon8Iv.setSelected(true);
            if (flag == 1) {
                mText82Tv.setText("进行中");
            } else {

                mText82Tv.setText("已完成");
            }
            mLine81V.setSelected(true);
            mLine82V.setSelected(true);
            mRound8V.setSelected(true);
            mLine91V.setSelected(true);
            mLine92V.setSelected(true);
            mRound9V.setSelected(true);
        }

    }


    @OnClick(R.id.cause_tv)
    void clickCause(View view) {
        if (mBean != null) {

            CausePop causePop = new CausePop(this, mBean/*.UserInfoRemake, mBean.SignRemake*/);
            causePop.setOnPopListenter(new CausePop.OnPopListenter() {
                @Override
                public void onConfirm() {
//                    ToastUtil.showShort("跳转到签约列表");
                  /*  Intent intent = new Intent(QueryResultActivity.this, SignActivity.class);
                    startActivity(intent);*/
                }
            });

            causePop.showAtLocation(mTitleTv, Gravity.CENTER, 0, 0);
        }
    }
}
