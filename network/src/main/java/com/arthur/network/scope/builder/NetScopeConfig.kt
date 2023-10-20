package com.arthur.network.scope.builder

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.arthur.network.cache.CacheStrategy
import com.arthur.network.callback.DefaultHttpCallback
import com.arthur.network.model.ErrorInfo
import com.arthur.network.scope.BaseCoroutineScope
import com.arthur.network.scope.LoadingCoroutineScope
import com.arthur.network.scope.NetCoroutineScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 *  * @Author Arthur
 *  * @Data 2023/5/18
 *  * @Description
 * ## 网络请求配置
 */
open class NetScopeConfig<T>(
    var request: suspend () -> T?,
    lifecycleOwner: LifecycleOwner? = null,
    lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseConfig(lifecycleOwner, lifeEvent, dispatcher) {

    /*******************callBack*******************/
    /**
     * 请求回调 外层设置来监听请求状态
     */
    var callback: DefaultHttpCallback<T>? = null

    /**
     * 请求开始
     */
    open var reqStart: (() -> Unit)? = null

    /**
     * 请求成功
     */
    open var success: ((T) -> Unit)? = null

    /**
     * 请求失败
     */
    open var failed: ((ErrorInfo) -> Unit)? = null

    /**
     * 缓存
     */
    open var cache: ((T) -> Unit)? = null

    /**
     * 请求结束
     */
    open var finished: ((Throwable?) -> Unit)? = null
    /*******************callBack*******************/

    /*******************请求过程额外配置*******************/
    /**
     * 是否开启错误提示
     */
    open var showErrorTip: Boolean = true

    /**
     * 是否开启Loading
     */
    open var showLoading: Boolean = false

    /*******************请求过程额外配置*******************/

    /*******************缓存相关配置*******************/
    /**
     * 缓存KEY
     */
    open var cacheKey: String = ""

    /**
     * 缓存策略
     */
    open var cacheStrategy: CacheStrategy? = null

    /**
     * 读取缓存成功后, 网络请求失败是否处理错误信息
     */
    open var cacheBreakError = false

    /**
     * 读取缓存成功后是否关闭加载动画
     */
    open var cacheBreakLoading: Boolean = true

    /**
     * 可以自定义缓存执行逻辑，在此作用域会优先执行
     */
    var cacheReader: (suspend () -> T?)? = null

    /**
     * 是否需要缓存
     */
    val needCache: Boolean
        get() {
            return cacheStrategy != null && cacheStrategy != CacheStrategy.ONLINE_ONLY
        }

    /**
     * 是否需要真的网络请求成功回调
     */
    val needSuccessCallback: Boolean
        get() {
            // 不需要缓存的时候
            // 有缓存并且缓存策略是[CacheStrategy.CACHE_FIRST]的时候
            // 有缓存并且缓存策略是[CacheStrategy.CACHE_ONLY] 但是缓存读取失败的时候
            return !needCache
                    || cacheStrategy == CacheStrategy.CACHE_FIRST
                    || (cacheStrategy == CacheStrategy.CACHE_ONLY && !cacheSucceed)
        }

    /**
     * 是否读取缓存成功
     */
    var cacheSucceed = false

    /*******************缓存相关配置*******************/

    open class NetBuilder<T>(open val config: NetScopeConfig<T>) {

        fun callback(callback: DefaultHttpCallback<T>?): NetBuilder<T> {
            this.config.callback = callback
            return this
        }

        fun start(block: (() -> Unit)? = null): NetBuilder<T> {
            this.config.reqStart = block
            return this
        }

        fun success(block: ((T) -> Unit)? = null): NetBuilder<T> {
            this.config.success = block
            return this
        }

        fun failed(block: ((ErrorInfo) -> Unit)? = null): NetBuilder<T> {
            this.config.failed = block
            return this
        }

        fun cache(block: ((T) -> Unit)? = null): NetBuilder<T> {
            this.config.cache = block
            return this
        }

        fun finished(block: ((Throwable?) -> Unit)? = null): NetBuilder<T> {
            this.config.finished = block
            return this
        }

        fun showErrorTip(showErrorTip: Boolean): NetBuilder<T> {
            this.config.showErrorTip = showErrorTip
            return this
        }

        fun showLoading(showLoading: Boolean): NetBuilder<T> {
            this.config.showLoading = showLoading
            return this
        }

        /**
         * # 启用缓存读取
         * @param cacheKey 缓存Key,目前先有外部传入，请确保唯一性
         * @param cacheStrategy 缓存策略
         * @param breakError 读取缓存成功后不再处理错误信息
         * @param breakLoading 读取缓存成功后结束加载动画
         * @param cacheReader 自定义缓存执行逻辑，在此作用域会优先执行
         *
         * 这里指的读取缓存也可以替换为其他任务, 比如读取数据库或者其他接口数据
         */
        fun cacheReader(
            cacheKey: String = "",
            cacheStrategy: CacheStrategy = CacheStrategy.CACHE_FIRST,
            breakError: Boolean = false,
            breakLoading: Boolean = true,
            cacheReader: (suspend () -> T)? = null
        ): NetBuilder<T> {
            this.config.cacheKey = cacheKey
            this.config.cacheStrategy = cacheStrategy
            this.config.cacheBreakError = breakError
            this.config.cacheBreakLoading = breakLoading
            this.config.cacheReader = cacheReader
            return this
        }

        open fun launch(): BaseCoroutineScope {
            val scope = if (config.showLoading) {
                LoadingCoroutineScope<T>(config)
            } else {
                NetCoroutineScope<T>(config)
            }
            scope.launch()
            return scope
        }
    }
}