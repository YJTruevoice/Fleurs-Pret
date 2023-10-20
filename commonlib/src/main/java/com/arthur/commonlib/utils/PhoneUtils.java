package com.arthur.commonlib.utils;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.regex.Matcher;

/**
 * 我们这里定义的海外手机格式，必须以+国际区号开始
 * <p>
 * 手机格式分成两种，国内手机号码，必须为11位，且符合一定规则 海外手机，必须以+开头，后面为国际区号
 * <p>
 * <p>
 * Created by zhixian.yang@nowcoder.com on 2018/5/25.
 */
public class PhoneUtils {

    /**
     * 是否是中国移动
     */
    public static boolean isChinaYidong(String phone) {
        String regexMobile = "^1(3[4-9]|47|5[012789]|8[23478])\\d{8}$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regexMobile);
        Matcher m = p.matcher(phone.trim());
        return m.matches();
    }


    /**
     * 判断是否是海外手机 海外手机，必须以+开头，后面为国际区号
     */
    public static boolean isOverSeaPhone(String phone) {
        if (StringUtil.isBlank(phone)) {
            return false;
        }
        phone = phone.trim();
        if (!phone.startsWith("+")) {
            return false;
        }

        if (phone.startsWith("+86")) {
            return false;
        }

        if (isNativePhone(phone)) {
            return false;
        }

        phone = phone.substring(1);
        if (phone.length() < 3) {
            return false;
        }
        if (!NumberUtils.isDigits(phone)) {
            return false;
        }
        return true;
    }

    public static boolean isPhone(String s) {
        return isOverSeaPhone(s) || isNativePhone(s);
    }


    public static boolean isNativePhone(String phone) {
        if (StringUtil.isBlank(phone)) {
            return false;
        }
        phone = phone.trim();
        // 去掉国际区号
        if (phone.startsWith("+86")) {
            phone = phone.substring(3);
        } else if (phone.startsWith("86")) {
            phone = phone.substring(2);
        }

        java.lang.String regexMobile = "^1([3-9][0-9])\\d{8}$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regexMobile);
        Matcher m = p.matcher(phone.trim());
        return m.matches();
    }

    public static boolean isNativePhonePrefix(String prefix) {
        java.lang.String regexMobile = "^1([3-9][0-9])$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regexMobile);
        Matcher m = p.matcher(prefix.trim());
        return m.matches();
    }


}

