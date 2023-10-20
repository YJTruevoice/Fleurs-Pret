package com.arthur.baselib.structure.base.io

import io.reactivex.Flowable
import io.reactivex.subscribers.DisposableSubscriber

/**
 *
 * created by guo.lei on 2022.04.12
 * io能力
 */
interface IIObility {
    /**
     * RXjava提供的线程切换能力
     */
    fun <T> subscribe(
        observable: Flowable<T>,
        observer: DisposableSubscriber<T>
    ): DisposableSubscriber<T>
}