package com.arthur.commonlib.utils

import android.os.Environment
import com.arthur.commonlib.ability.AppKit
import java.io.File

/**
 *
 * created by guo.lei on 2022.05.17
 * 外存路径使用规范
 */
object APPDir {
    /**
     * 获取缓存root
     */
    fun cacheRoot(): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            AppKit.context.externalCacheDir?.path ?: AppKit.context.cacheDir.path
        } else
            AppKit.context.cacheDir.path
    }

    fun fileRoot(): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            AppKit.context.getExternalFilesDir("main")?.path ?: AppKit.context.filesDir.path
        } else
            AppKit.context.filesDir.path
    }

    fun dbRoot(): String {
        val path = AppKit.context.filesDir.absolutePath + "/db"
        if (!File(path).exists()) {
            File(path).mkdirs()
        }
        return path
    }

    fun imageCache():String {
        return "${cacheRoot()}/image/".apply {
            mkDir(this)
        }
    }

    fun videoCache():String {
        return "${cacheRoot()}/video/".apply {
            mkDir(this)
        }
    }

    fun documentCache():String {
        return "${cacheRoot()}/document/".apply {
            mkDir(this)
        }
    }
    
    private fun mkDir(path: String) {
        File(path).apply {
            if (!exists()) {
                mkdirs()
            }
        }
    }
}