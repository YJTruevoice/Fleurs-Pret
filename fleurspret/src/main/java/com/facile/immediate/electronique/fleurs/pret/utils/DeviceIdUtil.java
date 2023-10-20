package com.facile.immediate.electronique.fleurs.pret.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arthur.commonlib.ability.AppKit;
import com.arthur.commonlib.ability.Logger;
import com.arthur.commonlib.utils.SPUtils;
import com.arthur.commonlib.utils.StringUtil;
import com.facile.immediate.electronique.fleurs.pret.AppConstants;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class DeviceIdUtil {

    // 异常值set，如果某个id属于异常值，应当再取别的
    private static final Set<String> outlierSets = new HashSet<>();

    static {
        outlierSets.add("unknown");
        outlierSets.add("00000000-0000-0000-0000-000000000000");
        outlierSets.add("0000000000000000");
        outlierSets.add("");
        outlierSets.add("00000000000000000000000000000000");
        outlierSets.add("null");
    }

    //初次启动App时，获取oaid较慢，为了降低误差
    //每次调用该方法时都重新获取clientId
    public static String getDeviceId() {
        if (!SPUtils.INSTANCE.getBoolean(AppConstants.KEY_POLICY_ACCEPTED)) {
            Logger.INSTANCE.logE("try get deviceid before policy agreed");
            return getNowcoderId();
        }

        // 2. 取serial
        String serial = getSERIAL();

        if (!outlierSets.contains(serial)) {
            return serial;
        }

        // 3. 取androidId
        String androidId = getAndroidId(AppKit.context);
        if (!outlierSets.contains(androidId)) {
            return androidId;
        }

        // 4. 以上四步，足以保障clientId不为空，以防万一，用nid兜底
        return getNowcoderId();
    }

    public static String getNowcoderId() {
        String deviceId = SPUtils.INSTANCE.getString(AppConstants.KEY_DEVICE_ID);
        if (StringUtil.isEmpty(deviceId)) {
            deviceId = System.currentTimeMillis() + "" + (new Random()).nextInt();
            SPUtils.INSTANCE.putData(AppConstants.KEY_DEVICE_ID, deviceId);
        }
        return deviceId;
    }

    /**
     * 获得设备的AndroidId
     * Android8以前，刷机、root、恢复出厂设置会改变
     * Android10以后，不同应用获取Android ID不同，对投放可能有影响
     */
    public static String getAndroidId(Context context) {
        String id = getCachedDeviceIdentityByType(DeviceIdentityType.ANDROIDID);
        try {
            if (StringUtil.isEmpty(id)) {
                id = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                updateCachedDeviceIdentityByType(id, DeviceIdentityType.ANDROIDID);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return StringUtil.check(id);
    }

    /**
     * 获得设备默认IMEI
     * 需要READ_PHONE_STATE权限
     * Android6以后需要动态申请
     * Android10以后全面禁止
     */
    public static String getIMEI(Context context) {
        String id = "";
        try {
            TelephonyManager tm = (TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE);
            id = tm.getDeviceId();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (id == null) {
            id = "";
        }
        return id;
    }

    /**
     * 获得设备序列号（如：WTK7N16923005607）, 个别设备无法获取
     * Android8以后android.os.Build.SERIAL返回unknown，
     * Build.getSerial() 可以，但同时需要申请READ_PHONE_STATE权限
     * Android10以后禁取
     */
    public static String getSERIAL() {
        String id = getCachedDeviceIdentityByType(DeviceIdentityType.SERIAL);
        try {
            if (StringUtil.isEmpty(id) && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                id = Build.SERIAL;
                updateCachedDeviceIdentityByType(id, DeviceIdentityType.SERIAL);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return StringUtil.check(id);
    }

    /**
     * 获得设备硬件uuid
     * 使用硬件信息，计算出一个随机数
     *
     * @return 设备硬件uuid
     */
    private static String getDeviceUUID() {
        try {
            String dev = "3883756" +
                    Build.BOARD.length() % 10 +
                    Build.BRAND.length() % 10 +
                    Build.DEVICE.length() % 10 +
                    Build.HARDWARE.length() % 10 +
                    Build.ID.length() % 10 +
                    Build.MODEL.length() % 10 +
                    Build.PRODUCT.length() % 10 +
                    Build.SERIAL.length() % 10;
            return new UUID(dev.hashCode(),
                    Build.SERIAL.hashCode()).toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * 获取mac地址
     * @return
     */
    public static String getMacAddress() {
        String mac = "";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mac = getMacAddressBelow7();
        } else  {
            mac = getMacFromHardware();
        }
        return mac;
    }

    /**
     * 6.0以上 7.0以下
     * @return
     */
    private static String getMacAddressBelow7() {
        String macSerial = "";
        String str = "";

        try {
            Process pp = Runtime.getRuntime().exec("cat/sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            while (null != str) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();//去空格
                    break;
                }
            }
        } catch (Exception ex) {
            // 赋予默认值
            ex.printStackTrace();
        }

        return macSerial;
    }

    /**
     * Android 7.0之后获取Mac地址
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET"></uses-permission>
     * @return
     */
    private static String getMacFromHardware() {
        try {
            ArrayList<NetworkInterface> all =
                    Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0"))
                    continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) return "";
                StringBuilder res1 = new StringBuilder();
                for (Byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (!TextUtils.isEmpty(res1)) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 取SHA1
     *
     * @param data 数据
     * @return 对应的hash值
     */
    private static byte[] getHashByString(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.reset();
            messageDigest.update(data.getBytes(StandardCharsets.UTF_8));
            return messageDigest.digest();
        } catch (Exception e) {
            return "".getBytes();
        }
    }

    /**
     * 转16进制字符串
     *
     * @param data 数据
     * @return 16进制字符串
     */
    private static String bytesToHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        String stmp;
        for (int n = 0; n < data.length; n++) {
            stmp = (Integer.toHexString(data[n] & 0xFF));
            if (stmp.length() == 1) {
                sb.append("0");
            }
            sb.append(stmp);
        }
        return sb.toString().toUpperCase(Locale.CHINA);
    }

    /**
     * 更新设备标识缓存
     * @param val
     * @param type
     */
    private static void updateCachedDeviceIdentityByType(@Nullable String val, DeviceIdentityType type) {
        String cacheKey = String.format(CACHE_KEY_DEVICE_IDENTITY, type.name);
        if (StringUtil.isEmpty(val)) {
            SPUtils.INSTANCE.remove(cacheKey, "");
        } else {
            SPUtils.INSTANCE.putData(cacheKey, val);
        }
    }

    /**
     * 根据类型从缓存中获取设备id
     * @param type
     * @return
     */
    @NonNull
    private static String getCachedDeviceIdentityByType(DeviceIdentityType type) {
        return SPUtils.INSTANCE.getString(String.format(CACHE_KEY_DEVICE_IDENTITY, type.name));
    }

    /**
     * 设备唯一标识缓存
     */
    public static String CACHE_KEY_DEVICE_IDENTITY = "cache_key_device_identity_%s";

    private enum DeviceIdentityType {
        SERIAL("serial"),
        OAID("oaid"),
        ANDROIDID("androidId");
        private String name;
        DeviceIdentityType(String name) {
            this.name = name;
        }
    }
}