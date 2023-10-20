package com.arthur.network.download

abstract class DownloadThrowableListener : DownloadListener {
    /**
     * 回调具体异常信息
     *
     * @param throwable
     */
    abstract fun onError(throwable: Throwable?)
}