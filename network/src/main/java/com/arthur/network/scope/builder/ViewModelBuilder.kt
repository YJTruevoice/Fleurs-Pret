package com.arthur.network.scope.builder

import com.arthur.network.scope.BaseCoroutineScope

/**
 * * @Author Arthur
 * * @Data 2023/5/24
 * * @Description
 * ## ViewModel中请求网络的网络构建器
 */
class ViewModelBuilder<T>(
    config: NetScopeConfig<T>,
    private val coroutineScope: BaseCoroutineScope
) : NetScopeConfig.NetBuilder<T>(config) {

    override fun launch(): BaseCoroutineScope {
        coroutineScope.launch()
        return coroutineScope
    }
}