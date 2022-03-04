package zxing.view;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.google.zxing.Result;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import zxing.decoding.CaptureActivityHandler;

/**
 * 扫码页面View需要实现的接口
 * Created by chenyen on 2017/8/30.
 */
public interface CaptureView {

    /**需要解析的码的种类*/
    @Retention(RetentionPolicy.SOURCE)
    @interface DecodeType {
        /**ISBN(条形码)*/
        int ISBN = 0;
        /**二维码*/
        int QRCODE = 1;
        /**全部类型*/
        int ALL = 2;
    }

    /**返回需要解析的码的种类*/
    @DecodeType int getDecodeType();

    /**绘制扫描器*/
    void drawViewfinder();

    /**处理扫描回调*/
    void handleDecode(Result result, Bitmap barcode);

    CaptureActivityHandler getHandler();

    /**获取扫描框范围*/
    Rect getScanRect();

}
