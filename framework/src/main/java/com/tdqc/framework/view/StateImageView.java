package com.tdqc.framework.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.tdqc.framework.R;

/**
 * 可以在xml布局中定义点击状态图片的ImageView。
 * Created by chenyen on 2017/6/8.
 */
public class StateImageView extends ImageView{

    public StateImageView(Context context) {
        this(context, null);
    }

    public StateImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StateImageView);
        Drawable defaultImage = typedArray.getDrawable(R.styleable.StateImageView_default_image);
        Drawable pressedImage = typedArray.getDrawable(R.styleable.StateImageView_pressed_image);
        typedArray.recycle();
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedImage);
        stateListDrawable.addState(new int[]{}, defaultImage);
        setImageDrawable(stateListDrawable);
    }

}
