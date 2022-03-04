package com.tdqc.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.internal.Util;

/**
 * 图片相关工具类
 * Created by chenyen on 2017/3/21.
 */
public class PhotoUtils {

    private static final String TAG = "PhotoUtils";
    private static Context appContext;

    protected static void init(Context context) {
        appContext = context.getApplicationContext();
    }


    /**
     * 启动拍照页面
     *
     * @param aOrF Activity or Fragment
     */
    public static void startTakePhotoActivity(@NonNull Object aOrF, String photoPath, String photoName, int requestCode) {
        if (aOrF == null || StringUtils.isEmpty(photoPath) || StringUtils.isEmpty(photoName)) {
            return;
        }
        File photoDirFile = new File(photoPath);
        if (!photoDirFile.exists()) {
            photoDirFile.mkdirs();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(photoPath + photoName);
        Uri imageUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        try {
            if (aOrF instanceof Activity) {
                ((Activity) aOrF).startActivityForResult(intent, requestCode);
            }
            if (aOrF instanceof Fragment) {
                ((Fragment) aOrF).startActivityForResult(intent, requestCode);
            }
        } catch (Exception e) {
            L.e(TAG, e);
            To.s("启动相机应用错误");
        }
    }


    /**
     * 图片压缩
     */
    @Nullable
    public static Bitmap zipPhoto(Uri uri) {
        Bitmap bitmap = null;
        if (uri == null) {
            return null;
        }
        InputStream in = null;
        try {
            in = appContext.getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            int i = 0;
            bitmap = null;
            while (true) {
                if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) { /*分辨率压缩*/
                    in = appContext.getContentResolver().openInputStream(uri);
                    options.inSampleSize = (int) Math.pow(2.0D, i);
                    options.inJustDecodeBounds = false;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    bitmap = BitmapFactory.decodeStream(in, null, options);
                    in.close();
                    break;
                }
                i += 1;
            }
        } catch (Exception e) {
            L.e(TAG, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return bitmap;
    }

    /**
     * 图片压缩
     */
    @Nullable
    public static Bitmap zipPhoto(@NonNull String imagePath) {
        Bitmap bitmap = null;
        if (StringUtils.isEmpty(imagePath)) {
            return null;
        }
        BufferedInputStream in = null;
        int bitmapDegree = getBitmapDegree(imagePath);
        try {
            in = new BufferedInputStream(new FileInputStream(new File(imagePath)));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            int i = 0;
            bitmap = null;
            while (true) {
                if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) {
                    /*分辨率压缩*/
                    in = new BufferedInputStream(new FileInputStream(new File(imagePath)));
                    options.inSampleSize = (int) Math.pow(2.0D, i);
                    options.inJustDecodeBounds = false;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    bitmap = BitmapFactory.decodeStream(in, null, options);
                    if (bitmapDegree != 0) {
                        bitmap = rotateBitmapByDegree(bitmap, bitmapDegree);
                    }
                    in.close();
                    break;
                }
                i += 1;
            }
        } catch (Exception e) {
            L.e(TAG, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        if (bitmap != null) {
            return BitmapUtils.compressScale(bitmap);
        }
        return bitmap;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    private static int getBitmapDegree(String path) {
        int degree = 0;//被旋转的角度
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight() - 1, matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            BitmapUtils.MyRecycle(bm);
            // bm.recycle();
        }
        return returnBm;
    }

    /**
     * 把Bitmap存到本地
     *
     * @bitmap bitmap文件
     * @savePath 存储路径
     * @photoName 文件名称
     */
    @Nullable
    public static File savePhotoToSDCard(@NonNull Bitmap bitmap, @NonNull String savePath, @NonNull String photoName) {
        if (bitmap == null || StringUtils.isEmpty(savePath) || StringUtils.isEmpty(photoName)) {
            return null;
        }
        File photoFile = null;
        if (SDCardUtils.isSDCardAvailable()) {
            File dir = new File(savePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            photoFile = new File(savePath, photoName + (photoName.endsWith(".png") ? "" : ".png"));
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
                    fileOutputStream.flush();
                }
            } catch (Exception e) {
                L.e(TAG, e);
                try {
                    photoFile.delete();
                } catch (Exception ex) {
                }
            } finally {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                }
            }
        }
        return photoFile;
    }

    /**
     * 启动相册页面
     *
     * @param aOrF Activity or Fragment
     */
    public static void startPickPhotoActivity(@NonNull Object aOrF, int requestCode) {
        if (aOrF == null) {
            return;
        }
      Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setType("image/*");
        if (aOrF instanceof Activity) {
            ((Activity) aOrF).startActivityForResult(intent, requestCode);
        }
        if (aOrF instanceof Fragment) {
            ((Fragment) aOrF).startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 调起相机拍照，默认前置相机
     */
    public static Uri takePhoto2(Object aOrF, String pathname, int requestCode) {
        if (aOrF == null) {
            return null;
        }
        //创建File对象，用于存储拍照后的照片
        File outputImage = new File(appContext.getExternalCacheDir(), pathname);
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //启动相机程序
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(appContext, "com.len.scannerproject.fileprovider", outputImage);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        if (aOrF instanceof Activity) {
            ((Activity) aOrF).startActivityForResult(intent, requestCode);
        }
        if (aOrF instanceof Fragment) {
            ((Fragment) aOrF).startActivityForResult(intent, requestCode);
        }
        return imageUri;
    }

    /**
     * 调起相机拍照
     */
    public static Uri takePhoto(Object aOrF, String pathname, int requestCode) {
        if (aOrF == null) {
            return null;
        }
        //创建File对象，用于存储拍照后的照片
        File outputImage = new File(appContext.getExternalCacheDir(), pathname);
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //启动相机程序
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(appContext, "com.len.scannerproject.fileprovider", outputImage);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        if (aOrF instanceof Activity) {
            ((Activity) aOrF).startActivityForResult(intent, requestCode);
        }
        if (aOrF instanceof Fragment) {
            ((Fragment) aOrF).startActivityForResult(intent, requestCode);
        }
        return imageUri;
    }

    /**
     * 处理相册选中图片
     */
    @TargetApi(19)
    public static String handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        imagePath = getPath(uri);
        return imagePath;
    }

    public static String handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        return imagePath;
    }

    public static String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = appContext.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(appContext, uri)) {
            // ExternalStorageProvider
            //一些三方的文件浏览器会进入到这个方法中，例如ES
            //QQ文件管理器不在此列
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(appContext, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(appContext, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            if (isQQMediaDocument(uri)) {
                String path = uri.getPath();
                File fileDir = Environment.getExternalStorageDirectory();
                File file = new File(fileDir, path.substring("/QQBrowser".length(), path.length()));
                return file.exists() ? file.toString() : null;
            }
            return getDataColumn(appContext, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }


    /**
     * 使用第三方qq文件管理器打开
     *
     * @param uri
     * @return
     */
    public static boolean isQQMediaDocument(Uri uri) {
        return "com.tencent.mtt.fileprovider".equals(uri.getAuthority());
    }


    public static File getFile(Context mc, Uri muri) {
        //uri转换成file
        String[] arr = {MediaStore.Images.Media.DATA};
        Cursor cursor = mc.getContentResolver().query(muri, arr, null, null, null);
        int imgIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String imgPath = cursor.getString(imgIndex);
        File file = new File(imgPath);

        return file;
    }

    /**
     * 适配拍照获取uri问题
     */
    public static String getFilePath(Context context, Uri uri) {
        String picPath = "";
        if (Build.VERSION.SDK_INT >= 24) {
            picPath = getFilePathForN(context, uri);
        } else {
            picPath = getImageAbsolutePath(context, uri);
        }
        return picPath;
    }

    /**
     * 文件uri
     **/
    public static String getFilePathForNew(Context context, Uri uri) {
        try {
            Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                String name = (returnCursor.getString(nameIndex));
                File file = new File(context.getFilesDir(), name);

                if (uri == null)
                    return "";
                ContentResolver contentResolver = context.getContentResolver();
                if (contentResolver == null)
                    return "";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                ParcelFileDescriptor parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                if (fileDescriptor == null)
                    return "";
                FileInputStream inputStream = new FileInputStream(fileDescriptor);
                FileOutputStream outputStream = new FileOutputStream(file);
                int read = 0;
                int maxBufferSize = 1 * 1024 * 1024;
                int bytesAvailable = inputStream.available();
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                final byte[] buffers = new byte[bufferSize];
                while ((read = inputStream.read(buffers)) != -1) {
                    outputStream.write(buffers, 0, read);
                }
                returnCursor.close();
                inputStream.close();
                outputStream.close();
                parcelFileDescriptor.close();
                return file.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFilePathForN(Context context, Uri uri) {
        try {
            Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                String name = (returnCursor.getString(nameIndex));
                File file = new File(context.getFilesDir(), name);
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                FileOutputStream outputStream = new FileOutputStream(file);
                int read = 0;
                int maxBufferSize = 1 * 1024 * 1024;
                int bytesAvailable = inputStream.available();
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                final byte[] buffers = new byte[bufferSize];
                while ((read = inputStream.read(buffers)) != -1) {
                    outputStream.write(buffers, 0, read);
                }
                returnCursor.close();
                inputStream.close();
                outputStream.close();
                return file.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param
     * @param imageUri
     * @author yaoxing
     * @date
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Context context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            // 1.1 ExternalStorageProvider
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                // 1.2 DownloadsProvider
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                // 1.3 MediaProvider
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri)) {
                return imageUri.getLastPathSegment();
            }
            if (isQQMediaDocument(imageUri)) {
                String path = imageUri.getPath();
                File fileDir = Environment.getExternalStorageDirectory();
                File file = new File(fileDir, path.substring("/QQBrowser".length(), path.length()));
                return file.exists() ? file.toString() : null;
            }
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }
    /**
     * Android 10 以上适配
     * @param context
     * @param uri
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static String uriToFileApiQ(Context context, Uri uri) {
        File file = null;
        //android10以上转换
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(uri.getPath());
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //把文件复制到沙盒目录
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                try {
                    InputStream is = contentResolver.openInputStream(uri);
                    File cache = new File(context.getExternalCacheDir().getAbsolutePath(), Math.round((Math.random() + 1) * 1000) + displayName);
                    FileOutputStream fos = new FileOutputStream(cache);
                    FileUtils.copy(is, fos);
                    file = cache;
                    fos.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.getAbsolutePath();
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return  uriToFileApiQ(context,uri);
            }
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }



    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
