package com.arthur.network

import androidx.fragment.app.FragmentActivity
import com.arthur.commonlib.ability.ILoading
import com.arthur.network.converter.JSONConverter
import com.arthur.network.listener.OkHttpEventListener
import okhttp3.CookieJar
import okhttp3.EventListener
import okhttp3.Interceptor
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * 网络基础配置项，使用builder模式
 * 配置项包括：
 *
 * 支持使用者添加的
 * interceptors - 网络拦截器，用于添加自定义拦截器
 * commonParams - 通用参数，通用参数可以通过添加自定义拦截器添加，也可以将通用参数的键值对加入commonParams
 * commonHeaders - 通用请求头，通用请求头可以通过添加自定义拦截器添加，也可以将通用headers的键值对加入commonHeaders
 *
 * 超时相关
 * connectTimeout - 链接超时时长，默认10秒
 * readTimeout - 读取操作超时时长，默认10秒
 * writeTimeout - 写操作超时时长，默认60秒
 * timeUnit - 超时时长的时间单位，默认是秒，@see [TimeUnit]，会影响上面三个超时时长
 *
 * 缓存相关（暂时还没有用起来）
 * cacheSize - 网络缓存文件大小，默认是10M
 * cacheDir - 网络缓存文件的路径，默认是context.getCacheDir()+/nowcoder/nc-network;
 */
class NetOptions {
    /**
     * 拦截器
     */
    private val interceptors: MutableList<Interceptor> = mutableListOf()
    private val networkInterceptors: MutableList<Interceptor> = mutableListOf()

    /**
     * 通用参数
     */
    private var commonParams: (() -> HashMap<String, String>) = { hashMapOf() }

    /**
     * 通用请求头
     */
    private var commonHeaders: (() -> HashMap<String, String>) = { hashMapOf() }

    /**
     * 自定义请求过程事件监听器工厂（后期可能会用到）
     */
    private var eventListenerFactory: OkHttpEventListener.EventFactory? =
        OkHttpEventListener.EventFactory()

    /**
     * 首选 BaseUrl
     */
    var domainMain: String = ""
        private set

    /**
     * 多baseUrl集合
     */
    var domainMap: ConcurrentHashMap<String, String> = ConcurrentHashMap()
        private set

    /**
     * 超时相关
     */
    var connectTimeout: Int = 10
        private set
    var readTimeout: Int = 10
        private set
    var writeTimeout: Int = 60
        private set
    var timeUnit: TimeUnit = TimeUnit.SECONDS
        private set

    //缓存相关
    /**
     * 缓存文件的大小，默认10M
     */
    var cacheSize: Long = 1024 * 1024 * 10L
        private set
    var cacheDir: File? = null
        private set

    /**
     * 获取APP的debug模式
     *
     * @return
     */
    var isDebug: Boolean = false
        private set

    /**
     * 接口请求失败是否重试
     *
     * @return
     */
    var isRetry: Boolean = true
        private set

    /**
     * 接口缓存参数白名单
     */
    var netCacheWhiteList: List<String>? = null
        private set

    /**
     * 接口缓存结果过滤器
     */
    var netCacheFilter: Map<String, Any>? = null
        private set

    /**
     * 是否进行SSL验证，false：不验证，可以抓包；true：验证，不可抓包（默认）
     */
    var isSslOpen: Boolean = true
        private set

    /**
     * cookie处理器
     */
    var cookieMgr: CookieJar? = null
        private set

    /**
     * json数据解析器
     */
    var jsonConverter: JSONConverter = JSONConverter()
        private set

    /**
     * 网络请求loading
     */
    var loadingDialog: ILoading? = null
        private set

    /**
     * 当前顶部activity
     */
    var topActivityGetter: (() -> FragmentActivity?)? = null
        private set

    /**
     * 默认cacheKey
     */
    var cacheKeyDefault: (String) -> String = { it }
        private set

    /**
     * 网络超时提示
     */
    var tipServiceTimeout: String? = null
        private set

    /**
     * 网络连接错误
     */
    var tipConnectError: String? = null
        private set

    /**
     * 网络连接错误
     */
    var tipConnectErrorOther: String? = null
        private set

    /**
     * 数据解析错误
     */
    var tipDataParseError: String? = null
        private set

    /**
     * 数据不合规、空数据
     */
    var tipDataEmptyError: String? = null
        private set

    /**
     * 数据不合规
     */
    var tipDataIllegalError: String? = null
        private set

    /**
     * 未知异常
     */
    var tipUnknownError: String? = null
        private set

    /**
     * 默认业务层错误
     */
    var tipDefaultBusinessError: String? = null
        private set

    /**
     * 网络错误定制处理
     */
    var errorHandler: ((Int) -> Boolean)? = null
        private set

    fun setTipServiceTimeout(tipServiceTimeout:String){
        this.tipServiceTimeout = tipServiceTimeout
    }
    fun setTipConnectError(tipConnectError:String){
        this.tipConnectError = tipConnectError
    }
    fun setTipConnectErrorOther(tipConnectErrorOther:String){
        this.tipConnectErrorOther = tipConnectErrorOther
    }
    fun setTipDataParseError(tipDataParseError:String){
        this.tipDataParseError = tipDataParseError
    }
    fun setTipDataEmptyError(tipDataEmptyError:String){
        this.tipDataEmptyError = tipDataEmptyError
    }
    fun setTipDataIllegalError(tipDataIllegalError:String){
        this.tipDataIllegalError = tipDataIllegalError
    }
    fun setTipUnknownError(tipUnknownError:String){
        this.tipUnknownError = tipUnknownError
    }
    fun setTipDefaultBusinessError(tipDefaultBusinessError:String){
        this.tipDefaultBusinessError = tipDefaultBusinessError
    }
    fun setErrorHandler(errorHandler: ((Int) -> Boolean)){
        this.errorHandler = errorHandler
    }

    /**
     * 设置默认cacheKey
     */
    fun setDefaultCacheKey(cacheKeyDefault: ((String) -> String)) {
        this.cacheKeyDefault = cacheKeyDefault
    }

    /**
     * 设置网络请求loading
     */
    fun setTopActivityGetter(topActivityGetter: (() -> FragmentActivity?) = { null }): NetOptions {
        this.topActivityGetter = topActivityGetter
        return this
    }

    /**
     * 设置网络请求loading
     */
    fun setNetLoadingDialog(loadingDialog: ILoading): NetOptions {
        this.loadingDialog = loadingDialog
        return this
    }

    /**
     * 设置数据解析器
     */
    fun setConverter(jsonConverter: JSONConverter): NetOptions {
        this.jsonConverter = jsonConverter
        return this
    }

    /**
     * 设置cookie管理器
     */
    fun setCookieMgr(cookieMgr: CookieJar): NetOptions {
        this.cookieMgr = cookieMgr
        return this
    }

    /**
     * 设置首选BaseUrl
     *
     * @param baseUrl
     */
    fun setDomainMain(baseUrl: String): NetOptions {
        this.domainMain = baseUrl
        return this
    }

    /**
     * 设置多个BaseUrl
     */
    fun setDomainMap(domainMap: MutableMap<String, String> = mutableMapOf()) {
        for ((key, value) in domainMap) {
            this.domainMap[key] = value
        }
    }

    /**
     * 设置是否进行ssl验证
     *
     * @param sslOpen false 可以抓包，true 不可以抓包
     */
    fun setSslOpen(sslOpen: Boolean): NetOptions {
        this.isSslOpen = sslOpen
        return this
    }

    /**
     * 添加一个自定义拦截器
     *
     * @param interceptor 自定义Retrofit拦截器
     * @return self
     */
    fun addFirstInterceptor(interceptor: Interceptor): NetOptions {
        this.interceptors.add(0, interceptor)
        return this
    }

    /**
     * 添加一个自定义拦截器
     *
     * @param interceptor 自定义Retrofit拦截器
     * @return self
     */
    fun addInterceptor(interceptor: Interceptor): NetOptions {
        this.interceptors.add(interceptor)
        return this
    }

    /**
     * 添加缓存拦截器
     *
     * @param interceptor
     * @return
     */
    fun addNetworkInterceptor(interceptor: Interceptor): NetOptions {
        this.networkInterceptors.add(interceptor)
        return this
    }

    /**
     * 通过Map添加一系列的通用参数
     *
     * @param params 通用参数Map
     * @return self
     */
    fun addCommonParams(params: (() -> HashMap<String, String>) = { hashMapOf() }): NetOptions {
        this.commonParams = params
        return this
    }

    /**
     * 添加一个通用参数
     *
     * @param key   参数名
     * @param value 参数值
     * @return self
     */
    fun addCommonParams(key: String, value: String): NetOptions {
        this.commonParams.invoke()[key] = value
        return this
    }

    /**
     * 清除所有的通用参数
     *
     * @return self
     */
    fun clearCommonParams(): NetOptions {
        this.commonParams.invoke().clear()
        return this
    }

    /**
     * 移除某一个通用参数
     *
     * @param key 要移除的参数名
     * @return self
     */
    fun removeCommonParams(key: String?): NetOptions {
        this.commonParams.invoke().remove(key)
        return this
    }

    /**
     * 通过Map添加一系列的通用header
     *
     * @param headers 通用headerMap
     * @return self
     */
    fun addCommonHeaders(headers: (() -> HashMap<String, String>) = { hashMapOf() }): NetOptions {
        this.commonHeaders = headers
        return this
    }

    /**
     * 添加一个通用header
     *
     * @param key   header名
     * @param value header值
     * @return self
     */
    fun addCommonHeaders(key: String, value: String): NetOptions {
        this.commonHeaders.invoke()[key] = value
        return this
    }

    /**
     * 清除所有的通用header
     *
     * @return self
     */
    fun clearCommonHeaders(): NetOptions {
        this.commonHeaders.invoke().clear()
        return this
    }

    /**
     * 移除某一个通用header
     *
     * @param key 要移除的header名
     * @return self
     */
    fun removeCommonHeaders(key: String?): NetOptions {
        this.commonHeaders.invoke().remove(key)
        return this
    }

    /**
     * 设置链接超时时长
     *
     * @param connectTimeout 超时时长
     * @return self
     */
    fun setConnectTimeout(connectTimeout: Int): NetOptions {
        this.connectTimeout = connectTimeout
        return this
    }

    /**
     * 设置读操作超时时长
     *
     * @param readTimeout 读操作超市时长
     * @return self
     */
    fun setReadTimeout(readTimeout: Int): NetOptions {
        this.readTimeout = readTimeout
        return this
    }

    /**
     * 设置写操作超时时长
     *
     * @param writeTimeout 写操作超时时长
     * @return self
     */
    fun setWriteTimeout(writeTimeout: Int): NetOptions {
        this.writeTimeout = writeTimeout
        return this
    }

    /**
     * 设置超时时长的单位
     *
     * @param timeUnit 时长的单位，@see [TimeUnit]
     * @return self
     */
    fun setTimeUnit(timeUnit: TimeUnit): NetOptions {
        this.timeUnit = timeUnit
        return this
    }

    /**
     * 设置缓存文件的大小，默认是10
     *
     * @param cacheSize 1024*1024*N
     */
    fun setCacheSize(cacheSize: Long): NetOptions {
        this.cacheSize = cacheSize
        return this
    }

    /**
     * 设置OK请求事件
     *
     * @param eventListenerFactory
     * @return
     */
    fun setEventListenerFactory(eventListenerFactory: OkHttpEventListener.EventFactory?): NetOptions {
        this.eventListenerFactory = eventListenerFactory
        return this
    }

    /**
     * 设置debug模式
     *
     * @param debug
     */
    fun setDebug(debug: Boolean): NetOptions {
        this.isDebug = debug
        return this
    }

    /**
     * 设置接口请求失败是否重试
     *
     * @param retry true，重试一次；false，不重试
     */
    fun setRetry(retry: Boolean): NetOptions {
        this.isRetry = retry
        return this
    }

    /**
     * 设置cacheDir
     */
    fun setCache(cacheDir: File?): NetOptions {
        this.cacheDir = cacheDir
        return this
    }

    //-----------------------------以下不对外开放----------------------------------------------
    fun getInterceptors(): List<Interceptor> {
        return interceptors
    }


    fun getNetworkInterceptors(): List<Interceptor> {
        return networkInterceptors
    }

    fun getCommonParams(): (() -> HashMap<String, String>) {
        return commonParams
    }

    fun getCommonHeaders(): (() -> HashMap<String, String>) {
        return commonHeaders
    }

    fun getEventListenerFactory(): EventListener.Factory? {
        return eventListenerFactory
    }
}