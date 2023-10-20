package com.arthur.network.callback

import io.reactivex.subscribers.ResourceSubscriber

/**
 * 默认订阅
 */
abstract class DefaultHttpSubscriber<T> : ResourceSubscriber<T>(), IHttpCallback<T> {
    var cacheAble: Boolean = false

    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onComplete() {
        onFinish()
    }

    override fun onStart() {
        super.onStart()
        onReqStart()
    }

    override fun onReqStart() {
    }

    override fun onFinish() {
    }

    override fun onCache(result: T) {
    }
}