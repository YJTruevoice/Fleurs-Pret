package com.arthur.network.scope.builder

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 *  * @Author Arthur
 *  * @Data 2023/5/24
 *  * @Description
 * ## Base config
 */
open class BaseConfig(
    /**
     * 指定LifecycleOwner处于生命周期下取消网络请求/作用域
     */
    open var lifecycleOwner: LifecycleOwner? = null,

    /**
     * 销毁时机
     */
    open var lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,

    /**
     * 调度器, 默认运行在[Dispatchers.Main]即主线程下
     */
    open var dispatcher: CoroutineDispatcher = Dispatchers.IO
)