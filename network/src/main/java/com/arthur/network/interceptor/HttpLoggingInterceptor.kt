package com.arthur.network.interceptor

import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.http.promisesBody
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.logging.Level.INFO
import java.util.logging.Logger

/**
 * 网络请求log打印拦截器
 */
class HttpLoggingInterceptor(tag: String = "NC-Net") : Interceptor {
    @Volatile
    private var printLevel = Level.BODY
    private var colorLevel: java.util.logging.Level = INFO
    private val logger: Logger = Logger.getLogger(tag)

    enum class Level {
        NONE,  //不打印log
        BASIC,  //只打印 请求首行 和 响应首行
        HEADERS,  //打印请求和响应的所有 HttpHeaders
        BODY //所有数据全部打印
    }

    fun setPrintLevel(level: Level) {
        printLevel = level
    }

    fun setColorLevel(level: java.util.logging.Level = INFO) {
        colorLevel = level
    }

    fun log(message: String?) {
        logger.log(colorLevel, message)
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        if (printLevel == Level.NONE) {
            return chain.proceed(request)
        }

        //请求日志拦截
        logForRequest(request, chain.connection())

        //执行请求，计算请求时间
        val startNs = System.nanoTime()
        val response: Response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            log("<-- HTTP FAILED: $e")
            throw e
        }
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        //响应日志拦截
        return logForResponse(response, tookMs)
    }

    @Throws(IOException::class)
    private fun logForRequest(request: Request, connection: Connection?) {
        val logBody = printLevel == Level.BODY
        val logHeaders = printLevel == Level.BODY || printLevel == Level.HEADERS
        val requestBody = request.body
        val hasRequestBody = requestBody != null
        val protocol = connection?.protocol() ?: Protocol.HTTP_1_1
        try {
            val requestStartMessage = "--> " + request.method + ' ' + request.url + ' ' + protocol
            log(requestStartMessage)
            if (logHeaders) {
                val headers = request.headers
                var i = 0
                val count = headers.size
                while (i < count) {
                    log("\t" + headers.name(i) + ": " + headers.value(i))
                    i++
                }
                log(" ")
                if (logBody && hasRequestBody) {
                    if (isPlaintext(requestBody!!.contentType())) {
                        bodyToString(request)
                    } else {
                        log("\tbody: maybe [file part] , too large too print , ignored!")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            log("--> END " + request.method)
        }
    }

    private fun logForResponse(response: Response, tookMs: Long): Response {
        val builder: Response.Builder = response.newBuilder()
        val clone: Response = builder.build()
        var responseBody = clone.body
        val logBody = printLevel == Level.BODY
        val logHeaders = printLevel == Level.BODY || printLevel == Level.HEADERS
        try {
            log("<-- " + clone.code + ' ' + clone.message + ' ' + clone.request.url + " (" + tookMs + "ms）")
            if (logHeaders) {
                val headers = clone.headers
                var i = 0
                val count = headers.size
                while (i < count) {
                    log("\t" + headers.name(i) + ": " + headers.value(i))
                    i++
                }
                log(" ")
                if (logBody && clone.promisesBody()) {
                    if (isPlaintext(responseBody!!.contentType())) {
                        val body = responseBody.string()
                        log("\tbody:$body")
                        responseBody = body.toResponseBody(responseBody.contentType())
                        return response.newBuilder().body(responseBody).build()
                    } else {
                        log("\tbody: maybe [file part] , too large too print , ignored!")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            log("<-- END HTTP")
        }
        return response
    }

    private fun bodyToString(request: Request) {
        try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body!!.writeTo(buffer)
            var charset = UTF8
            val contentType = copy.body!!.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF8)
            }
            log("\tbody:" + buffer.readString(charset!!))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private val UTF8 = Charset.forName("UTF-8")

        /**
         * Returns true if the body in question probably contains human readable text. Uses a small sample
         * of code points to detect unicode control characters commonly used in binary file signatures.
         */
        private fun isPlaintext(mediaType: MediaType?): Boolean {
            if (mediaType == null) return false
            if (mediaType.type == "text") {
                return true
            }
            var subtype = mediaType.subtype
            subtype = subtype.lowercase(Locale.ROOT)
            if (subtype.contains("x-www-form-urlencoded")
                || subtype.contains("json")
                || subtype.contains("xml")
                || subtype.contains("html")
            )
                return true
            return false
        }
    }

}