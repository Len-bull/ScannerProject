package com.tdqc.util;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

/**
 * Toast
 * Created by chenyen on 2016/12/8.
 */
public class To {

    private static Toast toast;
    private static Context appContext;
    private static Handler handler;
    private static View customToastView = null;
    private static TextView customToastTextView = null;

    protected static void init(Context context){
        appContext = context.getApplicationContext();
        handler = new Handler(appContext.getMainLooper());
    }

    /**show*/
    public static void s(@StringRes int resourcesId){
        s(ResourceUtils.getString(resourcesId));
    }

    /**show*/
    public static void s(final String msg) {
        if(StringUtils.isEmpty(msg)){
            return;
        }
        if(Thread.currentThread().getId() == appContext.getMainLooper().getThread().getId()){
            showToastCore(msg);
            return;
        }
		/*强迫主线程执行*/
        handler.post(new Runnable() {
            @Override
            public void run() {
                showToastCore(msg);
            }
        });
    }
    /**show*/
    public static void slong(final String msg) {
        if(StringUtils.isEmpty(msg)){
            return;
        }
        if(Thread.currentThread().getId() == appContext.getMainLooper().getThread().getId()){
            showToastCoreLong(msg);
            return;
        }
		/*强迫主线程执行*/
        handler.post(new Runnable() {
            @Override
            public void run() {
                showToastCoreLong(msg);
            }
        });
    }

    /**取消toast*/
    public static void cancelToast(){
        if(toast != null){
            toast.cancel();
            toast = null;
        }
    }

    /**设置自定义的Toast样式*/
    public static void setCustomToast(@LayoutRes int layoutId, @IdRes int toastTextId){
        if(layoutId == 0 || toastTextId == 0){
            return;
        }
        customToastView = View.inflate(appContext, layoutId, null);
        if(customToastView == null){
            customToastView = null;
            return;
        }
        View toastText = customToastView.findViewById(toastTextId);
        if(toastText == null || !(toastText instanceof TextView)){
            customToastView = null;
            return;
        }
        customToastTextView = (TextView) toastText;
        toast = null;
    }

    /**toast核心代码*/
    private static void showToastCore(String msg) {
        try{
        if (toast == null) {
            if(customToastView != null && customToastTextView != null){ /*自定义toast*/
                toast = new Toast(appContext);
                toast.setView(customToastView);
                toast.setDuration(Toast.LENGTH_SHORT);
            }else{
                toast = Toast.makeText(appContext, msg + "", Toast.LENGTH_SHORT);
            }
            /*置屏幕中央*/
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        if(customToastView != null && customToastTextView != null) { /*自定义toast*/
            customToastTextView.setText(msg);
        }else{
            toast.setText(msg);
        }
        toast.show();
        }catch (Exception e){

        }
    }
    /**toast核心代码*/
    private static void showToastCoreLong(String msg) {
        if (toast == null) {
            if(customToastView != null && customToastTextView != null){ /*自定义toast*/
                toast = new Toast(appContext);
                toast.setView(customToastView);
                toast.setDuration(Toast.LENGTH_SHORT);
            }else{
                toast = Toast.makeText(appContext, msg + "", Toast.LENGTH_LONG);
            }
            /*置屏幕中央*/
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        if(customToastView != null && customToastTextView != null) { /*自定义toast*/
            customToastTextView.setText(msg);
        }else{
            toast.setText(msg);
        }
        toast.show();
    }
}
