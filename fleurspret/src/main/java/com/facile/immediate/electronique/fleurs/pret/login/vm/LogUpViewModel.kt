package com.facile.immediate.electronique.fleurs.pret.login.vm

import android.app.Application
import androidx.lifecycle.launchNet
import com.arthur.baselib.structure.mvvm.BaseViewModel
import com.arthur.baselib.structure.mvvm.SingleLiveEvent
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.ability.Toaster
import com.arthur.commonlib.utils.StringUtil
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.UserManager
import com.facile.immediate.electronique.fleurs.pret.login.model.LogUpModel
import com.facile.immediate.electronique.fleurs.pret.login.model.UserInfoEntity
import com.facile.immediate.electronique.fleurs.pret.login.model.VerifyCodeEntity

class LogUpViewModel(application: Application) : BaseViewModel<LogUpModel>(application) {

    val verifyCodeLiveData: SingleLiveEvent<VerifyCodeEntity?> = SingleLiveEvent()
    val logUpSuccessLiveData: SingleLiveEvent<UserInfoEntity?> = SingleLiveEvent()

    var phone = ""
    var code = ""

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
        }.failed {
            Toaster.showToast(it.message)
        }.launch()
    }

    fun logUp() {
        if (StringUtil.isEmpty(phone)) {
            Toaster.showToast(AppKit.context.getString(R.string.text_le_num_ro_de_t_l_phone_portable_est_vide))
            return
        }

        if (StringUtil.isEmpty(code)) {
            Toaster.showToast(AppKit.context.getString(R.string.text_le_code_de_v_rification_est_vide))
            return
        }

        launchNet {
            mModel.logUp("+221$phone", code)
        }.success {
            it.aggressiveParentMethod?.let { user ->
                UserManager.saveLocal(user)
                logUpSuccessLiveData.value = user
            }
        }.failed {
            Toaster.showToast(it.message)
        }.launch()
    }
}