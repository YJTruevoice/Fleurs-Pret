package com.facile.immediate.electronique.fleurs.pret.utils

import android.content.Context
import android.net.Uri
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.ability.Logger
import com.arthur.commonlib.utils.APPDir
import com.arthur.commonlib.utils.FileUtils
import com.arthur.commonlib.utils.ScreenUtils
import com.arthur.commonlib.utils.StringUtil
import java.io.File
import java.util.*

/**
 * 将uri指向的文件保存到
 * @param context
 * @param uri
 * @param cb
 */
fun cacheFileFromUri(context: Context?, uri: Uri?, cb: ((cacheFile: File?) -> Unit)?) {
    if (context == null || uri == null) {
        cb?.invoke(null)
        return
    }

    val contentResolver = context.contentResolver
    // 查询文件基础信息
    FileUtils.getFileBaseInfoByUri(contentResolver, uri)?.let { fileInfo ->
        val sourceFile =
            "${APPDir.documentCache()}${UUID.randomUUID()}.${FileUtils.getExtensionName(fileInfo.first)}"
        RXUtils.asyncDo({
            // 缓存文件到私有路径，避免权限获取
            FileUtils.saveFileFromStream(sourceFile, contentResolver.openInputStream(uri))
        }, { suc ->
            if (!suc) {
                return@asyncDo
            }

            var result = File(sourceFile)
            // 如果是图片文件，压缩后回调
            if (ImageUtil.isImageFile(sourceFile)) {
                try {
                    val screenWith = ScreenUtils.getScreenWidth(context)
                    val screenHeight = ScreenUtils.getScreenHeight(context)
                    // 按照屏幕宽高加载，防止溢出
                    val bitmap = ImageUtil.loadScaledBitmap(sourceFile, screenWith, screenHeight)
                    // 旋转并压缩文件
                    val scaled = ImageUtil.scaleBitMap(
                        bitmap,
                        AppKit.context,
                        screenWith,
                        screenHeight,
                        ImageUtil.getRotateDegree(sourceFile)
                    )
                    // 源文件删除
                    result.delete()
                    result = scaled
                } catch (e: Exception) {
                    Logger.logE(StringUtil.check(e.message))
                }
            }

            cb?.invoke(result)
        })
    } ?: kotlin.run {
        cb?.invoke(null)
    }
}

fun compressImage(path: String): File? {
    return null
}