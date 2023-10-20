package com.arthur.commonlib.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.arthur.commonlib.ability.Logger;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {
    /**
     * 把字节数组保存为一个文件
     *
     * @param b
     * @param outputFile
     * @return
     */
    public static File getFileFromBytes(byte[] b, String outputFile) {
        File ret = null;
        BufferedOutputStream stream = null;
        try {
            ret = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(ret);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            // log.error("helper:get file from byte process error!");
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // log.error("helper:get file from byte process error!");
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }


    /**
     * 创建目录
     *
     * @param path
     */
    public static void setMkdir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
            Log.e("file", "目录不存在  创建目录    ");
        } else {
            Log.e("file", "目录存在");
        }
    }

    /**
     * 获取目录名称
     *
     * @param url
     * @return FileName
     */
    public static String getFileName(String url) {
        int lastIndexStart = url.lastIndexOf("/");
        if (lastIndexStart != -1) {
            return url.substring(lastIndexStart + 1);
        } else {
            return null;
        }
    }

    /**
     * 删除该目录下的文件
     *
     * @param path
     */
    public static void delFile(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public static String readAsseetFile(String path, Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = context.getAssets().open(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    /**
     * 获取文件扩展名
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if (filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf('.');
            if (dot > -1 && dot < filename.length() - 1) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }

    public static boolean prepareParent(File file) {
        boolean isSuccess = true;
        if (file != null && file.getParentFile() != null && !file.getParentFile().exists()) {
            isSuccess = file.getParentFile().mkdirs();
        }
        return isSuccess;
    }

    /**
     * 将流写入指定文件，多用于ContentResolver打开的InputStream
     * @return
     */
    public static boolean saveFileFromStream(String dest,InputStream fis) {
        boolean isSuccess = true;
        if (!StringUtil.isEmpty(dest) && fis != null) {
            File destFile = new File(dest);
            prepareParent(destFile);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(destFile);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            } catch (IOException e) {
                Logger.INSTANCE.logE(e.getMessage());
                isSuccess = false;
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                    fis.close();
                } catch (IOException e) {
                    Logger.INSTANCE.logE(e.getMessage());
                }
            }
        }
        return isSuccess;
    }

    /**
     * 根据uri查找该uri对应文件的名称和大小，注：该方法不需要存储权限
     * @param resolver
     * @param uri
     * @return
     */
    public static Pair<String,Long> getFileBaseInfoByUri(ContentResolver resolver, Uri uri) {
        if (resolver == null || uri == null) {
            return null;
        }
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        if (returnCursor != null) {
            try {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                if (returnCursor.moveToFirst()) {
                    return new Pair<>(returnCursor.getString(nameIndex),returnCursor.getLong(sizeIndex));
                }
                returnCursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
