package com.arthur.network.converter.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * @Author Arthur
 * @Data 2022/5/12
 * @Description Gson转换器工厂
 */
class GsonConverterFactory private constructor() : Converter.Factory() {
    private val gson: Gson = GsonBuilder().serializeNulls().create()

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val typeString = object : TypeToken<String?>() {}.type
        return if (type === typeString) {
            StringResponseBodyConverter()
        } else {
            val adapter = gson.getAdapter(TypeToken.get(type))
            GsonResponseBodyConverter(adapter, type)
        }
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>, methodAnnotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<*, RequestBody> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return GsonRequestBodyConverter(gson, adapter)
    }

    companion object {
        /**
         * Create an instance using `gson` for conversion. Encoding to JSON and
         * decoding from JSON (when no charset is specified by a header) will use UTF-8.
         */
        fun create(): GsonConverterFactory {
            return GsonConverterFactory()
        }
    }

}