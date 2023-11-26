package com.facile.immediate.electronique.fleurs.pret.common.json.tools

import android.app.ActivityManager
import android.content.Context
import android.os.Environment
import android.os.StatFs
import com.arthur.commonlib.ability.AppKit
import com.facile.immediate.electronique.fleurs.pret.common.json.bean.DailyMud
import java.io.File


object StoreTool {
    fun generateDailyMud(): DailyMud {
        return DailyMud(
            cloudyEffectGrandMatPopularFaith = "${getTotalMemory()}",
            ableBalanceFrogPacificCop = "${getAvailableMemory()}",
            deepForeheadMinibusSunshineNursery = "${getExternalMemorySize()}",
            lessThinkingSparrow = "${getAvailableExternalMemorySize()}",
            uglySadRecorder = "${getUsedExternalMemorySize()}",
            richYours = "${getTotalStorageSize()}",
            proudKilometreBriefSnow = "${getAvailableStorageSize()}",
            paleTableEachStudentLivingMotto = isExternalStorageAvailable(),
            extraordinaryFatEyewitnessJar = isInternalStorageAvailable(),
            rectangleSoftCartoonRecentQuality = "${getMaxHeapSize()}",
            crazyPopAdvertisement = "${getHeapSize()}",
            normalClearMidday = "${getFreeMemory()}",
        )
    }

    // 获取总内存大小
    private fun getTotalMemory(): Long {
        val path: File = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        return stat.totalBytes
    }

    // 获取内存可用大小
    private fun getAvailableMemory(): Long {
        val path: File = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        return stat.availableBytes
    }

    // 获取内存卡大小
    private fun getExternalMemorySize(): Long {
        return if (isExternalStorageAvailable() == 1) {
            val path: File = Environment.getExternalStorageDirectory()
            val stat = StatFs(path.path)
            stat.totalBytes
        } else {
            0
        }
    }

    // 获取内存卡可使用量
    private fun getAvailableExternalMemorySize(): Long {
        return if (isExternalStorageAvailable() == 1) {
            val path: File = Environment.getExternalStorageDirectory()
            val stat = StatFs(path.path)
            stat.availableBytes
        } else {
            0
        }
    }

    // 获取内存卡已使用量
    private fun getUsedExternalMemorySize(): Long {
        return getExternalMemorySize() - getAvailableExternalMemorySize()
    }

    // 获取总存储大小
    private fun getTotalStorageSize(): Long {
        val path: File = Environment.getRootDirectory()
        val stat = StatFs(path.path)
        return stat.totalBytes
    }

    // 获取可用存储大小
    private fun getAvailableStorageSize(): Long {
        val path: File = Environment.getRootDirectory()
        val stat = StatFs(path.path)
        return stat.availableBytes
    }

    // 是否有外置的SD卡
    private fun isExternalStorageAvailable(): Int {
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) 1 else 0
    }

    // 是否有内置的SD卡
    private fun isInternalStorageAvailable(): Int {
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED_READ_ONLY) 1 else 0
    }

    // 获取应用最大分配的堆内存大小
    private fun getMaxHeapSize(): Long {
        val activityManager =
            AppKit.context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return run {
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            memoryInfo.totalMem
        }
    }

    // 获取当前应用已分配的堆内存大小
    private fun getHeapSize(): Long {
        val activityManager =
            AppKit.context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return run {
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            memoryInfo.totalMem - memoryInfo.availMem
        }
    }

    // 获取当前应用的剩余内存大小
    private fun getFreeMemory(): Long {
        val activityManager =
            AppKit.context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return run {
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            memoryInfo.availMem
        }
    }

}