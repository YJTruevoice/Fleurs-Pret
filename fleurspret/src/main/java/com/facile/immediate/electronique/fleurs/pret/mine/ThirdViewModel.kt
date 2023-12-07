package com.facile.immediate.electronique.fleurs.pret.mine

import android.app.Application
import androidx.lifecycle.launchNet
import com.arthur.baselib.structure.mvvm.BaseViewModel
import com.arthur.baselib.structure.mvvm.SingleLiveEvent
import com.facile.immediate.electronique.fleurs.pret.common.user.UserManager
import com.facile.immediate.electronique.fleurs.pret.mine.model.ThirdModel
import com.facile.immediate.electronique.fleurs.pret.common.user.UserBasicEntity

class ThirdViewModel(app: Application) : BaseViewModel<ThirdModel>(app) {

    val userNameLiveData: SingleLiveEvent<UserBasicEntity?> = SingleLiveEvent()
    val userHeadImgLiveData: SingleLiveEvent<String?> = SingleLiveEvent()

    fun getUserName() {
        launchNet {
            mModel.getUserInfoBasic()
        }.success {
            it.aggressiveParentMethod?.let { userBasic ->
                userNameLiveData.value = userBasic
            }
        }.launch()
    }

    fun getUserHeadPortrait() {
        launchNet {
            mModel.getUserHeadPortrait()
        }.success {
            it.aggressiveParentMethod?.let { userBasic ->
                userHeadImgLiveData.value = userBasic.everydayRainfallCookieGuidance
            }
        }.launch()
    }

    fun quit() {
        // 退出登录
        UserManager.logout()
    }


}