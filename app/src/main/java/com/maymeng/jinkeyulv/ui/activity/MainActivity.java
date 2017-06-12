package com.maymeng.jinkeyulv.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.api.RetrofitHelper;
import com.maymeng.jinkeyulv.base.BaseApplication;
import com.maymeng.jinkeyulv.base.RxBaseActivity;
import com.maymeng.jinkeyulv.bean.BaseNetBean;
import com.maymeng.jinkeyulv.bean.LoginBean;
import com.maymeng.jinkeyulv.ui.pop.ExitPop;
import com.maymeng.jinkeyulv.utils.SPUtil;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends RxBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.iamge_22)
    ImageView mIamge22;
    @BindView(R.id.tv_22)
    TextView mTv22;
    @BindView(R.id.layout_22)
    CardView mLayout22;
    @BindView(R.id.layout1)
    LinearLayout mLayout1;
    @BindView(R.id.layout2)
    LinearLayout mLayout2;
    @BindView(R.id.layout3)
    LinearLayout mLayout3;
    @BindView(R.id.layout4)
    LinearLayout mLayout4;
    @BindView(R.id.layout5)
    LinearLayout mLayout5;

    //    用于避免重复点击事件
    private boolean mFlag = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mTitleTv.setText("金科玉律");
    }

    @Override
    public void initToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
//            actionBar.setHomeAsUpIndicator(R.drawable.back_white_img);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_exit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
//                ToastUtil.showShort(this, "注销");
                ExitUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    AlertDialog.Builder mDialog;

    //注销退出
    private void ExitUser() {
        ExitPop exitPop = new ExitPop(this);
        exitPop.setOnPopListenter(new ExitPop.OnPopListenter() {
            @Override
            public void onConfirm() {
                loginOut();

                /*SPUtil.clear(MainActivity.this);

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();*/
            }
        });

        exitPop.showAtLocation(mTitleTv, Gravity.CENTER, 0, 0);






        /*if (mDialog == null) {
            mDialog = new AlertDialog.Builder(this);
            mDialog.setTitle("是否注销当前账号？");
            mDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            mDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);

                    finish();
                }
            });

        }
        mDialog.show();*/
    }

    //退出登录
    private void loginOut() {
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
                .loginOut(bean.AccountId)
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
                    public void onNext(BaseNetBean baseNetBean) {
//                        ToastUtil.showShort(TextUtils.isEmpty(baseNetBean.ResponseMessage) ? "设置状态成功" : baseNetBean.ResponseMessage);

                        SPUtil.clear(MainActivity.this);

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
    }

    @Override
    public void loadData() {
//        showProgressDialog(" 加载中... ");

    }

    //新的派单
    @OnClick(R.id.layout1)
    void clickLayout1(View view) {
        if (mFlag) {
            mFlag = false;
//            view.setEnabled(false);
            skipByAnimation(view, 0);
        }
    }

    //信息校验
    @OnClick(R.id.layout2)
    void clickLayout2(View view) {
        if (mFlag) {
            mFlag = false;
//            view.setEnabled(false);
            skipByAnimation(view, 1);
        }
    }

    //新建派单
    @OnClick(R.id.layout3)
    void clickLayout3(View view) {
        if (mFlag) {
            mFlag = false;
//            view.setEnabled(false);
            skipByAnimation(view, 2);
        }
    }

    //进度查询
    @OnClick(R.id.layout4)
    void clickLayout4(View view) {
        if (mFlag) {
            mFlag = false;
//            view.setEnabled(false);
            skipByAnimation(view, 3);
        }
    }

    //上传签约
    @OnClick(R.id.layout5)
    void clickLayout5(View view) {
        if (mFlag) {
            mFlag = false;
//            view.setEnabled(false);
            skipByAnimation(view, 4);
        }
    }

   /*
    @OnClick(R.id.layout5)
    void clickLayout5(View view) {
        if (mFlag) {
            mFlag = false;
//            view.setEnabled(false);
            skipByAnimation(view, 4);
        }
    }*/

    private void skipByAnimation(View view, final int position) {
        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) mLayout22.getLayoutParams();

        int[] location = new int[2];
        view.getLocationInWindow(location);
        int x = location[0];
        int y = location[1];

        layoutParams1.height = view.getHeight();
        layoutParams1.width = view.getWidth();

        layoutParams1.leftMargin = x;
        layoutParams1.topMargin = y;

        mLayout22.setLayoutParams(layoutParams1);

        int res = 0;
        String text = "";

        switch (position) {
            case 0:
                res = R.drawable.new_order;
                text = "新的派单";
                break;
            case 1:
                res = R.drawable.new_order3;
                text = "信息校验";
                break;
            case 2:
                res = R.drawable.new_order2;
                text = "新建派单";
                break;
            case 3:
                res = R.drawable.query_pro;
                text = "进度查询";
                break;
            case 4:
                res = R.drawable.icon_33;
                text = "上传签约";
                break;
            default:
                break;
        }


        mIamge22.setImageResource(res);
        mTv22.setText(text);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.home_zoom);
        mLayout22.startAnimation(animation);
        mLayout22.setVisibility(View.VISIBLE);

        BaseApplication.getInstance().mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLayout22.setVisibility(View.GONE);
                Intent intent = null;
                if (position == 0) {
                    intent = new Intent(MainActivity.this, NewDispatchActivity.class);
//                    mLayout1.setEnabled(true);
                } else if (position == 1) {
                    intent = new Intent(MainActivity.this, InfoCheckOneActivity.class);
//                    intent = new Intent(MainActivity.this, InfoCheckFourActivity.class);
//                    intent = new Intent(MainActivity.this, InfoCheckOneActivity.class);
//                    mLayout2.setEnabled(true);
                } else if (position == 2) {
                    intent = new Intent(MainActivity.this, WriteInfoOneActivity.class);
//                    intent = new Intent(MainActivity.this, WriteInfoFiveActivity.class);

                    //用于标记是新建派单
                    BaseApplication.getInstance().setPageFlag(1);
                    BaseApplication.getInstance().setWriteInfoBean(null);
//                    mLayout3.setEnabled(true);
                } else if (position == 3) {
                    intent = new Intent(MainActivity.this, QueryActivity.class);
//                    mLayout4.setEnabled(true);
                } else if (position == 4) {
//                    intent = new Intent(MainActivity.this, SignUploadActivity.class);
                    intent = new Intent(MainActivity.this, SignActivity.class);
//                    mLayout5.setEnabled(true);
                }
                mFlag = true;
                startActivity(intent);
            }
        }, 300);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            // 不退出程序，进入后台
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
    //新的派单
    @OnClick(R.id.layout1)
    void clickLayout1(View view) {
        Intent intent = new Intent(this, NewDispatchActivity.class);
        startActivity(intent);
    }
    //信息校验
    @OnClick(R.id.layout2)
    void clickLayout2(View view) {
        Intent intent = new Intent(this, InfoCheckOneActivity.class);
        startActivity(intent);
    }

    //资料录入
    @OnClick(R.id.layout3)
    void clickLayout3(View view) {
        Intent intent = new Intent(this, WriteInfoOneActivity.class);
        startActivity(intent);
    }
    //进度查询
    @OnClick(R.id.layout4)
    void clickLayout4(View view) {
        Intent intent = new Intent(this, QueryActivity.class);
        startActivity(intent);
    }
    //新建派单
    @OnClick(R.id.layout5)
    void clickLayout5(View view) {
        Intent intent = new Intent(this, WriteInfoOneActivity.class);
        startActivity(intent);
    }*/
}
