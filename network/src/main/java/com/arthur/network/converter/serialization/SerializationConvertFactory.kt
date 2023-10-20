package com.arthur.network.converter.serialization

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * * @Author Arthur
 * * @Data 2022/10/9
 * * @Description
 * ### Serialization转化器工厂
 * 1. `kotlin官方出品, 推荐使用 `
 * 2. `kotlinx.serialization 是Kotlin上是最完美的序列化工具`
 * 3. `支持多种反序列化数据类型Pair/枚举/Map...`
 * 4. `多配置选项`
 * 5. `支持动态解析`
 * 6. `支持ProtoBuf/CBOR/JSON等数据`
 *
 * ### todo 待完善
 */
class SerializationConvertFactory : Converter.Factory() {

    companion object {
        fun create(): SerializationConvertFactory {
            return SerializationConvertFactory()
        }
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return super.responseBodyConverter(type, annotations, retrofit)
    }

}