package com.tdqc.util;

import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

/**
 * CharSequence相关工具类，高亮字符，可点击字符等。
 * Created by chenyen on 2016/12/8.
 */
public class CharSequenceUtils {

    /**自定义ClickableSpan对象  可以去掉下划线*/
    public static abstract class WithoutLineClickableSpan extends ClickableSpan {
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);
        }
    }

    /**
     * 显示部分可点击文字
     * @param text      显示全部内容
     * @param clickSpan 点击监听回调
     * @param mTextView TextView
     * @param start     开始位
     * @param end       结束位
     * @param textColor 设置点击文字颜色
     */
    public static void setClickableText(String text, ClickableSpan clickSpan, TextView mTextView, int start, int end,@ColorRes int textColor) {
        if (text != null && clickSpan != null && mTextView != null) {
            try{
                SpannableString spanableInfo = new SpannableString(text);
                spanableInfo.setSpan(clickSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mTextView.setText(spanableInfo);
                mTextView.setLinkTextColor(ResourceUtils.getColor(textColor));
                mTextView.setMovementMethod(LinkMovementMethod.getInstance());
            }catch (Exception e){
                L.e(e);
            }
        }
    }

    /**
     * 显示部分可点击文字
     * @param text      显示全部内容
     * @param clickSpan 点击监听回调
     * @param mTextView TextView
     * @param desStr    可以点击的文字
     * @param textColor 设置点击文字颜色
     */
    public static void setClickableText(String text, ClickableSpan clickSpan, TextView mTextView, String desStr,@ColorRes int textColor) {
        if (!StringUtils.isEmpty(text) && !StringUtils.isEmpty(desStr)) {
            int start = text.indexOf(desStr);
            int end = start + desStr.length();
            setClickableText(text, clickSpan, mTextView, start, end, textColor);
        }
    }

    /**
     * 设置不同大小的字体
     * @param text 整个内容
     * @param desStr 需要设置特殊大小的内容
     * @param textSizeSp 特殊字体的大小
     * */
    public static void setDiffSizeText(@NonNull String text, @NonNull TextView mTextView, String desStr, int textSizeSp) {
        if (!StringUtils.isEmpty(text) && !StringUtils.isEmpty(desStr) && mTextView != null) {
            int start = text.indexOf(desStr);
            int end = start + desStr.length();
            Spannable spannable = new SpannableString(text);
            spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dp2px(textSizeSp)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTextView.setText(spannable);
        }
    }

    /**
     * 设置拨打电话  电话号高亮的 Span
     * @param tvDes      目标textView
     * @param description 电话号前描述文字
     */
    public static void setClickCallSpan(final Activity a, TextView tvDes, String description, final String phone) {
        String tips = description + phone + " ";
        ClickableSpan clickSpan = new WithoutLineClickableSpan() {
            @Override
            public void onClick(View widget) {
                /**拨打客服*/
                ContextUtils.call(a, phone);
            }
        };
        //设置点击监听
        setClickableText(tips, clickSpan, tvDes, phone, R.color.text_clickable);
    }

    /**
     * 高亮某段字体
     * @param fullText      一段字体全部内容
     * @param highLightText 需要高亮的某段字体
     * @param color         高亮的颜色
     * @return
     */
    public static CharSequence getColorText(@NonNull String fullText,@NonNull String highLightText, @ColorRes int color) {
        if(fullText != null && highLightText != null){
            String hintText = String.format(fullText, highLightText);
            int index = hintText.indexOf(highLightText);
            return index == -1 ? fullText:getColorText(hintText, color, index, index + highLightText.length());
        }
        return null;
    }

    /**
     * 返回一个高亮spannable
     * @param content 文本内容
     * @param color   高亮颜色
     * @param start   起始位置
     * @param end     结束位置
     * @return 高亮spannable
     */
    public static CharSequence getColorText(String content, @ColorRes int color, int start, int end) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        start = start >= 0 ? start : 0;
        end = end <= content.length() ? end : content.length();
        SpannableString spannable = new SpannableString(content);
        CharacterStyle span = new ForegroundColorSpan(ResourceUtils.getColor(color));
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 返回一个高亮spannable
     * @param content 文本内容
     * @param colors  高亮颜色集合
     * @param starts  起始位置集合
     * @param ends    结束位置集合
     * @return 高亮spannable
     */
    public static CharSequence getColorText(String content, int[] colors, int[] starts, int[] ends) {
        SpannableString spannable = new SpannableString(content);
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        for (int i = 0; i < colors.length; i++) {
            int color = colors[i];
            int start = starts[i];
            int end = ends[i];
            start = start >= 0 ? start : 0;
            end = end <= content.length() ? end : content.length();
            CharacterStyle span = new ForegroundColorSpan(ResourceUtils.getColor(color));
            spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

}
