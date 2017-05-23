package com.maymeng.jinkeyulv.ui.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.base.RxBaseActivity;
import com.maymeng.jinkeyulv.utils.ImageUtil;
import com.maymeng.jinkeyulv.view.PictureViewPager;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by  leijiaxq
 * Date        2017/5/5 17:49
 * Describe    展示图片的页面
 */

public class ShowPictureActivity extends RxBaseActivity {
    @BindView(R.id.view_pager)
    PictureViewPager mViewPager;
    @BindView(R.id.back_iv)
    ImageView mBackIv;
    @BindView(R.id.indicator_tv)
    TextView mIndicatorTv;

    private String[] mImgUrl;
    private int pagerPosition;
    private SamplePagerAdapter mAdapter;

    @Override
    public int getLayoutId() {
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_show_picture;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

        String paths = getIntent().getStringExtra("ImageUrl");
        if (!TextUtils.isEmpty(paths)) {
            mImgUrl = paths.split(";");
        }

        pagerPosition = getIntent().getIntExtra("ImagePosition", 0);


        if (mImgUrl != null) {
            if (mImgUrl.length > 0) {
                mAdapter = new SamplePagerAdapter();
                mViewPager.setAdapter(mAdapter);
                //                mHackyViewPager.setCurrentItem(number);
            }
        }
        if (mViewPager != null) {
            if (mViewPager.getAdapter() != null) {

                CharSequence text = getString(R.string.viewpager_indicator, 1, mViewPager.getAdapter().getCount());
                mIndicatorTv.setText(text);
            }


            // 更新下标
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                @Override
                public void onPageSelected(int arg0) {
                    CharSequence text = getString(R.string.viewpager_indicator, arg0 + 1, mViewPager.getAdapter().getCount());
                    mIndicatorTv.setText(text);
                }

            });
            //        if (savedInstanceState != null) {
            //            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
            //        }

            mViewPager.setCurrentItem(pagerPosition);

        }
    }

    @Override
    public void initToolBar() {

    }

    @OnClick(R.id.back_iv)
    void clickBack(View view) {
        finish();
    }


    class SamplePagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return mImgUrl.length;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());

            photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //			photoView.setImageResource(sDrawables[position]);
            ImageUtil.getInstance().displayImage(ShowPictureActivity.this, mImgUrl[position], photoView);


            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

}
