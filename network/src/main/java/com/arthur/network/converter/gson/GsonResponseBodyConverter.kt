package com.arthur.network.converter.gson

import com.google.gson.TypeAdapter
import com.arthur.network.Net
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type

/**
 * @Author Arthur
 * @Data 2022/5/12
 * @Description Gson响应体转换器
 */
class GsonResponseBodyConverter<T> internal constructor(
    private val adapter: TypeAdapter<T>,
    private val type: Type
) : Converter<ResponseBody, T> {
    override fun convert(value: ResponseBody): T? {
        return Net.client.netOptions.jsonConverter.converter(value.string(), type)
    }
}