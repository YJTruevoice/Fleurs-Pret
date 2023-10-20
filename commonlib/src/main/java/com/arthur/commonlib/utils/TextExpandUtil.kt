package com.arthur.commonlib.utils

import android.text.Layout
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.StaticLayout
import android.text.style.ClickableSpan
import android.widget.TextView
import kotlin.math.max

class TextExpandUtil {

    companion object {

        /**
         * 在textView末尾展示展开等字样，可点击
         *
         * @param originText 原始文本
         * @param expandText 省略号末尾添加的文本，最后返回的内容为  ***… {expandText}
         * @param tv 目标TextView容器，需要用其进行测量
         * @param alwaysShowExpandText 是否需要在内容未展示满时也在末尾拼接ExpandText
         * @param maxLine 最大展示行数
         * @param span 对expandText设置的span
         */
        fun getEndExpandText(
            originText: CharSequence,
            expandText: CharSequence,
            tv: TextView,
            alwaysShowExpandText: Boolean,
            maxLine: Int,
            span: ClickableSpan? = null
        ): CharSequence {
            if (!alwaysShowExpandText) {
                val layout = createStaticLayout(originText, tv)
                if (layout.lineCount <= maxLine) {
                    return originText
                }
            }
            val handledString = "${
                getSubTextForExpand(originText, expandText, tv, maxLine)
            }…$expandText"
            return SpannableStringBuilder(handledString).apply {
                span?.let {
                    setSpan(
                        it,
                        handledString.length - expandText.length,
                        handledString.length,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }

        /**
         * 递归找到拼接展开字样的位置，返回拼接后的文字
         */
        private fun getSubTextForExpand(
            text: CharSequence,
            expandText: CharSequence,
            tv: TextView,
            maxLine: Int
        ): CharSequence {
            val textWithExpand = "$text…$expandText"
            val layout = createStaticLayout(textWithExpand, tv)
            return if (layout.lineCount <= maxLine) {
                text
            } else {
                //对有换行符的文本如果也直接裁剪对应字数可能会使文本行数大量减少，单独处理
                val lastLinefeed = text.lastIndexOf("\n")
                val end = max(lastLinefeed, layout.getLineEnd(maxLine - 1) - "…$expandText".length)
                if (end < 0) {
                    text
                } else {
                    getSubTextForExpand(
                        text.subSequence(
                            0,
                            end
                        ), expandText, tv, maxLine
                    )
                }
            }
        }

        /**
         * 获取对应的StaticLayout用以获取换行位置
         */
        private fun createStaticLayout(spannable: CharSequence, tv: TextView): Layout {
            val contentWidth = tv.width - tv.paddingLeft - tv.paddingRight
            return StaticLayout(
                spannable,
                tv.paint,
                contentWidth,
                Layout.Alignment.ALIGN_NORMAL,
                tv.lineSpacingMultiplier,
                tv.lineSpacingExtra,
                false
            )
        }
    }

}