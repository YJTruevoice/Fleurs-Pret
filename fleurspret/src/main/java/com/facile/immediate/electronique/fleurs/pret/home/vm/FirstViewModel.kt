package com.facile.immediate.electronique.fleurs.pret.home.vm

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.launchNet
import com.arthur.baselib.structure.mvvm.BaseViewModel
import com.arthur.baselib.structure.mvvm.SingleLiveEvent
import com.arthur.commonlib.ability.Toaster
import com.facile.immediate.electronique.fleurs.pret.home.model.FirstModel
import com.facile.immediate.electronique.fleurs.pret.home.model.GlobalInfo

class FirstViewModel(application: Application) : BaseViewModel<FirstModel>(application) {

    val globalInfoLiveData: SingleLiveEvent<GlobalInfo?> = SingleLiveEvent()

    var globalInfo: GlobalInfo? = null

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        getAppSettings()
    }

    private fun getAppSettings() {
        launchNet {
            mModel.appSetting("afraidDecemberSlimClassicalTechnology,brownTopic")
        }.success { res ->
            res.aggressiveParentMethod?.let {
                globalInfo = it
                globalInfoLiveData.value = it
            }
        }.failed {
            Toaster.showToast(it.message)
        }.launch()
    }
}