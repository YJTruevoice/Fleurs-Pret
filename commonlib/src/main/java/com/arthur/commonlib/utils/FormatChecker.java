package com.arthur.commonlib.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author biaodi
 */
public class FormatChecker {

    //号码段，暂时还没用
    public static final String[] phoneStarter = {
            "139", "138", "137", "136", "135", "134", "147", "150", "151", "152", "157", "158", "159",
            "178", "182", "183", "184", "187", "188", "130", "131", "132", "155", "156", "185", "186",
            "145", "176", "133", "153", "177", "180", "181", "189"
    };
    public static Pattern emailPattern = Pattern
            .compile("^([a-zA-Z0-9_\\+\\.-])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})");

    /**
     * 检查电子邮箱地址师是否符合格式。
     *
     * @param email
     */
    public static boolean checkEmail(String email) {
        if (StringUtils.length(email) > 40 || StringUtils.length(email) <= 0) {
            return false;
        }

        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 检查手机号码是否符合格式。手机号码是11位数字
     *
     * @param phone
     */
    public static boolean checkPhone(String phone) {
        return NumberUtils.isDigits(phone) && StringUtils.length(phone) == 11;
    }

    public static boolean isPhone(String phone) {
        return PhoneUtils.isPhone(phone);
    }


    public static boolean isEmail(String email) {
        if (StringUtils.length(email) > 40 || StringUtils.length(email) <= 0) {
            return false;
        }

        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();

    }

    public static String findPhoneOrEmailAny(String content) {
        String phone = findPhone(content);
        if (StringUtils.isBlank(phone)) {
            phone = findEmail(content);
        }

        return phone;
    }

    public static String findEmail(String content) {
        if (StringUtils.isBlank(content)) {
            return "";
        }


        Pattern emailPatternFind = Pattern
                .compile("([a-zA-Z0-9_\\+\\.-])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})");

        Matcher matcher = emailPatternFind.matcher(content);
        if (matcher.find()) {
            return matcher.group();
        }

        return "";
    }


    public static String findPhone(String content) {
        if (StringUtils.isBlank(content)) {
            return "";
        }

        Pattern numberPattern = Pattern
                .compile("(\\+?[0-9]{5,})");

        Matcher matcher = numberPattern.matcher(content);

        while (matcher.find()) {
            // 找到可能电话字段
            //System.out.println(matcher.group(0));
            if (PhoneUtils.isPhone(matcher.group(0))) {
                return matcher.group(0);
            }

        }

        return "";

    }

}
