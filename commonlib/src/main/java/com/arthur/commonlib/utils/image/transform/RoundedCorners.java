package com.arthur.commonlib.utils.image.transform;

import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

import androidx.annotation.NonNull;

/**
 * Created by guo.lei on 2023/1/30
 */
public final class RoundedCorners extends BitmapTransformation {
    private static final String ID = "com.nowcoder.app.nowcoderuilibrary.utils.image.transform.RoundedCorners";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private final int topLeftRadius;
    private final int topRightRadius;
    private final int bottomLeftRadius;
    private final int bottomRightRadius;

    private final int hash;

    /**
     * @param topLeftRadius     the topLeftRadius radius (in device-specific pixels).
     * @param topRightRadius    the topRightRadius radius (in device-specific pixels).
     * @param bottomLeftRadius  the bottomLeftRadius radius (in device-specific pixels).
     * @param bottomRightRadius the bottomRightRadius radius (in device-specific pixels).
     * @throws IllegalArgumentException if rounding radius is 0 or less.
     */
    public RoundedCorners(int topLeftRadius, int topRightRadius, int bottomLeftRadius, int bottomRightRadius) {
        Preconditions.checkArgument(topLeftRadius >= 0, "topLeftRadius must be greater than 0.");
        Preconditions.checkArgument(topRightRadius >= 0, "topRightRadius must be greater than 0.");
        Preconditions.checkArgument(bottomLeftRadius >= 0, "bottomLeftRadius must be greater than 0.");
        Preconditions.checkArgument(bottomRightRadius >= 0, "bottomRightRadius must be greater than 0.");
        this.topLeftRadius = topLeftRadius;
        this.topRightRadius = topRightRadius;
        this.bottomLeftRadius = bottomLeftRadius;
        this.bottomRightRadius = bottomRightRadius;

        hash = Util.hashCode(Util.hashCode(Util.hashCode(topLeftRadius), Util.hashCode(topRightRadius)), Util.hashCode(Util.hashCode(bottomLeftRadius), Util.hashCode(bottomRightRadius)));
    }

    @Override
    protected Bitmap transform(
            @NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return TransformationUtils.roundedCorners(pool, toTransform, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RoundedCorners) {
            RoundedCorners other = (RoundedCorners) o;
            return topLeftRadius == other.topLeftRadius
                    && topRightRadius == other.topRightRadius
                    && bottomRightRadius == other.bottomRightRadius
                    && bottomLeftRadius == other.bottomLeftRadius
                    ;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Util.hashCode(ID.hashCode(), hash);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);

        byte[] radiusData = ByteBuffer.allocate(4).putInt(hash).array();
        messageDigest.update(radiusData);
    }
}