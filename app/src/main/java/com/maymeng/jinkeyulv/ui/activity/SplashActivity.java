package com.maymeng.jinkeyulv.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.base.BaseApplication;
import com.maymeng.jinkeyulv.bean.LoginBean;
import com.maymeng.jinkeyulv.utils.SPUtil;

/**
 * Created by  leijiaxq
 * Date        2017/4/11 18:20
 * Describe
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        BaseApplication.getInstance().mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                jude2MainPage();
            }
        }, 2000);
    }

    //判断用户是否登录过，若是登录过，进入首页
    private void jude2MainPage() {
        int account_id = (int) SPUtil.get(this, Constants.ACCOUNT_ID, 0);
        if (account_id != 0) {
            String account_name = (String) SPUtil.get(this, Constants.ACCOUNT_NAME, "");
            LoginBean.ResponseDataBean bean = new LoginBean.ResponseDataBean();
            bean.AccountId = account_id;
            bean.AccountName = account_name;
            BaseApplication.getInstance().setLoginBean(bean);

            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
            finish();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
            finish();
        }
    }

//    private boolean isIn;

//    private void toMainActivity() {
//        /*if (isIn) {
//            return;
//        }*/
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
//        finish();
////        isIn = true;
//    }


}
