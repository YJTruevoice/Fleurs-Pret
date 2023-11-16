package com.arthur.network.download

import android.annotation.SuppressLint
import com.arthur.network.Net
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.*
import okio.Buffer
import okio.BufferedSink
import okio.Source
import okio.source
import java.io.File
import java.io.IOException
import java.util.ArrayList

class Uploader @SuppressLint("CheckResult") constructor(private val listener: UploadListener?) {

    private val observable: Observable<FileLoadingBean>
    private var emitter: ObservableEmitter<FileLoadingBean>? = null
    private var isCanceled = false
    private var listenTime: Long = 0

    fun upload(
        url: String,
        formFileName: String,
        file: File,
        params: HashMap<String, String> = hashMapOf(),
        headers: HashMap<String, String> = hashMapOf()
    ) {
        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart(
            formFileName,
            file.name,
            createFileUploadBody(MultipartBody.FORM, file)
        )

        headers.putAll(Net.client.netOptions.getCommonHeaders().invoke())
        var hsBuilder: Headers.Builder? = null
        if (headers.isNotEmpty()) {
            hsBuilder = Headers.Builder()
            val keys = headers.keys
            for (key in keys) {
                hsBuilder.add(key, headers[key] ?: "")
            }
        }

        params.putAll(Net.client.netOptions.getCommonParams().invoke())
        for ((key, value) in params.entries) {
            builder.addFormDataPart(key, value)
        }


        val requestBody: MultipartBody = builder.build()
        val requestBuilder = Request.Builder()

        requestBuilder.url(url).post(requestBody)
        if (hsBuilder != null) {
            requestBuilder.headers(hsBuilder.build())
        }

        val request: Request = requestBuilder.build()
        val okBuilder = OkHttpClient.Builder()
        val client: OkHttpClient = okBuilder.build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val fileLoadingBean = FileLoadingBean()
                fileLoadingBean.type = 5
                fileLoadingBean.msg = e.message
                emitter!!.onNext(fileLoadingBean)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val fileLoadingBean = FileLoadingBean()
                fileLoadingBean.type = 3
                fileLoadingBean.msg = null
                try {
                    fileLoadingBean.msg = response.body!!.source().readUtf8()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                emitter!!.onNext(fileLoadingBean)
            }
        })
    }

    private fun createFileUploadBody(contentType: MediaType?, file: File): RequestBody {
        return object : RequestBody() {
            override fun contentType(): MediaType? {
                return contentType
            }

            override fun contentLength(): Long {
                return file.length()
            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                val source: Source
                try {
                    source = file.source()
                    val buf = Buffer()
                    val fileLoadingBean = FileLoadingBean()
                    fileLoadingBean.type = 1
                    fileLoadingBean.total = contentLength()
                    emitter!!.onNext(fileLoadingBean)
                    listenTime = System.currentTimeMillis()
                    var sum: Long = 0
                    var progress = 0
                    var readCount: Long
                    while (source.read(buf, 2048).also { readCount = it } != -1L) {
                        sink.write(buf, readCount)
                        sum += readCount
                        listener!!.onProgress(sum, contentLength())
                        val percent = (100 * sum / contentLength()).toInt()
                        val time = System.currentTimeMillis()
                        if (percent > progress && (percent == 1 || percent == 100 || time - listenTime > 100)) {
                            progress = percent
                            listenTime = time
                            fileLoadingBean.type = 2
                            fileLoadingBean.progress = progress
                            emitter!!.onNext(fileLoadingBean)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    val fileLoadingBean = FileLoadingBean()
                    fileLoadingBean.type = 5
                    fileLoadingBean.msg = e.message
                    emitter!!.onNext(fileLoadingBean)
                }
            }
        }
    }

    /**
     * 取消正在上传的任务，listener会收到onCancel回调
     */
    fun cancel() {
        isCanceled = true
        val fileLoadingBean = FileLoadingBean()
        fileLoadingBean.type = 4
        emitter!!.onNext(fileLoadingBean)
    }

    init {
        observable =
            Observable.create { e: ObservableEmitter<FileLoadingBean>? ->
                if (emitter == null) emitter = e
            }
        observable.observeOn(AndroidSchedulers.mainThread())
            .subscribe { bean: FileLoadingBean ->
                if (this.listener == null) return@subscribe
                when (bean.type) {
                    1 -> this.listener.onStart(bean.total)
                    2 -> this.listener.onProgress(bean.progress)
                    3 -> this.listener.onFinish(bean.msg)
                    4 -> this.listener.onCancel()
                    5 -> this.listener.onError(bean.msg)
                }
            }
    }
}