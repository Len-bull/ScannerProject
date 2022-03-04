package com.tdqc.util;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;

/**
 * 文件相关
 * Created by chenyen on 2016/12/8.
 */
public class FileUtils {

    private static final String TAG = "FileUtils";

    /** 创建文件路径 */
    public static File mkdir(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 判断文件大小
     *
     * @return 单位字节
     */
    public static long getFileSize(File file) {
        long totalSize = 0;
        try { /*防止权限问题导致崩溃*/
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    totalSize += f.length();
                }
            } else {
                totalSize += file.length();
            }
        } catch (Exception e) {
            L.e(e);
        }
        return totalSize;
    }

    /** 友好显示文件大小 */
    public static String formetFileSize(@NonNull File file) {
        if (file == null) {
            return "";
        }
        long fileSize = getFileSize(file);
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileSize == 0) {
            return wrongSize;
        }
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "KB";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /** 删除文件或是文件夹 */
    public static void deleteFile(File file) {
        try {
            if (file == null) {
                return;
            }
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                if (childFiles == null || childFiles.length == 0) {
                    file.delete();
                    return;
                }
                for (int i = 0; i < childFiles.length; i++) {
                    deleteFile(childFiles[i]);
                }
                file.delete();
            }
        } catch (Exception e) {
            L.e(TAG, e);
        }
    }

    /**
     * 写文件 utf-8编码
     *
     * @param append 是否在原来文件的基础上继续写。
     */
    public static void writeFileData(String filePath, String fileName, String message, boolean append) {
        FileOutputStream fout = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            fout = new FileOutputStream(new File(filePath + "/" + fileName), append);
            byte[] bytes = message.getBytes();
            fout.write(bytes);
        } catch (Exception e) {
            L.e(e);
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    L.e(e);
                }
            }
        }
    }

    /**
     * 复制文件到指定文件
     *
     * @param inputStream
     * @param toPath      复制到的文件
     * @return true 成功，false 失败
     */
    public static boolean copy(InputStream inputStream, String toPath) {
        boolean result = false;
        if (inputStream == null) {
            return result;
        }
        FileOutputStream out = null;
        try {
            FileUtils.mkdir(toPath.substring(0, toPath.lastIndexOf("/") + 1));
            File file = new File(toPath);
            out = new FileOutputStream(file);
            copy(inputStream, out);
            result = true;
        } catch (Throwable ex) {
            L.e("copy file error", ex);
        } finally {
            closeQuietly(inputStream);
            closeQuietly(out);
        }
        return result;
    }

    /** copy */
    public static void copy(InputStream in, OutputStream out) throws IOException {
        if (!(in instanceof BufferedInputStream)) {
            in = new BufferedInputStream(in);
        }
        if (!(out instanceof BufferedOutputStream)) {
            out = new BufferedOutputStream(out);
        }
        int len = 0;
        byte[] buffer = new byte[1024];
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable ignored) {
                L.e(ignored.getMessage(), ignored);
            }
        }
    }

    /**
     * 读取文本文件中的内容
     *
     * @param filePath 文件地址
     * @return
     */
    public static String readFile(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return "";
        }
        StringBuilder stringBuffer = new StringBuilder();
        File file = new File(filePath);
        if (file.isDirectory()) {
            L.e("FileUtils", "readFile:" + filePath + "\n The File doesn't not exist.");
        } else {
            InputStream instream = null;
            InputStreamReader inputreader = null;
            BufferedReader buffreader = null;
            try {
                instream = new FileInputStream(file);
                inputreader = new InputStreamReader(instream);
                buffreader = new BufferedReader(inputreader);
                String line;
                while ((line = buffreader.readLine()) != null) { // 分行读取
                    stringBuffer.append(line).append("\n");
                }
            } catch (Exception e) {
                L.e("FileUtils", e);
            } finally {
                if (instream != null) {
                    try {
                        instream.close();
                    } catch (Exception e) {
                    }
                }
                if (inputreader != null) {
                    try {
                        inputreader.close();
                    } catch (Exception e) {
                    }
                }
                if (buffreader != null) {
                    try {
                        buffreader.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        return stringBuffer.toString();
    }

    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();

    }





}
