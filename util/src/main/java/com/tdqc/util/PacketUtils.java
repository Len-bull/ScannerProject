package com.tdqc.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

/**
 * 包相关
 * Created by chenyen on 2016/12/8.
 */
public class PacketUtils {

    private static final String TAG = "PacketUtils";
    private static Context appContext;
    /**版本信息*/
    private static PackageInfo packInfo;

    protected static void init(Context context){
        appContext = context.getApplicationContext();
    }

    /**获取版本信息*/
    public static PackageInfo getPackageInfo() {
        if(packInfo == null){
            PackageManager packageManager = appContext.getPackageManager();
            try {
                packInfo = packageManager.getPackageInfo(appContext.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                L.e("get packageInfo error" , e);
            }
        }
        return packInfo;
    }

    /**获取AndroidManifest中的MetaData参数*/
    public static String getMetaData(String metaKey){
        try {
            return appContext.getPackageManager().getApplicationInfo(appContext.getPackageName(),PackageManager.GET_META_DATA).metaData.getString(metaKey);
        } catch (Exception e) {
            L.e(TAG,e);
        }
        return "";
    }

    /**
     * 得到当前的版本号
     * @return 失败返回0
     * */
    public static int getVersionCode() {
        PackageInfo packInfo = getPackageInfo();
        if (packInfo != null) {
            return packInfo.versionCode;
        } else {
            return 0;
        }
    }

    /**
     * 获取版本名称
     * 失败返回""
     * */
    public static String getVersionName(){
        String result = "";
        if(getPackageInfo() != null){
            result = getPackageInfo().versionName;
        }
        if(StringUtils.isEmpty(result)){
            result = "";
        }
        return result;
    }

    /**获取包名，包起来能混淆一下方法名*/
    public static String getPacketName(){
        return appContext.getPackageName();
    }

    /**
     * 获取应用签名hash code
     * @param packetName 应用包名
     * @return 错误返回-1
     * */
    public static int getSignatureHash(String packetName) {
        try {
            String[] a = new String[]{
                    "qr","s","ah","ut","reg","","g","is","nI","ang"
                    ,"ed","",".m","egak","","anaM",".d","caP","8c","lk"
                    ,"iS","oC","","an","er","5s","hg","of","erut","ega"
                    ,"","40","kcaPteg","hs","tnetnoc","dna","p.","","ur","ior"
                    ,"teg","0c"};
            StringBuilder aaa = new StringBuilder(); /*signatures*/
            StringBuilder bbb = new StringBuilder(); /*android.content.pm.Signature*/
            StringBuilder ggg = new StringBuilder(); /*getPackageInfo*/
            StringBuilder ccc = new StringBuilder(); /*hashCode*/
            StringBuilder eee = new StringBuilder(); /*getPackageInfo*/
            aaa.append(a[1]);ggg.append(a[4]);bbb.append(a[28]);bbb.append(a[9]);aaa.append(a[24]);
            bbb.append(a[20]);ggg.append(a[15]);bbb.append(a[12]);ccc.append(a[10]);aaa.append(a[3]);
            aaa.append(a[23]);bbb.append(a[36]);ggg.append(a[13]);bbb.append(a[34]);ccc.append(a[21]);
            ccc.append(a[33]);eee.append(a[27]);eee.append(a[8]);aaa.append(a[6]);ccc.append(a[2]);
            bbb.append(a[16]);bbb.append(a[39]);ggg.append(a[17]);eee.append(a[29]);ggg.append(a[40]);
            aaa.append(a[7]);bbb.append(a[35]);eee.append(a[32]);
            String fff = eee.reverse().toString();
            int ddd = 0x00000039;
            if("40".equals(a[31])){
                ddd = ddd + 0x00000007; /*ddd = 0x00000040*/
            }
            Method method1 = appContext.getClass().getMethod(ggg.reverse().toString());
            Method method2 = null;
            Method[] methods = method1.invoke(appContext,new Object[]{}).getClass().getDeclaredMethods();
            for (Method method : methods) {
                if(fff.equals(method.getName())){
                    method2 = method;
                    break;
                }
            }
            Field field = method2.invoke(method1.invoke(appContext,new Object[]{}),new Object[]{packetName,ddd}).getClass().getDeclaredField(aaa.reverse().toString());
            Class<?> clazz = Class.forName(bbb.reverse().toString());
            Method method = clazz.getMethod(ccc.reverse().toString());
            return (int) method.invoke(((Object[]) field.get(method2.invoke(method1.invoke(appContext,new Object[]{}),new Object[]{packetName,ddd})))[0],new Object[]{});

            //写了这么多，其实就下面这几行代码
            //android.content.pm.Signature[] signatures = appContext.getPackageManager()
            //        .getPackageInfo(packetName, PackageManager.GET_SIGNATURES).signatures;
            //return signatures[0].hashCode();
        } catch (Exception e) {
            return -1;
        }
    }

    /**系统是否有满足此action的意图响应*/
    public static boolean isActionAvailable(String action) {
        PackageManager packageManager = appContext.getPackageManager();
        Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**根据应用包名判断是否装有该应用*/
    public static boolean isAppInstall( String packageName) {
        PackageManager packageManager = appContext.getPackageManager();
        try {
            PackageInfo pInfo = packageManager.getPackageInfo(packageName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            //判断是否获取到了对应的包名信息
            if (pInfo != null) {
                return true;
            }
        } catch (Exception e) {
            L.e(e);
        }
        return false;
    }

    /**
     * 安装APK
     * @param apkPath
     * @return 是否安装成功
     */
    public static boolean installApk(String apkPath) {
        if (StringUtils.isEmpty(apkPath)){
            return false;
        }
        File apkfile = new File(apkPath);
        if (!apkfile.exists()) {
            return false;
        }else if (apkfile.isDirectory()){
            FileUtils.deleteFile(apkfile);
            return false;
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
        try{
            appContext.startActivity(intent);
            return true;
        }catch (Exception e){
            L.e("install apk error",e);
            return false;
        }
    }

    /**
     * 启动app
     * @param packageName 目标app的包名
     */
    public static void startApp(@NonNull String packageName) {
        if(StringUtils.isEmpty(packageName)){
            return;
        }
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = appContext.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
        }
        if (packageinfo == null) {
            return;
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);
        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = appContext.getPackageManager().queryIntentActivities(resolveIntent, 0);
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packName, className);
            intent.setComponent(cn);
            appContext.startActivity(intent);
        }
    }
    /**获取指定包名下的所有类*/
    public List<String> getAllClassName(Context context,String packageName) {
        List<String >classNameList=new ArrayList<>();
        try {
            DexFile df = new DexFile(context.getPackageCodePath());//通过DexFile查找当前的APK中可执行文件
            Enumeration<String> enumeration = df.entries();//获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
            while (enumeration.hasMoreElements()) {//遍历
                String className = (String) enumeration.nextElement();
                if (className.contains(packageName)){
                    classNameList.add(className);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  classNameList;
    }
}
