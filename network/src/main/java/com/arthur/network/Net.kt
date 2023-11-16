package com.arthur.network

import com.arthur.network.converter.ConverterFactory
import com.arthur.network.converter.Factory
import com.arthur.network.https.SslCheckHelper
import com.arthur.network.interceptor.CommonHeadersInterceptor
import com.arthur.network.interceptor.HttpLoggingInterceptor
import com.arthur.network.interceptor.MultiHostInterceptor
import com.arthur.network.interceptor.RequestBodyConverterInterceptor
import com.arthur.network.interceptor.SaveServerTimeInterceptor
import com.arthur.network.interceptor.*
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.InvocationInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

class Net private constructor() {

    /**
     * 网络配置
     */
    var netOptions: NetOptions = NetOptions()

    /**
     * 是否是debug包
     */
    @Deprecated("后期直接用netOptions中的isDebug变量")
    var isDebug = false

    // 记录服务器时间跟本地时间的 时间间隔
    var timeGap: Long = 0
        private set

    companion object {
        const val TAG = "NC-Net"

        val client by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            Net()
        }
    }

    /**
     * 通过[NetOptions]构建一个[OkHttpClient]
     *
     * @param options 网络基础配置
     * @return OkHttpClient实例
     */
    fun createClient(options: NetOptions): OkHttpClient {
        val builder: OkHttpClient.Builder = getBuilder(options)
        return builder.build()
    }

    private fun getBuilder(options: NetOptions): OkHttpClient.Builder {
        this.netOptions = options
        this.isDebug = options.isDebug
        val builder = OkHttpClient.Builder()
        if (!options.isSslOpen) {
            val sslCheckHelper = SslCheckHelper()
            builder.sslSocketFactory(sslCheckHelper.allSSLSocketFactory, sslCheckHelper.allTrustManager)
        }

        options.getEventListenerFactory()?.let {
            builder.eventListenerFactory(it)
        }

        //添加外部拦截器
        val interceptors = options.getInterceptors()
        if (interceptors.isNotEmpty()) {
            for (interceptor: Interceptor? in interceptors) {
                if (interceptor != null) {
                    builder.addInterceptor(interceptor)
                }
            }
        }
        builder.addInterceptor(InvocationInterceptor())

        // 添加外部网络拦截器
        val netInterceptors = options.getNetworkInterceptors()
        if (netInterceptors.isNotEmpty()) {
            for (interceptor: Interceptor? in netInterceptors) {
                if (interceptor != null) {
                    builder.addNetworkInterceptor(interceptor)
                }
            }
        }

        builder.addInterceptor(RequestBodyConverterInterceptor())
            .addInterceptor(CommonHeadersInterceptor(options.getCommonHeaders()))
            .addInterceptor(MultiHostInterceptor(options.domainMap, options.domainMain))// 添加多baseUrl解析拦截器
            .addInterceptor(HttpLoggingInterceptor(TAG)) // 添加网络请求log打印拦截
            .addInterceptor(SaveServerTimeInterceptor()) // 保存服务器时间拦截器

        //设置超时
        builder.connectTimeout(options.connectTimeout.toLong(), options.timeUnit)
        builder.readTimeout(options.readTimeout.toLong(), options.timeUnit)
        builder.writeTimeout(options.writeTimeout.toLong(), options.timeUnit)
        //错误重连
        builder.retryOnConnectionFailure(options.isRetry)
        //Cookie
        options.cookieMgr?.let {
            builder.cookieJar(it)
        }
        // 缓存设置
        options.cacheDir?.let {
            builder.cache(Cache(it, options.cacheSize))
        }
        return builder
    }

    /**
     * 通过[OkHttpClient] 构建一个[Retrofit]
     *
     * @param baseUrl 基础的URL，多个URL的公共部分，
     * @param client  OkHttpClient实例
     * @return Retrofit实例
     */
    fun createRetrofit(baseUrl: String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ConverterFactory.create(Factory.GSON))
            .build()
    }

    /**
     * 通过[NetOptions] 构建一个[Retrofit]
     *
     * @param netOptions 网络配置
     * @return Retrofit实例
     */
    fun createRetrofit(netOptions: NetOptions = NetOptions()): Retrofit {
        return createRetrofit(netOptions.domainMain, createClient(netOptions))
    }

    /**
     * 根据api类创建一个service
     */
    fun <T> createApiService(apiClass: Class<T>, netOptions: NetOptions = NetOptions()): T {
        return createRetrofit(netOptions).create(apiClass)
    }

    /**
     * 发起一个网络请求，通过[ResourceSubscriber]进行接收并去处理数据
     *
     * @param flowable           网络请求的Flowable
     * @param resourceSubscriber 接收并处理网络结果的ResourceSubscriber，处理网络正常相应的数据
     * @return Disposable，用于释放网络请求的资源
     */
    fun <T> request(flowable: Flowable<T>, resourceSubscriber: ResourceSubscriber<T>): Disposable {
        return flowable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(resourceSubscriber) as Disposable
    }

    /**
     * 发起一个支持延时的网络请求，通过[ResourceSubscriber]进行接收并去处理数据
     *
     * @param flowable           网络请求的Flowable
     * @param resourceSubscriber 接收并处理网络结果的ResourceSubscriber，处理网络正常相应的数据
     * @param delayTime          延迟发送时间，单位毫秒
     * @return Disposable，用于释放网络请求的资源
     */
    fun <T> request(
        flowable: Flowable<T>,
        resourceSubscriber: ResourceSubscriber<T>,
        delayTime: Long
    ): Disposable {
        return flowable.delay(delayTime, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(resourceSubscriber) as Disposable
    }

    /**
     * 增加一个具有延迟回调的请求 可以设置延时请求开始的监听
     * @param flowable           网络请求的Flowable
     * @param resourceSubscriber 接收并处理网络结果的ResourceSubscriber，处理网络正常相应的数据
     * @param delayTime          延迟发送时间，单位毫秒
     * @param delayListener      延时请求开始的监听
     * @return Disposable 用于释放网络请求的资源
     */
    fun <T> request(
        flowable: Flowable<T>,
        resourceSubscriber: ResourceSubscriber<T>,
        delayTime: Long,
        delayListener: DelayListener?
    ): Disposable {
        return Flowable.just(delayTime).delay(delayTime, TimeUnit.MILLISECONDS).subscribe {
            delayListener?.onRequestStart()
            request(flowable, resourceSubscriber)
        }
    }

    /**
     * 校准服务器差值
     */
    fun correctServerTimeGap(serverTime: Long) {
        timeGap = serverTime - System.currentTimeMillis()
    }

    /**
     * 延迟请求回调
     */
    interface DelayListener {
        fun onRequestStart()
    }
}