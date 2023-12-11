package com.facile.immediate.electronique.fleurs.pret.common.consumer

import android.content.Context
import android.content.Intent
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.utils.AppUtils
import com.facile.immediate.electronique.fleurs.pret.common.user.UserManager
import im.crisp.client.ChatActivity
import im.crisp.client.Crisp


object CrispMgr {
    fun preSet() {
        resetChatSession()
        paramsSet()
    }

    private fun resetChatSession() {
        Crisp.resetChatSession(AppKit.context)
    }

    private fun paramsSet() {
        Crisp.setTokenID(UserManager.phoneNumber())//登录手机号
        Crisp.setUserPhone(UserManager.phoneNumber())//登录手机号
        //判断是否为测试账号，登录后返回字段testCustFlag
        if ("1" == UserManager.user()?.thoroughBriefGreatSomebody) {
            Crisp.setSessionSegment("test")
        } else {
            AppUtils.getPackageName(AppKit.context)?.let {
                AppUtils.getAppName(AppKit.context, it)?.let {
                    Crisp.setSessionSegment(it)
                }
            }//当前App名字 (appName)
        }
    }

    fun setEmail(email: String) {
        Crisp.setUserEmail(email);// 进件⻚⼿动输的邮箱
    }

    fun launchChat(context: Context) {
        paramsSet()
        val crispIntent = Intent(context, ChatActivity::class.java)
        context.startActivity(crispIntent)
    }
}