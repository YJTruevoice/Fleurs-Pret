package com.arthur.network.converter.serialization

import okhttp3.RequestBody
import retrofit2.Converter

/**
 * @Author Arthur
 * @Data 2022/10/9
 * @Description
 *
 */
class SerializationRequestBodyConverter<T> : Converter<T, RequestBody> {
    override fun convert(value: T): RequestBody {
        TODO("Not yet implemented")
    }
}