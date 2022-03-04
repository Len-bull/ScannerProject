package com.leon.channel.hepler;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.leon.channel.reader.ChannelReader;
import com.leon.channel.reader.IdValueReader;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 *
 * Created by chenyen on 2017/4/24.
 */
public class ChannelReaderUtil {

    private static final String TAG = "ChannelReaderUtil";
    private static String mChannelCache;


    public static String getChannel(Context context) {
        if (mChannelCache == null) {
            String channel = getChannelByV2(context);
            if (channel == null) {
                channel = getChannelByV1(context);
            }
            mChannelCache = channel;
        }

        return mChannelCache;
    }

    /**
     * if apk use v2 signature , please use this mathod to get channel info
     *
     * @param context
     * @return
     */
    public static String getChannelByV2(Context context) {
        String apkPath = getApkPath(context);
        String channel = ChannelReader.getChannel(new File(apkPath));
        Log.i(TAG, "getChannelByV2 , channel = " + channel);
        return channel;
    }

    /**
     * if apk only use v1 signature , please use this mathod to get channel info
     *
     * @param context
     * @return
     */
    public static String getChannelByV1(Context context) {
        String apkPath = getApkPath(context);
        String channel = ChannelReader.getChannelByZipComment(new File(apkPath));
        Log.i(TAG, "getChannelByV1 , channel = " + channel);
        return channel;
    }


    /**
     * get String value from apk by id
     *
     * @param context
     * @param id
     * @return
     */
    public static String getStringValueById(Context context, int id) {
        String apkPath = getApkPath(context);
        String value = IdValueReader.getStringValueById(new File(apkPath), id);
        Log.i(TAG, "id = " + id + " , value = " + value);
        return value;
    }

    /**
     * get byte[] from apk by id
     *
     * @param context
     * @param id
     * @return
     */
    public static byte[] getByteValueById(Context context, int id) {
        String apkPath = getApkPath(context);
        return IdValueReader.getByteValueById(new File(apkPath), id);
    }

    /**
     * find all Id-Value Pair from Apk
     *
     * @param context
     * @return
     */
    public static Map<Integer, ByteBuffer> getAllIdValueMap(Context context) {
        String apkPath = getApkPath(context);
        return IdValueReader.getAllIdValueMap(new File(apkPath));
    }

    /**
     * 获取已安装的APK路径
     *
     * @param context
     * @return
     */
    private static String getApkPath(Context context) {
        String apkPath = null;
        try {
            ApplicationInfo applicationInfo = context.getApplicationInfo();
            if (applicationInfo == null) {
                return null;
            } else {
                apkPath = applicationInfo.sourceDir;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return apkPath;
    }

}
