package com.tdqc.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 网络相关
 * Created by chenyen on 2016/12/8.
 */
public class NetworkUtils {

    /**网络类型枚举*/
    public enum NetworkType {
        NETWORK_2G,
        NETWORK_3G,
        NETWORK_WIFI,
        NETWORK_NULL
    }

    private static Context appContext;

    protected static void init(Context context){
        appContext = context.getApplicationContext();
    }

    /**判断手机是否可以上网*/
    public static boolean isNetBreak() {
        return getNetworkType() == NetworkType.NETWORK_NULL;
    }

    /**
     * 获取网络类型
     * @return NetworkType
     */
    public static NetworkType getNetworkType() {
        NetworkType result = NetworkType.NETWORK_NULL;
        ConnectivityManager connect = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connect.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                if (networkInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS
                        || networkInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA
                        || networkInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE) {
                    return NetworkType.NETWORK_2G;
                } else {
                    return NetworkType.NETWORK_3G;
                }
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return NetworkType.NETWORK_WIFI;
            }
        }
        return result;
    }

    // 网络连接判断
    public static boolean netWorkCheck(){
        ConnectivityManager cm =  (ConnectivityManager)appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if( info != null ){
            return info.isConnected();
        } else {
            return false;
        }
    }

}
