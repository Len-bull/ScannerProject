package com.tdqc.framework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * 重写Support-v4 的ViewPager，原生的写的有点问题
 * Created by chenyen on 2017/7/12.
 */
public class TdqcViewPager extends ViewPager {



    public TdqcViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TdqcViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isEnabled()) {
            if (getCurrentItem() == 0 && getChildCount() == 0) {
                return false;
            }
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isEnabled()) {
            if (getCurrentItem() == 0 && getChildCount() == 0) {
                return false;
            }
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

}
