package com.tdqc.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.DrawableRes;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

/**
 * 设备相关
 * Created by chenyen on 2016/12/8.
 */
public class DeviceUtils {

    private static Context appContext;
    private static final String TAG = "DeviceUtils";
    /** 屏幕宽 */
    private static int screenWidth = 0;
    /** 屏幕高 */
    private static int screenHeight = 0;
    /** 状态栏高度 */
    private static int statusBarHeight = 0;
    /** 底部操作栏高度 */
    private static int navigationHeight = 0;
    /** 设备唯一识别码 */
    private static String deviceId = null;
    /** 存储deviceId的sp name */
    private static String SP_NAME_DEVICE_ID = "SP_DEVICE_ID";
    /** 存储deviceId的sp key */
    private static String SP_KEY_DEVICE_ID = "DEVICE_ID";

    protected static void init(Context context) {
        appContext = context.getApplicationContext();
    }

    /**
     * 获取屏幕宽度
     *
     * @return pixels
     */
    public static int getScreenWidth() {
        if (screenWidth == 0) {
            getScreenSize();
        }
        return screenWidth;
    }

    /**
     * 获取屏幕高度
     *
     * @return pixels
     */
    public static int getScreenHeight() {
        if (screenHeight == 0) {
            getScreenSize();
        }
        return screenHeight;
    }

    /** 获取displayMetrics */
    public static DisplayMetrics getDisplayMetrics() {
        WindowManager wm = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    /** 获取屏幕尺寸 */
    private static void getScreenSize() {
        DisplayMetrics displayMetrics = getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
    }

    /** 获取状态栏高度 */
    public static int getStatusBarHeight() {
        if (statusBarHeight != 0) {
            return statusBarHeight;
        }
        int resourceId = appContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return resourceId > 0 ? appContext.getResources().getDimensionPixelSize(resourceId) : 0;
    }


    /** 获取底部操作栏高度 */
    public static int getNavigationHeight() {
        if (navigationHeight != 0) {
            return navigationHeight;
        }
        int resourceId = appContext.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return resourceId > 0 ? appContext.getResources().getDimensionPixelSize(resourceId) : 0;
    }

    /**
     * 获取手机CUP架构参数
     *
     * @return nullable
     */
    public static ArrayList<String> getCpuABI() {
        ArrayList<String> result = new ArrayList<>();
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (StringUtils.contains(field.getName(), "CPU_ABI")) {
                    result.add(field.get(null).toString());
                }
            } catch (Exception e) {
                L.e("an error occured when collect crash info", e);
            }
        }
        return result;
    }

    /** 检测当前环境是否是模拟器 */
    public static boolean checkIsEmulator() {
        String[] known_pipes = {"/dev/socket/qemud", "/dev/qemu_pipe"};
        try {
            for (int i = 0; i < known_pipes.length; i++) {
                String pipes = known_pipes[i];
                File qemu_socket = new File(pipes);
                if (qemu_socket.exists()) {
                    L.i("current device is emulator");
                    return true;
                }
            }
            L.i("current device is not emulator");
            return false;
        } catch (Exception ex) {
            L.i("checkIsEmulator error:" + ex.getMessage());
            return false;
        }
    }

    /**
     * deviceID的组成为：渠道标志+识别符来源标志+hash后的终端识别符
     * 渠道标志为：
     * 1，andriod（android）
     * 识别符来源标志：
     * 1， wifi mac地址（wifi）；
     * 2， IMEI（imei）；
     * 3， 序列号（sn）；
     * 4， id：随机码。若前面的都取不到时，则随机生成一个随机码，需要缓存。
     *
     * @return
     */
    public static String getDeviceId() {
        if (!StringUtils.isEmpty(deviceId)) {
            return deviceId;
        }
        StringBuilder deviceIdBuilder = new StringBuilder();
        // 渠道标志
        deviceIdBuilder.append("android");
        try {
            //wifi mac地址
            String wifiMac = getWifiMac();
            if (!StringUtils.isEmpty(wifiMac)) {
                deviceIdBuilder.append("wifi");
                deviceIdBuilder.append(wifiMac);
                deviceId = deviceIdBuilder.toString();
                return deviceId;
            }
            //IMEI（imei）
            String imei = getIMEI();
            if (!StringUtils.isEmpty(imei)) {
                deviceIdBuilder.append("imei");
                deviceIdBuilder.append(imei);
                deviceId = deviceIdBuilder.toString();
                return deviceId;
            }
            //序列号（sn）
            TelephonyManager tm = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
            String sn = tm.getSimSerialNumber();
            if (!StringUtils.isEmpty(sn)) {
                deviceIdBuilder.append("sn");
                deviceIdBuilder.append(sn);
                deviceId = deviceIdBuilder.toString();
                return deviceId;
            }
            //如果上面都没有， 则生成一个id：随机码
            String uuid = getUUID();
            if (!StringUtils.isEmpty(uuid)) {
                deviceIdBuilder.append("id");
                deviceIdBuilder.append(uuid);
                deviceId = deviceIdBuilder.toString();
                return deviceId;
            }
        } catch (Exception e) {
            L.e(TAG, "get device id error", e);
            deviceIdBuilder.append("id").append(getUUID());
        }
        deviceId = deviceIdBuilder.toString();
        return deviceId;
    }

    /** 得到设备IMEI */
    private static String getIMEI() {
        TelephonyManager TelephonyMgr = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();
        //android 10以上已经获取不了imei了 用 android id代替
        if (TextUtils.isEmpty(szImei)) {
            szImei = Settings.System.getString(appContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return szImei;

    }

    /** 得到设备mac */
    private static String getWifiMac() {
        WifiManager wm = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
        return m_szWLANMAC;
    }

    /** 得到全局唯一UUID并存入SP供下次使用 */
    public static String getUUID() {
        String uuid = SpUtils.getString(SP_NAME_DEVICE_ID, SP_KEY_DEVICE_ID, "");
        if (StringUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            SpUtils.putString(SP_NAME_DEVICE_ID, SP_KEY_DEVICE_ID, uuid);
        }
        return uuid;
    }

    /** android版本是否大于M */
    public static boolean isOverSdkM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    /** 本地图片路径 */
    public static String getMipmapToUri(int resId) {
        Resources r = appContext.getResources();
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(resId) + "/"
                + r.getResourceTypeName(resId) + "/"
                + r.getResourceEntryName(resId));
        return uri.getPath();


    }

    public static String getResourcesUri(@DrawableRes int id) {
        Resources resources = appContext.getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
        return uriPath;
    }

    /**
     * 设置状态栏深色浅色切换
     */
    public static boolean setStatusBarDarkTheme(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setCommonUI(activity, dark);
            } else {//其他情况
                return false;
            }

            return true;
        }
        return false;
    }

    //设置6.0 状态栏深色浅色切换
    public static boolean setCommonUI(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = activity.getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (dark) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                if (decorView.getSystemUiVisibility() != vis) {
                    decorView.setSystemUiVisibility(vis);
                }
                return true;
            }
        }
        return false;

    }
}
