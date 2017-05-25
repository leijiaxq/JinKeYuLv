package com.maymeng.jinkeyulv.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.api.RetrofitHelper;
import com.maymeng.jinkeyulv.base.BaseApplication;
import com.maymeng.jinkeyulv.base.RxBaseActivity;
import com.maymeng.jinkeyulv.bean.BaseBean;
import com.maymeng.jinkeyulv.bean.LoginBean;
import com.maymeng.jinkeyulv.bean.SignBean;
import com.maymeng.jinkeyulv.ui.adapter.SignAdapter;
import com.maymeng.jinkeyulv.utils.SPUtil;
import com.maymeng.jinkeyulv.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by  leijiaxq
 * Date        2017/4/20 11:09
 * Describe    签约文件列表页
 */
public class SignActivity extends RxBaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swip_refresh)
    SwipeRefreshLayout mSwipRefresh;

    private boolean mIsAllLoaded;

    private int mPageIndex = 1;
    private List<SignBean.ResponseDataBean> mDatas = new ArrayList<>();
    private SignAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_query_all;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

        mTitleTv.setText("申请签约");

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
        mSwipRefresh.setOnRefreshListener(this);
        mSwipRefresh.setColorSchemeColors(getResources().getIntArray(R.array.gplus_colors));

//        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager)
                        .findLastVisibleItemPosition();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();

                if (!mIsAllLoaded && visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition >= totalItemCount - 1 && mDatas.size() >= Constants.SIZE) {
                    mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                    loadMore();
                }
               /* int firstCompletelyVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition != 0) {
                    mFab.setVisibility(View.VISIBLE);
                } else {
                    mFab.setVisibility(View.GONE);
                }*/
            }
        });

        initAdapter();
//        mSwipRefresh.setRefreshing(true);
        showProgressDialog("正在加载");
        onRefresh();
//        makeData();
    }

    private void initAdapter() {
        mAdapter = new SignAdapter(this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new SignAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(SignActivity.this, SignUploadActivity.class);
                SignBean.ResponseDataBean bean = mDatas.get(position);
                intent.putExtra("SignBean", bean);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onRefresh() {
        mPageIndex = 1;
        mIsAllLoaded = false;
        mAdapter.isAllLoad = false;

        getSignByAccountID();

    }

    private void loadMore() {
        mPageIndex++;

//        getSignByAccountID();
        BaseApplication.getInstance().mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getSignByAccountID();
            }
        },Constants.WAIT_TIME_LOADMORE);

    }

    private void getSignByAccountID() {
        LoginBean.ResponseDataBean bean = BaseApplication.getInstance().getLoginBean();
        if (bean == null) {
            bean = new LoginBean.ResponseDataBean();

            int account_id = (int) SPUtil.get(this, Constants.ACCOUNT_ID, 0);
            String account_name = (String) SPUtil.get(this, Constants.ACCOUNT_NAME, "");
            bean.AccountId = account_id;
            bean.AccountName = account_name;
            BaseApplication.getInstance().setLoginBean(bean);
        }

        RetrofitHelper.getBaseApi()
                .getSignByAccountID(bean.AccountId, mPageIndex, Constants.SIZE)
                .compose(this.<SignBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SignBean>() {
                    @Override
                    public void onCompleted() {
                        if (mSwipRefresh != null) {
                            mSwipRefresh.setRefreshing(false);
                        }
                        hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mSwipRefresh != null) {
                            mSwipRefresh.setRefreshing(false);
                        }
                        showNetError();
                    }

                    @Override
                    public void onNext(SignBean signBean) {
                        if (Constants.OK.equals(signBean.StateCode)) {
                            finishTask(signBean);
                        } else {
                            ToastUtil.showShort(TextUtils.isEmpty(signBean.ResponseMessage) ? Constants.ERROR : signBean.ResponseMessage);
                        }
                    }
                });
    }


    @Override
    public void finishTask(BaseBean bean) {
        if (bean instanceof SignBean) {
            setSignBeanData((SignBean) bean);
        }
    }

    private void setSignBeanData(SignBean bean) {
        if (bean.ResponseData == null) {
            bean.ResponseData = new ArrayList<>();
        }

        if (bean.ResponseData.size() < Constants.SIZE) {   //数据加载完了
            mIsAllLoaded = true;
            mAdapter.isAllLoad = true;
        }

        if (mPageIndex == 1) {
            mDatas.clear();
            mRecyclerView.scrollToPosition(0);

            if (bean.ResponseData.size() == 0) {
                ToastUtil.showShort("没有数据！");
            }
        }

        mDatas.addAll(bean.ResponseData);
        mAdapter.notifyDataSetChanged();
    }

}
