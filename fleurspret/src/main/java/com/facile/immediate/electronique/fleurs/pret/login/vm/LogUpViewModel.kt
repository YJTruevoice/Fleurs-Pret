package com.facile.immediate.electronique.fleurs.pret.login.vm

import android.app.Application
import androidx.lifecycle.launchNet
import com.arthur.baselib.structure.mvvm.BaseViewModel
import com.arthur.baselib.structure.mvvm.SingleLiveEvent
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.ability.Toaster
import com.arthur.commonlib.utils.StringUtil
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.consumer.CrispMgr
import com.facile.immediate.electronique.fleurs.pret.common.event.UserInfoUpdate
import com.facile.immediate.electronique.fleurs.pret.common.user.UserManager
import com.facile.immediate.electronique.fleurs.pret.login.model.LogUpModel
import com.facile.immediate.electronique.fleurs.pret.login.model.UserInfoEntity
import com.facile.immediate.electronique.fleurs.pret.login.model.VerifyCodeEntity
import org.greenrobot.eventbus.EventBus

class LogUpViewModel(application: Application) : BaseViewModel<LogUpModel>(application) {

    val verifyCodeLiveData: SingleLiveEvent<VerifyCodeEntity?> = SingleLiveEvent()
    val logUpSuccessLiveData: SingleLiveEvent<UserInfoEntity?> = SingleLiveEvent()

    var phone = ""
    var code:CharSequence= ""

    fun getVerifyCode() {
        if (StringUtil.isEmpty(phone)) {
            Toaster.showToast(AppKit.context.getString(R.string.text_le_num_ro_de_t_l_phone_portable_est_vide))
            return
        }
        launchNet {
            mModel.getVerifyCode("+221$phone")
        }.success {
            it.aggressiveParentMethod?.let { verifyCode ->
                code = verifyCode.dearProperArgument ?: ""
                verifyCodeLiveData.value = verifyCode
            }
        }.showErrorTip(true).launch()
    }

    fun logUp() {
        if (StringUtil.isEmpty(phone)) {
            Toaster.showToast(AppKit.context.getString(R.string.text_le_num_ro_de_t_l_phone_portable_est_vide))
            return
        }

        if (StringUtil.isEmpty(code.toString())) {
            Toaster.showToast(AppKit.context.getString(R.string.text_le_code_de_v_rification_est_vide))
            return
        }

        launchNet {
            mModel.logUp("+221$phone", code.toString())
        }.success {
            it.aggressiveParentMethod?.let { user ->
                user.usualExtraordinaryScholarshipQuickHardship = "+221$phone"
                UserManager.saveLocal(user)
                UserManager.savePhone(phone)
                CrispMgr.preSet()
                EventBus.getDefault().post(UserInfoUpdate())
                logUpSuccessLiveData.value = user
            }
        }.showErrorTip(true).launch()
    }
}