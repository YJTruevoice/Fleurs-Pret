package com.arthur.network.scope.builder

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.arthur.network.scope.BaseCoroutineScope
import com.arthur.network.scope.MultiTaskCoroutineScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 *  * @Author Arthur
 *  * @Data 2023/5/23
 *  * @Description
 * ## 带缓存设置的config
 */
class MultiTaskConfig(
    val requests: Map<String, suspend () -> Any?>?,
    lifecycleOwner: LifecycleOwner? = null,
    lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseConfig(lifecycleOwner, lifeEvent, dispatcher) {

    /**
     * 所有任务结束
     */
    var allFinished: ((Map<String, Any?>?) -> Unit)? = null

    open class MultiTaskBuilder(private val config: MultiTaskConfig) {

        /**
         * 所有请求结束
         */
        fun finisher(finisher: ((Map<String, Any?>?) -> Unit)? = null): MultiTaskBuilder {
            this.config.allFinished = finisher
            return this
        }

        open fun launch(): BaseCoroutineScope {
            val scope = MultiTaskCoroutineScope(config)
            scope.launch()
            return scope
        }
    }
}