package com.facile.immediate.electronique.fleurs.pret.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Environment;

import androidx.exifinterface.media.ExifInterface;


import com.arthur.commonlib.ability.Logger;
import com.arthur.commonlib.utils.APPDir;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import id.zelory.compressor.Compressor;


public class ImageUtil {

    public static Bitmap scale(Bitmap bm, int w, int h) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        // 计算缩放比例
        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);

        return newbm;
    }

    public static File scaleBitMap(Bitmap bitmap, Context context) {
        return scaleBitMap(bitmap, context, 1080, 1080);
    }

    public static File scaleBitMap(Bitmap bitmap, Context context,int rotateDegree) {
        return scaleBitMap(bitmap, context, 1080, 1080,rotateDegree);
    }

    public static File scaleBitMap(Bitmap bitmap, Context context, int maxWidth, int maxHeight,int rotateDegree){
        String filename = "pret_" + System.currentTimeMillis() + ".jpeg";
        File folder = new File(APPDir.INSTANCE.imageCache());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File f = new File(folder, filename);
        try {
            f.createNewFile();
        } catch (IOException e) {
            Logger.INSTANCE.logE(e.getMessage());
        }

        Bitmap finalBitmap;
        if(rotateDegree != 0){
            Matrix matrix = new Matrix();
            matrix.postRotate(rotateDegree);
            finalBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        }else{
            finalBitmap = bitmap;
        }

        // save to file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
            fos.write(bos.toByteArray());
            fos.flush();
        } catch (IOException e) {
            Logger.INSTANCE.logE(e.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Logger.INSTANCE.logE(e.getMessage());
                }
            }
        }

        try {
            File dstFile = new Compressor(context)
                    .setMaxWidth(maxWidth)
                    .setMaxHeight(maxHeight)
                    .setQuality(90)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(context.getExternalFilesDir(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .compressToFile(f);

            return dstFile;
        } catch (Exception e) {
            Logger.INSTANCE.logE(e.getMessage());
        }

        return null;
    }

    public static File scaleBitMap(Bitmap bitmap, Context context, int maxWidth, int maxHeight) {
        return scaleBitMap(bitmap,context,maxWidth,maxHeight,0);
    }

    public static byte[] readFileToByte(File file) {
        int size = (int) file.length();
        byte[] bitMapData = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bitMapData, 0, bitMapData.length);
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitMapData;
    }

    public static byte[] convertBitmapToJPEGByteArray(Bitmap image, int jpgQuality){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, jpgQuality, bos);
            byte[] imageContent = bos.toByteArray();
            bos.flush();
            bos.close();
            return imageContent;
        }catch (IOException var4){
            Logger.INSTANCE.logE("ImageUtil" + var4.getMessage());
            return null;
        }
    }


    //https://www.jianshu.com/p/7df654ad02d9
    /**
     * 获取图片的旋转角度。
     * 只能通过原始文件获取，如果已经进行过bitmap操作无法获取。
     */
    public static int getRotateDegree(String path) {
        int result = 0;
        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    result = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    result = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    result = 270;
                    break;
            }
        } catch (IOException ignore) {
            return 0;
        }
        return result;
    }

    /**
     * 获取缩放数值。
     * 取值请参考 {@link #evaluateWH(float, float, float, float)}
     */
    private static float getScale(float srcWidth, float srcHeight, float destWidth, float destHeight) {
        int evaluateWH = evaluateWH(srcWidth, srcHeight, destWidth, destHeight);
        switch (evaluateWH) {
            case 0:
                return destWidth / srcWidth;
            case 1:
                return destHeight / srcHeight;
            default:
                return 1f;
        }
    }

    /**
     * 评估使用宽或者高计算缩放比例
     * 以最大缩放比例为准，如宽比例为 0.5， 高比例为0.8，返回宽
     *
     * @return 0：宽， 1：高， -1：不缩放
     */
    private static int evaluateWH(float srcWidth, float srcHeight, float destWidth, float destHeight) {
        if (srcWidth < 1f || srcHeight < 1f || srcWidth < destWidth && srcHeight < destHeight) {
            return -1;
        }
        int result;
        if (destWidth > 0 && destHeight > 0) {
            result = destWidth / srcWidth < destHeight / srcHeight ? 0 : 1;
        } else if (destWidth > 0) {
            result = 0;
        } else if (destHeight > 0) {
            result = 1;
        } else {
            result = -1;
        }
        return result;
    }

    // 参考Hashmap::tableSizeFor，根据传入的缩放比例倍数，获取一个临近的2的次幂的数
    public static int inSampleSizeFor(int n) {
        int maxNum = 1 << 30;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        n = (n < 0) ? 1 : (n >= maxNum) ? maxNum : n + 1;
        return n >>> 1 == 0 ? n : n >>> 1;
    }

    /**
     * 加载缩放bitmap。
     * 根据期望宽高自动获取合适的缩放比例, 具体看{@link #evaluateWH(float, float, float, float)}
     *
     * @param path      图片路径
     * @param maxWidth  期望宽度
     * @param maxHeight 期望高度
     */
    public static Bitmap loadScaledBitmap(String path, int maxWidth, int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int srcHeight = options.outHeight;
        int srcWidth = options.outWidth;
        // decode失败
        if (srcHeight == -1 || srcWidth == -1) {
            return null;
        }

        // 当比例差距过大时，先通过inSampleSize加载bitmap降低内存消耗
        float scale = getScale(srcWidth, srcHeight, maxWidth, maxHeight);
        int evaluateWH = evaluateWH(srcWidth, srcHeight, maxWidth, maxHeight);
        options.inSampleSize = inSampleSizeFor((int) (1 / scale));
        options.inJustDecodeBounds = false;
        if (evaluateWH == 0) {
            options.inScaled = true;
            options.inDensity = srcWidth;
            options.inTargetDensity = maxWidth * options.inSampleSize;
        } else if (evaluateWH == 1) {
            options.inScaled = true;
            options.inDensity = srcHeight;
            options.inTargetDensity = maxHeight * options.inSampleSize;
        } else {
            options.inScaled = false;
        }
        return BitmapFactory.decodeFile(path, options);
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Bitmap getOverlayBitmap(Bitmap bg, Bitmap fg) {
        Canvas canvas = new Canvas(bg);
        int left = (bg.getWidth() - fg.getWidth()) >> 1;
        int top = (bg.getHeight() - fg.getHeight()) >> 1;
        canvas.drawBitmap(fg,left, top, null);
        return bg;
    }

    /**
     * 判断某文件是否是图片文件
     * @param path
     * @return
     */
    public static boolean isImageFile(@NotNull String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            if (options.outWidth != -1 && options.outHeight != -1) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}
