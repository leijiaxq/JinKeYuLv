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
import com.maymeng.jinkeyulv.api.RxBus;
import com.maymeng.jinkeyulv.base.BaseApplication;
import com.maymeng.jinkeyulv.base.RxBaseActivity;
import com.maymeng.jinkeyulv.bean.LoginBean;
import com.maymeng.jinkeyulv.bean.RxBusBean;
import com.maymeng.jinkeyulv.bean.WriteInfoBean;
import com.maymeng.jinkeyulv.utils.DateUtil;
import com.maymeng.jinkeyulv.utils.KeyboardUtil;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by  leijiaxq
 * Date        2017/4/19 15:48
 * Describe    录入资料第四个页面
 */
public class WriteInfoFourActivity extends RxBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.round4_tv)
    TextView mRound4Tv;
    @BindView(R.id.line4_tv)
    View mLine4Tv;
    @BindView(R.id.status4_tv)
    TextView mStatus4Tv;
    @BindView(R.id.hospital_et)
    EditText mHospitalEt;  //医院名称
    @BindView(R.id.department_et)
    EditText mDepartmentEt;//科室及床号
    @BindView(R.id.survey_tv)
    TextView mSurveyTv;//查勘人员
    @BindView(R.id.time_tv)
    TextView mTimeTv;//查勘时间

    TimePickerView mPvCustomTime;


    @Override
    public int getLayoutId() {
        return R.layout.activity_writeinfo_four;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mTitleTv.setText("录入资料");
        mRound4Tv.setSelected(true);
        mStatus4Tv.setSelected(true);
        mLine4Tv.setSelected(true);

        LoginBean.ResponseDataBean bean = BaseApplication.getInstance().getLoginBean();
        if (bean != null) {
            mSurveyTv.setText(TextUtils.isEmpty(bean.AccountName) ? "" : bean.AccountName);
        }

        WriteInfoBean writeInfoBean = BaseApplication.getInstance().getWriteInfoBean();
        if (writeInfoBean != null) {
//            mHospitalEt.setText(TextUtils.isEmpty(writeInfoBean.HospitalName) ? "" : writeInfoBean.HospitalName);
            if (TextUtils.isEmpty(writeInfoBean.HospitalName)) {
                mHospitalEt.setText("");
            } else {
                mHospitalEt.setText(writeInfoBean.HospitalName);
                mHospitalEt.setSelection(writeInfoBean.HospitalName.length());
            }


            mDepartmentEt.setText(TextUtils.isEmpty(writeInfoBean.SectionBed) ? "" : writeInfoBean.SectionBed);
            if (!TextUtils.isEmpty(writeInfoBean.AuditTime)) {
                if (writeInfoBean.AuditTime.length() >= 10) {
                    String time = writeInfoBean.AuditTime.substring(0, 10);
                    mTimeTv.setText(time);
                }
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
       /* Intent intent = new Intent(this, WriteInfoFiveActivity.class);
        startActivity(intent);*/
        toNextPage();
    }


    @OnClick(R.id.time_tv)
    void clickTime(View view) {
        KeyboardUtil.hideSoftInput(this);
        showSelectTimeDialog((TextView) view);
    }

    private void showSelectTimeDialog(TextView view) {
        if (mPvCustomTime != null && mPvCustomTime.isShowing()) {
            return;
        }

        Calendar selectedDate = Calendar.getInstance();//系统当前时间

        String str = view.getText().toString();
        if (!TextUtils.isEmpty(str)) {
            int i1 = 0, i2 = 0, i3 = 0;
            String[] split = str.split("-");
            if (split.length >= 3) {
                i1 = Integer.valueOf(split[0]);
                i2 = Integer.valueOf(split[1]);
                i3 = Integer.valueOf(split[2]);
            } else if (split.length == 2) {
                i1 = Integer.valueOf(split[0]);
                i2 = Integer.valueOf(split[1]);
            } else if (split.length == 1) {
                i1 = Integer.valueOf(split[0]);
            }
            selectedDate.set(i1, i2 - 1, i3);
        }

        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2100, 11, 31);

        mPvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
//                Toast.makeText(MainActivity.this, getTime(date), Toast.LENGTH_SHORT).show();

                String timeByDate = DateUtil.getTimeByYearMonthDay(date);

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
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xffcccccc)
                .build();
        mPvCustomTime.show();
    }

    //下一步
    private void toNextPage() {
        String hospital = mHospitalEt.getText().toString().trim();
        String department = mDepartmentEt.getText().toString().trim();
//        String survey = mSurveyEt.getText().toString().trim();
        String time = mTimeTv.getText().toString().trim();

        WriteInfoBean bean = BaseApplication.getInstance().getWriteInfoBean();
        if (bean == null) {
            bean = new WriteInfoBean();
        }
        bean.HospitalName = hospital;
        bean.SectionBed = department;
//        bean.AuditAccountId = survey;
        bean.AuditTime = time;
        BaseApplication.getInstance().setWriteInfoBean(bean);

        Intent intent = new Intent(this, WriteInfoFiveActivity.class);
        startActivity(intent);

    }

}
