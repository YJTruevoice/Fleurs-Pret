package com.facile.immediate.electronique.fleurs.pret.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan

class StringSpanUtils {

    companion object {
        /**
         * 文字前后拼接图片
         */
        fun appendIcon(
            string: CharSequence,
            drawablesEnd: ArrayList<Drawable?>,
            drawablesStart: ArrayList<Drawable?> = arrayListOf()
        ): CharSequence {
            if (drawablesEnd.isEmpty() && drawablesStart.isEmpty()) return string
            val originLength = string.length
            val sb = if (drawablesStart.isNotEmpty()) {
                StringBuilder("i ").apply {
                    for (index in 1 until drawablesStart.size) {
                        append("i ")
                    }
                    append(string)
                }
            } else {
                StringBuilder(string)
            }

            for (index in 0 until drawablesEnd.size) {
                //行尾空格占位会被省略，改为用其他字符占位 http://cn.voidcc.com/question/p-bmmrypne-bdz.html
                sb.append(" i")
            }
            val afterString = sb.toString()
            var p = 0
            val spannableString = SpannableString(afterString)
            for (index in 0 until drawablesStart.size) {
                try {
                    val imgSpan = drawablesStart[index]?.let {
                        it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
                        CenterVerticalImageSpan(it)
                    }
                    spannableString.setSpan(imgSpan, p++, p++, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                } catch (e: IndexOutOfBoundsException) {
                    e.printStackTrace()
                }
            }
            p = originLength + drawablesStart.size * 2 + 1
            for (index in 0 until drawablesEnd.size) {
                try {
                    val imgSpan = drawablesEnd[index]?.let {
                        it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
                        CenterVerticalImageSpan(it)
                    }
                    spannableString.setSpan(imgSpan, p++, p++, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                } catch (e: IndexOutOfBoundsException) {
                    e.printStackTrace()
                }
            }
            return spannableString
        }

        /**
         * 解析html格式的文字
         */
        fun fromHtml(origin: String?):Spanned? {
            return origin?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(origin, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(origin)
                }
            }
        }
    }
}

class CenterVerticalImageSpan(drawable: Drawable) : ImageSpan(
    drawable
) {
    /**
     * update the text line height
     */
    override fun getSize(
        paint: Paint, text: CharSequence, start: Int, end: Int,
        fontMetricsInt: Paint.FontMetricsInt?
    ): Int {
        val drawable = drawable
        val rect = drawable.bounds
        if (fontMetricsInt != null) {
            val fmPaint = paint.fontMetricsInt
            val fontHeight = fmPaint.descent - fmPaint.ascent
            val drHeight = rect.bottom - rect.top
            val centerY = fmPaint.ascent + fontHeight / 2
            fontMetricsInt.ascent = centerY - drHeight / 2
            fontMetricsInt.top = fontMetricsInt.ascent
            fontMetricsInt.bottom = centerY + drHeight / 2
            fontMetricsInt.descent = fontMetricsInt.bottom
        }
        return rect.right
    }

    /**
     * see detail message in android.text.TextLine
     *
     * @param canvas the canvas, can be null if not rendering
     * @param text the text to be draw
     * @param start the text start position
     * @param end the text end position
     * @param x the edge of the replacement closest to the leading margin
     * @param top the top of the line
     * @param y the baseline
     * @param bottom the bottom of the line
     * @param paint the work paint
     */
    override fun draw(
        canvas: Canvas, text: CharSequence, start: Int, end: Int,
        x: Float, top: Int, y: Int, bottom: Int, paint: Paint
    ) {
        val drawable = drawable
        canvas.save()
        val fmPaint = paint.fontMetricsInt
        val fontHeight = fmPaint.descent - fmPaint.ascent
        val centerY = y + fmPaint.descent - fontHeight / 2
        val transY = centerY - (drawable.bounds.bottom - drawable.bounds.top) / 2
        canvas.translate(x, transY.toFloat())
        drawable.draw(canvas)
        canvas.restore()
    }
}