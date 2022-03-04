package com.tdqc.util;

import android.content.Context;

/**
 * 初始化工具类
 * Created by chenyen on 2016/12/8.
 */
public class UtilsInit {

    /**初始化工具类*/
    public static void init(Context context){
        ProcessUtils.init(context);
        ContextUtils.init(context);
        ResourceUtils.init(context);
        To.init(context);
        StringUtils.init(context);
        SpUtils.init(context);
        PacketUtils.init(context);
        NetworkUtils.init(context);
        DeviceUtils.init(context);
        PhotoUtils.init(context);
        //BadgeUtil.init(context);
        LocalBroadcastUtils.init(context);
        //PermissionUtils.init(context);
    }

}
