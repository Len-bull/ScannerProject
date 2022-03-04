package com.len.scannerproject.config;

import android.os.Environment;

import com.len.scannerproject.BuildConfig;


/**
 * 配置文件
 * chenyen
 * 2021/3/4 15:16
 */
public class Config {
    /** 打包类型枚举 */
    public enum BuildType {
        /** 正式 */ RELEASE,
        /** 测试 */ DEBUG
    }

    /** 打包类型 */
    public static final BuildType buildType
            = BuildConfig.BUILD_TYPE.equals("debug") ? BuildType.DEBUG : BuildType.RELEASE;
    /** 默认请求头地址 */
    public static String REQUEST_URL = "";
    /** 订单信息 请求头 */
    public static String ORDER_URL = "";
    /** 正式请求地址  */
    public static final String RELEASE_REQUEST = "https://scan.mgtv100.com/api/v1/";

    /** 测试请求地址 */
    public static final String DEBUG_REQUEST = "https://scan.mgtv100.com/api/v1/";
    /** 是否执行自定义动画，在Espresso自动化测试时动画会阻塞测试线程 */
    public static boolean isAnimationEnable = true;

    static {
        REQUEST_URL = buildType == BuildType.RELEASE ? RELEASE_REQUEST : DEBUG_REQUEST;
    }

    /***判断是不是正式环境*/
    public static boolean isNetUrl() {
        return REQUEST_URL.equals(RELEASE_REQUEST);
    }

    /** spname */
    public static final String SP_NAME = "SHI_AN_SP";
    /** app文件存储基础目录 */
    public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SHIAN/";
    /** 图片存储子目录 */
    public static final String SECOND_PATH_PHOTO = "photo/";
    /** 正常网络环境超时时间 */
    public static final long REQUEST_TIMEOUT_MILS = 30 * 1000;
    /** 2G网络环境下超时时间 */
    public static final long REQUEST_TIMEOUT_MILS_2G = 30 * 1000;

}