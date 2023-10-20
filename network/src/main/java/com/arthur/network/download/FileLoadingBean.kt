package com.arthur.network.download

data class FileLoadingBean(
    /**
     * 1 onStart,2 onProgress,3 onFinish,4 onCancel,5 onError
     */
    var type: Int = 0,

    /**
     * 文件大小
     */
    var total: Long = 0,

    /**
     * 已下载大小
     */
    var progress: Int = 0,

    /**
     * 异常信息
     */
    var msg: String? = null,

    /**
     * 异常
     */
    var throwable: Throwable? = null
)