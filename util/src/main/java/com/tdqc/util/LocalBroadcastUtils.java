package com.tdqc.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * 本地广播相关工具类
 * Created by chenyen on 2016/12/8.
 */
public class LocalBroadcastUtils {

    private static Context appContext;

    protected static void init(Context context){
        appContext = context.getApplicationContext();
    }

    /**注册本地广播,需要用unregisterLocalReceiver()方法注销*/
    public static void registerLocalReceiver(BroadcastReceiver mReceiver, String[] actions) {
        if (mReceiver != null) {
            IntentFilter filter = new IntentFilter();
            for (String action : actions) {
                filter.addAction(action);
            }
            LocalBroadcastManager.getInstance(appContext).registerReceiver(mReceiver, filter);
        }
    }

    /**
     * 本地广播注销
     * 用LocalBroadcaseManager注册的广播需要用这个方法注销
     */
    public static void unregisterLocalReceiver(BroadcastReceiver receiver) {
        if (receiver != null) {
            LocalBroadcastManager.getInstance(appContext).unregisterReceiver(receiver);
        }
    }

    /**发送本地广播*/
    public static void sendLocalBroadcase(Intent intent) {
        LocalBroadcastManager.getInstance(appContext).sendBroadcast(intent);
    }

}
