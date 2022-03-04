package com.tdqc.framework.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 重写LinearLayoutManager修复android 原生LinearLayoutManager的崩溃问题。
 * Created by chenyen on 2017/7/6.
 */
public class TdqcLinearLayoutManager extends LinearLayoutManager {
    public TdqcLinearLayoutManager(Context context) {
        super(context);
    }

    public TdqcLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public TdqcLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try { /*捕获异常*/
            super.onLayoutChildren(recycler, state);
        } catch (Exception e) {}
    }

}
