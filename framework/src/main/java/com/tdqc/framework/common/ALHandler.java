package com.tdqc.framework.common;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 防止内存溢出Handler，全称AvoidLeakHandler。
 * Created by chenyen on 2017/2/27.
 */
public abstract class ALHandler<T> extends Handler {

    private WeakReference<T> weakReferenceUI = null;

    public ALHandler(T ui) {
        this.weakReferenceUI = new WeakReference<>(ui);
    }

    public ALHandler(T ui,Looper looper) {
        super(looper);
        this.weakReferenceUI = new WeakReference<>(ui);
    }

    @Override
    public void handleMessage(Message msg) {
        T ui = weakReferenceUI.get();
        if(ui != null){
            hanldeMessage(msg, ui);
        }
    }

    public abstract void hanldeMessage(Message msg,T ui);

}
