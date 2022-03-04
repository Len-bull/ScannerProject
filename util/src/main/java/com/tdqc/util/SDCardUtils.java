package com.tdqc.util;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * SD卡相关
 * Created by chenyen on 2016/12/9.
 */
public class SDCardUtils {

    /**判断SD卡可用时的最低可用空间标准 单位 M*/
    public static final int MIN_STORAGE_AVAILABLE_SIZE = 20;

    /**SD是否可用*/
    public static boolean isSDCardAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**判断SD卡是否可用并有足够空间可写入*/
    public static boolean isSDCardAvailableAndSize() {
        return isSDCardAvailable()
                && !isSDCardReadOnly()
                && getSDCardAvailableStorageSize() > (MIN_STORAGE_AVAILABLE_SIZE * 1024 * 1024);
    }

    /**SD是否只读*/
    public static boolean isSDCardReadOnly(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

    /**
     * 判断SD卡剩余空间
     * @return 单位字节
     * */
    public static long getSDCardAvailableStorageSize(){
        long availableSize;
        File path = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(path.getPath());
        long blockSize;
        long availableBlocks;
        if(Build.VERSION.SDK_INT > 17){
            blockSize = statFs.getBlockSizeLong();
            availableBlocks = statFs.getAvailableBlocksLong();
        }else{
            blockSize = statFs.getBlockSize();
            availableBlocks = statFs.getAvailableBlocks();
        }
        availableSize = availableBlocks * blockSize;
        return availableSize;
    }

}
