package com.tdqc.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Base64相关
 * Created by chenyen on 2016/12/8.
 */
public class Base64Utils {

    /**
     * 将文件转成base64 字符串
     * @param path 文件路径
     * @return 失败则返回""
     */
    public static String fileToBase64(String path) {
        FileInputStream inputFile = null;
        try {
            File file = new File(path);
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (Exception e) {
            L.e(e);
            if(inputFile != null){
                try {
                    inputFile.close();
                } catch (Exception e1) {}
            }
        }
        return "";
    }

    /** 图片转成Base64流,不压缩 */
    public static String bitmapToBase64(@NonNull Bitmap photo) {
        if(photo == null){
            return "";
        }
        // 创建一个字节数组输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        // 将图片转换成base64字符串
        String imgStr = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        return imgStr;
    }

    /** Base64流转成Bitmap*/
    @Nullable
    public static Bitmap base64ToBitmap(@NonNull String bser64Stream) {
        if(StringUtils.isEmpty(bser64Stream)){
            return null;
        }
        byte[] bytes = Base64.decode(bser64Stream.substring(bser64Stream.indexOf(",")+1), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    /**
     * 把Bitmap转Byte
     */
    public static byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
