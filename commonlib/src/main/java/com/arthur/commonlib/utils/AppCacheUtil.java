package com.arthur.commonlib.utils;

import android.content.Context;

import com.arthur.commonlib.file.FileUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * app 缓存
 */
public class AppCacheUtil {

    public static final String ROOT_DIR = "nowcoder";
    public static final String IMAGE_DIR = ROOT_DIR + "/images";
    public static final String CACHE_DIR_PIC_CHAT = IMAGE_DIR + "/chat";

    /**
     * 计算缓存
     */
    public static String getCacheSize(Context context) {
        File imsdkImg = getClassGroupImageDir(context);
        File cacheDir = context.getExternalCacheDir();
        File fileDir = context.getExternalFilesDir(null);
        long size = FileUtil.getFolderSize(imsdkImg) + FileUtil.getFolderSize(cacheDir) + FileUtil.getFolderSize(fileDir);
        return FileUtil.formatFileSize(context, size, FileUtil.SizeUnit.MB);
    }


    /**
     * 清除缓存
     */
    public static void clearCache(Context context) {
        File imsdkImg = getClassGroupImageDir(context);
        File cacheDir = context.getExternalCacheDir();
        File fileDir = context.getExternalFilesDir(null);
        if (imsdkImg != null) {
            FileUtil.deleteDir(imsdkImg.getAbsolutePath(), false);
        }
        if (cacheDir != null) {
            FileUtil.deleteDir(cacheDir.getAbsolutePath(), false);
        }
        if (fileDir != null) {
            FileUtil.deleteDir(fileDir.getAbsolutePath(), false);
        }
    }

    /**
     * 保存 String数据 到 缓存中
     *
     * @param context context
     * @param key     保存的key
     * @param value   保存的String数据
     */
    public static void put(Context context, String key, String value) {
        File file = new File(context.getCacheDir() + "/" + key);
        BufferedWriter out = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            out = new BufferedWriter(new FileWriter(file), 1024);
            out.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取 String数据
     *
     * @param context context
     * @param key     保存的key
     * @return String 保存的String数据
     */
    public static String getString(Context context, String key) {
        File file = new File(context.getCacheDir() + "/" + key);
        if (!file.exists()) {
            return null;
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            StringBuilder readString = new StringBuilder();
            String currentLine;
            while ((currentLine = in.readLine()) != null) {
                readString.append(currentLine);
            }
            return readString.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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

    public static File getClassGroupImageDir(Context context) {
        File imgDir;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            imgDir = new File(
                    android.os.Environment.getExternalStorageDirectory(),
                    CACHE_DIR_PIC_CHAT);
        } else {
            imgDir = context.getDir(ROOT_DIR, 0);
        }
        if (!imgDir.exists()) {
            imgDir.mkdirs();
        }
        return imgDir;
    }

}
