package com.tdqc.util;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String
 * Created by chenyen on 2016/12/8.
 */
public class StringUtils {

    private static Context appContext;

    protected static void init(Context context) {
        appContext = context.getApplicationContext();
    }

    /** 判断字符串是否为空 */
    public static boolean isEmpty(String content) {
        return content == null || content.trim().equals("") || content.trim().equals("null");
    }

    /** 判断字符串是否为空 */
    public static boolean isEmpty(CharSequence content) {
        return content == null || isEmpty(content.toString());
    }

    /** 避免空指针异常 */
    public static boolean contains(String string, String cs) {
        if (isEmpty(string) || isEmpty(cs)) {
            return false;
        }
        return string.contains(cs);
    }

    /** 判断字符串是否包含中文 */
    public static boolean isContainChinese(@NonNull String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return Pattern.compile("[\u4e00-\u9fa5]").matcher(str).find();
    }

    /**
     * 邮箱格式
     *
     * @return 是邮箱返回true，其他情况返回false
     */
    public static boolean isEmail(@NonNull String emailAddr) {
        if (StringUtils.isEmpty(emailAddr)) {
            return false;
        }
        return emailAddr.matches("(\\S)+[@]{1}(\\S)+[.]{1}(\\w)+");
    }

    /** 判断是否为手机号码 */
    public static boolean isMobiPhoneNum(@NonNull String telNum) {
        if (StringUtils.isEmpty(telNum)) {
            return false;
        }
        return Pattern.compile("1[0-9]{10}", Pattern.CASE_INSENSITIVE).matcher(telNum).matches();
    }

    /**
     * 判断字符串是否在限定的长度之内
     *
     * @return 在限定内返回false，其他情况返回true，null返回true
     */
    public static boolean outOfLength(@NonNull String str, int startLength, int endLenght) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        return str.length() < startLength || str.length() > endLenght;
    }

    /** 身份证号码验证：15-18位 */
    public static boolean isIdentityCard(@NonNull String idNum) {
        if (!StringUtils.isEmpty(idNum)) {
            //定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）
            Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
            //通过Pattern获得Matcher
            Matcher idNumMatcher = idNumPattern.matcher(idNum);
            //判断用户输入是否为身份证号
            if (idNumMatcher.matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    /** 获取掩藏特定字符的邮箱 */
    public static String getMaskEmail(@NonNull String email) {
        try {
            if (StringUtils.contains(email, "@")) {
                int idx = email.indexOf("@");
                StringBuffer sb = new StringBuffer("");
                if (idx > 2) {
                    return sb.append(email.substring(0, idx - 2)).append("**").append(email.substring(idx)).toString();
                } else {
                    return email;
                }
            } else if (isMobiPhoneNum(email)) {
                StringBuffer sb = new StringBuffer("");
                return sb.append(email.subSequence(0, 3))
                        .append(email.substring(3, email.length() - 4).replaceAll("\\w", "*"))
                        .append(email.substring(email.length() - 4)).toString();
            } else {
                if (StringUtils.isEmpty(email) || email.length() == 1) {
                    return email;
                }
                StringBuffer sb = new StringBuffer("");
                return sb.append(email.substring(0, 1)).append(email.substring(1).replaceAll("\\w", "*")).toString();
            }
        } catch (Exception e) {
            return StringUtils.isEmpty(email) ? "" : email;
        }
    }

    /**
     * 获取掩盖银行卡号数字，最后4位数字除外
     *
     * @return 格式化失败返回空串
     */
    public static String getMaskBankAccount(String bankName, @NonNull String bankaccount) {
        StringBuffer sb = new StringBuffer(bankName == null ? "" : bankName);
        if (StringUtils.isEmpty(bankaccount) || bankaccount.length() < 4) {
            sb.append(bankaccount);
        } else {
            sb.append("\t尾号\t").append(bankaccount.substring(bankaccount.length() - 4));
        }
        return sb.toString();
    }

    /**
     * 掩盖身份证号，前后4位数字除外
     *
     * @return 格式化失败返回空串
     */
    public static String getMaskIdentityCard(@NonNull String identitycard) {
        StringBuffer result = new StringBuffer("");
        try {
            result.append(identitycard.substring(0, identitycard.length() - 4)).append("****");
        } catch (Exception e) {
        }
        return result.toString();
    }

    /**
     * 格式话手机号　中间四位*
     *
     * @param mobilePhone 　　手机号码
     * @return 格式化失败返回空串
     */
    public static String getMaskMobilePhoneNumber(@NonNull String mobilePhone) {
        String result = "";
        try {
            result = mobilePhone.substring(0, 3) +
                    mobilePhone.substring(3, mobilePhone.length() - 4).replaceAll("\\w", "*")
                    + mobilePhone.substring(mobilePhone.length() - 4);
        } catch (Exception e) {
        }
        return result;
    }

    /** 转换数字保留两位小数0.00 */
    public static String convertPrice(double price) {
        String result = "";
        DecimalFormat df = new DecimalFormat("#.00");
        if (price != 0) {
            String str = String.valueOf(price);
            if (str.startsWith("0")) {
                result = df.format(price);
                result = "0" + result;
            } else {
                result = df.format(price);
            }
        } else {
            result = "0.00";
        }
        return result;
    }


    /**
     * 验证密码至少包含大小写字母及数字中的两种
     * 是否包含
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }


    /**
     * 验证密码至少包含大小写字母及数字中的两种
     * 是否包含
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit2(String str) {

       // String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![~!@#$%^&*()_+|<>,.?/:;'\\[\\]\\{\\}]+$){6,20}$";(?![\w]+$)[a-zA-Z0-9]+$
        String regex ="^(?![0-9]+$)(?![a-zA-Z]+$)(?![~!@#$%^&*()_+|<>,.?/:;'\\{\\}\\[\\]])^[0-9A-Za-z~!@#$%^&*()_+|<>,.?/:;'\\{\\}\\[\\]]{6,20}$";
       // String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![\\W]+$)[a-zA-Z0-9\\W]{6,20}$";
        boolean isRight = str.matches(regex);
        return isRight;
    }
    /**
     * 验证密码至少包含大小写字母及数字中的两种 8位以上
     * 是否包含
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit3(String str) {

        String regex ="^(?![0-9]+$)(?![a-zA-Z]+$)(?![~!@#$%^&*()_+|<>,.?/:;'\\{\\}\\[\\]])^[0-9A-Za-z~!@#$%^&*()_+|<>,.?/:;'\\{\\}\\[\\]]{8,99}$";
        boolean isRight = str.matches(regex);
        return isRight;
    }

}
