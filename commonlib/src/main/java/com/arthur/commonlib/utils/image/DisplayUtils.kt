package com.arthur.commonlib.utils.image

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.ability.Logger
import com.arthur.commonlib.utils.StringUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File
import java.security.MessageDigest


class DisplayUtils {

    companion object {

        fun displayImage(path: String, imageView: ImageView, placeHolder: Int) {
            displayImage(path, imageView, placeHolder, 1f)
        }

        fun displayImage(path: String, imageView: ImageView, placeHolder: Int, thumbnail: Float) {
            val activity = imageView.context as Activity
            if (activity.isFinishing || activity.isDestroyed) {
                return
            }
            if (StringUtil.isBlank(path)) {
                Logger.logE("display img path不存在 + $path")
                return
            }
            if (placeHolder > 0) {
                Glide.with(imageView).load(path).placeholder(placeHolder).into(imageView)
            } else {
                Glide.with(imageView).load(path).thumbnail().into(imageView)
            }
        }

        fun displayImage(path: String?, imageView: ImageView) {
            if (path.isNullOrBlank()) {
                return
            }
            displayImage(path, imageView, 0)
        }

        fun displayImage(path: String, imageView: ImageView, thumbnail: Float) {
            displayImage(path, imageView, 0, thumbnail)
        }

        fun displayImage(
            path: String,
            imageView: ImageView,
            @DrawableRes placeHolder: Int?,
            callback: (Bitmap) -> Unit
        ) {
            Glide.with(imageView)
                .asBitmap()
                .placeholder(placeHolder?:Color.parseColor("#EBEBEB"))
                .load(path)
                .into(object : CustomViewTarget<ImageView, Bitmap>(imageView) {
                    override fun onLoadFailed(errorDrawable: Drawable?) {

                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        callback(resource)
                    }

                    override fun onResourceCleared(placeholder: Drawable?) {

                    }

                })
        }

        fun displayAsDrawable(path: String, context: Activity, callback: (Drawable) -> Unit) {
            val target1 = object : CustomTarget<Drawable>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    callback.invoke(resource)
                }
            }
            Glide.with(context).asDrawable().load(path).into(target1)
        }

        /**
         * 加载圆形图片
         */
        fun displayImageAsCircle(
            path: String?,
            imageView: ImageView,
            placeHolder: Int = 0
        ) {
            if (StringUtil.isBlank(path) && placeHolder == 0) {
                Logger.logE("display img path不存在 + $path")
                return
            }
            val requestOptions: RequestOptions = RequestOptions()
                .placeholder(placeHolder)
                .error(placeHolder)
                .transform(CircleCrop())
                .dontAnimate()
            Glide.with(imageView.context).load(path).apply(requestOptions).into(imageView)
        }

        /**
         * 可显示圆角图片
         */
        fun displayImageAsRound(
            path: Any?,
            imageView: ImageView,
            placeHolder: Int = 0,
            radius: Int = 0
        ) {
            if ((path is Int && path <= 0 || path is String && path.isBlank() || path !is Int && path !is String) && placeHolder == 0) {
                Logger.logE("display img path不存在 + $path")
                return
            }
            val requestOptions: RequestOptions = RequestOptions()
                .placeholder(placeHolder)
                .error(placeHolder)
                .transform(
                    CenterCrop(), RoundedCorners(radius)
                )
                .dontAnimate()
            Glide.with(imageView.context).load(path).apply(requestOptions).into(imageView)
        }

        /**
         * 可显示圆角图片
         */
        fun displayImageAsRound(
            path: Any?,
            imageView: ImageView,
            placeHolder: Int = 0,
            topLeft: Int = 0,
            topRight: Int = 0,
            bottomLeft: Int = 0,
            bottomRight: Int = 0
        ) {
            if ((path is Int && path <= 0 || path is String && path.isBlank() || path !is Int && path !is String) && placeHolder == 0) {
                Logger.logE("display img path不存在 + $path")
                return
            }
            val requestOptions: RequestOptions = RequestOptions()
                .placeholder(placeHolder)
                .error(placeHolder)
                .transform(
                    CenterCrop(), com.arthur.commonlib.utils.image.transform.RoundedCorners(topLeft, topRight, bottomLeft, bottomRight)
                )
                .dontAnimate()
            Glide.with(imageView.context).load(path).apply(requestOptions).into(imageView)
        }

        /**
         * 可显示圆角图片、也可加边框
         */
        fun displayImageAsRoundWithBorder(
            path: Any,
            imageView: ImageView,
            placeHolder: Int = 0,
            radius: Int = 0,
            borderWidth: Int = 0,
            borderColor: Int = 0
        ) {
            if ((path is Int && path <= 0 || path is String && path.isBlank() || path !is Int && path !is String) && placeHolder == 0) {
                Logger.logE("display img path不存在 + $path")
                return
            }
            val requestOptions: RequestOptions = RequestOptions()
                .placeholder(placeHolder)
                .error(placeHolder)
                .transform(
                    RoundTransform(
                        imageView.context,
                        radius = radius,
                        borderWidth = borderWidth,
                        borderColor = borderColor
                    ), RoundedCorners(radius)
                )
                .dontAnimate()
            Glide.with(imageView.context).load(path).apply(requestOptions).into(imageView)
        }

        /**
         * 图片资源是否已经缓存，必须在IO线程调用
         */
        fun isImageCached(url: String): Boolean {
            return getImageCachedFile(url)?.let {
                it.exists()
            } ?: false
        }

        /**
         * 图片资源是否已经缓存，必须在IO线程调用
         */
        fun getImageCachedFile(url: String): File? {
            return try {
                Glide.with(AppKit.context)
                    .downloadOnly()
                    .load(url)
                    .apply(RequestOptions().onlyRetrieveFromCache(true))
                    .submit().get()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        /**
         * 下载图片资源
         * 会阻塞调用线程！必须在IO线程调用
         */
        fun downloadFile(url: String): File? {
            return kotlin.runCatching {
                Glide.with(AppKit.context)
                    .downloadOnly()
                    .load(url)
                    .submit().get()
            }.getOrNull()
        }
    }

    internal class RoundTransform(
        context: Context,
        radius: Int = 0,
        borderWidth: Int = 0,
        borderColor: Int = Color.parseColor("#EBEBEB")
    ) : BitmapTransformation() {
        private var mBorderPaint: Paint? = null
        private var mRadius: Float = 0f
        private var mBorderWidth: Float = 0f

        init {
            this.mRadius = radius.toFloat()
            this.mBorderWidth = borderWidth.toFloat()

            if (borderWidth > 0) {
                mBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    isDither = true
                    isAntiAlias = true
                    color = borderColor
                    style = Paint.Style.STROKE
                    strokeWidth = mBorderWidth
                }
            }
        }

        override fun transform(
            pool: BitmapPool,
            toTransform: Bitmap,
            outWidth: Int,
            outHeight: Int
        ): Bitmap? {
            return roundCrop(pool, toTransform)
        }

        private fun roundCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
            if (source == null) return null
            var result: Bitmap? = pool[source.width, source.height, Bitmap.Config.ARGB_8888]
            if (result == null) {
                result = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
            }
            val canvas = Canvas(result!!)
            val paint = Paint()
            paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint.isAntiAlias = true
            if (mBorderPaint != null) {
                //绘制圆角带边框
                drawRoundRect(
                    canvas, paint,
                    source.width.toFloat(), source.height.toFloat(), mBorderPaint!!
                )
            } else {
                //绘制圆角
                val rectF = RectF(0f, 0f, source.width.toFloat(), source.height.toFloat())
                canvas.drawRoundRect(rectF, mRadius, mRadius, paint)
            }
            return result
        }

        private fun drawRoundRect(
            canvas: Canvas,
            paint: Paint,
            width: Float,
            height: Float,
            borderPaint: Paint
        ) {
            val halfBorder = mBorderWidth / 2
            val path = Path()
            val pos = FloatArray(8)
            var shift = 15
            var index = 3
            while (index >= 0) { //设置四个边角的弧度半径
                pos[2 * index + 1] = if (shift and 1 > 0) mRadius else 0f
                pos[2 * index] = if (shift and 1 > 0) mRadius else 0f
                shift = shift shr 1
                index--
            }
            path.addRoundRect(
                RectF(halfBorder, halfBorder, width - halfBorder, height - halfBorder),
                pos, Path.Direction.CW
            )
            canvas.drawPath(path, paint) //绘制要加载的图形
            canvas.drawPath(path, borderPaint) //绘制边框
        }

        override fun updateDiskCacheKey(messageDigest: MessageDigest) {}
    }

}