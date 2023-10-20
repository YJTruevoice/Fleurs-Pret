package com.arthur.network.scope.builder

import com.arthur.network.scope.BaseCoroutineScope

/**
 * * @Author Arthur
 * * @Data 2023/5/25
 * * @Description
 * ## ViewModel中多任务构建器
 */
class ViewModelMultiTaskBuilder(
    config: MultiTaskConfig,
    private val coroutineScope: BaseCoroutineScope
) : MultiTaskConfig.MultiTaskBuilder(config) {

    override fun launch(): BaseCoroutineScope {
        coroutineScope.launch()
        return coroutineScope
    }
}