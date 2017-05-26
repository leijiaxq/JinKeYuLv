package com.maymeng.jinkeyulv.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.api.RetrofitHelper;
import com.maymeng.jinkeyulv.api.RxBus;
import com.maymeng.jinkeyulv.base.BaseApplication;
import com.maymeng.jinkeyulv.base.RxBaseActivity;
import com.maymeng.jinkeyulv.bean.BaseNetBean;
import com.maymeng.jinkeyulv.bean.LoginBean;
import com.maymeng.jinkeyulv.bean.RxBusBean;
import com.maymeng.jinkeyulv.bean.WriteInfoBean;
import com.maymeng.jinkeyulv.utils.DateUtil;
import com.maymeng.jinkeyulv.utils.KeyboardUtil;
import com.maymeng.jinkeyulv.utils.SPUtil;
import com.maymeng.jinkeyulv.utils.ToastUtil;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by  leijiaxq
 * Date        2017/4/19 13:36
 * Describe    录入资料第一个页面
 */
public class WriteInfoOneActivity extends RxBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.round1_tv)
    TextView mRound1Tv;
    @BindView(R.id.status1_tv)
    TextView mStatus1Tv;
    @BindView(R.id.number_et)
    EditText mNumberEt;             //备案号
    @BindView(R.id.time_tv)
    TextView mTimeTv;//出险时间
    @BindView(R.id.address_et)
    EditText mAddressEt;//出险地点
    @BindView(R.id.type_et)
    EditText mTypeEt;//伤亡类型
    @BindView(R.id.process_et)
    EditText mProcessEt;//出险经过
    @BindView(R.id.line1_tv)
    View mLine1Tv;
    private TimePickerView mPvCustomTime;

    private long waitTime;
    private long mWaitTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_writeinfo_one;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mTitleTv.setText("录入资料");
        mRound1Tv.setSelected(true);
        mStatus1Tv.setSelected(true);
        mLine1Tv.setSelected(true);

        WriteInfoBean bean = BaseApplication.getInstance().getWriteInfoBean();
        if (bean != null) {
            if (TextUtils.isEmpty(bean.CaseNumber)) {
                mNumberEt.setText("");
            } else {
                mNumberEt.setText(bean.CaseNumber);
                mNumberEt.setSelection(bean.CaseNumber.length());
            }

            if (!TextUtils.isEmpty(bean.OutDangerTime)) {
                String time = bean.OutDangerTime.replace("T", " ");
                if (time.length() >= 16) {
                    time = time.substring(0, 16);
                    mTimeTv.setText(time);
                }
            }
            mAddressEt.setText(TextUtils.isEmpty(bean.OutDangerAddress) ? "" : bean.OutDangerAddress);
            mTypeEt.setText(TextUtils.isEmpty(bean.CasualtiesType) ? "" : bean.CasualtiesType);
            mProcessEt.setText(TextUtils.isEmpty(bean.OutDangerDescription) ? "" : bean.OutDangerDescription);
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
        //用于--填完第五页数据后finish掉页面
        RxBus.getDefault().toObservable(RxBusBean.class)
                .compose(this.<RxBusBean>bindToLifecycle())
                .subscribe(new Action1<RxBusBean>() {
                    @Override
                    public void call(RxBusBean bean) {
//                        getCartListDataNet();
                        if (bean.id == Constants.RXBUS_ONE) {
                            finish();
                        }
                    }
                });
    }

    @OnClick(R.id.next_tv)
    void clickNext(View view) {
        /*Intent intent = new Intent(this, WriteInfoTwoActivity.class);
        startActivity(intent);*/
        toNextPage();

    }


    @OnClick(R.id.time_tv)
    void clickTime(TextView view) {

        KeyboardUtil.hideSoftInput(this);
        showSelectTimeDialog((TextView) view);
    }

    private void showSelectTimeDialog(TextView view) {

        if (mPvCustomTime != null && mPvCustomTime.isShowing()) {
            return;
        }

        String str = view.getText().toString();

        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        if (!TextUtils.isEmpty(str)) {
            String[] split = str.split("-");
            int i1 = 0, i2 = 0, i3 = 0, i4 = 0, i5 = 0;

            if (split.length >= 3) {
                i1 = Integer.valueOf(split[0]);
                i2 = Integer.valueOf(split[1]);

                String[] split1 = split[2].split(" ");
                if (split1.length >= 2) {
                    i3 = Integer.valueOf(split1[0]);

                    String[] split2 = split1[1].split(":");
                    if (split2.length >= 2) {
                        i4 = Integer.valueOf(split2[0]);
                        i5 = Integer.valueOf(split2[1]);
                    }
                }
            }
            selectedDate.set(i1, i2 - 1, i3, i4, i5);
        }


        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2100, 11, 31);

        mPvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
//                Toast.makeText(MainActivity.this, getTime(date), Toast.LENGTH_SHORT).show();

                String timeByDate = DateUtil.getTimeByDate(date);

                mTimeTv.setText(TextUtils.isEmpty(timeByDate) ? "" : timeByDate);

            }
        })
                .setContentSize(16)
                .setDividerColor(0xFFDDDDDD)//设置分割线的颜色
                .setTextColorCenter(0xFF353535)//设置选中项的颜色
                .setLineSpacingMultiplier(2f)//设置两横线之间的间隔倍数
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
//                .isCyclic(true)//是否循环滚动
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLabel("年", "月", "日", "时", "分", "秒") //设置空字符串以隐藏单位提示   hide label
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        TextView tvSubmit = (TextView) v.findViewById(R.id.confirm_tv);
                        TextView ivCancel = (TextView) v.findViewById(R.id.cancel_tv);

                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPvCustomTime.dismiss();
                                mPvCustomTime.returnData();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY_HOUR_MIN)
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xffcccccc)
                .build();
        mPvCustomTime.show();
    }


    //进入下一步
    private void toNextPage() {
        WriteInfoBean bean = BaseApplication.getInstance().getWriteInfoBean();
        if (bean == null) {
            bean = new WriteInfoBean();
        }

        String number = mNumberEt.getText().toString().trim();
        String time = mTimeTv.getText().toString().trim();
        String addreess = mAddressEt.getText().toString().trim();
        String type = mTypeEt.getText().toString().trim();
        String process = mProcessEt.getText().toString().trim();

        bean.CaseNumber = number;
        bean.OutDangerTime = time;
        bean.OutDangerAddress = addreess;
        bean.CasualtiesType = type;
        bean.OutDangerDescription = process;

        BaseApplication.getInstance().setWriteInfoBean(bean);

        int pageFlag = BaseApplication.getInstance().getPageFlag();
        if (pageFlag == 0 || pageFlag == 2) {
            showProgressDialog("正在提交...");
            mWaitTime = System.currentTimeMillis();
            updateCaseNet(bean.OrderId, number, time, addreess, type, process);
        } else {
            Intent intent = new Intent(this, WriteInfoTwoActivity.class);
            startActivity(intent);
        }

    }

    private void updateCaseNet(int orderID, String number, String time, String addreess, String type, String process) {
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
                .updateCaseNet(bean.Token, orderID, number, time, addreess, type, process)
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
                                SPUtil.clear(WriteInfoOneActivity.this);
                                Intent intent = new Intent(WriteInfoOneActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {

//                            finishTask(bean);
                                final String str = bean.ResponseMessage;

                                long l = System.currentTimeMillis();
                                if (l - mWaitTime >= Constants.WAIT_TIME) {
                                    hideProgressDialog();
                                    ToastUtil.showShort(TextUtils.isEmpty(str) ? "提交成功" : str);
                                    Intent intent = new Intent(WriteInfoOneActivity.this, WriteInfoTwoActivity.class);
                                    startActivity(intent);
                                } else {
                                    BaseApplication.getInstance().mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideProgressDialog();
                                            ToastUtil.showShort(TextUtils.isEmpty(str) ? "提交成功" : str);
                                            Intent intent = new Intent(WriteInfoOneActivity.this, WriteInfoTwoActivity.class);
                                            startActivity(intent);
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


//    @Override
//    public void finishTask(BaseBean bean) {
//        if (bean instanceof AddCaseBean) {
//            setAddCaseBeanData((AddCaseBean) bean);
//        }
//    }
//
//
//    private void setAddCaseBeanData(AddCaseBean bean) {
//        if (bean != null) {
//
//            WriteInfoBean writeInfoBean = BaseApplication.getInstance().getWriteInfoBean();
//            if (writeInfoBean == null) {
//                writeInfoBean = new WriteInfoBean();
//            }
//            if (bean.ResponseData != null) {
//                writeInfoBean.CaseId = bean.ResponseData.CaseID;
//            }
//        }
//        Intent intent = new Intent(this, WriteInfoTwoActivity.class);
//        startActivity(intent);
//    }
}

