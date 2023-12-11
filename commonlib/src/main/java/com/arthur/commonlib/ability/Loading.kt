package com.arthur.commonlib.ability

import android.app.Activity

object Loading : ILoading {

    private var loadingImpl: ILoading? = null

    fun init(loading: ILoading) {
        loadingImpl = loading
    }

    override fun startLoading(ac: Activity) {
        loadingImpl?.startLoading(ac)
    }

    override fun startLoading(ac: Activity, message: String) {
        loadingImpl?.startLoading(ac, message)
    }

    override fun closeLoading() {
        loadingImpl?.closeLoading()
    }

}

interface ILoading {
    fun startLoading(ac: Activity)
    fun startLoading(ac: Activity, message: String)
    fun closeLoading()
}