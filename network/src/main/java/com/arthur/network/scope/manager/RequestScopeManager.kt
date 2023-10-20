package com.arthur.network.scope.manager

import com.arthur.commonlib.ability.Logger
import com.arthur.network.listener.RequestMethodInvokeObserver

/**
 *  * @Author Arthur
 *  * @Data 2023/5/23
 *  * @Description
 * ## 网络请求作用域观察者管理器，主要是用于将网络请求的信息分发给每个发起的作用域
 * * 从[retrofit2.InvocationInterceptor]分发出来
 */
object RequestScopeManager {
    const val TAG = "RequestScopeManager"

    private fun currentThread(): String {
        Thread.currentThread().apply {
            return "${toString()} ${hashCode()}"
        }
    }

    /**
     * 调用监听
     */
    private val invokeObservers = InheritableThreadLocal<RequestMethodInvokeObserver>()

    /**
     * 注册当前线程的observer
     */
    fun register(scope: RequestMethodInvokeObserver) {
        Logger.logI(TAG, "register:$scope curThread ${currentThread()}")
        invokeObservers.set(scope)
    }

    /**
     * 获取当前observer
     */
    fun getObserver(): RequestMethodInvokeObserver? {
        val observer = invokeObservers.get()
        Logger.logI(TAG, "getObserver:$observer curThread ${currentThread()}")
        return observer
    }

    /**
     * 解注当前线程的observer
     */
    fun unregister() {
        Logger.logI(TAG, "unregister curThread ${currentThread()}")
        invokeObservers.remove()
    }

}