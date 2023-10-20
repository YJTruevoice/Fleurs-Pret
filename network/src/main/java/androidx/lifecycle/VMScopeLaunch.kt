package androidx.lifecycle

import com.arthur.network.TaskCollector
import com.arthur.network.scope.LoadingCoroutineScope
import com.arthur.network.scope.MultiTaskCoroutineScope
import com.arthur.network.scope.builder.MultiTaskConfig
import com.arthur.network.scope.builder.NetScopeConfig
import com.arthur.network.scope.builder.ViewModelBuilder
import com.arthur.network.scope.builder.ViewModelMultiTaskBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * ViewModel调用
 * 内部的网络请求会跟随其作用域的生命周期
 * @param dispatcher 调度器
 * @param block 网络句柄
 * * 调用示例
 * ```
 * launchNet {
 *
 * }.success {
 *
 * }.failed {
 *
 * }.launch()
 * ```
 */
fun <T> ViewModel.launchNet(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: suspend () -> T?
): NetScopeConfig.NetBuilder<T> {
    val netScopeConfig = NetScopeConfig(block, dispatcher = dispatcher)
    val scope = LoadingCoroutineScope(netScopeConfig)
    this.addCloseable(scope)
    return ViewModelBuilder(netScopeConfig, scope)
}

/**
 * ## 多个任务异步进行
 * * ViewModel调用
 * * 内部的网络请求会跟随其作用域的生命周期
 * @param taskCollector 任务收集器
 * @param dispatcher 调度器
 * * 调用示例
 * ```
 * launchMultiTask(
 *      TaskCollector()
 *          .put("requestKey1") {  }
 *          .put("requestKey2") {  }
 *      ).finisher {
 *
 *      }.launch()
 * ```
 */
fun ViewModel.launchMultiTask(
    taskCollector: TaskCollector,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
): MultiTaskConfig.MultiTaskBuilder {
    val config = MultiTaskConfig(taskCollector.values, dispatcher = dispatcher)
    val scope = MultiTaskCoroutineScope(config)
    this.addCloseable(scope)
    return ViewModelMultiTaskBuilder(config, scope)
}
