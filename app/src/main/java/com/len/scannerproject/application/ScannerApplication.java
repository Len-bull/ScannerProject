package com.len.scannerproject.application;

import android.app.Application;

import com.tdqc.util.UtilsInit;

public class ScannerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /*工具类初始化*/
        UtilsInit.init(getApplicationContext());
    }
}
