package com.arthur.commonlib.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import androidx.core.content.ContextCompat
import java.io.IOException

/**
 * 利用gps获取位置信息工具类
 */
class GPSUtils {

    private var locationManager: LocationManager? = null

    /**
     * 获取Location信息
     */
    fun getLocation(context: Context): Location? {
        // 是否已经授权
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager // 默认Android GPS定位实例
        var location: Location? = null

        locationManager?.let {
            val providers = it.allProviders
            for (provider in providers) {
                val l = it.getLastKnownLocation(provider) ?: continue
                //找最合适的location
                if (location == null || l.accuracy < (location?.accuracy ?: 0f)) {
                    location = l
                }
            }
        }
        return location
    }

    /**
     * 获取经纬度坐标
     * @return Pair<String, String> first:经度latitude second:纬度longitude
     */
    fun getCoordinate(context: Context): Pair<Double, Double>? {
        val location = getLocation(context)
        return if (location == null) {
            null
        } else {
            Pair(location.latitude, location.longitude)
        }
    }

    /**
     * 根据经度纬度 获取国家，省份
     */
    fun getAddress(latitude: Double, longitude: Double, context: Context): String {
        var cityName = ""
        var addList: List<Address>? = null
        val ge = Geocoder(context)
        try {
            addList = ge.getFromLocation(latitude, longitude, 1)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (!addList.isNullOrEmpty()) {
            for (i in addList.indices) {
                val ad = addList[i]
                cityName += ad.countryName + " " + ad.locality + " " + ad.subLocality
            }
        }
        return cityName
    }

    /**
     * 如果没有开启gps跳转到相应设置页面
     */
    fun gotoOpenGPS(context: Context) {
        if (locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == false) {
            //跳转到手机打开GPS页面
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

}