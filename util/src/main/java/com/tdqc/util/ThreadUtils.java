package com.tdqc.util;

/**
 * 线程相关工具类
 * Created by chenyen on 2017/2/21.
 */
public class ThreadUtils {

    /**线程暂停*/
    public static void sleep(long timeMillis){
        try {
            Thread.sleep(timeMillis);
        } catch (Exception e) {}
    }

}
