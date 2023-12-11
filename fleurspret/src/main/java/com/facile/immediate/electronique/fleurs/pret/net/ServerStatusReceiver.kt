package com.facile.immediate.electronique.fleurs.pret.net

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.arthur.commonlib.ability.Logger
import com.arthur.commonlib.ability.Toaster
import com.facile.immediate.electronique.fleurs.pret.common.user.UserManager
import com.facile.immediate.electronique.fleurs.pret.login.LogUpActivity

/**
 * @Author Arthur
 * @Data 2022/8/10
 * @Description
 * 网络状态广播接收器
 */
class ServerStatusReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if (action == NetBizConstant.ServerStatusAction.STATUS_ACTION) {
            val code = intent.getIntExtra(NetBizConstant.ServerStatusKey.CODE, 0)
            val msg = intent.getStringExtra(NetBizConstant.ServerStatusKey.MSG)
            val url = intent.getStringExtra(NetBizConstant.ServerStatusKey.URL) ?: ""
            if (code == NetBizConstant.ErrorCode.ERROR_CODE_TOKEN_INVALID) {// 服务器提示需要登录
                Logger.logE("$msg")
                Toaster.showToast("$msg")
                // 如果token则将token失效的消息通知handler
                // 登录失效跳转登录页
                UserManager.logout()
                context?.startActivity(Intent(context, LogUpActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }
}