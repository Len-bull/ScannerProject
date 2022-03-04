package com.tdqc.framework.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.tdqc.framework.R;

/**
 * 水波效果自定义View
 * Created by chenyen on 2017/7/26.
 */
public class WaveView extends View {

    private Path mAbovePath;
    private Paint mAboveWavePaint;
    private DrawFilter mDrawFilter;
    private float φ;
    private OnWaveAnimationListener mWaveAnimationListener;
    private int aboveColor,bottomColor;
    public boolean isAnimationEnable = true;

    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttr(context,attrs);
        init();
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttr(context,attrs);
        init();
    }

    private void getAttr(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        aboveColor = typedArray.getColor(R.styleable.WaveView_aboveColor, 0);
        bottomColor = typedArray.getColor(R.styleable.WaveView_bottomColor, 0);
        typedArray.recycle();
    }

    private void init(){
        setBackgroundColor(bottomColor);
        mAbovePath = new Path();
        mAboveWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAboveWavePaint.setAntiAlias(true);
        mAboveWavePaint.setStyle(Paint.Style.FILL);
        mAboveWavePaint.setColor(aboveColor);
        mDrawFilter = new PaintFlagsDrawFilter(0,Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(mDrawFilter);
        mAbovePath.reset();
        φ -= 0.1f;
        float y;
        double ω = 2 * Math.PI / getWidth();
        mAbovePath.moveTo(getLeft(),getTop());
        for (float x = 0; x <= getWidth(); x += 20) {
            /**
             *  y=Asin(ωx+φ)+k
             *  A—振幅越大，波形在y轴上最大与最小值的差值越大
             *  ω—角速度， 控制正弦周期(单位角度内震动的次数)
             *  φ—初相，反映在坐标系上则为图像的左右移动。这里通过不断改变φ,达到波浪移动效果
             *  k—偏距，反映在坐标系上则为图像的上移或下移。
             */
            y = (float) (getHeight()/2 * Math.cos(ω * x + φ) + getHeight()/2);
            mAbovePath.lineTo(x, y);

            //回调 把y坐标的值传出去
            if(mWaveAnimationListener != null){
                mWaveAnimationListener.onWaveAnimation(y);
            }
        }
        mAbovePath.lineTo(getRight(),getTop());
        canvas.drawPath(mAbovePath,mAboveWavePaint);
        if(isAnimationEnable){
            postInvalidateDelayed(30);
        }
    }

    public void setOnWaveAnimationListener(OnWaveAnimationListener l) {
        this.mWaveAnimationListener = l;
    }

    public interface OnWaveAnimationListener{
        void onWaveAnimation(float y);
    }

}
