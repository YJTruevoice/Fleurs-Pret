package com.facile.immediate.electronique.fleurs.pret.common.json.tools

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.net.Proxy
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.provider.Settings.Secure
import android.telephony.CellInfo
import android.telephony.CellInfoCdma
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoWcdma
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.utils.SPUtils
import com.facile.immediate.electronique.fleurs.pret.common.json.bean.LatestRankPrettyBravery
import com.facile.immediate.electronique.fleurs.pret.common.json.bean.SmallPioneerCharacterAfricanSuperman
import com.facile.immediate.electronique.fleurs.pret.common.json.bean.SmellyGuitarHelpfulIndependence
import com.facile.immediate.electronique.fleurs.pret.common.json.bean.SplendidScholarshipChildLeadingBedroom
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr
import com.facile.immediate.electronique.fleurs.pret.utils.DeviceIdUtil
import java.io.File
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.Enumeration
import java.util.Locale
import java.util.TimeZone


object GeneralDataTool {
    fun generateDate(): SmallPioneerCharacterAfricanSuperman {
        return SmallPioneerCharacterAfricanSuperman(
            steadyRepairsAsianClinicTiredAgriculture = SPUtils.getString(NetMgr.CommonParamsKey.GOOGLE_AD_ID),
            bentSilentHotel = DeviceIdUtil.getAndroidId(AppKit.context),
            boringLiberationFortunateFortnight = phoneType(),
            civilNutDog = DeviceIdUtil.getMacAddress(),
            northernGruelSeason = AppKit.context.resources.configuration.locale.isO3Language,
            primaryComradePureAdvice = Locale.getDefault().displayLanguage,
            lazyAncientSeamanMercifulDot = AppKit.context.resources.configuration.locale.isO3Country,
            holyDirectorAntarcticChildhood = DeviceIdUtil.getIMEI(AppKit.context),
            excellentSuchValue = "",
            europeanShyFuneral = netOpName(),
            standardNutMistakenDad = netOpType(),
            looseArcticEffort = TimeZone.getDefault().getDisplayName(false, 0),
            thirstyUnderstandingDelightedEagle = langu(),
            shyBedroomClassicalAntarctica = isProxy(),
            excellentTentCarriage = isVPN(),
            lowBlueRecentGolf = isUSBD(),
            primaryEveryoneSize = SystemClock.elapsedRealtime(),
            crazyBusyTitleEachCastle = System.currentTimeMillis(),
            newVacationDrug = SystemClock.uptimeMillis(),
            latestRankPrettyBravery = senzorArr(),
        )
    }

    fun generateOthers(): SmellyGuitarHelpfulIndependence {
        return SmellyGuitarHelpfulIndependence(
            electricRightLiteraryTrial = isXXXRot(),
            southernNecessaryConceitedDot = (System.currentTimeMillis() - SystemClock.elapsedRealtimeNanos() / 1000000L).toString(),
            coolCabbage = "1",
            hardScientistVariety = if (isSimu()) "1" else "0",
            badPassportMentalUnknownPrescription = dbmmm(),
            fairStandHayLazyYourselves = SystemClock.elapsedRealtime().toString(),
            cloudyDeath = SystemClock.uptimeMillis().toString(),
        )
    }

    fun generatePushUsers(): SplendidScholarshipChildLeadingBedroom {
        var hostIp: String = ""
        try {
            val nis: Enumeration<*> = NetworkInterface.getNetworkInterfaces()
            var ia: InetAddress? = null
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement() as NetworkInterface
                val ias = ni.inetAddresses
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement()
                    if (ia is Inet6Address) {
                        continue
                    }
                    val ip = ia.hostAddress
                    if ("127.0.0.1" != ip) {
                        hostIp = ia.hostAddress ?: ""
                        break
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
            hostIp = ""
        }
        return SplendidScholarshipChildLeadingBedroom(
            australianComfortableThatGranny = hostIp,
            modernAverageThirstyMinister = hostIp
        )
    }

    fun internalAudFiles():Int{
        var result = 0

        try {
            val cursor: Cursor? = AppKit.context.contentResolver.query(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                arrayOf(
                    "date_added",
                    "date_modified",
                    "duration",
                    "mime_type",
                    "is_music",
                    "year",
                    "is_notification",
                    "is_ringtone",
                    "is_alarm"
                ),
                null as String?,
                null as Array<String?>?,
                "title_key"
            )
            while (cursor != null && cursor.moveToNext()) {
                ++result
            }
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        return result
    }
    fun internalImgFiles():Int{
        var result = 0

        try {
            val cursor: Cursor? = AppKit.context.contentResolver.query(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                arrayOf(
                    "datetaken",
                    "date_added",
                    "date_modified",
                    "height",
                    "width",
                    "latitude",
                    "longitude",
                    "mime_type",
                    "title",
                    "_size"
                ),
                null as String?,
                null as Array<String?>?,
                null as String?
            )
            while (cursor != null && cursor.moveToNext()) {
                ++result
            }
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return result
    }
    fun internalVidFiles():Int{
        var result = 0
        try {
            val arrayOfString = arrayOf("date_added")
            val cursor: Cursor? = AppKit.context.contentResolver.query(
                MediaStore.Video.Media.INTERNAL_CONTENT_URI,
                arrayOfString,
                null as String?,
                null as Array<String?>?,
                null as String?
            )
            while (cursor != null && cursor.moveToNext()) {
                ++result
            }
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return result
    }

    private fun phoneType(): String {
        return when ((AppKit.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).phoneType) {
            TelephonyManager.PHONE_TYPE_NONE -> "None"
            TelephonyManager.PHONE_TYPE_GSM -> "GSM"
            TelephonyManager.PHONE_TYPE_CDMA -> "CDMA"
            TelephonyManager.PHONE_TYPE_SIP -> "SIP"
            else -> "Unknown"
        }
    }

    private fun netOpName(): String {
        var networkOperatorName = ""
        try {
            networkOperatorName =
                (AppKit.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).networkOperatorName
        } catch (E: Exception) {
            E.printStackTrace()
        }
        if (TextUtils.isEmpty(networkOperatorName)) {
            networkOperatorName = "Unknown"
        }
        return networkOperatorName
    }

    private fun netOpType(): String {
        val cm =
            AppKit.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        return if (info != null && info.isAvailable) {
            if (info.type == ConnectivityManager.TYPE_WIFI) {
                "wifi"
            } else if (info.type == ConnectivityManager.TYPE_MOBILE) {
                when (info.subtype) {
                    TelephonyManager.NETWORK_TYPE_GSM, TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> "2G"
                    TelephonyManager.NETWORK_TYPE_TD_SCDMA, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> "3G"
                    TelephonyManager.NETWORK_TYPE_IWLAN, TelephonyManager.NETWORK_TYPE_LTE -> "4G"
                    TelephonyManager.NETWORK_TYPE_NR -> "5G"
                    else -> {
                        val subtypeName = info.subtypeName
                        if (subtypeName.equals("TD-SCDMA", ignoreCase = true)
                            || subtypeName.equals("WCDMA", ignoreCase = true)
                            || subtypeName.equals("CDMA2000", ignoreCase = true)
                        ) {
                            "3G"
                        } else {
                            "UNKNOWN"
                        }
                    }
                }
            } else {
                "UNKNOWN"
            }
        } else "NETWORK_NO"
    }

    private fun langu(): String {
        var language = ""
        try {
            val locale: Locale = AppKit.context.getResources().getConfiguration().locale
            language = locale.language
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return language
    }

    private fun isProxy(): Boolean {
        var proxyAddress: String? = ""
        var proxyPort = -1
        try {
            proxyAddress = System.getProperty("http.proxyHost")
            val portStr = System.getProperty("http.proxyPort")
            proxyPort = (portStr ?: "-1").toInt()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        return !TextUtils.isEmpty(proxyAddress) && proxyPort != -1
    }

    private fun isVPN(): Boolean {
        try {
            val defaultHost: String = Proxy.getDefaultHost()
            return !TextUtils.isEmpty(defaultHost)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun isUSBD(): Boolean {
        try {
            return Secure.getInt(AppKit.context.contentResolver, "adb_enabled", 0) > 0
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun senzorArr(): ArrayList<LatestRankPrettyBravery> {
        val senzorArr = arrayListOf<LatestRankPrettyBravery>()
        try {
            val sensors =
                (AppKit.context.getSystemService(Context.SENSOR_SERVICE) as SensorManager).getSensorList(
                    -1
                )
            val var3: Iterator<*> = sensors.iterator()
            while (var3.hasNext()) {
                val sensor = var3.next() as Sensor
                senzorArr.add(
                    LatestRankPrettyBravery(
                        boringSufferingLooseReasonAggressivePhysicist = sensor.type.toString(),
                        irishGradeUndergroundAmericanPostcard = sensor.name.toString(),
                        shallowMotorbikeDelightHandsomePineapple = sensor.version.toString(),
                        crazyFreezingDiscount = sensor.vendor.toString(),
                        thatShadeFalsePrideDiagram = sensor.maximumRange.toString(),
                        independentNeighbourhoodJudgementBlueCelebration = sensor.minDelay.toString(),
                        similarDisadvantageItalianZipper = sensor.power.toString(),
                        suddenMissTraveler = sensor.resolution.toString(),
                    )
                )
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        return senzorArr
    }

    private fun isXXXRot(): String {
        var bool = false

        try {
            bool = !(!File("/system/bin/su").exists() && !File("/system/xbin/su").exists())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return if (bool) "1" else "0"
    }

    private fun isSimu(): Boolean {
        return try {
            val tm = AppKit.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val imei = tm.deviceId
            if (imei != null && imei == "000000000000000") {
                true
            } else {
                Build.MODEL == "sdk" || Build.MODEL == "google_sdk"
            }
        } catch (var2: java.lang.Exception) {
            false
        }
    }

    private fun dbmmm(): String {
        var dbm = -1

        try {
            val cellInfoList = if (ActivityCompat.checkSelfPermission(
                    AppKit.context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                (AppKit.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).allCellInfo

            } else {
                return ""
            }
            if (null != cellInfoList) {
                val var3: Iterator<*> = cellInfoList.iterator()
                while (var3.hasNext()) {
                    val cellInfo = var3.next() as CellInfo
                    when (cellInfo) {
                        is CellInfoGsm -> {
                            val cellSignalStrengthGsm = cellInfo.cellSignalStrength
                            dbm = cellSignalStrengthGsm.dbm
                        }

                        is CellInfoCdma -> {
                            val cellSignalStrengthCdma = cellInfo.cellSignalStrength
                            dbm = cellSignalStrengthCdma.dbm
                        }

                        is CellInfoWcdma -> {
                            val cellSignalStrengthWcdma = cellInfo.cellSignalStrength
                            dbm = cellSignalStrengthWcdma.dbm
                        }

                        is CellInfoLte -> {
                            val cellSignalStrengthLte = cellInfo.cellSignalStrength
                            dbm = cellSignalStrengthLte.dbm
                        }
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return dbm.toString()
    }

}