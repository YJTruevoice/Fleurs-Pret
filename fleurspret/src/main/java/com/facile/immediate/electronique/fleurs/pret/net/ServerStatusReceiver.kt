package com.facile.immediate.electronique.fleurs.pret.net

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.arthur.commonlib.ability.Logger
import com.arthur.commonlib.ability.Toaster
import com.facile.immediate.electronique.fleurs.pret.net.NetConstant

/**
 * @Author Arthur
 * @Data 2022/8/10
 * @Description
 * 网络状态广播接收器
 */
class ServerStatusReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if (action == NetConstant.ServerStatusAction.STATUS_ACTION) {
            val code = intent.getIntExtra(NetConstant.ServerStatusKey.CODE, 0)
            val msg = intent.getStringExtra(NetConstant.ServerStatusKey.MSG)
            val url = intent.getStringExtra(NetConstant.ServerStatusKey.URL) ?: ""
            if (code == NetConstant.ErrorCode.ERROR_CODE_TOKEN_INVALID) {// 服务器提示需要登录
                Logger.logE("$msg")
                Toaster.showToast("$msg")
                // 如果token则将token失效的消息通知handler
                // todo 登录失效跳转登录页
            }
        }
    }
}