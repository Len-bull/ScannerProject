package com.tdqc.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 资源获取相关方法
 * Created by chenyen on 2016/12/8.
 */
public class ResourceUtils {

    private static Context appContext;

    protected static void init(Context context){
        appContext = context.getApplicationContext();
    }

    /** 获取颜色 */
    public static int getColor(int colorRes) {
        return ContextCompat.getColor(appContext, colorRes);
    }

    /** 获取Drawable */
    public static Drawable getDrawable(int drawableId) {
        return ContextCompat.getDrawable(appContext, drawableId);
    }

    /**获取String资源*/
    public static String getString(int stringId) {
        return appContext.getString(stringId);
    }

    /**
     * 根据资源文件名获取资源id
     * @param context
     * @param resourceType
     * @param name
     * @return
     */
    //public static int getResourceIdByName(Context context, String resourceType, String name) {
    //    if (TextUtils.isEmpty(resourceType)) {
    //        resourceType = "mipmap";
    //    }
    //    return context.getResources().getIdentifier(name, resourceType, context.getPackageName());
    //}

    /**
     * 从Assets中读取图片
     * @param fileName
     * @return
     */
    //public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
    //    Bitmap image = null;
    //    InputStream is = null;
    //    android.content.res.AssetManager am = context.getResources().getAssets();
    //    try {
    //        is = am.open(fileName);
    //        image = BitmapFactory.decodeStream(is);
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    } finally {
    //        if (is != null) {
    //            try {
    //                is.close();
    //            } catch (IOException e) {
    //                e.printStackTrace();
    //            }
    //        }
    //    }
    //    return image;
    //}

    /**从Assets获取String*/
    public static String getStringFromAssetsFile(String fileName){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = appContext.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {}
        return stringBuilder.toString();
    }

}
