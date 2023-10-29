package com.facile.immediate.electronique.fleurs.pret.home.vm

import android.app.Application
import androidx.lifecycle.launchNet
import com.arthur.baselib.structure.mvvm.BaseViewModel
import com.arthur.baselib.structure.mvvm.SingleLiveEvent
import com.facile.immediate.electronique.fleurs.pret.home.ProductType
import com.facile.immediate.electronique.fleurs.pret.home.model.FirstModel
import com.facile.immediate.electronique.fleurs.pret.home.model.GlobalInfo
import com.facile.immediate.electronique.fleurs.pret.home.model.MultiP

class FirstViewModel(application: Application) : BaseViewModel<FirstModel>(application) {

    val prodTypeLiveData: SingleLiveEvent<ProductType> = SingleLiveEvent()
    val singleProHLiveData: SingleLiveEvent<GlobalInfo?> = SingleLiveEvent()
    val multiProHLiveData: SingleLiveEvent<List<MultiP>?> = SingleLiveEvent()
    val refreshCompleteLiveData: SingleLiveEvent<Boolean?> = SingleLiveEvent()

    var globalInfo: GlobalInfo? = null

    fun sOrM() {
        launchNet {
            mModel.multiProH()
        }.success { res ->
            res.aggressiveParentMethod?.let {
                if (it.size > 1) {
                    prodTypeLiveData.value = ProductType.M
                } else {
                    prodTypeLiveData.value = ProductType.S
                }
            }
        }.failed {
            prodTypeLiveData.value = ProductType.UNKNOWN
        }.showLoading(true).launch()
    }

    fun singleProH() {
        launchNet {
            mModel.singleProH("afraidDecemberSlimClassicalTechnology,brownTopic")
        }.success { res ->
            res.aggressiveParentMethod?.let {
                globalInfo = it
                singleProHLiveData.value = it
            }
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