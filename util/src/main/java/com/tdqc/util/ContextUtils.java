package com.tdqc.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Context相关
 * Created by chenyen on 2016/12/8.
 */
public class ContextUtils {

    private static Context appContext;
    private static final String TAG = "ContextUtils";

    protected static void init(Context context) {
        appContext = context.getApplicationContext();
    }

    /** 调用发送短信页面 */
    public static void sendSMS(@NonNull Activity a, @NonNull String number, @NonNull String message) {
        if (a != null && !StringUtils.isEmpty(number) && !StringUtils.isEmpty(message)) {
            Uri uri = Uri.parse("smsto:" + (StringUtils.isEmpty(number) ? "" : number));
            Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
            sendIntent.putExtra("sms_body", message);
            try {
                a.startActivity(sendIntent);
            } catch (Exception e) {
                L.e(TAG, e);
                To.s("启动短信应用错误");
            }
        }
    }

    /** 复制文本 */
    public static void copyText(String text) {
        ClipboardManager cmb = (ClipboardManager)
                appContext.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(text);
    }

    /**
     * 拨打电话,跳入电话编辑页
     *
     * @param number 拨打的电话号码
     */
    public static void call(@NonNull Activity a, @NonNull String number) {
        if (a != null && !StringUtils.isEmpty(number)) {
            Uri uri = Uri.parse("tel:" + number);
            Intent callIntent = new Intent(Intent.ACTION_DIAL, uri);
            try {
                a.startActivity(callIntent);
            } catch (Exception e) {
                L.e(TAG, e);
                To.s("启动拨号应用错误");
            }
        }
    }

    /** 尝试使用浏览器进行下载 */
    public static void switchToBrowserDownload(String fileUrl) {
        if (!StringUtils.isEmpty(fileUrl)) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(fileUrl));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                appContext.startActivity(intent);
            } catch (Exception e) {
                To.s("下载错误 65933");
            }
        }
    }

    /** 复印文字内容到系统剪切板 */
    public static void copyTextToClipboard(String text) {
        if (!StringUtils.isEmpty(text)) {
            ClipboardManager clip = (ClipboardManager) appContext.getSystemService(Context.CLIPBOARD_SERVICE);
            clip.setPrimaryClip(ClipData.newPlainText(null, text));
        }
    }

    /**
     * 从字符串中截取连续6位数字组合 ([0-9])截取六位数字 进行前后断言不能出现数字 用于从短信中获取动态密码
     *
     * @param content 短信内容
     * @return 截取得到的6位动态密码
     */
    public static String getDynamicPwd(String content) {
        // 此正则表达式验证六位数字的短信验证码
        String regEx = "(?<![0-9])([0-9]{" + 6 + "})(?![0-9])";
        Pattern pattern = Pattern.compile(regEx);
        //Pattern pattern = Pattern.compile("(?<![0-9])([0-9])(?![0-9])");
        Matcher matcher = pattern.matcher(content);
        String dynamicPwd = "";
        while (matcher.find()) {
            dynamicPwd = matcher.group();
        }
        return dynamicPwd;
    }

}
