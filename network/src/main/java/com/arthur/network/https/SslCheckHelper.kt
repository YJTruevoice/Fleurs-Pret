package com.arthur.network.https

import android.annotation.SuppressLint
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class SslCheckHelper {
    private var trustAllManager: X509TrustManager? = null

    /**
     * @return 忽略所有证书的SSLSocketFactory
     */
    val allSSLSocketFactory: SSLSocketFactory
        get() = try {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf<TrustManager>(allTrustManager), SecureRandom())
            sslContext.socketFactory
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    /**
     * @return 忽略所有证书的TrustManager
     */
    val allTrustManager: X509TrustManager
        get() {
            if (trustAllManager == null) {
                trustAllManager = @SuppressLint("CustomX509TrustManager")
                object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            }
            return trustAllManager!!
        }
}