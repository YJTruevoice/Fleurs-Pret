package com.facile.immediate.electronique.fleurs.pret.utils

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import java.util.regex.Pattern

/**
 * 文字变色工具类
 */
object HighlightStrUtil {

    /**
     * 关键字高亮变色
     *
     * @param color
     * 变化的色值
     * @param text
     * 文字
     * @param keywords
     * 文字中的关键字list
     * @return 结果SpannableString
     */
    fun matchHighlight(color: Int, text: String, keywords: List<String>?): SpannableString {
        val s = SpannableString(text)
        val txt = escapeExprSpecialWord(text)

        keywords?.forEach {
            val k = escapeExprSpecialWord(it)
            if (!TextUtils.isEmpty(k) && txt.contains(k)) {
                try {
                    val p = Pattern.compile(k)
                    val m = p.matcher(s)
                    while (m.find()) {
                        val start = m.start()
                        val end = m.end()
                        s.setSpan(
                            ForegroundColorSpan(color),
                            start,
                            end,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        return s
    }

    /**
     * 关键字高亮变色 只有一个高亮词
     *
     * @param color  变化的色值
     * @param text 文字
     * @param keyword 文字中的关键字
     * @return 结果SpannableString
     */
    fun matchHighlight(color: Int, text: String, keyword: String): SpannableString {
        val s = SpannableString(text)
        val txt = escapeExprSpecialWord(text)
        val k = escapeExprSpecialWord(keyword)

        if (!TextUtils.isEmpty(k) && txt.contains(k)) {
            try {
                val p = Pattern.compile(k)
                val m = p.matcher(s)
                while (m.find()) {
                    val start = m.start()
                    val end = m.end()
                    s.setSpan(
                        ForegroundColorSpan(color),
                        start,
                        end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return s
    }

    /**
     * 转义正则特殊字符 （$()*+.[]?\^{},|）
     *
     * @param keyword
     * @return keyword
     */
    private fun escapeExprSpecialWord(k: String): String {
        var keyword: String = k
        if (!TextUtils.isEmpty(keyword)) {
            val fbsArr = arrayOf(
                "\\",
                "$",
                "(",
                ")",
                "*",
                "+",
                ".",
                "[",
                "]",
                "?",
                "^",
                "{",
                "}",
                "|"
            )
            for (key in fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key)
                }
            }
        }
        return keyword
    }
}