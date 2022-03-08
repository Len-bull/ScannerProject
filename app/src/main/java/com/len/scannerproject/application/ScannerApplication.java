package com.len.scannerproject.application;


import com.aisier.architecture.base.BaseApp;
import com.tdqc.util.UtilsInit;

public class ScannerApplication extends BaseApp {
    @Override
    public void onCreate() {
        super.onCreate();
        /*工具类初始化*/
        UtilsInit.init(getApplicationContext());
    }
}
