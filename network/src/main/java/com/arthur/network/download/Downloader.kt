package com.arthur.network.download

import android.annotation.SuppressLint
import com.arthur.network.Net
import com.arthur.network.https.SslCheckHelper
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

@SuppressLint("CheckResult")
class Downloader constructor(private val downloadListener: DownloadListener?) {
    private var progress = 0
    private var isCanceled = false
    private val observable: Observable<FileLoadingBean>
    private var emitter: ObservableEmitter<FileLoadingBean>? = null
    private var fileLoadingBean: FileLoadingBean? = null
    private var listenTime: Long = 0

    /**
     * 开始一次下载任务，可以多次调用，但是一个DownloadHelper对象只允许一次下载一个文件，下载完成后再调用此方法开启新的下载
     * 如果需要并发下载文件，需要new 多个DownloadHelper对象，设置对应的多个listener，分别调用download方法
     *
     * @param url  网络文件的url
     * @param file 保存到本地的目录，此目录合法即可，不需要创建出真正的文件，此方法帮助创建文件
     */
    fun download(url: String, file: File) {
        var request: Request? = null
        try {
            request = Request.Builder()
                .url(url)
                .build()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (request == null) {
            downloadListener?.onError("bad request")
            return
        }
        val okHttpClientBuilder = OkHttpClient.Builder()
        if (Net.client.netOptions.isDebug) {
            val sslCheckHelper = SslCheckHelper()
            okHttpClientBuilder.sslSocketFactory(
                sslCheckHelper.allSSLSocketFactory,
                sslCheckHelper.allTrustManager
            )
        }
        okHttpClientBuilder.retryOnConnectionFailure(true)
        val client: OkHttpClient = okHttpClientBuilder.build()
        //异步请求
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                fileLoadingBean = FileLoadingBean()
                fileLoadingBean?.type = 5
                fileLoadingBean?.msg = e.message
                fileLoadingBean?.throwable = e
                emitter?.onNext(fileLoadingBean!!)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    fileLoadingBean = FileLoadingBean()
                    fileLoadingBean?.type = 5
                    fileLoadingBean?.msg = response.message
                    fileLoadingBean?.throwable = DownloadHttpException(response)
                    emitter?.onNext(fileLoadingBean!!)
                    return
                }
                var `is`: InputStream? = null
                val buf = ByteArray(2048)
                var len: Int
                var fos: FileOutputStream? = null
                try {
                    //储存下载文件的目录
                    if (file.exists()) {
                        file.delete()
                    }
                    val parentFile = file.parentFile
                    if (parentFile != null && !parentFile.exists()) {
                        makeDir(parentFile)
                    }
                    file.createNewFile()
                    `is` = response.body!!.byteStream()
                    val total = response.body!!.contentLength()
                    fos = FileOutputStream(file)
                    var sum: Long = 0
                    fileLoadingBean = FileLoadingBean()
                    fileLoadingBean?.type = 1
                    fileLoadingBean?.total = total
                    emitter?.onNext(fileLoadingBean!!)
                    listenTime = System.currentTimeMillis()
                    while (`is`.read(buf).also { len = it } != -1) {
                        if (isCanceled) {
                            break
                        }
                        fos.write(buf, 0, len)
                        sum += len.toLong()
                        val percent = (100 * sum / total).toInt()
                        val time = System.currentTimeMillis()
                        if (percent > progress && (percent == 1 || percent == 100 || time - listenTime > 100)) {
                            progress = percent
                            listenTime = time
                            fileLoadingBean = FileLoadingBean()
                            fileLoadingBean?.type = 2
                            fileLoadingBean?.progress = percent
                            emitter?.onNext(fileLoadingBean!!)
                        }
                    }
                    fos.flush()
                    if (!isCanceled) {
                        //下载完成
                        fileLoadingBean = FileLoadingBean()
                        fileLoadingBean?.type = 3
                        fileLoadingBean?.total = sum
                        emitter?.onNext(fileLoadingBean!!)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    fileLoadingBean = FileLoadingBean()
                    fileLoadingBean?.type = 5
                    fileLoadingBean?.throwable = e
                    fileLoadingBean?.msg = e.message
                    emitter?.onNext(fileLoadingBean!!)
                } finally {
                    try {
                        `is`?.close()
                        fos?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }

    private fun makeDir(dir: File) {
        if (!dir.exists()) {
            dir.parentFile?.let { makeDir(it) }
            dir.mkdir()
        }
    }

    /**
     * 取消正在下载的任务，listener会收到onCancel回调
     */
    fun cancel() {
        isCanceled = true
        fileLoadingBean = FileLoadingBean()
        fileLoadingBean?.type = 4
        emitter?.onNext(fileLoadingBean!!)
    }

    /**
     * 构造方法，传入一个下载监听器listener，可以收到下载开始、百分比进度、结束、异常、取消等回调
     */
    init {
        observable =
            Observable.create { e: ObservableEmitter<FileLoadingBean>? ->
                if (emitter == null) emitter = e
            }
        observable.observeOn(AndroidSchedulers.mainThread())
            .subscribe { bean: FileLoadingBean ->
                if (this.downloadListener == null) return@subscribe
                when (bean.type) {
                    1 -> this.downloadListener.onStart(bean.total)
                    2 -> this.downloadListener.onProgress(bean.progress)
                    3 -> this.downloadListener.onFinish(bean.total)
                    4 -> this.downloadListener.onCancel()
                    5 -> {
                        this.downloadListener.onError(bean.msg)
                        if (downloadListener is DownloadThrowableListener) {
                            downloadListener.onError(bean.throwable)
                        }
                    }
                }
            }
    }
}