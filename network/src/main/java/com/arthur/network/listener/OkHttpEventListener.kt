package com.arthur.network.listener

import com.arthur.commonlib.ability.Logger
import okhttp3.Call
import okhttp3.EventListener
import okhttp3.Protocol
import okhttp3.Request
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Proxy

/**
 * * @Author Arthur
 * * @Data 2023/5/29
 * * @Description
 * ##
 */
open class OkHttpEventListener : EventListener() {

    companion object {
        const val TAG = "OkHttpEventListener"
    }

    private fun currentThread(): String {
        Thread.currentThread().apply {
            return "${toString()} ${hashCode()}"
        }
    }

    override fun callStart(
        call: Call
    ) {
        val request: Request = call.request()
        Logger.logD(TAG, "callStart curThread ${currentThread()}\n${request.hashCode()} $request")
//        RequestScopeManager.addRequestObserver(request)
    }

    override fun dnsStart(
        call: Call, domainName: String
    ) {
        Logger.logD(TAG, "dnsStart curThread ${currentThread()}")
    }

    override fun dnsEnd(
        call: Call, domainName: String, inetAddressList: List<@JvmSuppressWildcards InetAddress>
    ) {
        Logger.logD(TAG, "dnsEnd curThread ${currentThread()}")
    }

    override fun connectStart(
        call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy
    ) {
        Logger.logD(TAG, "connectStart curThread ${currentThread()}")
    }

    override fun connectFailed(
        call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy, protocol: Protocol?, ioe: IOException
    ) {
        Logger.logD(TAG, "connectFailed curThread ${currentThread()}")
    }


    override fun requestBodyEnd(call: Call, byteCount: Long) {
        Logger.logD(TAG, "requestBodyEnd curThread ${currentThread()}")
    }

    override fun requestFailed(call: Call, ioe: IOException) {
        Logger.logD(TAG, "requestFailed curThread ${currentThread()}")
    }

    override fun responseBodyStart(call: Call) {
        Logger.logD(TAG, "responseBodyStart curThread ${currentThread()}")
    }

    override fun responseBodyEnd(call: Call, byteCount: Long) {
        Logger.logD(TAG, "responseBodyEnd curThread ${currentThread()}")
    }

    override fun responseFailed(
        call: Call, ioe: IOException
    ) {
        Logger.logD(TAG, "responseFailed curThread ${currentThread()}")
    }

    override fun callEnd(
        call: Call
    ) {
        Logger.logD(TAG, "callEnd curThread ${currentThread()}")
    }

    override fun callFailed(
        call: Call, ioe: IOException
    ) {
        Logger.logD(TAG, "callFailed curThread ${currentThread()}")
    }

    override fun canceled(
        call: Call
    ) {
        Logger.logD(TAG, "canceled curThread ${currentThread()}")
    }

    class EventFactory : Factory {
        override fun create(call: Call): EventListener = OkHttpEventListener()
    }
}