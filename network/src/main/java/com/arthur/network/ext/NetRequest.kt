package com.arthur.network.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.arthur.network.Net
import com.arthur.network.TaskCollector
import com.arthur.network.scope.builder.MultiTaskConfig
import com.arthur.network.scope.builder.NetScopeConfig
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.subscribers.ResourceSubscriber
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 *  * @Author Arthur
 *  * @Data 2022/8/11
 *  * @Description
 * ### 提供几个Rx请求便捷方式
 */

/**
 * 发起一个网络请求，通过[ResourceSubscriber]进行接收并去处理数据
 *
 * @param resourceSubscriber 接收并处理网络结果的ResourceSubscriber，处理网络正常相应的数据
 * @return Disposable 用于释放网络请求的资源
 */
fun <T> Flowable<T>.request(resourceSubscriber: ResourceSubscriber<T>): Disposable {
    return Net.client.request(this, resourceSubscriber)
}

/**
 * 发起一个支持延时的网络请求，通过[ResourceSubscriber]进行接收并去处理数据
 *
 * @param resourceSubscriber 接收并处理网络结果的ResourceSubscriber，处理网络正常相应的数据
 * @param delayTime          延迟发送时间，单位毫秒
 * @return Disposable，用于释放网络请求的资源
 */
fun <T> Flowable<T>.request(resourceSubscriber: ResourceSubscriber<T>, delayTime: Long): Disposable {
    return Net.client.request(this, resourceSubscriber, delayTime)
}

/**
 * 增加一个具有延迟回调的请求 可以设置延时请求开始的监听
 * @param resourceSubscriber 接收并处理网络结果的ResourceSubscriber，处理网络正常相应的数据
 * @param delayTime          延迟发送时间，单位毫秒
 * @param delayListener      延时请求开始的监听
 * @return Disposable 用于释放网络请求的资源
 */
fun <T> Flowable<T>.request(
    resourceSubscriber: ResourceSubscriber<T>, delayTime: Long,
    delayListener: Net.DelayListener?
): Disposable {
    return Net.client.request(this, resourceSubscriber, delayTime, delayListener)
}


/*************************************网络请求 协程调用扩展****************************************/

/**
 * ##调起网络请求作用域 开始网络请求
 * *该作用域生命周期跟随整个应用, 注意内存泄漏
 * @param dispatcher 调度器
 * @param block 网络请求句柄
 * * 调用示例：
 * ```
 * scopeNet{
 *
 * }.success{
 *
 * }.failed {
 *
 * }.launch()
 * ```
 */
fun <T> scopeNet(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        block: suspend () -> T?
): NetScopeConfig.NetBuilder<T> {
    return NetScopeConfig.NetBuilder(NetScopeConfig(block, dispatcher = dispatcher))
}

/**
 * ## 该函数比[scopeNet]多了自动取消作用域功能
 *
 * 该作用域生命周期跟随LifecycleOwner. 比如传入Activity会默认在 FragmentActivity.onDestroy 时取消网络请求.
 * @receiver 可传入FragmentActivity/AppCompatActivity, 或者其他的实现了LifecycleOwner的类
 * @param lifeEvent 取消请求时机
 * @param dispatcher 调度器
 * @param block 网络请求句柄
 * * 调用示例：
 * ```
 * scopeNetLife {
 * }.success {
 * }.failed {
 * }.launch()
 * ```
 */
fun <T> LifecycleOwner.scopeNetLife(
    lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: suspend () -> T?
): NetScopeConfig.NetBuilder<T> {
    return NetScopeConfig.NetBuilder(NetScopeConfig(block, this, lifeEvent, dispatcher))
}

/**
 * ## 多个任务异步进行
 * * 该作用域生命周期跟随整个应用, 注意内存泄漏,慎用！！
 * @param taskCollector 任务收集器
 * @param dispatcher 调度器
 * * 调用示例
 * ```
 * scopeMultiTask {
 *      TaskCollector()
 *          .put("requestKey1") {  }
 *          .put("requestKey2") {  }
 *      ).finisher {
 *
 *      }.launch()
 * ```
 */
fun scopeMultiTask(
    taskCollector: TaskCollector,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): MultiTaskConfig.MultiTaskBuilder {
    return MultiTaskConfig.MultiTaskBuilder(MultiTaskConfig(taskCollector.values, dispatcher = dispatcher))
}


/**
 * ## 多个任务异步进行
 * * 该作用域生命周期跟随LifecycleOwner. 比如传入Activity会默认在 FragmentActivity.onDestroy 时取消网络请求.
 * @receiver 可传入FragmentActivity/AppCompatActivity, 或者其他的实现了LifecycleOwner的类
 * @param taskCollector 任务收集器[TaskCollector]
 * @param lifeEvent 取消请求时机
 * @param dispatcher 调度器
 * * 调用示例
 * ```
 * scopeMultiTaskLife(
 *      TaskCollector()
 *          .put("requestKey1") {  }
 *          .put("requestKey2") {  }
 *      ).finisher {
 *
 *      }.launch()
 * ```
 */
fun LifecycleOwner.scopeMultiTaskLife(
    taskCollector: TaskCollector,
    lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): MultiTaskConfig.MultiTaskBuilder {
    return MultiTaskConfig.MultiTaskBuilder(MultiTaskConfig(taskCollector.values, this, lifeEvent, dispatcher))
}



