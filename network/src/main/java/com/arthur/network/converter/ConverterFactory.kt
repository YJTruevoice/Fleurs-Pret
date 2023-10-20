package com.arthur.network.converter

import com.arthur.network.converter.gson.GsonConverterFactory
import com.arthur.network.converter.serialization.SerializationConvertFactory
import retrofit2.Converter

/**
 * * @Author Arthur
 * * @Data 2022/10/8
 * * @Description
 * ### 网络请求响应体数据转化器工厂
 */
object ConverterFactory {

    fun create(type: Factory = Factory.GSON): Converter.Factory {
        return when (type) {
            Factory.SERIALIZATION -> {
                SerializationConvertFactory.create()
            }
            Factory.MOSHI -> {
                SerializationConvertFactory.create()
            }
            else -> {
                GsonConverterFactory.create()
            }
        }
    }
}