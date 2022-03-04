package com.tdqc.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * View相关方法
 * Created by chenyen on 2016/12/8.
 */
public class ViewUtils {

    private static long lastClickTime;

    /**方向枚举*/
    public enum  Direction {
        TOP,LEFT,BOTTOM,RIGHT
    }

    /**判断是否短时间内重复点击*/
    public static boolean doubleClick() {
        long now = System.currentTimeMillis();
        long interval = now - lastClickTime;
        if (0 < interval && interval < 800) {
            return true;
        }
        lastClickTime = now;
        return false;
    }

    /**设置View背景*/
    public static void setViewBackground(@NonNull View view, @NonNull Drawable background) {
        if(view != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(background);
            } else {
                view.setBackgroundDrawable(background);
            }
        }
    }

    /**
     * 设置edittext是否可编辑
     * @param et
     * @param editable
     */
    public static void setEditable(@NonNull EditText et, boolean editable) {
        if(et != null){
            if(editable){
                et.setEnabled(true);
                et.setTextColor(ResourceUtils.getColor(R.color.text_normal));
            }else{
                et.setEnabled(false);
                et.setTextColor(ResourceUtils.getColor(R.color.text_disabled));
            }
        }
    }

    /**
     * 根据传入的View宽高比例计算View的实际高度。
     * @param percent 高宽百分比，比如高宽比为100:200，则这里传入50；
     * @return pixel
     * */
    public static int getActualHeightFromPercent(@NonNull Activity activity, int percent) {
        if(activity == null){
            return 0;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        return displayWidth * percent / 100;
    }

    /**
     * 设置TextView drawableTop
     * @param textView
     * @param drawableId
     * @param direction 图片方向
     */
    public static void setTextViewDrawable(@NonNull TextView textView, @DrawableRes int drawableId, @NonNull  Direction direction) {
        Drawable drawable = ResourceUtils.getDrawable(drawableId);
        if (textView == null || drawable == null){
            return;
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 这一步必须要做,否则不会显示.
        switch (direction){
            case TOP:
                textView.setCompoundDrawables(null, drawable, null, null);
                break;
            case BOTTOM:
                textView.setCompoundDrawables(null, null, null, drawable);
                break;
            case LEFT:
                textView.setCompoundDrawables(drawable, null, null, null);
                break;
            case RIGHT:
                textView.setCompoundDrawables(null, null, drawable, null);
                break;
        }
    }

    /**使listview固定高度从而适应viewpager*/
    public static void setListViewHeightBasedOnChildren(@NonNull ListView listView) {
        if(listView == null){
            return;
        }
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**传入View生成截图*/
    @Nullable
    public static Bitmap loadBitmapFromView(View v ) {
        if (v == null) {
            return null;
        }
        try {
            Bitmap screenshot;
            screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
            Canvas c = new Canvas(screenshot);
            c.translate(-v.getScrollX(), -v.getScrollY());
            v.draw(c);
            return screenshot;
        } catch (Exception e) {
            return null;
        }
    }
    /** 截取图片 */
    public static Bitmap getViewBp(View v) {
        if (null == v) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        if (Build.VERSION.SDK_INT >= 11) {
            v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(),
                    View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
                    v.getHeight(), View.MeasureSpec.EXACTLY));
            v.layout((int) v.getX(), (int) v.getY(),
                    (int) v.getX() + v.getMeasuredWidth(),
                    (int) v.getY() + v.getMeasuredHeight());
        } else {
            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        }
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache(), 0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.setDrawingCacheEnabled(false);
        v.destroyDrawingCache();
        return b;

    }
    /**传入View生成截图，自定义宽高*/
    @Nullable
    public static Bitmap loadBitmapFromView(View v,int width,int height){
        if (v == null || width <= 0 || height <= 0) {
            return null;
        }
        try {
            Bitmap screenshot;
            screenshot = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Canvas c = new Canvas(screenshot);
            c.translate(-v.getScrollX(), -v.getScrollY());
            v.draw(c);
            return screenshot;
        } catch (Exception e) {
            return null;
        }
    }

}
