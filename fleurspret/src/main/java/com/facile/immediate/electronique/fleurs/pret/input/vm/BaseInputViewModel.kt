package com.facile.immediate.electronique.fleurs.pret.input.vm

import android.app.Application
import androidx.lifecycle.launchNet
import com.arthur.baselib.structure.mvvm.SingleLiveEvent
import com.facile.immediate.electronique.fleurs.pret.common.config.ConfigViewModel
import com.facile.immediate.electronique.fleurs.pret.common.json.model.JSONNeed
import com.facile.immediate.electronique.fleurs.pret.input.model.InputModel
import com.facile.immediate.electronique.fleurs.pret.mine.model.UserBasicEntity

open class BaseInputViewModel(app: Application) : ConfigViewModel<InputModel>(app) {

    val jsonNeedUpLiveData: SingleLiveEvent<JSONNeed?> = SingleLiveEvent()
    val jsonUpSucLiveData: SingleLiveEvent<Any?> = SingleLiveEvent()
    val userBasicLiveData: SingleLiveEvent<UserBasicEntity?> = SingleLiveEvent()

    fun preInputInfo(pageType: Int) {
        launchNet {
            mModel.preInputInfo(pageType = pageType)
        }.success {
            userBasicLiveData.value = it.aggressiveParentMethod
        }.showLoading(true).launch()
    }

    fun jsonNeed() {
        launchNet {
            mModel.isNeedJsonUp()
        }.success {
            jsonNeedUpLiveData.value = it.aggressiveParentMethod
        }.launch()
    }

    fun bigJsonUp() {
        launchNet {
            mModel.jsonFeature()
        }.success {
            jsonUpSucLiveData.value = it.aggressiveParentMethod
        }.showLoading(true).launch()
    }
}