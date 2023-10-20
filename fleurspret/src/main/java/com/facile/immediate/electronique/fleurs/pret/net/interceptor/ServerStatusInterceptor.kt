package com.facile.immediate.electronique.fleurs.pret.net.interceptor

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.utils.json.JsonUtils
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import com.facile.immediate.electronique.fleurs.pret.net.NetConstant
import okhttp3.Interceptor
import okhttp3.Response
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * @Author Arthur
 * @Data 2022/8/4
 * @Description
 * 接口返回状态拦截器，某些特殊状态直接广播发给上层处理
 */
class ServerStatusInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        return responseCast(response)
    }

    private fun responseCast(response: Response): Response {
        val source = response.body?.source()
        try {
            source?.let {
                it.request(Long.MAX_VALUE)
                val buffer = source.buffer
                val contentType = response.body?.contentType()
                val charset: Charset =
                    contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
                val resultString = buffer.clone().readString(charset)

                val baseResponse: BaseResponse<*>? =
                    JsonUtils.fromJson(resultString, BaseResponse::class.java)
                if (baseResponse != null) {
                    if (baseResponse.code != NetConstant.SuccessCode.CODE_SUCCESS) { // 其余非0的code码都播出去
                        val intent = Intent()
                        intent.putExtra(NetConstant.ServerStatusKey.CODE, baseResponse.code)
                        intent.putExtra(NetConstant.ServerStatusKey.MSG, baseResponse.msg)
                        intent.putExtra(
                            NetConstant.ServerStatusKey.URL,
                            response.request.url.toUrl().toString()
                        )
                        intent.action = NetConstant.ServerStatusAction.STATUS_ACTION
                        LocalBroadcastManager.getInstance(AppKit.context).sendBroadcast(intent)
                    }
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
        return response
    }
}