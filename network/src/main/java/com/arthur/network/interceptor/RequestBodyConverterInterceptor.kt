package com.arthur.network.interceptor

import com.arthur.network.NetConstant
import okhttp3.Interceptor
import okhttp3.Response

/**
 * ## RequestBody 转换拦截器
 *  * @Author Arthur
 *  * @Data 2023/3/1
 *  * @Description 业务定义网络句柄时都以表单形式提交参数,
 *  如果在header里指定了[NetConstant.BODY_REQUEST_METHOD_HEADER],
 *  则意味着需要RequestBody方式请求，这里需要做一下状态
 */
class RequestBodyConverterInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        chain.request().apply {
//            Logger.logI("NC-Net", "RequestBodyConverterInterceptor requestMethod ${header(
//                NetConstant.KEY_REQUEST_METHOD)}")
//            if (method == "POST" && body is FormBody) {
//                val requestMethod = header(NetConstant.KEY_REQUEST_METHOD)
//                if (requestMethod == NetConstant.VAL_BODY_REQUEST) {
//                    Logger.logI("NC-Net", "RequestBodyConverterInterceptor request body converter formBody -> body")
//                    (body as? FormBody)?.apply {
//                        if (size > 0) {
//                            val paramJson = JSONObject()
//                            for (i in 0 until size) {
//                                val value = value(i)
//                                val validator = JSONValidator.from(value)
//
//                                paramJson[name(i)] = when(validator.type) {
//                                    JSONValidator.Type.Array -> {
//                                        JSON.parseArray(value)
//                                    }
//                                    JSONValidator.Type.Object -> {
//                                        JSON.parseObject(value)
//                                    }
//                                    else -> {
//                                        value
//                                    }
//                                }
//                            }
//                            val contentType: MediaType? = NetConstant.JSON_MEDIA_TYPE_ENCODE_METHOD.toMediaTypeOrNull()
//                            val requestBody = paramJson.toString().toRequestBody(contentType)
//                            return chain.proceed(newBuilder().post(requestBody).build())
//                        }
//                    }
//                }
//            }
            return chain.proceed(this)
        }
    }
}