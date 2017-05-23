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

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.api.RetrofitHelper;
import com.maymeng.jinkeyulv.api.RxBus;
import com.maymeng.jinkeyulv.base.BaseApplication;
import com.maymeng.jinkeyulv.base.RxBaseActivity;
import com.maymeng.jinkeyulv.bean.AddCaseBean;
import com.maymeng.jinkeyulv.bean.BaseBean;
import com.maymeng.jinkeyulv.bean.LoginBean;
import com.maymeng.jinkeyulv.bean.RxBusBean;
import com.maymeng.jinkeyulv.bean.WriteInfoBean;
import com.maymeng.jinkeyulv.utils.DateUtil;
import com.maymeng.jinkeyulv.utils.KeyboardUtil;
import com.maymeng.jinkeyulv.utils.RegexUtil;
import com.maymeng.jinkeyulv.utils.SPUtil;
import com.maymeng.jinkeyulv.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by  leijiaxq
 * Date        2017/4/19 15:04
 * Describe    录入资料第二个页面
 */
public class WriteInfoTwoActivity extends RxBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.round2_tv)
    TextView mRound2Tv;
    @BindView(R.id.line2_tv)
    View mLine2Tv;
    @BindView(R.id.status2_tv)
    TextView mStatus2Tv;
    @BindView(R.id.name_et)
    EditText mNameEt;//姓名
    @BindView(R.id.gender_tv)
    TextView mGenderTv;//性别
    @BindView(R.id.age_et)
    EditText mAgeEt;//年龄
    @BindView(R.id.phone_et)
    EditText mPhoneEt;//手机号码
    @BindView(R.id.address_et)
    EditText mAddressEt;//户籍地址
    @BindView(R.id.nature_tv)
    TextView mNatureTv;//户籍性质
    @BindView(R.id.number_et)
    EditText mNumberEt;//身份证号
    @BindView(R.id.time1_tv)
    TextView mTime1tv;//入住时间
    @BindView(R.id.time2_tv)
    TextView mTime2tv;          //至今时间
    @BindView(R.id.address_live_et)
    EditText mAddressLiveEt;   //现居住地址
    @BindView(R.id.next_tv)
    TextView mNextTv;

    TimePickerView mPvCustomTime;

    //true 为入住时间，false为至今居住时间
    private boolean mTimeFlag = true;

    List<String> listGender = new ArrayList<>();
    List<String> listNature = new ArrayList<>();

    OptionsPickerView mOptionsPickerView;
    private long mWaitTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_writeinfo_two;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mTitleTv.setText("录入资料");
        mRound2Tv.setSelected(true);
        mStatus2Tv.setSelected(true);
        mLine2Tv.setSelected(true);

        WriteInfoBean bean = BaseApplication.getInstance().getWriteInfoBean();
        if (bean != null) {
//            mNameEt.setText(TextUtils.isEmpty(bean.CustomerName) ? "" : bean.CustomerName);
            if (TextUtils.isEmpty(bean.CustomerName)) {
                mNameEt.setText("");
            } else {
                mNameEt.setText(bean.CustomerName);
                mNameEt.setSelection(bean.CustomerName.length());
            }


            mGenderTv.setText(TextUtils.isEmpty(bean.Sex) ? "" : bean.Sex);
            mAgeEt.setText(bean.Age <= 0 ? "" : bean.Age + "");
            mPhoneEt.setText(TextUtils.isEmpty(bean.Phone) ? "" : bean.Phone);
            mAddressEt.setText(TextUtils.isEmpty(bean.HouseholdRegisterAddress) ? "" : bean.HouseholdRegisterAddress);
            mNatureTv.setText(TextUtils.isEmpty(bean.HouseholdRegisterType) ? "" : bean.HouseholdRegisterType);
            mNumberEt.setText(TextUtils.isEmpty(bean.IDCard) ? "" : bean.IDCard);
            mAddressLiveEt.setText(TextUtils.isEmpty(bean.LiveInfo) ? "" : bean.LiveInfo);

            if (!TextUtils.isEmpty(bean.LiveStartTime)) {
//                String time = bean.OutDangerTime.replace("T", " ");
                if (bean.LiveStartTime.length() >= 7) {
                    String time = bean.LiveStartTime.substring(0, 7);
                    mTime1tv.setText(time);
                }
            }
            if (!TextUtils.isEmpty(bean.LiveEndTime)) {
//                String time = bean.OutDangerTime.replace("T", " ");
                if (bean.LiveEndTime.length() >= 7) {
                    String time = bean.LiveEndTime.substring(0, 7);
                    mTime2tv.setText(time);
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


//        listGender.add("请选择");
        listGender.add("男");
        listGender.add("女");

        listNature.add("城镇");
        listNature.add("农村");

    }

    @OnClick(R.id.next_tv)
    void clickNext(View view) {
       /* Intent intent = new Intent(this, WriteInfoThreeActivity.class);
        startActivity(intent);*/
        toNextPage();
    }


    @OnClick(R.id.time1_tv)
    void clickTime1(View view) {
        mTimeFlag = true;
        KeyboardUtil.hideSoftInput(this);
        showSelectTimeDialog((TextView) view);
    }

    @OnClick(R.id.time2_tv)
    void clickTime2(View view) {
        mTimeFlag = false;
        KeyboardUtil.hideSoftInput(this);
        showSelectTimeDialog((TextView) view);
    }

    @OnClick(R.id.gender_tv)
    void clickGender(View view) {
        KeyboardUtil.hideSoftInput(this);
        showSelectOpitionDialog(true, (TextView) view);
    }

    @OnClick(R.id.nature_tv)
    void clickNature(View view) {
        KeyboardUtil.hideSoftInput(this);
        showSelectOpitionDialog(false, (TextView) view);
    }

    private void showSelectTimeDialog(TextView view) {
        if (mPvCustomTime != null && mPvCustomTime.isShowing()) {
            return;
        }

        Calendar selectedDate = Calendar.getInstance();//系统当前时间

        String str = view.getText().toString();
        if (!TextUtils.isEmpty(str)) {
            int i1 = 0, i2 = 0;
            String[] split = str.split("-");
            if (split.length >= 2) {
                i1 = Integer.valueOf(split[0]);
                i2 = Integer.valueOf(split[1]);
            } else if (split.length == 1) {
                i1 = Integer.valueOf(split[0]);
            }
            selectedDate.set(i1, i2, 0);

        }


        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2100, 11, 31);

        mPvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
//                Toast.makeText(MainActivity.this, getTime(date), Toast.LENGTH_SHORT).show();

                String timeByDate = DateUtil.getTimeByYearMonth(date);
                if (mTimeFlag) {
                    mTime1tv.setText(TextUtils.isEmpty(timeByDate) ? "" : timeByDate);
                } else {
                    mTime2tv.setText(TextUtils.isEmpty(timeByDate) ? "" : timeByDate);
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
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.confirm_tv);
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
                .setType(TimePickerView.Type.YEAR_MONTH)
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xffcccccc)
                .build();
        mPvCustomTime.show();
    }


    private void showSelectOpitionDialog(final boolean flag, TextView view) {

        if (mOptionsPickerView != null && mOptionsPickerView.isShowing()) {
            return;
        }

        mOptionsPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                if (flag) {
                    String tx = listGender.get(options1);
                    mGenderTv.setText(tx);
                } else {
                    String tx = listNature.get(options1);
                    mNatureTv.setText(tx);
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
        if (flag) {
            mOptionsPickerView.setPicker(listGender);//添加数据
            if (!TextUtils.isEmpty(str)) {
                int i1 = 0;
                if (listGender.contains(str)) {
                    i1 = listGender.indexOf(str);
                }
                mOptionsPickerView.setSelectOptions(i1);
            }


        } else {
            mOptionsPickerView.setPicker(listNature);//添加数据
            if (!TextUtils.isEmpty(str)) {
                int i1 = 0;
                if (listNature.contains(str)) {
                    i1 = listNature.indexOf(str);
                }
                mOptionsPickerView.setSelectOptions(i1);
            }
        }


        mOptionsPickerView.show();
    }

    private void toNextPage() {
        String name = mNameEt.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showShort("姓名不能为空");
            return;
        }
        String number = mNumberEt.getText().toString().trim();

        if (TextUtils.isEmpty(number)) {
            ToastUtil.showShort("身份证号码不能为空");
            return;
        }

        if (number.length() == 15) {
            if (!RegexUtil.isIDCard15(number)) {
                ToastUtil.showShort("身份证号码有误，请重新输入");
                return;
            }
        } else if (number.length() == 18) {
            if (!RegexUtil.isIDCard18(number)) {
                ToastUtil.showShort("身份证号码有误，请重新输入");
                return;
            }
        } else {
            ToastUtil.showShort("身份证号码长度不对");
            return;
        }

        WriteInfoBean bean = BaseApplication.getInstance().getWriteInfoBean();
        if (bean == null) {
            bean = new WriteInfoBean();
        }
        bean.CustomerName = name;
        bean.IDCard = number;

        String gender = mGenderTv.getText().toString().trim();
        String age = mAgeEt.getText().toString().trim();
        String phone = mPhoneEt.getText().toString().trim();
        String address = mAddressEt.getText().toString().trim();
        String nature = mNatureTv.getText().toString().trim();
        String time1 = mTime1tv.getText().toString().trim();
        String time2 = mTime2tv.getText().toString().trim();
        String addressLive = mAddressLiveEt.getText().toString().trim();
        if ("请选择".equals(gender)) {
            gender = "";
        }
        if (TextUtils.isEmpty(age)) {
            age = "0";
        }

        bean.Sex = gender;

        bean.Age = Integer.valueOf(age);
        bean.Phone = phone;
        bean.HouseholdRegisterAddress = address;
        bean.HouseholdRegisterType = nature;
        bean.LiveInfo = addressLive;

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


        bean.LiveStartTime = time1;
        bean.LiveEndTime = time2;
        BaseApplication.getInstance().setWriteInfoBean(bean);

        int pageFlag = BaseApplication.getInstance().getPageFlag();
        if (pageFlag == 1) {
            LoginBean.ResponseDataBean bean1 = BaseApplication.getInstance().getLoginBean();

            if (bean1 == null) {
                bean1 = new LoginBean.ResponseDataBean();

                int account_id = (int) SPUtil.get(this, Constants.ACCOUNT_ID, 0);
                String account_name = (String) SPUtil.get(this, Constants.ACCOUNT_NAME, "");
                bean1.AccountId = account_id;
                bean1.AccountName = account_name;
                BaseApplication.getInstance().setLoginBean(bean1);
            }

            showProgressDialog("正在提交...");
            mWaitTime = System.currentTimeMillis();
            addCaseNet(bean1.AccountId, name, bean.CaseNumber, bean.OutDangerTime, bean.OutDangerAddress, bean.CasualtiesType, bean.OutDangerDescription);

        } else {
            Intent intent = new Intent(this, WriteInfoThreeActivity.class);
            startActivity(intent);
        }

    }

    private void addCaseNet(int accountId, String name, String number, String time, String addreess, String type, String process) {
        RetrofitHelper.getBaseApi()
                .addCaseNet(accountId, name, number, time, addreess, type, process)
                .compose(this.<AddCaseBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddCaseBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showNetError();
                    }

                    @Override
                    public void onNext( AddCaseBean bean) {
//                        ToastUtil.showShort(TextUtils.isEmpty(bean.ResponseMessage) ? Constants.ERROR : bean.ResponseMessage);
                        if (Constants.OK.equals(bean.StateCode)) {
                            final String str = bean.ResponseMessage;

                            long l = System.currentTimeMillis();
                            if (l - mWaitTime >= Constants.WAIT_TIME) {
                                hideProgressDialog();
                                ToastUtil.showShort(TextUtils.isEmpty(str) ? "提交成功" : str);
                                finishTask(bean);
                            } else {
                                final AddCaseBean addCaseBean = bean;
                                BaseApplication.getInstance().mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideProgressDialog();
                                        ToastUtil.showShort(TextUtils.isEmpty(str) ? "提交成功" : str);
                                        finishTask(addCaseBean);
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
        if (bean instanceof AddCaseBean) {
            setAddCaseBeanData((AddCaseBean) bean);
        }
    }


    private void setAddCaseBeanData(AddCaseBean bean) {
        WriteInfoBean writeInfoBean = BaseApplication.getInstance().getWriteInfoBean();
        if (writeInfoBean == null) {
            writeInfoBean = new WriteInfoBean();
        }
        if (bean.ResponseData != null) {
            writeInfoBean.CaseId = bean.ResponseData.CaseID;
            writeInfoBean.OrderId = bean.ResponseData.OrderID;
        }
        Intent intent = new Intent(this, WriteInfoThreeActivity.class);
        startActivity(intent);
    }
}
