package com.arthur.network.scope

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.arthur.network.runMain
import com.arthur.network.scope.builder.MultiTaskConfig
import com.arthur.network.withMain
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 *  * @Author Arthur
 *  * @Data 2023/3/27
 *  * @Description
 * ## 处理多个耗时任务的协程作用域
 */
class MultiTaskCoroutineScope(val taskConfig: MultiTaskConfig) : BaseCoroutineScope() {

    init {
        runMain {
            taskConfig.lifecycleOwner?.lifecycle?.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (taskConfig.lifeEvent == event) cancel()
                }
            })
        }
    }


    override val coroutineContext: CoroutineContext = taskConfig.dispatcher + SupervisorJob()


    override fun launch() {
        launch {
            val results = taskConfig.requests?.toList()?.map {
                async {
                    Pair(it.first, runCatching { it.second.invoke() }.getOrNull())
                }
            }?.awaitAll()
            withMain {
                taskConfig.allFinished?.invoke(results?.toMap())
            }
        }
    }
}

