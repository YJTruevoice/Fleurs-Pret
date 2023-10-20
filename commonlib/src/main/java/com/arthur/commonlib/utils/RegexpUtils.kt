package com.arthur.commonlib.utils

import java.util.regex.Pattern

class RegexpUtils {

    companion object {
        /**
         * 匹配话题（话题至多支持25字）
         */
        fun getTopicList(content: String, maxLength: Int = 25): ArrayList<String> {
            val list = ArrayList<String>(2)
            if (content.contains("#")) {
                val matcher = Pattern.compile("#([^#^\\s]{1,${maxLength}})#").matcher(content)
                while (matcher.find()) {
                    matcher.group(1)?.let {
                        list.add(it)
                    }
                }
            }
            return list
        }

        /**
         * 匹配文字位置
         *
         * @param originContent 原始内容
         * @param str 需要匹配的文字内容
         * @param needEscape 是否需要转义特殊字符，不转义的话str中带有?等正则匹配关键字时会匹配不到
         *
         * @return 匹配到的位置信息Pair<起始位置，结束位置>
         */
        fun getStrPosition(originContent: CharSequence, str: String, needEscape: Boolean = false): ArrayList<Pair<Int, Int>> {
            val pattern = Pattern.compile(if (needEscape) escapeExprSpecialWord(str) else str)
            val result = ArrayList<Pair<Int, Int>>(5)
            if (originContent.contains(str)) {
                val matcher = pattern.matcher(originContent)
                while (matcher.find()) {
                    result.add(Pair(matcher.start(), matcher.end()))
                }
            }
            return result
        }

        /**
         * 转义正则特殊字符 （$()*+.[]?\^{},|）
         *
         * @param keyword
         * @return
         */
        fun escapeExprSpecialWord(keyword: String): String {
            var result = keyword
            if (result.isNotBlank()) {
                val fbsArr =
                    arrayOf("\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|")
                for (key in fbsArr) {
                    if (result.contains(key)) {
                        result = result.replace(key, "\\" + key)
                    }
                }
            }
            return result
        }
    }
}