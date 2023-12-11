package com.facile.immediate.electronique.fleurs.pret.utils

import com.arthur.commonlib.ability.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable

object RXUtils {
    fun asyncDo(runnable: Runnable?) {
        asyncDo(Callable {
            runnable?.run()
        },null)
    }

    fun <T : Any> asyncDo(callable: Callable<T?>, successCall: Consumer<T>?) {
        asyncDo(callable,successCall) {
            Logger.logE(it?.message ?: "")
        }
    }

    fun <T : Any> asyncDo(callable: Callable<T?>, successCall: Consumer<T>?, errorCallback:((Throwable?) -> Unit)?) {
        var subscribe = Observable.create<T> {emitter ->
            try {
                callable.call()?.let {
                    emitter.onNext(it)
                } ?: kotlin.run {
                    emitter.onError(EmptyData())
                }
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(successCall ?: Consumer {
                Logger.logE("RXUtils done with result ignored")
            }, Consumer {
                errorCallback?.invoke(it)
                Logger.logE(it.message ?: "")
            })
    }

    class EmptyData :java.lang.Exception()
}