package com.arthur.network.converter.gson

import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException

/**
 * @Author Arthur
 * @Data 2022/5/12
 * @Description 字符串结果转换器
 */
class StringResponseBodyConverter : Converter<ResponseBody, String> {
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): String {
        return value.use {
            value.string()
        }
    }
}