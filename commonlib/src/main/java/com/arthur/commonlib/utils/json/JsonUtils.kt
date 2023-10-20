package com.arthur.commonlib.utils.json

import com.google.gson.*
import com.arthur.commonlib.ability.Logger
import com.arthur.commonlib.utils.json.adapter.GsonAdapterFactory
import java.lang.reflect.Type
import java.util.*

/**
 * Created by guo.lei on 2022/11/17
 *
 */
object JsonUtils {

    val gsonClient by lazy {
        gsonClient()
    }

    fun <T> fromJson(jsonStr: String?, clz: Class<T>): T? {
        try {
            return gsonClient.fromJson(jsonStr, clz)
        } catch (e: Exception) {
            Logger.logE("JsonUtils", "Json FormatError:${e.message} -> $jsonStr")
        }
        return null
    }

    fun <T> fromJson(jsonStr: String?, type: Type): T? {
        try {
            return gsonClient.fromJson(jsonStr, type)
        } catch (e: Exception) {
            Logger.logE("JsonUtils", "Json FormatError:${e.message} -> $jsonStr")
        }
        return null
    }

    /**
     * 判断是否是json格式：对象或数组
     */
    fun isJson(jsonStr: String?): Boolean {
        return parseAsJsonElement(jsonStr)?.let {
            it.isJsonObject || it.isJsonArray
        } ?: false
    }

    /**
     * 判断是否是json对象格式
     */
    fun isJsonObject(jsonStr: String?): Boolean {
        return parseAsJsonElement(jsonStr)?.isJsonObject ?: false
    }

    /**
     * 判断是否是json数组格式
     */
    fun isJsonArray(jsonStr: String?): Boolean {
        return parseAsJsonElement(jsonStr)?.isJsonArray ?: false
    }

    /**
     * 解析为Gson的[JsonObject]对象
     */
    fun parseGsonObject(jsonStr: String?): JsonObject? {
        return parseAsJsonElement(jsonStr)?.asJsonObject
    }

    /**
     * 转换为json字符串
     */
    fun toJsonString(obj: Any?): String? {
        return obj?.let {
            gsonClient().toJson(obj)
        }
    }

    /**
     * 判断是否是json数组格式
     */
    private fun parseAsJsonElement(jsonStr: String?): JsonElement? {
        try {
            return JsonParser().parse(jsonStr)
        } catch (e: Exception) {
            printError(e, jsonStr)
        }
        return null
    }

    /**
     * 打印错误
     */
    private fun printError(e: Exception, jsonStr: String?) {
        Logger.logE("JsonUtils", "Json FormatError:${e.message} -> $jsonStr")
    }

    /**
     * 获取json处理句柄，GSON实现
     */
    private fun gsonClient(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Date::class.java, JsonDeserializer { json, _, _ ->
                val timeStamp = kotlin.runCatching { json.asJsonPrimitive.asLong }.getOrNull() ?: System.currentTimeMillis()
                Date(timeStamp)
            })
            .registerTypeAdapterFactory(GsonAdapterFactory())
            .setLenient()
            .create()
    }

}