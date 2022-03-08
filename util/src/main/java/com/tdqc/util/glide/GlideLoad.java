package com.tdqc.util.glide;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tdqc.util.Base64Utils;
import com.tdqc.util.R;
import com.tdqc.util.ResourceUtils;
import com.tdqc.util.TimeUtils;
import com.tdqc.util.To;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


/**
 * 加载图片的工具类
 */
public class GlideLoad {

    private static DiskCacheStrategy diskCache = DiskCacheStrategy.ALL;//磁盘缓存
    private static boolean isSkipMemoryCache = false;//禁止内存缓存

    public static void init() {

    }

    /**
     * Glide特点
     * 使用简单
     * 可配置度高，自适应程度高
     * 支持常见图片格式 Jpg png gif webp
     * 支持多种数据源  网络、本地、资源、Assets 等
     * 高效缓存策略    支持Memory和Disk图片缓存 默认Bitmap格式采用RGB_565内存使用至少减少一半
     * 生命周期集成   根据Activity/Fragment生命周期自动管理请求
     * 高效处理Bitmap  使用Bitmap Pool使Bitmap复用，主动调用recycle回收需要回收的Bitmap，减小系统回收压力
     * 这里默认支持Context，Glide支持Context,Activity,Fragment，FragmentActivity
     * 策略解说：
     * all:缓存源资源和转换后的资源
     * none:不作任何磁盘缓存
     * source:缓存源资源
     * result：缓存转换后的资源
     */

    //设置缓存策略
    public static void loadImageViewDiskCache(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView);
    }


    /**
     * 会先加载缩略图
     */

    //设置缩略图支持
    public static void loadImageViewThumbnail(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).thumbnail(0.1f).into(mImageView);
    }

    /**
     * api提供了比如：centerCrop()、fitCenter()等
     */
    //设置动态转换
    public static void loadImageViewCrop(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).centerCrop().into(mImageView);
    }


    //清理磁盘缓存
    public static void GuideClearDiskCache(Context mContext) {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(mContext).clearDiskCache();
    }

    //清理内存缓存
    public static void GuideClearMemory(Context mContext) {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(mContext).clearMemory();
    }

    /**
     * 清除图片所有缓存
     */
    public void clearImageAllCache(Context context) {
        clearImageDiskCache(context);
        clearImageMemoryCache(context);
        String ImageExternalCatchDir=(context.getCacheDir() + "/"+ InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR);
        //String ImageExternalCatchDir=context.getExternalCacheDir()+ExternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR;
        deleteFolderFile(ImageExternalCatchDir, true);
    }
    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath filePath
     * @param deleteThisPath deleteThisPath
     */
    private void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 清除图片磁盘缓存
     */
    public void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                        // BusUtil.getBus().post(new GlideCacheClearSuccessEvent());
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 加载默认 图片
     *
     * @param imageView
     */
    public static void loadDefult(ImageView imageView) {
        load(R.drawable.icon_pic_load, imageView);
    }

    /**
     * 加载默认图片，为圆形
     *
     * @param imageView
     */
    public static void loadDefultCropCircle(ImageView imageView) {
        loadCropCircle(R.drawable.icon_pic_load, imageView);
    }



    /**
     * 加载banner图片
     */
    public static void loadBannerImage(String url, ImageView imageView) {
      //  RoundedCorners roundedCorners = new RoundedCorners(10);
      //  RequestOptions options = bitmapTransform(roundedCorners);
        Glide.with(imageView.getContext())
                .load(url).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(imageView.getDrawable())
                .error(R.drawable.icon_pic_load)
                .dontAnimate()
               // .apply(options)
               // .transform(new CenterCrop(), new RoundTransformation(imageView.getContext(), 5))
                /**设置圆角 CenterCrop需在这里设置*/
                .into(imageView);
    }
    /**
     * 加载图片，并处理成圆角
     * 自定义角度
     *
     * @param imgUrl
     * @param round
     * @param imageView
     */
    public static void loadRound(String imgUrl, int round, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(imgUrl).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(imageView.getDrawable())
                .error(R.drawable.icon_pic_load)
                .apply(bitmapTransform(new RoundTransformation(imageView.getContext(), round)))
                .into(imageView);
    }
    /**
     * 加载图片，并处理成圆角
     * 自定义角度
     *
     * @param imgUrl
     * @param
     * @param imageView
     */
    public static void loadPic(String imgUrl,  ImageView imageView) {
        RoundedCorners roundedCorners = new RoundedCorners(10);
        RequestOptions options = bitmapTransform(roundedCorners);
        Glide.with(imageView.getContext())
                .load(imgUrl).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(imageView.getDrawable())
                .dontAnimate()
               // .apply(options)
                .apply(new RequestOptions()
                        .transforms(new CenterCrop(), new RoundedCorners(7)
                        ))
                .error(R.drawable.icon_pic_load)
                //.transform(new CenterCrop(), new RoundTransformation(imageView.getContext(), 5))
                .into(imageView);


    }

    /**
     * 加载项目下的本地图片
     *
     * @param resId
     * @param imageView
     */
    public static void load(Integer resId, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(resId).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .into(imageView);
    }
    /**
     * 加载图片
     *
     * @param url
     *
     * @param imageView
     */
    public static void load(String url, ImageView imageView) {
        String imgUrl = url;
        Glide.with(imageView.getContext())
                .load(imgUrl).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(imageView.getDrawable())
                .dontAnimate()
                .error(R.drawable.icon_pic_load)
                .into(imageView);
    }
    /**
     * 加载图片，并处理成圆形
     *
     * @param url
     * @param imageView
     */

    public static void loadCropCircle(String url, ImageView imageView) {
        String imgUrl = url;
        Glide.with(imageView.getContext())
                .load(imgUrl).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(imageView.getDrawable())
                .error(R.drawable.user_img_default)
                .apply(bitmapTransform(new CropCircleTransformation(imageView.getContext())))
                .into(imageView);
    }

    /**
     * 加载图片，并处理成圆形
     *
     * @param url
     * @param imageView
     */
    public static void loadCompanyHead(String url, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(imageView.getDrawable())
                .error(R.drawable.user_img_default)
                .dontAnimate()
                .apply(bitmapTransform(new GlideCircleWithBorder(imageView.getContext(),1, ResourceUtils.getColor(R.color.white))))
               // .apply(bitmapTransform(new CropCircleTransformation(imageView.getContext())))
                .into(imageView);
    }
    /**
     * 加载图片，并处理成圆形
     *
     * @param url
     * @param imageView
     */
    public static void loadHeadWithWhite(String url, ImageView imageView) {

        Glide.with(imageView.getContext())
                .load(url).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(imageView.getDrawable())
                .dontAnimate()
               // .error(R.mipmap.icon_me_head)
                .apply(bitmapTransform(new GlideCircleWithBorder(imageView.getContext(),1, ResourceUtils.getColor(R.color.white))))
                //.transform(new GlideCircleWithBorder(imageView.getContext(), 3, ResourceUtils.getColor(R.color.white)))
                .into(imageView);


    }

    /**
     * 加载项目下的本地图片，并处理为圆形
     *
     * @param resId
     * @param imageView
     */
    public static void loadCropCircle(Integer resId, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(resId).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                //.error(R.mipmap.icon_me_head)
                .placeholder(imageView.getDrawable())
                .dontAnimate()
                .apply(bitmapTransform(new CropCircleTransformation(imageView.getContext())))
                .into(imageView);
    }

    /**
     * 加载项目下的本地图片，并处理为圆形
     *
     * @param imgUrl
     * @param imageView
     */
    public static void loadCropCircleHead(String imgUrl, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(imgUrl).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(imageView.getDrawable())
                .dontAnimate()
               // .error(R.mipmap.icon_me_head)
                .apply(bitmapTransform(new CropCircleTransformation(imageView.getContext())))
                .into(imageView);

    }

    /**
     * 加载图片，并处理成圆角
     * 默认角度
     *
     * @param imgUrl
     * @param imageView
     */
    public static void loadHead(String imgUrl, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(imgUrl).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(imageView.getDrawable())
                .error(R.drawable.icon_pic_load)
                .apply(bitmapTransform(new RoundTransformation(imageView.getContext())))
                .into(imageView);
    }

    /**
     * 加载头像图片，并处理成圆角
     * 默认角度
     *
     * @param imgUrl
     * @param imageView
     */
    public static void loadRound(String imgUrl, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(imgUrl).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(imageView.getDrawable())
                .error(R.drawable.user_img_default)
                .apply(bitmapTransform(new RoundTransformation(imageView.getContext())))
                .into(imageView);
    }

    /** 加载图片配合监听 */
    public static void loadWithListener(String imgUrl, ImageView imageView, DrawableImageViewTarget mRequestListener) {
        Glide.with(imageView.getContext()).load(imgUrl)
                .load(imgUrl).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .error(R.drawable.icon_pic_load)
                .into(mRequestListener);
    }


    public static void getBitmap(final Context context, String path) {
        Glide.with(context)
                .asBitmap()
                .load(path)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (resource!=null) {
                            byte[] bytes = Base64Utils.Bitmap2Bytes(resource);
                            savaBitmap(context, bytes);
                        }
                    }
                })
                ;

    }


    /**
     * 依据图片路径获取bitmap
     */
    public static Bitmap getBitmap2(final Context context, String path) {
        try {
            Bitmap bitmap = Glide.with(context)
                    .asBitmap() //必须
                    .load(path)
                    .centerCrop()
                    .into(500, 500).get();

            return bitmap;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    /** Receiver扫描更新图库 **/
    public static void refreshAlbumByReceiver(Context context, String path) {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + path)));
    }
    /**
     * 刷新相册
     * @param path
     */
    public static void refrshAlbumByMediaScannerConnection(Context context,String path) {
        String[] paths = {path};
        String[] mimeTypes = {"image/jpeg", "image/png","image/jpg"};
        MediaScannerConnection.scanFile(context, paths, mimeTypes,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });
    }
    // 保存图片到手机指定目录
    public static void savaBitmap(Context context, byte[] bytes) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileOutputStream fos = null;
            String filePath = Util.getDownloadPath(context);
            try {

                File imgDir = new File(filePath);
                if (!imgDir.exists()) {
                    imgDir.mkdirs();
                }
                String imgName = filePath + TimeUtils.getCurrentTimeStr(TimeUtils.TimeFormat.YYYY_MM_DD_HH_MM_SS) + ".jpg";
                File imgNamefile = new File(imgName);
                if (!imgNamefile.exists()) {
                    imgNamefile.createNewFile();
                }
                fos = new FileOutputStream(imgNamefile);
                fos.write(bytes);
                To.s("图片已保存到" + imgName);
              //  Toast.makeText(context, "图片已保存到" + imgName, Toast.LENGTH_SHORT).show();
                refreshAlbumByReceiver(context,imgName);
                //refrshAlbumByMediaScannerConnection(context,imgName);
              //  context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).setData(Uri.fromFile(imgNamefile)));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            To.s("请检查SD卡是否可用");
           // Toast.makeText(context, "请检查SD卡是否可用", Toast.LENGTH_SHORT).show();
        }
    }

}
