package com.arthur.commonlib.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.arthur.commonlib.ability.Toaster.showToast

class SystemIntentUtils {

    companion object {
        fun gotoStore(context: Context) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse("market://details?id=" + context.packageName)
            //存在手机里没安装应用市场的情况，跳转会包异常，做一个接收判断
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                showToast("跳转应用市场失败")
            }
        }
    }

}