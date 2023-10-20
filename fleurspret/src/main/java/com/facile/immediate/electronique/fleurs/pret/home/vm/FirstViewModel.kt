package com.facile.immediate.electronique.fleurs.pret.home.vm

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.launchNet
import com.arthur.baselib.structure.mvvm.BaseViewModel
import com.arthur.commonlib.ability.Logger
import com.facile.immediate.electronique.fleurs.pret.home.model.FirstModel

class FirstViewModel(application: Application) : BaseViewModel<FirstModel>(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    override fun processLogic() {
        super.processLogic()
        getAppSettings()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        getAppSettings()
    }

    private fun getAppSettings() {
        launchNet {
            mModel.appSetting("")
        }.success {
            Logger.logI(it.data.toString())
        }.failed {
            Logger.logE(it.message)
        }.launch()
    }
}