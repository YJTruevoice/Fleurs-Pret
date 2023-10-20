package com.arthur.commonlib.ability

import android.app.Activity

object Loading : com.arthur.commonlib.ability.ILoading {

    private var loadingImpl: com.arthur.commonlib.ability.ILoading? = null

    fun init(loading: com.arthur.commonlib.ability.ILoading) {
        com.arthur.commonlib.ability.Loading.loadingImpl = loading
    }

    override fun startLoading(ac: Activity) {
        com.arthur.commonlib.ability.Loading.loadingImpl?.startLoading(ac)
    }

    override fun startLoading(ac: Activity, message: String) {
        com.arthur.commonlib.ability.Loading.loadingImpl?.startLoading(ac, message)
    }

    override fun closeLoading() {
        com.arthur.commonlib.ability.Loading.loadingImpl?.closeLoading()
    }

}

interface ILoading {
    fun startLoading(ac: Activity)
    fun startLoading(ac: Activity, message: String)
    fun closeLoading()
}