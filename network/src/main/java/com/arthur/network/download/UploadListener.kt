package com.arthur.network.download

interface UploadListener {
    /**
     * 上传开始
     *
     * @param totalLength 需要上传的文件的总大小
     */
    fun onStart(totalLength: Long)

    /**
     * 上传进度
     *
     * @param progress 1，2，3...100
     */
    fun onProgress(progress: Int)

    /**
     * 上传进度
     * 注意：此回调方法在子线程中回调，不提供UI线程回调，UI线程回调请使用onProgress(int progress)
     *
     * @param uploadLength 实际上传的大小
     * @param totalLength  上传的总大小
     */
    fun onProgress(uploadLength: Long, totalLength: Long)

    /**
     * 上传完成
     *
     * @param response 接口返回内容
     */
    fun onFinish(response: String?)

    /**
     * 上传取消
     */
    fun onCancel()

    /**
     * 上传异常
     *
     * @param message 异常信息
     */
    fun onError(message: String?)
}