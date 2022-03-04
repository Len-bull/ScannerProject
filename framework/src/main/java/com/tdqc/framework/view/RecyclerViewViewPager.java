package com.tdqc.framework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * 重写Support-v4 的ViewPager，原生的写的有点问题
 * Created by chenyen on 2017/7/12.
 */
public class RecyclerViewViewPager extends ViewPager {


    public RecyclerViewViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewViewPager(Context context) {
        super(context);
    }
    //是否可以进行滑动
    private boolean canScroll = true;//默认可以滑动
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int height = 0;
//        for (int i = 0; i < getChildCount(); i++) {
//            View child = getChildAt(i);
//            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//            int h = child.getMeasuredHeight();
//            if (h > height) height = h;
//        }
//
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return canScroll;
    }

}
