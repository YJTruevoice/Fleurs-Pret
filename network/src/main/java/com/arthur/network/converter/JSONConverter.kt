package com.arthur.network.converter

import com.arthur.commonlib.utils.json.JsonUtils
import com.arthur.network.exception.NetBaseException
import org.json.JSONObject
import java.lang.reflect.Type

/**
 *  * @Author Arthur
 *  * @Data 2023/2/28
 *  * @Description
 * ## json解析
 */
open class JSONConverter {
    /**
     * 网络请求成功业务码
     */
    open var successCode: Int = 0

    /**
     * codeKey
     */
    open var codeKey: String = "code"

    /**
     * msgKey
     */
    open var msgKey: String = "msg"

    /**
     * dataKey
     */
    open var dataKey: String = "data"

    @Throws(Exception::class)
    fun <R> converter(value: String, type: Type): R? {
        val response: JSONObject?
        val code: Int
        try {
            response = JSONObject(value)
            code = response.optInt(codeKey, -1)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
        if (code == successCode) {
            return converterInternal(value, type)
        } else {
            throw NetBaseException(
                msg = response.optString(msgKey, "无提示信息"),
                eCode = code,
                response = response.opt(dataKey)?.toString()
            )
        }
    }

    @Throws(Exception::class)
    open fun <R> converterInternal(value: String, type: Type): R? {
        return JsonUtils.gsonClient.fromJson(value, type)
    }
}