package com.arthur.commonlib.utils;

import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import androidx.annotation.Nullable;

public class StringUtil {
    // 验证是否是数字的校验
    private static final String numberPatternStr = "\\d+(.\\d+)?|-\\d+(.\\d+)?$";

    private static final Pattern emojiPattern = Pattern.compile("(?:[\uD83C\uDF00-\uD83D\uDDFF]" +
                    "|[\uD83E\uDD00-\uD83E\uDDFF]|[\uD83D\uDE00-\uD83D\uDE4F]" +
                    "|[\uD83D\uDE80-\uD83D\uDEFF]|[\u2600-\u26FF]\uFE0F?" +
                    "|[\u2700-\u27BF]\uFE0F?|\u24C2\uFE0F?|[\uD83C\uDDE6-\uD83C\uDDFF]{1,2}" +
                    "|[\uD83C\uDD70\uD83C\uDD71\uD83C\uDD7E\uD83C\uDD7F\uD83C\uDD8E\uD83C\uDD91-\uD83C\uDD9A]\uFE0F?" +
                    "|[\u0023\u002A\u0030-\u0039]\uFE0F?\u20E3|[\u2194-\u2199\u21A9-\u21AA]\uFE0F?" +
                    "|[\u2B05-\u2B07\u2B1B\u2B1C\u2B50\u2B55]\uFE0F?|[\u2934\u2935]\uFE0F?" +
                    "|[\u3030\u303D]\uFE0F?|[\u3297\u3299]\uFE0F?" +
                    "|[\uD83C\uDE01\uD83C\uDE02\uD83C\uDE1A\uD83C\uDE2F\uD83C\uDE32-\uD83C\uDE3A\uD83C\uDE50\uD83C\uDE51]\uFE0F?" +
                    "|[\u203C\u2049]\uFE0F?|[\u25AA\u25AB\u25B6\u25C0\u25FB-\u25FE]\uFE0F?|[\u00A9\u00AE]\uFE0F?" +
                    "|[\u2122\u2139]\uFE0F?|\uD83C\uDC04\uFE0F?|\uD83C\uDCCF\uFE0F?" +
                    "|[\u231A\u231B\u2328\u23CF\u23E9-\u23F3\u23F8-\u23FA]\uFE0F?)",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    /**
     * 查看str是否为空
     *
     * @param str 待判断的str
     * @return true:传入值为空,false:不为空
     */
    public static boolean isNull(String str) {
        if (str == null) {
            return true;
        } else if (str.length() == 0) {
            return true;
        } else {
            return str.equalsIgnoreCase("null");
        }
    }

    /**
     * 查看一个字符串是否包含在一个字符串数组中
     *
     * @param res 待查看的字符串
     * @param tar 是否包含这个字符串的数组
     * @return true:包含,false:不包含
     */
    public static boolean isContains(String res, String... tar) {
        if (tar == null || res == null) {
            return false;
        }
        for (String str : tar) {
            if (res.equals(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证是否是纯数字: 以下都是纯数字 0.0 0 0.00 100000 12.54
     *
     * @param validateNum 待验证的文本
     * @return false不是纯数字, true:是纯数字
     */
    public static boolean verifyTrueNumber(String validateNum) {
        if (isNull(validateNum)) {
            return false;
        }
        return Pattern.compile(numberPatternStr).matcher(validateNum).matches();
    }

    /**
     * 查看一个值类型的参数是否包含在一个值类型参数的数组中
     * =======两个参数的类型必须是一模一样的,如如果传int型,则tar也必须是int型,否则会判断错误======
     *
     * @param res 待查看的数字
     * @param tar 是否包含这个数字的数组
     * @return true:包含,false:不包含
     */
    public static boolean isObjContains(Object res, Object... tar) {
        if (tar == null || res == null) {
            return false;
        }
        for (Object item : tar) {
            if (res == item) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对buffer增加新的字符串和分隔符(如果这个串不为空的话)
     *
     * @param buffer    待添加的buffer
     * @param addStr    添加入的str
     * @param splitSign 添加完成后结尾的分隔符
     */
    public static void AppendNotEmptyStr(StringBuffer buffer, String addStr, String splitSign) {
        if (addStr != null && addStr.length() != 0 && !addStr.equals("")) {
            buffer.append(addStr + splitSign);
        }
    }

    /**
     * 去除结尾的某个字符串
     *
     * @param buffer
     * @param splitSign
     */
    public static void removeLastSplitSign(StringBuffer buffer, String splitSign) {
        if (buffer.length() != 0 && buffer.lastIndexOf(splitSign) == buffer.length() - splitSign.length()) {
            buffer = buffer.replace(buffer.length() - splitSign.length(), buffer.length(), "");
        }
    }

    /**
     * 去除开头的某个字符串
     *
     * @param buffer
     * @param splitSign
     */
    public static void removeFristSplitSign(StringBuffer buffer, String splitSign) {
        if (buffer.length() != 0 && buffer.indexOf(splitSign) == 0) {
            buffer = buffer.replace(0, splitSign.length(), "");
        }
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input)) {
            return true;
        }

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * @param rStr 源字符串
     * @param rFix 要查找替换的字符串
     * @param rRep 替换成的字符串
     * @return
     */
    public static String StrReplace(String rStr, String rFix, String rRep) {
        int l = 0;
        String gRtnStr = rStr;
        do {
            l = rStr.indexOf(rFix, l);
            if (l == -1) {
                break;
            }
            gRtnStr = rStr.substring(0, l) + rRep + rStr.substring(l + rFix.length());
            l += rRep.length();
            rStr = gRtnStr;
        } while (true);
        return gRtnStr;
    }

    /**
     * 截断
     *
     * @param str
     * @param maxLength
     * @return
     */
    public static String truncationStr(String str, int maxLength) {
        if (str == null) {
            return "";
        }
        if (str.length() <= maxLength) {
            return str;
        }

        return substring(str, 0, maxLength) + "...";
    }

    /**
     * 保留两位小数的金额格式
     */

    public static String moneyString(double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(value);

    }

    /**
     * 检查字符串是否含有表情true为包含，false则不包含
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(CharSequence source) {
        return emojiPattern.matcher(source).find();
    }

    /**
     * 16进制字符串转int
     */
    public static int hexStringToInt(String hexString) {
        if (hexString.length() < 2 || !hexString.startsWith("0x")) {
            return 0;
        }
        String newHexString = hexString.substring(2);
        int result = 0;
        char[] charArray = newHexString.toCharArray();
        for (char i = 0; i < charArray.length; i++) {
            int intNum = Integer.parseInt(String.valueOf(charArray[i]));
            result = result * 16 + intNum;
        }
        return result;
    }

    public static String check(String cs) {
        return (cs == null ? "" : cs);
    }

    public static String substring(final String str, int start, int end) {
        if (str == null) {
            return null;
        }

        // handle negatives
        if (end < 0) {
            end = str.length() + end; // remember end is negative
        }
        if (start < 0) {
            start = str.length() + start; // remember start is negative
        }

        // check length next
        if (end > str.length()) {
            end = str.length();
        }

        // if start is greater than end, return ""
        if (start > end) {
            return "";
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * 数字转义成带%字符串
     *
     * @param percent
     * @return
     */
    public static String getPercentString(float percent) {
        return String.format(Locale.US, "%d%%", (int) (percent * 100));
    }

    /**
     * 获取32位uuid
     *
     * @return
     */
    public static String get32UUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * <p>Checks if a CharSequence is whitespace, empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is null, empty or whitespace
     * @since 2.0
     * @since 3.0 Changed signature from isBlank(String) to isBlank(CharSequence)
     */
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 生成唯一号
     *
     * @return
     */
    public static String get36UUID() {
        UUID uuid = UUID.randomUUID();
        String uniqueId = uuid.toString();
        return uniqueId;
    }


    /**
     * counter ASCII character as one, otherwise two
     *
     * @param str
     * @return count
     */
    public static int counterChars(String str) {
        // return
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            int tmp = str.charAt(i);
            if (tmp > 0 && tmp < 127) {
                count += 1;
            } else {
                count += 2;
            }
        }
        return count;
    }

    /**
     * 去掉字符串前后空格
     *
     * @param str
     * @return
     */
    public static String parseEmpty(String str) {
        if (str == null || "null".equals(str.trim())) {
            str = "";
        }
        return str.trim();
    }


    /**
     * 计算字符串长度，汉字算2个长度
     *
     * @param str
     * @return
     */
    public static int strLength(String str) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        if (!isEmpty(str)) {
            for (int i = 0; i < str.length(); i++) {
                String temp = str.substring(i, i + 1);
                if (temp.matches(chinese)) {
                    valueLength += 2;
                } else {
                    valueLength += 1;
                }
            }
        }
        return valueLength;
    }

    /**
     * 把InputStream转换成String
     *
     * @param is
     * @return
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            if (sb.indexOf("\n") != -1 && sb.lastIndexOf("\n") == sb.length() - 1) {
                sb.delete(sb.lastIndexOf("\n"), sb.lastIndexOf("\n") + 1);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    /**
     * IP地址转行成数字
     *
     * @param ip
     * @return
     */
    public static long ip2int(String ip) {
        ip = ip.replace(".", ",");
        String[] items = ip.split(",");
        return Long.valueOf(items[0]) << 24 | Long.valueOf(items[1]) << 16 | Long.valueOf(items[2]) << 8
                | Long.valueOf(items[3]);
    }


    /**
     * unGzip操作
     *
     * @param param
     * @return
     * @throws IOException
     */
    public static String unGzip(byte[] param) throws IOException {
        ByteArrayOutputStream out = null;
        ByteArrayInputStream in = null;
        GZIPInputStream gzip = null;
        byte[] b = null;
        try {
            out = new ByteArrayOutputStream();
            in = new ByteArrayInputStream(param);
            gzip = new GZIPInputStream(in);
            byte[] byteArry = new byte[256];
            int n = -1;
            while ((n = gzip.read(byteArry)) != -1) {
                out.write(byteArry, 0, n);
            }
            b = out.toByteArray();
            out.flush();
        } finally {
            if (out != null) {
                out.close();
            }
            if (gzip != null) {
                gzip.close();
            }
            if (in != null) {
                in.close();
            }
        }
        return new String(b, StandardCharsets.UTF_8);
    }

    /**
     * 对string进行gzip操作
     *
     * @param str
     * @return
     * @throws IOException
     */
    public static byte[] gZip(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream byteOut = null;
        GZIPOutputStream gzipOut;
        byte[] outPut = null;
        try {
            byteOut = new ByteArrayOutputStream();

            gzipOut = new GZIPOutputStream(byteOut);
            gzipOut.write(str.getBytes(StandardCharsets.UTF_8));

            gzipOut.finish();
            gzipOut.close();

            outPut = byteOut.toByteArray();
            byteOut.flush();
            byteOut.close();
        } finally {
            if (byteOut != null) {
                byteOut.close();
            }
        }
        return outPut;
    }

    /**
     * 对string进行gzip操作
     *
     * @param str
     * @return
     * @throws IOException
     */
    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream byteOut = null;
        GZIPOutputStream gzipOut;
        try {
            byteOut = new ByteArrayOutputStream();

            gzipOut = new GZIPOutputStream(byteOut);
            gzipOut.write(str.getBytes(StandardCharsets.UTF_8));

            gzipOut.finish();
            gzipOut.close();

            byteOut.flush();
            byteOut.close();
        } finally {
            if (byteOut != null) {
                byteOut.close();
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return byteOut.toString(StandardCharsets.ISO_8859_1);
        } else {
            return byteOut.toString("ISO-8859-1");
        }
    }


    /**
     * 判断是否url
     *
     * @param url
     * @return 是否url
     */
    public static boolean isCorrectUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            return url.startsWith("http") || url.startsWith("https");
        }
        return false;
    }


    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isTrimEmpty(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断字符串是否为null或全为空白字符
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空白字符<br> {@code false}: 不为null且不全空白字符
     */
    public static boolean isSpace(String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) {
            return true;
        }
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        return a == null ? b == null : a.equalsIgnoreCase(b);
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) {
            return s;
        }
        return (char) (s.charAt(0) - 32) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) {
            return s;
        }
        return (char) (s.charAt(0) + 32) + s.substring(1);
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(String s) {
        int len = length(s);
        if (len <= 1) {
            return s;
        }
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 比较两个版本号 version1和 version2。
     *
     * @param version1
     * @param version2
     * @return 如果 version1> version2 返回 1，如果 version1< version2 返回 -1， 除此之外返回 0。
     */
    public static int compareVersion(String version1, String version2) {
        if (version1 == null) {
            version1 = "";
        }
        if (version2 == null) {
            version2 = "";
        }
        int n = version1.length(), m = version2.length();
        int i = 0, j = 0;
        while (i < n || j < m) {
            int x = 0;
            for (; i < n && version1.charAt(i) != '.'; ++i) {
                x = x * 10 + version1.charAt(i) - '0';
            }
            ++i; // 跳过点号

            int y = 0;
            for (; j < m && version2.charAt(j) != '.'; ++j) {
                y = y * 10 + version2.charAt(j) - '0';
            }
            ++j; // 跳过点号
            if (x != y) {
                return x > y ? 1 : -1;
            }
        }
        return 0;
    }


    /**
     * 删除所有的空白符：换行、空格、tab
     */
    public static String removeBlanks(String str) {
        String result = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            result = m.replaceAll("");
        }
        return result;
    }

    /**
     * 将字符串中的连续的多个换行缩减成一个换行，并去除首尾空白
     *
     * @param str 要处理的内容
     * @return 返回的结果
     */
    public static String replaceLineBlanks(String str) {
        String result = "";
        if (str != null) {
            Pattern p = Pattern.compile("(\r?\n(\\s*\r?\n)+)");
            Matcher m = p.matcher(str.trim());
            result = m.replaceAll("\r\n");
        }
        return result;
    }

    public static boolean isAllNotEmpty(String... args) {
        for (String s : args) {
            if (isEmpty(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 删除字符串首尾指定字符
     */
    public static String customTrim(@Nullable String str, char c) {
        if (isEmpty(str)) {
            return str;
        }

        char[] chars = str.toCharArray();
        int len = chars.length;
        int st = 0;
        while ((st < len) && (chars[st] == c)) {
            st++;
        }
        while ((st < len) && (chars[len - 1] == c)) {
            len--;
        }

        return (st > 0) || (len < chars.length) ? str.substring(st, len) : str;
    }

    public static SpannableStringBuilder getColorString(String fullStr, String colorStr, int color) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder(fullStr);
        if (fullStr.contains(colorStr)) {
            int index = fullStr.indexOf(colorStr);
            spannableString.setSpan(new ForegroundColorSpan(color), index, index + colorStr.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }
}
