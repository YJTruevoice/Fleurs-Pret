package com.arthur.commonlib.utils;

import static android.content.Context.ACTIVITY_SERVICE;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProcessUtil {
    private static volatile String packageName = null;
    private static volatile String  processName = null;

    private static AtomicBoolean isMainProcess = null;

    /**
     * 综合方式获取进程名称，推荐使用
     */
    public static String getCurrentProcessName(Context context) {
        if (!TextUtils.isEmpty(processName)) {
            return processName;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Application.getProcessName();
        }

        // Using the same technique as Application.getProcessName() for older devices
        // Using reflection since ActivityThread is an internal API

        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");

            // Before API 18, the method was incorrectly named "currentPackageName", but it still returned the process name
            // See https://github.com/aosp-mirror/platform_frameworks_base/commit/b57a50bd16ce25db441da5c1b63d48721bb90687
            String methodName = Build.VERSION.SDK_INT >= 18 ? "currentProcessName" : "currentPackageName";

            Method getProcessName = activityThread.getDeclaredMethod(methodName);
            return (String) getProcessName.invoke(null);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return getCurrentProcessName2(context);
        }
    }

    /**
     * 通过比较 pid 的方式获取进程名称，需要调用 getRunningAppProcesse，不推荐使用
     */
    public static String getCurrentProcessName2(Context context) {
        if (!TextUtils.isEmpty(processName)) {
            return processName;
        }
        if (context == null) {
            return "";
        } else {
            int myPid = Process.myPid();
            if (myPid <= 0) {
                return "";
            } else {
                ActivityManager.RunningAppProcessInfo myProcess = null;
                ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

                try {
                    Iterator var3 = activityManager.getRunningAppProcesses().iterator();

                    while (var3.hasNext()) {
                        ActivityManager.RunningAppProcessInfo process = (ActivityManager.RunningAppProcessInfo) var3.next();
                        if (process.pid == myPid) {
                            myProcess = process;
                            break;
                        }
                    }
                } catch (Exception var13) {
                }

                if (myProcess != null) {
                    return myProcess.processName;
                } else {
                    byte[] b = new byte[128];
                    FileInputStream in = null;

                    try {
                        in = new FileInputStream("/proc/" + myPid + "/cmdline");
                        int len = in.read(b);
                        if (len <= 0) {
                            return "";
                        } else {
                            int i = 0;

                            while (true) {
                                if (i < len) {
                                    if (b[i] <= 128 && b[i] > 0) {
                                        ++i;
                                        continue;
                                    }

                                    len = i;
                                }

                                String var16 = new String(b, 0, len);
                                return var16;
                            }
                        }
                    } catch (Exception var11) {
                        var11.printStackTrace();
                        return "";
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 判断当前进程是否为主进程，检测异常失败返回false
     * @param context
     * @return
     */
    public static boolean isRunningInMainProcess(Context context, boolean defaultValue) {
        if (isMainProcess != null) {
            return isMainProcess.get();
        }
        if (packageName == null) {
            packageName = context.getPackageName();
        }
        String processName = getCurrentProcessName(context);

        if (TextUtils.isEmpty(processName) || TextUtils.isEmpty(packageName)) {
            return defaultValue; // unknown null fallback defaultValue;
        }

        isMainProcess = new AtomicBoolean(processName.equals(packageName));

        return isMainProcess.get();
    }
}
