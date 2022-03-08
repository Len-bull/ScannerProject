package com.tdqc.util.glide;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 一些工具方法
 *
 * @author DoneYang 2017/6/14
 */

public class Util {

    private static long lastClickTime;

    /**
     * 生成某个范围内的随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int getRandom(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

    /**
     * 判断对象是否为空
     *
     * @param o
     * @return
     */
    public static boolean isEmpty(Object o) {
        if (o == null)
            return true;
        if (o instanceof String)
            return (String.valueOf(o).trim().replace(" ", "").length() == 0);
        else if (o instanceof List)
            return ((List<?>) o).isEmpty();
        else if (o instanceof Map)
            return ((Map<?, ?>) o).isEmpty();
        else if (o instanceof String[])
            return ((String[]) o).length == 0;
        else
            return false;
    }

    /**
     * Get status bar height
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }
        return result;
    }

    /**
     * 格式转换
     *
     * @param pattern
     * @param milli
     * @return
     */
    public static String formatTime(String pattern, long milli) {
        int m = (int) (milli / (60 * 1000));
        int s = (int) ((milli / 1000) % 60);
        String mm = String.format("%02d", m);
        String ss = String.format("%02d", s);
        return pattern.replace("m", mm).replace("ss", ss);
    }

    /**
     * 防多次点击
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 根据 分辨率为1080*1920  的密度进行转换
     *
     * @param context
     * @param val
     * @return
     */
    public static float deviceAllDPI(Context context, float val) {
        final float density = context.getResources().getDisplayMetrics().density;
        return val * (density / 3);
    }


    /**
     * 透明度
     *
     * @param context
     * @param alpha
     */
    public static void setAlpha(Context context, float alpha) {
        Window window = ((Activity) context).getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.alpha = alpha;
        window.setAttributes(layoutParams);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public static void setEnable(EditText editText, final View view) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    view.setEnabled(false);
                } else {
                    view.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int lenth = editable.length();
                if (lenth == 1 && editable.toString().equals("0")) {
                    editable.clear();
                }
            }
        });
    }

    public static void setEnabled(EditText editText, final View view) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    view.setEnabled(false);
                } else {
                    view.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


    /**
     * 显示/隐藏
     *
     * @param editText
     * @param view
     */
    public static void setVisibility(final EditText editText, final View view) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    view.setVisibility(View.GONE);
                } else {
                    if (charSequence.toString().equals(" ")) {
                        editText.setText("");
                    }
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 进度百分比
     */
    public static float getPercentage(float base, float coefficient, long currentSize, long totalSize) {
        return base + (float) currentSize / (float) totalSize * coefficient;
    }

    //    /**
    //     * 获取设备ID
    //     *
    //     * @param context
    //     * @return
    //     */
    //    public static String getDeviceId(Context context) {
    //        DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
    //        String id = deviceUuidFactory.getDeviceUuidStr();
    //        if (!Util.isEmpty(id)) {
    //            return id;
    //        }
    //        return "";
    //    }

    /**
     * 判断是否是debug模式
     *
     * @param context
     * @return
     */
    public static boolean isDebug(Context context) {
        ApplicationInfo info = context.getApplicationInfo();
        return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    public static void setSpanString(Context context, String text, int style, TextView textView
            , int start, int end) {
        SpannableString spanstr_tv_test = new SpannableString(text);
        spanstr_tv_test.setSpan(new TextAppearanceSpan(context, style), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //        spanstr_tv_test.setSpan(new TextAppearanceSpan(context, style), 4, 5,
        //                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //
        //        spanstr_tv_test.setSpan(new ForegroundColorSpan(Color.RED), 0, 3, 0);
        //        spanstr_tv_test.setSpan(new ForegroundColorSpan(Color.BLUE), 3, 4, 0);
        textView.setText(spanstr_tv_test);
    }

    /**
     * 手机验证
     *
     * @param mobile
     * @return
     */
    public static boolean isMobilePhone(String mobile) {
        //        Pattern pattern = Pattern
        //                .compile("[1][345789]\\d{9}");
        Pattern pattern = Pattern
                .compile("^1(3[0-9]|4[579]|5[0-35-9]|6[0-9]|7[0135-8]|8[0-9]|9[0-9])\\d{8}$");
        Matcher mc = pattern.matcher(mobile);
        return mc.matches();
    }

    /**
     * 图片-->bitmap
     */

    public static Bitmap getDrawBitmap(Context context, int intdrawable) {
        InputStream inputStream = context.getResources().openRawResource(intdrawable);
        BitmapDrawable drawable = new BitmapDrawable(inputStream);
        Bitmap bitmap = drawable.getBitmap();
        return bitmap;
    }

    /**
     * 将数据保留两位小数
     */
    public static double getTwoDecimal(double num) {
        DecimalFormat dFormat = new DecimalFormat("#.00");
        String yearString = dFormat.format(num);
        Double temp = Double.valueOf(yearString);
        return temp;
    }

    /**
     * 两位小数
     */
    public static String getDouble2(double val) {
        try {
            return String.format("%.2f", val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 两位小数
     */
    public static String getDouble1(double val) {
        try {
            return String.format("%.1f", val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 两位小数
     */
    public static String getDouble0(double val) {
        try {
            return String.format("%.0f", val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 4位小数
     */
    public static String getDouble4(double val) {
        try {
            return String.format("%.4f", val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 4位小数
     */
    public static String getDouble(double val) {
        try {
            return String.format("%f", val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 4位小数
     */
    public static String getDouble4(String val) {
        if (TextUtils.isEmpty(val)) {
            return "";
        }
        try {
            return String.format("%.4f", Double.parseDouble(val));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 两位小数
     */
    public static String getDouble2(String val) {
        if (TextUtils.isEmpty(val)) {
            return "";
        }
        try {
            return String.format("%.2f", Double.parseDouble(val));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保留两位少数，为零返回空(处理金额)
     */
    public static String getDouble2NoPrice(double val) {

        try {
            if (val == 0) {
                return "";
            } else {
                return "¥" + String.format("%.2f", val);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保留两位少数，为零返回空(处理金额)
     */
    public static String getDouble2NoPrice(String val) {
        if (TextUtils.isEmpty(val)) {
            return "";
        }
        try {
            Double parseDouble = Double.parseDouble(val);
            if (parseDouble == 0) {
                return "";
            } else {
                return "¥" + String.format("%.2f", Double.parseDouble(val));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保留两位少数，为零返回空(处理小米)
     */
    public static String getDouble2NoMi(String money, String val) {
        if (TextUtils.isEmpty(val)) {
            return "";
        }
        try {
            double parseDouble = Double.parseDouble(val);
            if (parseDouble == 0) {
                if (TextUtils.isEmpty(money)) {
                    return "¥ 0.00";
                }
                return "";
            } else {
                if (TextUtils.isEmpty(money)) {
                    return String.format("%.2f", Double.parseDouble(val)) + "小米";
                } else {
                    return "+" + String.format("%.2f", Double.parseDouble(val)) + "小米";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 保留两位少数，为零返回空(处理小米)
     */
    public static String getDouble2NoMi(String money, double val) {
        try {
            if (val == 0) {
                if (TextUtils.isEmpty(money)) {
                    return "¥ 0.00";
                }
                return "";
            } else {
                if (TextUtils.isEmpty(money)) {
                    return String.format("%.2f", val) + "小米";
                } else {
                    return "+" + String.format("%.2f", val) + "小米";
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDownloadPath(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { //android 11
            return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator+ "Farmers"+File.separator;
        }else {
            return Environment.getExternalStorageDirectory() +  File.separator+"Farmers"+ File.separator;
        }
    }

    /**判断是否app进去后台*/
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    System.out.print(String.format("Foreground App:", appProcess.processName));
                    return false;
                }else{
                    System.out.print("Background App:"+appProcess.processName);
                    return true;
                }
            }
        }
        return false;
    }

}


