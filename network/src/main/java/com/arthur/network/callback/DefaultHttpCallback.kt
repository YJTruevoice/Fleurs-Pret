package com.arthur.network.callback

/**
 * 默认的请求回调
 */
abstract class DefaultHttpCallback<T> : IHttpCallback<T> {
    var cacheAble: Boolean = false

    override fun onReqStart() {}
    override fun onFinish() {}
    override fun onSuccess(result: T) {}
    override fun onError(t: Throwable?) {}
    override fun onCache(result: T) {}
}