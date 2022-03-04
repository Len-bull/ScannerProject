package com.tdqc.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 软键盘相关方法
 * Created by chenyen on 2016/12/8.
 */
public class SoftkeyUtils {

    /**输入框要求得到焦点，弹出软键盘*/
    public static void edtRequestSoftKey(Activity a, EditText edt) {
        if (a != null && edt != null) {
            edt.requestFocus();
            ((InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(edt, 0);
        }
    }

    /**输入框要求得到焦点，弹出软键盘并清除内容*/
    public static void edtRequestSoftKeyClear(Activity a, EditText edt) {
        if (a != null && edt != null) {
            edt.setText("");
            edtRequestSoftKey(a, edt);
        }
    }

    /**隐藏软键盘*/
    public static void hideSoftKey(Activity a) {
        try {
            View currentFocus = a.getCurrentFocus();
            if (currentFocus == null) {
                return;
            }
            ((InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }
    }

}
