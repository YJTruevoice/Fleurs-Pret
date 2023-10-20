package com.arthur.commonlib.ecryption;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * MD5加密工具类
 */
public class MD5Utils {

    /**
     * 字符串MD5加密,默认大写
     *
     * @param value 字符串
     * @return MD5字符串
     */
    public static String encodeMD5(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(value.getBytes());
            byte[] messageDigest = digest.digest();
            String result = toHexString(messageDigest);
            if (result == null) {
                return "";
            } else {
                return result.toUpperCase(Locale.getDefault());
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 字符串MD5加密
     *
     * @param value 字符串
     * @return MD5字符串
     */
    public static byte[] encode(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(value.getBytes());
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将字符流转行成十六进制字符串
     *
     * @param keyData 字符流
     * @return 十六进制字符串
     */
    public static String toHexString(byte[] keyData) {
        if (keyData == null) {
            return null;
        }
        int expectedStringLen = keyData.length * 2;
        StringBuilder sb = new StringBuilder(expectedStringLen);
        for (byte keyDatum : keyData) {
            String hexStr = Integer.toString(keyDatum & 0x00FF, 16);
            if (hexStr.length() == 1) {
                hexStr = "0" + hexStr;
            }
            sb.append(hexStr);
        }
        return sb.toString();
    }


    /**
     * 获取文件流MD5加密后的字符串
     *
     * @param filePath 文件路径
     * @return MD5串
     */
    public static String encodeFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        MessageDigest digest = null;
        byte buffer[] = new byte[1024];
        int len;
        FileInputStream in = null;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(filePath);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }


    /**
     * 获取文件流MD5加密后的字符串
     *
     * @param file 文件
     * @return MD5串
     */
    public static String encodeFile(File file) {
        try {
            InputStream inputStream = new FileInputStream(file);

            // 获取一个文件的特征信息，签名信息。
            // md5
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            byte[] result = digest.digest();
            StringBuffer sb = new StringBuffer();
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    sb.append("0");
                }
                sb.append(str);
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取单个字符串的MD5值
     *
     * @param text
     * @return
     */
    public static String getStringMD5(String text) {
        byte[] hash = null;
        try {
            hash = MessageDigest.getInstance("MD5").digest(text.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }

        if (hash == null) {
            return "";
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString().toLowerCase();
    }
}
