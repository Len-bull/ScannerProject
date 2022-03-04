package com.tdqc.framework.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.tdqc.framework.R;
import com.tdqc.util.ResourceUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 可以在xml布局中定义点击状态图片的TextView。
 * Created by chenyen on 2017/6/8.
 */
public class StateImageTextView extends TextView{

    @Retention(RetentionPolicy.SOURCE)
    public @interface Direction {
        int top = 0;
        int left = 1;
        int right = 2;
        int bottom = 3;
    }

    public StateImageTextView(Context context) {
        this(context, null);
    }

    public StateImageTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateImageTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    /**在Java代码中new调用此构造方法*/
    public StateImageTextView(Context context, @Direction int direction, @DrawableRes int defaultImgRes, @DrawableRes int pressedImgRes) {
        this(context);
        setDrawable(ResourceUtils.getDrawable(defaultImgRes),ResourceUtils.getDrawable(pressedImgRes),direction);
    }

    private void init(Context context,AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StateImageView);
        Drawable defaultImage = typedArray.getDrawable(R.styleable.StateImageView_default_image);
        Drawable pressedImage = typedArray.getDrawable(R.styleable.StateImageView_pressed_image);
        int direction = typedArray.getInt(R.styleable.StateImageView_direction,0);
        typedArray.recycle();
        setDrawable(defaultImage, pressedImage, direction);
    }

    public void setDrawable(Drawable defaultImage, Drawable pressedImage,@Direction int direction) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedImage);
        stateListDrawable.addState(new int[]{}, defaultImage);
        int width = stateListDrawable.getMinimumWidth();
        int height = stateListDrawable.getMinimumHeight();
        stateListDrawable.setBounds(0,0,width,height);
        switch (direction){
            case Direction.top:
                setCompoundDrawables(null,stateListDrawable,null,null);
                break;
            case Direction.left:
                setCompoundDrawables(stateListDrawable,null,null,null);
                break;
            case Direction.right:
                setCompoundDrawables(null,null,stateListDrawable,null);
                break;
            case Direction.bottom:
                setCompoundDrawables(null,null,null,stateListDrawable);
                break;
        }
    }

}
