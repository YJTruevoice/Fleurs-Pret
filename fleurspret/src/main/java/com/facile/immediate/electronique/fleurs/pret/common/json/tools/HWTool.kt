package com.facile.immediate.electronique.fleurs.pret.common.json.tools

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import com.arthur.commonlib.ability.AppKit
import com.facile.immediate.electronique.fleurs.pret.common.json.bean.FrontTheftBirthplace
import com.facile.immediate.electronique.fleurs.pret.utils.DeviceIdUtil
import java.lang.reflect.Method
import kotlin.math.pow
import kotlin.math.sqrt


object HWTool {

    fun generateFrontTheftBirthplace(): FrontTheftBirthplace {
        return FrontTheftBirthplace(
            seriousAgriculturalAtmosphereBridegroom = Build.BRAND,
            simpleFloor = Build.VERSION.SDK_INT,
            cottonDrunkSign = Build.MODEL,
            dearMemorySingleEffect = pPixelSize(),
            flatCitizenKindThirstSouthernValley = Build.VERSION.RELEASE,
            electricalActiveFilm = Build.BRAND,
            tastySeaweed = serial(),
            commonLibrarianSwiftSecretary = Build.TIME,
            pleasedNewDepth = AppKit.context.resources.displayMetrics.widthPixels,
            broadDirectorChineseEssayScottishTranslator = AppKit.context.resources.displayMetrics.heightPixels,
            entireIndependentHay = Build.BOARD,
            frequentHomeworkCruelAsh = processor(),
            maximumExhibitionNobody = aiMei(),
            nervousAirlineInstitutionSquirrel = aiMei()
        )
    }

    private fun pPixelSize(): String {
        val display =
            (AppKit.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val displayMetrics = DisplayMetrics()
        display.getMetrics(displayMetrics)
        return sqrt(
            (displayMetrics.heightPixels.toFloat() / displayMetrics.ydpi).toDouble()
                .pow(2.0) + (displayMetrics.widthPixels.toFloat() / displayMetrics.xdpi).toDouble()
                .pow(2.0)
        ).toString()
    }

    private fun serial(): String {
        var serial: String? = null
        try {
            val c = Class.forName("android.os.SystemProperties")
            val get: Method = c.getMethod("get", String::class.java)
            serial = get.invoke(c, "ro.serialnocustom") as String?
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return serial
            ?: try {
                val clazz = Class.forName("android.os.SystemProperties")
                (clazz.getMethod(
                    "get",
                    String::class.java
                ).invoke(clazz, "ro.serialno") as String)
            } catch (var1: Exception) {
                ""
            }
    }

    private fun processor(): String {
        try {
            return Runtime.getRuntime().availableProcessors().toString()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return "0"
    }

    private fun aiMei(): String {
        val mei = DeviceIdUtil.getIMEI(AppKit.context)
        if (mei.isEmpty()) {
            return DeviceIdUtil.getDeviceId()
        }
        return mei
    }
}