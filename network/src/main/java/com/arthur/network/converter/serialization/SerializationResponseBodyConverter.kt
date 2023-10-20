package com.arthur.network.converter.serialization

import okhttp3.ResponseBody
import retrofit2.Converter

/**
 * @Author Arthur
 * @Data 2022/10/9
 * @Description
 *
 */
class SerializationResponseBodyConverter<T> : Converter<ResponseBody, T> {
    override fun convert(value: ResponseBody): T {
        TODO("Not yet implemented")
    }
}