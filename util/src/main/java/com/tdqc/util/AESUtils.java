package com.tdqc.util;

import android.text.TextUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by chenyen on 2017/11/29.
 */

public class AESUtils {

    private static final String ECB_PKCS5_PADDING = "AES/ECB/PKCS5Padding";//AES是加密方式 ECB是工作模式 PKCS5Padding是填充模式
    public static final String AES_KEY = "ZRHD666@!666ZRHD";

    /**
     * 加密
     *
     * @param key
     * @param cleartext
     * @return
     */
    public static String Encrypt(String key, String cleartext) {
        if (TextUtils.isEmpty(cleartext)) {
            return cleartext;
        }
        try {
            byte[] result = encryptbyte(key, cleartext.getBytes());
            return Base64Encoder.encode(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 加密
     */
    private static byte[] encryptbyte(String key, byte[] clear) throws Exception {
        byte[] raw = key.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(ECB_PKCS5_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    /**
     * 解密
     *
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String Decrypt(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(ECB_PKCS5_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64Decoder.decodeToBytes(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
}
