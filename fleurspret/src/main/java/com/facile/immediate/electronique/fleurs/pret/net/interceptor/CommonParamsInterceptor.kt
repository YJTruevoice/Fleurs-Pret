package com.facile.immediate.electronique.fleurs.pret.net.interceptor

import android.text.TextUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.*

/**
 * @Author Arthur
 * @Data 2022/8/3
 * @Description
 * 公共参数添加 拦截器
 */
class CommonParamsInterceptor(private val commonParams: (() -> HashMap<String, String>) = { hashMapOf() }) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        try {
            request = addCommonParams(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return chain.proceed(request)
    }

    /**
     * 添加公共参数
     */
    private fun addCommonParams(request: Request): Request {
        return when (request.method) {
            "POST" -> {
                addPostParams(request)
            }
            "GET" -> {
                addGetParams(request)
            }
            "DELETE" -> {
                addDeleteParams(request)
            }
            else -> {
                request
            }
        }
    }

    /**
     * Delete方式 没打签
     */
    private fun addDeleteParams(request: Request): Request {
        val requestBuilder = request.newBuilder()
        val newParams: MutableMap<String, String> = commonParams.invoke()
        val body = request.body
        try {
            body?.contentLength()?.let {
                if (it > 0) {
                    val sink = Buffer()
                    body.writeTo(sink)
                    val bs = sink.readByteArray()
                    if (bs.isNotEmpty()) {
                        val content = String(bs, Charset.forName("UTF-8"))
                        if (!TextUtils.isEmpty(content) && newParams.isNotEmpty()) {
                            val jsonObject = JSONObject(content)
                            for ((key, value) in newParams.entries) {
                                if (!jsonObject.has(key)) {
                                    jsonObject.put(key, value)
                                }
                            }

                            val newContent = jsonObject.toString()
                            val contentType: MediaType? =
                                "application/json;charset=utf-8".toMediaTypeOrNull()

                            val requestBody = newContent.toRequestBody(contentType)
                            requestBuilder.post(requestBody)
                        }
                    }
                } else {
                    //即可没有参数时，直接拼借在query后面
                    return addGetParams(request)
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return requestBuilder.build()
    }

    /**
     * post请求 添加公共参数 签名
     */
    private fun addPostParams(request: Request): Request {
        val requestBuilder = request.newBuilder()
        val newParams: MutableMap<String, String> = commonParams.invoke()
        when (val body = request.body) {
            is FormBody -> {
                val bodyBuilder = FormBody.Builder()
                if (body.size > 0) {
                    for (i in 0 until body.size) {
                        newParams[body.name(i)] = body.value(i)
                    }
                }
                for ((key, value) in newParams.entries) {
                    bodyBuilder.add(key, value)
                }
                return requestBuilder.post(bodyBuilder.build()).build()
            }
            is MultipartBody -> {
                val multipartBuilder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                val oldParts: List<MultipartBody.Part> = body.parts
                val conflictKeys = ArrayList<String>()
                if (oldParts.isNotEmpty()) {
                    for (part in oldParts) {
                        multipartBuilder.addPart(part)
                        val headerStr: String = part.headers.toString()
                        for ((key) in newParams.entries) {
                            if (headerStr.contains(key)) {
                                conflictKeys.add(key)
                            }
                        }
                    }
                }
                for ((key, value) in newParams.entries) {
                    if (!conflictKeys.contains(key)) {
                        multipartBuilder.addFormDataPart(key, value)
                    }
                }
                return requestBuilder.post(multipartBuilder.build()).build()
            }
            else -> {
                try {
                    body?.contentLength()?.let {
                        if (it > 0) {
                            val sink = Buffer()
                            body.writeTo(sink)
                            val bs = sink.readByteArray()
                            if (bs.isNotEmpty()) {
                                val content = String(bs, Charset.forName("UTF-8"))
                                if (!TextUtils.isEmpty(content) && newParams.isNotEmpty()) {
                                    val contentType: MediaType? =
                                        "application/json;charset=utf-8".toMediaTypeOrNull()

                                    val requestBody = content.toRequestBody(contentType)

                                    // 公共参数拼接到url
                                    val urlBuilder: HttpUrl.Builder = request.url.newBuilder()
                                    for ((key, value) in newParams) {
                                        urlBuilder.addQueryParameter(key, value)
                                    }

                                    return requestBuilder.post(requestBody).url(urlBuilder.build()).build()
                                }
                            }
                        } else {
                            //即没有参数时，直接拼接在query后面
                            return addGetParams(request)
                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        return requestBuilder.build()
    }

    /**
     * get请求 添加公共参数 签名
     */
    private fun addGetParams(request: Request): Request {
        val url = request.url
        val urlBuilder: HttpUrl.Builder = url.newBuilder()
        val pNames = url.queryParameterNames

        val newParams: MutableMap<String, String> = commonParams.invoke()
        if (pNames.isNotEmpty()) {
            for (i in pNames.indices) {
                newParams[url.queryParameterName(i)] = url.queryParameterValue(i) ?: ""
            }
        }

        for ((key, value) in newParams) {
            urlBuilder.removeAllQueryParameters(key)
            urlBuilder.addQueryParameter(key, value)
        }

        //添加公共参数
        val httpUrl: HttpUrl = urlBuilder.build()

        return request.newBuilder().url(httpUrl).build()
    }
}