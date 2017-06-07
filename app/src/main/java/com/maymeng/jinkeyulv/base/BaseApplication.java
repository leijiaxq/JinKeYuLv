package com.maymeng.jinkeyulv.base;

import android.app.Application;
import android.os.Handler;

import com.maymeng.jinkeyulv.bean.CheckUserBean;
import com.maymeng.jinkeyulv.bean.LoginBean;
import com.maymeng.jinkeyulv.bean.WriteInfoBean;
import com.maymeng.jinkeyulv.utils.LogUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

/**
 * Create by  leijiaxq
 * Date       2017/3/2 14:31
 * Describe
 */

public class BaseApplication extends Application {

    public static BaseApplication mInstance;
    public Handler mHandler;

    //校验信息；用于存储身份证号，姓名，手机号，银行卡号
    private CheckUserBean mCheckUserBean;

    //新建和修改信息时，写入的信息存在这个bean对象里
    public WriteInfoBean mWriteInfoBean;


    //登陆人员信息
    public LoginBean.ResponseDataBean mLoginBean;

    //用于标记 0是新的派单，1是新建派单，2是修改派单
    public int mPageFlag = 0;


    //签约费率
    public String mSignFee;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
      /*  if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/

        CrashReport.initCrashReport(getApplicationContext(), "00e9685c88", false);

        mHandler = new Handler();

        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);

        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token

                LogUtil.e(deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }

    public static BaseApplication getInstance() {
        return mInstance;
    }

    public CheckUserBean getCheckUserBean() {
        return mCheckUserBean;
    }

    public void setCheckUserBean(CheckUserBean checkUserBean) {
        mCheckUserBean = checkUserBean;
    }

    public WriteInfoBean getWriteInfoBean() {
        return mWriteInfoBean;
    }

    public void setWriteInfoBean(WriteInfoBean writeInfoBean) {
        mWriteInfoBean = writeInfoBean;
    }

    public LoginBean.ResponseDataBean getLoginBean() {
        return mLoginBean;
    }

    public void setLoginBean(LoginBean.ResponseDataBean loginBean) {
        mLoginBean = loginBean;
    }

    public int getPageFlag() {
        return mPageFlag;
    }

    public void setPageFlag(int pageFlag) {
        mPageFlag = pageFlag;
    }

    public String getSignFee() {
        return mSignFee;
    }

    public void setSignFee(String signFee) {
        mSignFee = signFee;
    }
}
