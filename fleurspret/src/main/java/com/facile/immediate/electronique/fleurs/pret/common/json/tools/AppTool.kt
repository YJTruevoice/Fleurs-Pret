package com.facile.immediate.electronique.fleurs.pret.common.json.tools

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.arthur.commonlib.ability.AppKit
import com.facile.immediate.electronique.fleurs.pret.common.json.bean.CertainBarLorrySaturday
import java.io.BufferedReader
import java.io.InputStreamReader


object AppTool {
    fun generateApp(): List<CertainBarLorrySaturday> {
        val packages: List<PackageInfo> = AppKit.context.packageManager.getInstalledPackages(0)
        val apps = arrayListOf<CertainBarLorrySaturday>()
        if (packages.isNotEmpty()) {
            try {
                for (i in packages.indices) {
                    val packageInfo = packages[i]
                    val name = packageInfo.applicationInfo.loadLabel(AppKit.context.packageManager)
                        .toString()
                    apps.add(
                        CertainBarLorrySaturday(
                            enoughDeal = name,
                            tastyBoatingFederalBirdcageSide = packageInfo.packageName,
                            consideratePrivateCupboardSmoothRelation = packageInfo.firstInstallTime,
                            silentExactCamelDustyNobody = packageInfo.versionName,
                            longPhotoAncientNieceSale = if (packageInfo.applicationInfo.flags and 1 == 0) 0 else 1,
                            anxiousEnglishLightning = packageInfo.versionCode,
                            lonelyHelicopterRace = packageInfo.applicationInfo.flags,
                            atlanticHopefulSoccer = packageInfo.lastUpdateTime
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            val pm: PackageManager = AppKit.context.packageManager
            val process = Runtime.getRuntime().exec("pm list packages")
            val bis = BufferedReader(InputStreamReader(process.inputStream))
            var line = ""
            while (bis.readLine().also { line = it } != null) {
                val packageInfo =
                    pm.getPackageInfo(line.replace("package:", ""), PackageManager.GET_GIDS)
                val name =
                    packageInfo.applicationInfo.loadLabel(AppKit.context.packageManager).toString()
                apps.add(
                    CertainBarLorrySaturday(
                        enoughDeal = name,
                        tastyBoatingFederalBirdcageSide = packageInfo.packageName,
                        consideratePrivateCupboardSmoothRelation = packageInfo.firstInstallTime,
                        silentExactCamelDustyNobody = packageInfo.versionName,
                        longPhotoAncientNieceSale = if (packageInfo.applicationInfo.flags and 1 == 0) 0 else 1,
                        anxiousEnglishLightning = packageInfo.versionCode,
                        lonelyHelicopterRace = packageInfo.applicationInfo.flags,
                        atlanticHopefulSoccer = packageInfo.lastUpdateTime
                    )
                )
            }
            bis.close()
        }

        return apps
    }


}