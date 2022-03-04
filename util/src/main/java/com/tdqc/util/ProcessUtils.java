package com.tdqc.util;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

/**
 * 进程相关
 * Created by chenyen on 2016/12/8.
 */
public class ProcessUtils {

    private static Context appContext;
    private static String processName = "";

    protected static void init(Context context){
        appContext = context.getApplicationContext();
    }

    /**判断当前是否运行在主进程*/
    public static boolean inMainProcess() {
        String packageName = appContext.getPackageName();
        String processName = getProcessName();
        return packageName.equals(processName);
    }

    /**
     * 获取当前进程名
     * @return 进程名
     */
    public static final String getProcessName() {
        String result = processName;
        if(!StringUtils.isEmpty(result)){
            return result;
        }
        ActivityManager am = ((ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE));
        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    result = info.processName;
                    break;
                }
            }
            if (!TextUtils.isEmpty(result)) {
                return result;
            }
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

}
