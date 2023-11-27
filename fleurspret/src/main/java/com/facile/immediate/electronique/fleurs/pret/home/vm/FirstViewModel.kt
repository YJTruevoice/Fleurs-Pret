package com.facile.immediate.electronique.fleurs.pret.home.vm

import android.app.Application
import androidx.lifecycle.launchNet
import com.arthur.baselib.structure.mvvm.BaseViewModel
import com.arthur.baselib.structure.mvvm.SingleLiveEvent
import com.facile.immediate.electronique.fleurs.pret.common.setting.GlobalSetting
import com.facile.immediate.electronique.fleurs.pret.home.model.FirstModel
import com.facile.immediate.electronique.fleurs.pret.home.model.MultiP
import com.facile.immediate.electronique.fleurs.pret.home.model.ProInfo

class FirstViewModel(application: Application) : BaseViewModel<FirstModel>(application) {

    val prodMultiTypeLiveData: SingleLiveEvent<List<MultiP>> = SingleLiveEvent()
    val prodSingleTypeLiveData: SingleLiveEvent<ProInfo> = SingleLiveEvent()
    val multiProHLiveData: SingleLiveEvent<List<MultiP>?> = SingleLiveEvent()
    val refreshCompleteLiveData: SingleLiveEvent<Boolean?> = SingleLiveEvent()
    val globalSettingLiveData: SingleLiveEvent<GlobalSetting?> = SingleLiveEvent()

    var globalSetting: GlobalSetting? = null

    fun sOrM() {
        launchNet {
            mModel.multiProH()
        }.success { res ->
            res.aggressiveParentMethod?.let {
                if (it.size > 1) {
                    prodMultiTypeLiveData.value = it
                } else if (it.size == 1) {
                    prodSingleTypeLiveData.value = it[0]
                }
            }
        }.showLoading(true).launch()
    }

    fun globalSetting(keys: String) {
        launchNet {
            mModel.globalSetting(keys)
        }.success { res ->
            globalSetting = res.aggressiveParentMethod
            globalSettingLiveData.value = res.aggressiveParentMethod
        }.finished {
            refreshCompleteLiveData.value = true
        }.launch()
    }

    fun multiProH() {
        launchNet {
            mModel.multiProH()
        }.success { res ->
            res.aggressiveParentMethod?.let {
                multiProHLiveData.value = it
            }
        }.finished {
            refreshCompleteLiveData.value = true
        }.launch()
    }
}