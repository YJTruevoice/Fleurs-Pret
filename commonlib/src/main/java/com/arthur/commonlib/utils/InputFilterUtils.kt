package com.arthur.commonlib.utils

import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.SpannableStringBuilder
import android.text.Spanned
import com.arthur.commonlib.ability.Toaster

/**
 * 常用InputFilter
 */
class InputFilterUtils {

    companion object {
        val emojiInputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            if (StringUtil.containsEmoji(source)) {
                ""
            } else source
        }

        fun lengthFilter(max: Int) = LengthFilter(max)
    }

    /**
     * 超出字数上限后会以Toast形式弹出提示，整体逻辑与LengthFilter相同
     */
    class WarningLengthFilter(val max: Int, val warning: String = "超出字数上限") : InputFilter {

        override fun filter(
            source: CharSequence, start: Int, end: Int, dest: Spanned,
            dstart: Int, dend: Int
        ): CharSequence? {
            var keep = max - (dest.length - (dend - dstart))
            return when {
                keep <= 0 -> {
                    Toaster.showToast(warning)
                    ""
                }
                keep >= end - start -> {
                    null // keep original
                }
                else -> {
                    Toaster.showToast(warning)
                    keep += start
                    if (Character.isHighSurrogate(source[keep - 1])) {
                        --keep
                        if (keep == start) {
                            return ""
                        }
                    }
                    source.subSequence(start, keep)
                }
            }
        }

    }

    /**
     * 过滤指定字符
     */
    class CharFilter private constructor(filterChars: CharArray?) : InputFilter {
        private val filterChars: CharArray = filterChars ?: CharArray(0)

        /**
         * @param source 输入的文字
         * @param start  输入-0，删除-0
         * @param end    输入-文字的长度，删除-0
         * @param dest   原先显示的内容
         * @param dstart 输入-原光标位置，删除-光标删除结束位置
         * @param dend   输入-原光标位置，删除-光标删除开始位置
         * @return null表示原始输入，""表示不接受输入，其他字符串表示变化值
         */
        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            if (needFilter(source)) {
                val builder = SpannableStringBuilder()
                var abStart = start
                for (i in start until end) {
                    if (isFilterChar(source[i])) {
                        if (i != abStart) {
                            builder.append(source.subSequence(abStart, i))
                        }
                        abStart = i + 1
                    }
                }
                if (abStart < end) {
                    builder.append(source.subSequence(abStart, end))
                }
                return builder
            }
            return null
        }

        private fun needFilter(source: CharSequence): Boolean {
            val s = source.toString()
            for (filterChar in filterChars) {
                if (s.indexOf(filterChar) >= 0) {
                    return true
                }
            }
            return false
        }

        private fun isFilterChar(c: Char): Boolean {
            for (filterChar in filterChars) {
                if (filterChar == c) {
                    return true
                }
            }
            return false
        }

        companion object {
            fun newlineCharFilter(): CharFilter {
                return CharFilter(charArrayOf('\n'))
            }

            fun whitespaceCharFilter(): CharFilter {
                return CharFilter(charArrayOf(' '))
            }

            fun returnCharFilter(): CharFilter {
                return CharFilter(charArrayOf('\r'))
            }

            fun nrCharFilter(): CharFilter {
                return CharFilter(charArrayOf('\n', '\r'))
            }
        }

    }


}