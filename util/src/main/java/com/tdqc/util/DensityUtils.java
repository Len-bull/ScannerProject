package com.tdqc.util;

import android.content.res.Resources;

/**
 * 尺寸换算
 * Created by chenyen on 2016/12/8.
 */
public class DensityUtils {

    public static int sp2px(float value) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (value * fontScale + 0.5f);
    }

    public static int px2sp(float value) {
        final float scale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (value / scale + 0.5f);
    }

    public static int dp2px(float value) {
        final float scale = Resources.getSystem().getDisplayMetrics().densityDpi;
        return (int) (value * (scale / 160) + 0.5f);
    }

    public static int px2dp(float value) {
        final float scale = Resources.getSystem().getDisplayMetrics().densityDpi;
        return (int) ((value * 160) / scale + 0.5f);
    }

}
