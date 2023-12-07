package com.arthur.network.scope

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.arthur.commonlib.ability.Logger
import com.arthur.commonlib.ability.Toaster
import com.arthur.commonlib.utils.json.JsonUtils
import com.arthur.network.Net
import com.arthur.network.NetConstant
import com.arthur.network.cache.NetCacheHandler
import com.arthur.network.errorprocessor.BaseApiErrorPreprocessor
import com.arthur.network.errorprocessor.IApiErrorPreprocessor
import com.arthur.network.exception.IllegalDataException
import com.arthur.network.listener.RequestMethodInvokeObserver
import com.arthur.network.runMain
import com.arthur.network.scope.builder.NetScopeConfig
import com.arthur.network.scope.manager.RequestScopeManager
import com.arthur.network.withMain
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import okhttp3.Headers
import okhttp3.RequestBody
import java.lang.reflect.Type
import java.net.URL
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * * @Author Arthur
 * * @Data 2022/9/22
 * * @Description
 *
 * ## 异步协程作用域
 * @param netConfig 请求配置
 */
open class NetCoroutineScope<T>(
    val netConfig: NetScopeConfig<T>
) : BaseCoroutineScope(), RequestMethodInvokeObserver {

    init {
        runMain {
            netConfig.lifecycleOwner?.lifecycle?.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (netConfig.lifeEvent == event) {
                        cancel()
                    }
                }
            })
        }
    }

    /**
     * 异常预处理
     */
    open val errorPreprocessor: IApiErrorPreprocessor = BaseApiErrorPreprocessor()

    /**
     * 异常处理器
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        runMain {
            handleError(throwable)
        }
    }

    override val coroutineContext: CoroutineContext =
        netConfig.dispatcher + exceptionHandler + SupervisorJob()

    /**
     * 最终使用的cache key
     */
    private var internalCacheKey = ""

    override fun launch() {
        launch(EmptyCoroutineContext) {
            withMain {
                start()
            }
            RequestScopeManager.register(this@NetCoroutineScope)
            val result: T? = netConfig.request.invoke()
            RequestScopeManager.unregister()
            withMain {
                handleSuccess(result)
            }
            if (netConfig.needCache) {
                JsonUtils.toJsonString(result)?.let {
                    NetCacheHandler.write(internalCacheKey, it)
                    Logger.logI(TAG, "写入缓存")
                }
            }
        }.invokeOnCompletion {
            runMain {
                finish(it)
            }
        }
    }

    /**
     * 错误处理
     */
    open fun handleError(t: Throwable) {
        t.printStackTrace()// 打印异常堆栈

        if (Net.client.netOptions.isDebug
            && t is java.lang.IllegalArgumentException
            && t.message?.contains("Unable to create call adapter for") == true
        ) {
            Toaster.showToast("你的网络请求句柄不是挂起（suspend）方法")
        }
        error(t)
    }

    override fun cancel(cause: CancellationException?) {
        val job = coroutineContext[Job]
            ?: error("Scope cannot be cancelled because it does not have a job: $this")
        job.cancel(cause)
    }

    override fun close() {
        cancel()
    }

    /**
     * 处理成功之后的数据
     */
    private fun handleSuccess(result: T?) {
        result?.let {
            success(it)
        } ?: handleError(
            IllegalDataException(
                msg = Net.client.netOptions.tipDataEmptyError
                    ?: NetConstant.ErrorPromptMsg.DATA_EMPTY_ERROR
            )
        )
    }

    /**
     *     override fun onReqStart() {}
     *     override fun onSuccess(result: T) {}
     *     override fun onCache(result: T) {}
     *     override fun onError(t: Throwable?) {}
     *     override fun onFinish() {}
     */
    open fun start() {
        netConfig.reqStart?.invoke()
        netConfig.callback?.onReqStart()
    }

    open fun success(result: T) {
        if (netConfig.needSuccessCallback) {
            netConfig.success?.invoke(result)
            netConfig.callback?.onSuccess(result)
            Logger.logI(TAG, "用线上")
        }
    }

    open fun cache(result: T) {
        netConfig.cache?.invoke(result)
        netConfig.callback?.onCache(result)
        Logger.logI(TAG, "用缓存")
    }

    open fun error(t: Throwable?) {
        val errorInfo = errorPreprocessor.processError(t)
        netConfig.failed?.invoke(errorInfo)

        netConfig.callback?.onError(t) ?: let {
            if (Net.client.netOptions.errorHandler?.invoke(errorInfo.errorCode) == false && netConfig.showErrorTip) {
                Toaster.showToast(errorInfo.message)
            }
        }
        cancel(CancellationException(t?.message))
    }

    open fun finish(t: Throwable?) {
        netConfig.finished?.invoke(t)
        netConfig.callback?.onFinish()
        cancel(CancellationException(t?.message))
    }

    /**
     * 网络请求句柄调用
     * @param url 回调出的接口名
     * @param responseType 此次请求要接收的数据类型
     * @param reqMethod 请求方式
     * @param requestBody 请求体类型
     */
    @Synchronized
    override fun onMethodInvoke(
        url: String,
        responseType: Type,
        reqMethod: String,
        requestBody: RequestBody?,
        headers: Headers
    ) {
        onCurrentRequestInvoke(url, responseType, reqMethod, requestBody, headers)
    }

    open fun onCurrentRequestInvoke(
        url: String,
        responseType: Type,
        reqMethod: String,
        requestBody: RequestBody?,
        headers: Headers
    ) {
        Logger.logI(
            TAG,
            String.format(
                "\n\nurl:%s\nresponseType:%s\nreqMethod:%s\nrequestBody:%s\nheaders:%s\n",
                url,
                responseType,
                reqMethod,
                requestBody,
                headers.toString()
            )
        )

        if (netConfig.needCache) {
            internalCacheKey = internalCacheKey(URL(url).path)
            Logger.logI(TAG, "cacheKey = $internalCacheKey")
            launch(EmptyCoroutineContext) {
                supervisorScope {
                    netConfig.cacheSucceed = try {
                        var cache: T? = null
                        netConfig.cacheReader?.invoke()?.let {
                            withMain {
                                cache(it)
                            }
                        } ?: let {
                            cache = NetCacheHandler.read(internalCacheKey, responseType)
                            cache?.let {
                                withMain {
                                    cache(it)
                                }
                            }
                        }
                        cache != null
                    } catch (e: Exception) {
                        false
                    }
                }
            }
        }
    }

    private fun internalCacheKey(path: String): String {
        val defaultKey = Net.client.netOptions.cacheKeyDefault.invoke(path)
        if (netConfig.cacheKey.isNotEmpty()) {
            return defaultKey.plus(netConfig.cacheKey)
        }
        return defaultKey
    }
}