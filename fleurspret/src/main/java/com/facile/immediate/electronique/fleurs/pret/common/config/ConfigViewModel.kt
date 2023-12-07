package com.facile.immediate.electronique.fleurs.pret.common.config

import android.app.Application
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.launchNet
import com.arthur.baselib.structure.base.IBaseModel
import com.arthur.baselib.structure.mvvm.BaseViewModel
import com.arthur.baselib.structure.mvvm.SingleLiveEvent
import com.facile.immediate.electronique.fleurs.pret.BuildConfig
import com.facile.immediate.electronique.fleurs.pret.input.model.Region
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr
import java.util.concurrent.atomic.AtomicBoolean

open class ConfigViewModel<M : IBaseModel>(app: Application) : BaseViewModel<M>(app) {

    private val configService = NetMgr.get().service<ConfigAPI>()

    val configLiveData: SingleLiveEvent<Pair<ConfigType, List<CommonConfigItem>?>> =
        SingleLiveEvent()

    val provinceLiveData: SingleLiveEvent<List<Region>?> = SingleLiveEvent()
    val cityLiveData: SingleLiveEvent<List<Region>?> = SingleLiveEvent()
    val distinctLiveData: SingleLiveEvent<List<Region>?> = SingleLiveEvent()

    val textWatcherLiveData: SingleLiveEvent<Editable?> = SingleLiveEvent()


    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            textWatcherLiveData.value = s
        }
    }

    private var configing = AtomicBoolean(false)

    fun config(target: ConfigType) {
        if (configing.get()) return
        configing.set(true)
        launchNet {
            configService.config(target.name)
        }.success {
            configLiveData.value = Pair(target, it.aggressiveParentMethod)
        }.finished {
            configing.set(false)
        }.showLoading(true).launch()
    }


    fun province(result: ((List<Region>?) -> Unit)? = null) {
        launchNet {
            configService.region("-1", "1")
        }.success {
            provinceLiveData.value = it.aggressiveParentMethod
            result?.invoke(it.aggressiveParentMethod)
        }.showLoading(true).launch()
    }

    fun cities(provinceId: String, result: ((List<Region>?) -> Unit)? = null) {
        launchNet {
            configService.region(provinceId, "2")
        }.success {
            cityLiveData.value = it.aggressiveParentMethod
            result?.invoke(it.aggressiveParentMethod)
        }.showLoading(true).launch()
    }

    fun district(cityId: String, result: ((List<Region>?) -> Unit)? = null) {
        launchNet {
            configService.region(cityId, "3")
        }.success {
            distinctLiveData.value = it.aggressiveParentMethod
            result?.invoke(it.aggressiveParentMethod)
        }.showLoading(true).launch()
    }
}