package com.maymeng.jinkeyulv.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by  leijiaxq
 * Date        2017/5/5 18:28
 * Describe
 */

public class PictureViewPager extends ViewPager {
    public PictureViewPager(Context context) {
        super(context);
    }

    public PictureViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

   /* @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
        }
        return false;
    }*/
}
