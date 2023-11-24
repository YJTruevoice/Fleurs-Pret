package com.facile.immediate.electronique.fleurs.pret.order.vm

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.launchNet
import com.arthur.baselib.structure.mvvm.BaseViewModel
import com.arthur.baselib.structure.mvvm.SingleLiveEvent
import com.facile.immediate.electronique.fleurs.pret.order.model.Ord
import com.facile.immediate.electronique.fleurs.pret.order.model.OrdModel

class SecondViewModel(app: Application) : BaseViewModel<OrdModel>(app) {

    val ordArrLiveEvent: SingleLiveEvent<List<Ord>?> = SingleLiveEvent()

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        ordArr()
    }

    private fun ordArr() {
        launchNet {
            mModel.ordArr()
        }.success {
            ordArrLiveEvent.value = it.aggressiveParentMethod
        }.showLoading(true).launch()
    }
}