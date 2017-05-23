package com.maymeng.jinkeyulv.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.api.RxBus;
import com.maymeng.jinkeyulv.base.BaseApplication;
import com.maymeng.jinkeyulv.base.RxBaseActivity;
import com.maymeng.jinkeyulv.bean.RxBusBean;
import com.maymeng.jinkeyulv.bean.WriteInfoBean;
import com.maymeng.jinkeyulv.utils.DateUtil;
import com.maymeng.jinkeyulv.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by  leijiaxq
 * Date        2017/4/19 15:35
 * Describe    录入资料第三个页面
 */
public class WriteInfoThreeActivity extends RxBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.round3_tv)
    TextView mRound3Tv;
    @BindView(R.id.line3_tv)
    View mLine3Tv;
    @BindView(R.id.status3_tv)
    TextView mStatus3Tv;
    @BindView(R.id.time1_tv)
    TextView mTime1Tv;     //开始工作时间
    @BindView(R.id.time2_tv)
    TextView mTime2Tv;//离职工作时间
    @BindView(R.id.social_security_tv)
    TextView mSocialSecurityTv; //是否有社保
    @BindView(R.id.contract_tv)
    TextView mContractTv;   //是否签订劳动合同
    @BindView(R.id.grant_tv)
    TextView mGrantTv; //工资发放形式

    TimePickerView mPvCustomTime;
    //true 为工作时间，false为至今离职时间
    private boolean mTimeFlag = true;


    List<String> listSocial = new ArrayList<>();
    //    List<String> listContract = new ArrayList<>();
    List<String> listGrant = new ArrayList<>();

    OptionsPickerView mOptionsPickerView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_writeinfo_three;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mTitleTv.setText("录入资料");
        mRound3Tv.setSelected(true);
        mStatus3Tv.setSelected(true);
        mLine3Tv.setSelected(true);

        WriteInfoBean bean = BaseApplication.getInstance().getWriteInfoBean();
        if (bean !=null) {
            if (!TextUtils.isEmpty(bean.JobStartTime)) {
                if (bean.JobStartTime.length() >= 10) {
                    String time = bean.JobStartTime.substring(0, 10);
                    mTime1Tv.setText(time);
                }
            }
            if (!TextUtils.isEmpty(bean.JobEndTime)) {
                if (bean.JobEndTime.length() >= 10) {
                    String time = bean.JobEndTime.substring(0, 10);
                    mTime2Tv.setText(time);
                }
            }

            mSocialSecurityTv.setText(TextUtils.isEmpty(bean.SocialSecurity)?"":bean.SocialSecurity);
            mContractTv.setText(TextUtils.isEmpty(bean.LaborContract)?"":bean.LaborContract);
            mGrantTv.setText(TextUtils.isEmpty(bean.SalaryFrom)?"":bean.SalaryFrom);

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

        listSocial.add("有");
        listSocial.add("无");

        listGrant.add("银行卡");
        listGrant.add("现金");

    }

    @OnClick(R.id.next_tv)
    void clickNext(View view) {
       /* Intent intent = new Intent(this, WriteInfoFourActivity.class);
        startActivity(intent);*/
        toNextPage();

    }



    @OnClick(R.id.time1_tv)
    void clickTime1(View view) {
        mTimeFlag = true;
        showSelectTimeDialog((TextView) view);
    }

    @OnClick(R.id.time2_tv)
    void clickTime2(View view) {
        mTimeFlag = false;
        showSelectTimeDialog((TextView) view);
    }

    private void showSelectTimeDialog(TextView view) {
        if (mPvCustomTime !=null && mPvCustomTime.isShowing()) {
            return;
        }

        Calendar selectedDate = Calendar.getInstance();//系统当前时间

        String str = view.getText().toString();
        if (!TextUtils.isEmpty(str)) {
            int i1 = 0, i2 = 0,i3 = 0;
            String[] split = str.split("-");
            if (split.length >= 3) {
                i1 = Integer.valueOf(split[0]);
                i2 = Integer.valueOf(split[1]);
                i3 = Integer.valueOf(split[2]);
            } else if (split.length == 2) {
                i1 = Integer.valueOf(split[0]);
                i2 = Integer.valueOf(split[1]);
            } else if (split.length ==1) {
                i1 = Integer.valueOf(split[0]);
            }
            selectedDate.set(i1,i2-1,i3);
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
                if (mTimeFlag) {
                    mTime1Tv.setText(TextUtils.isEmpty(timeByDate) ? "" : timeByDate);
                } else {
                    mTime2Tv.setText(TextUtils.isEmpty(timeByDate) ? "" : timeByDate);
                }

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


    @OnClick(R.id.social_security_tv)
    void clickSocial(View view) {
        showSelectOpitionDialog(0, (TextView) view);
    }

    @OnClick(R.id.contract_tv)
    void clickContract(View view) {
        showSelectOpitionDialog(1, (TextView) view);
    }

    @OnClick(R.id.grant_tv)
    void clickGrant(View view) {
        showSelectOpitionDialog(2, (TextView) view);
    }


    private void showSelectOpitionDialog(final int flag,TextView view) {
        if (mOptionsPickerView !=null && mOptionsPickerView.isShowing()) {
            return;
        }
        mOptionsPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                if (flag == 0) {
                    String tx = listSocial.get(options1);
                    mSocialSecurityTv.setText(tx);
                } else if (flag == 1) {
                    String tx = listSocial.get(options1);
                    mContractTv.setText(tx);
                } else if (flag == 2) {
                    String tx = listGrant.get(options1);
                    mGrantTv.setText(tx);
                }
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        TextView tvSubmit = (TextView) v.findViewById(R.id.confirm_tv);
                        TextView ivCancel = (TextView) v.findViewById(R.id.cancel_tv);

                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mOptionsPickerView.dismiss();
                                mOptionsPickerView.returnData();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mOptionsPickerView.dismiss();
                            }
                        });

                    }
                })
                .setContentTextSize(16)//滚轮文字大小
                .setDividerColor(0xFFDDDDDD)//设置分割线的颜色
                .setTextColorCenter(0xFF353535)//设置选中项的颜色
                .setLineSpacingMultiplier(2f)//设置两横线之间的间隔倍数
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .build();

        String str = view.getText().toString();
        if (flag == 0 || flag == 1) {
            mOptionsPickerView.setPicker(listSocial);//添加数据

            if (!TextUtils.isEmpty(str)) {
                int i1 = 0;
                if (listSocial.contains(str)) {
                    i1 = listSocial.indexOf(str);
                }
                mOptionsPickerView.setSelectOptions(i1);
            }

        } else {
            mOptionsPickerView.setPicker(listGrant);//添加数据
            if (!TextUtils.isEmpty(str)) {
                int i1 = 0;
                if (listGrant.contains(str)) {
                    i1 = listGrant.indexOf(str);
                }
                mOptionsPickerView.setSelectOptions(i1);
            }
        }
        mOptionsPickerView.show();
    }

    //下一步
    private void toNextPage() {
        String time1 = mTime1Tv.getText().toString().trim();
        String time2 = mTime2Tv.getText().toString().trim();
        String socialSecurity = mSocialSecurityTv.getText().toString().trim();
        String contract = mContractTv.getText().toString().trim();
        String grant = mGrantTv.getText().toString().trim();

        WriteInfoBean bean = BaseApplication.getInstance().getWriteInfoBean();
        if (bean == null) {
            bean = new WriteInfoBean();
        }
        if (!TextUtils.isEmpty(time1) && !TextUtils.isEmpty(time2)) {
            String replace1 = time1.replace("-", "");
            String replace2 = time2.replace("-", "");

            Integer integer1 = Integer.valueOf(replace1);
            Integer integer2 = Integer.valueOf(replace2);

            if (integer1 > integer2) {
                ToastUtil.showShort("开始时间不能大于结束时间");
                return;
            }

        }



        bean.JobStartTime = time1;
        bean.JobEndTime = time2;
        bean.SocialSecurity = socialSecurity;
        bean.LaborContract = contract;
        bean.SalaryFrom = grant;
        BaseApplication.getInstance().setWriteInfoBean(bean);

        Intent intent = new Intent(this, WriteInfoFourActivity.class);
        startActivity(intent);

    }
}
