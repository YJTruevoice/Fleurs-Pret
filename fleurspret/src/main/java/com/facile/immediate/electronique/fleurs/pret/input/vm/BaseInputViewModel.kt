package com.facile.immediate.electronique.fleurs.pret.input.vm

import android.app.Application
import androidx.lifecycle.launchNet
import com.arthur.baselib.structure.mvvm.SingleLiveEvent
import com.facile.immediate.electronique.fleurs.pret.common.config.ConfigViewModel
import com.facile.immediate.electronique.fleurs.pret.input.model.InputModel
import com.facile.immediate.electronique.fleurs.pret.mine.model.UserBasicEntity

open class BaseInputViewModel(app: Application) : ConfigViewModel<InputModel>(app) {

    val userBasicLiveData: SingleLiveEvent<UserBasicEntity?> = SingleLiveEvent()

    fun preInputInfo(pageType:Int) {
        launchNet {
            mModel.preInputInfo(pageType = pageType)
        }.success {
            userBasicLiveData.value = it.aggressiveParentMethod
        }.showLoading(true).launch()
    }
}