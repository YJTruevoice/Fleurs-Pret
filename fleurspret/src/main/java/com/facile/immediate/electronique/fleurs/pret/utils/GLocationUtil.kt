package com.facile.immediate.electronique.fleurs.pret.utils

import android.Manifest
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.arthur.commonlib.utils.GPSUtils
import com.arthur.commonlib.utils.SPUtils
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.OnSuccessListener

class GLocationUtil {
    private var context: Context? = null
    private var locationCallBack: LocationCallBack? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mSettingsClient: SettingsClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private var mCurrentLocation: Location? = null
    private var mRequestingLocationUpdates: Boolean = false

    companion object {
        private const val LOCAL_GOOGLE = "local_location"
        private const val KEY_LOCATION_LATITUDE = "KEY_LOCATION_LATITUDE"
        private const val KEY_LOCATION_LONGITUDE = "KEY_LOCATION_LONGITUDE"
        private const val KEY_GROUP_SDK = "KEY_GROUP_SDK"
        private const val KEY_GROUP_SERVICE = "KEY_GROUP_SERVICE"
        private const val KEY_GROUP_LAST = "KEY_GROUP_LAST"

        private const val REQUEST_CHECK_SETTINGS = 0x1
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2

        private val INSTANCE: GLocationUtil by lazy {
            GLocationUtil()
        }

        fun get(): GLocationUtil {
            return INSTANCE
        }

    }

    fun init(context: Context) {
        this.context = context.applicationContext
    }

    private fun config() {
        context?.let {
            mRequestingLocationUpdates = false
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(it)
            mSettingsClient = LocationServices.getSettingsClient(it)
            createLocationCallback()
            createLocationRequest()
            buildLocationSettingsRequest()

            try {
                if (ActivityCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                mFusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
                    try {
                        if (location != null) {
                            saveLocalLocationByLast(location)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun getLocalLocation(): Location? {
        var googleLocation: Location?

//            定位取值获取优先级
//            1.LocalLocationBySDK
//            2.LocalLocationByService
//            3.LocalLocationByLast
//
//            例如，优选取值1的结果、1为空取值2、2为空取值3
        googleLocation = getSystemLocation()
        if (googleLocation == null) {
            googleLocation = getServiceLocation()
        }

        if (googleLocation == null) {
            googleLocation = getLastLocation()
        }

        return googleLocation
    }

    fun startLocation() {
        startLocationUpdates()
    }


    private fun getSystemLocation(): Location? {
        return context?.let {
            GPSUtils().getLocation(it)
        }
    }

    private fun getServiceLocation(): Location? {
        return mCurrentLocation
    }

    private fun getLastLocation(): Location? {
        return mCurrentLocation
    }

    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                mCurrentLocation = locationResult.lastLocation
                stopLocationUpdates()
                mCurrentLocation?.let {
                    saveLocalLocationBySDK(it)
                    locationCallBack?.onLocationResult(it.latitude, it.longitude)
                }
            }
        }
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest.create()
        mLocationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.fastestInterval =
            FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.priority =
            LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    private fun buildLocationSettingsRequest() {
        mLocationRequest?.let {
            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(it)
            mLocationSettingsRequest = builder.build()
        }
    }

    private fun startLocationUpdates() {
        mRequestingLocationUpdates = if (!mRequestingLocationUpdates) {
            true
        } else {
            return
        }
        context?.let { ctx ->
            mLocationSettingsRequest?.let {
                mSettingsClient?.checkLocationSettings(it)?.addOnSuccessListener(
                    OnSuccessListener {
                        if (ActivityCompat.checkSelfPermission(
                                ctx,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            return@OnSuccessListener
                        }
                        mLocationRequest?.let { it1 ->
                            mLocationCallback?.let { it2 ->
                                Looper.myLooper()?.let { it3 ->
                                    mFusedLocationClient?.requestLocationUpdates(it1, it2, it3)
                                }
                            }
                        }
                    }
                )?.addOnFailureListener { e ->
                    val statusCode = (e as ApiException).statusCode
                    locationCallBack?.onGoogleapisFail("Location fail")
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
//                            val rae = e as? ResolvableApiException
//                            rae?.startResolutionForResult(ctx, REQUEST_CHECK_SETTINGS)
                        } catch (sie: SendIntentException) {
                        }

                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> mRequestingLocationUpdates =
                            false
                    }
                }
            }
        }
    }

    fun stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            return
        }
        mLocationCallback?.let {
            mFusedLocationClient?.removeLocationUpdates(it)?.addOnCompleteListener {
                mRequestingLocationUpdates = false
            }
        }
    }


    private fun saveLocalLocationBySDK(googleLocation: Location) {
        SPUtils.putData(KEY_LOCATION_LATITUDE, googleLocation.latitude, KEY_GROUP_SDK)
        SPUtils.putData(KEY_LOCATION_LONGITUDE, googleLocation.longitude, KEY_GROUP_SDK)
    }

    private fun saveLocalLocationByService(googleLocation: Location) {
        SPUtils.putData(KEY_LOCATION_LATITUDE, googleLocation.latitude, KEY_GROUP_SERVICE)
        SPUtils.putData(KEY_LOCATION_LONGITUDE, googleLocation.longitude, KEY_GROUP_SERVICE)
    }

    private fun saveLocalLocationByLast(googleLocation: Location) {
        SPUtils.putData(KEY_LOCATION_LATITUDE, googleLocation.latitude, KEY_GROUP_LAST)
        SPUtils.putData(KEY_LOCATION_LONGITUDE, googleLocation.longitude, KEY_GROUP_LAST)
    }

    fun setLocationCallBack(locationCallBack: LocationCallBack?) {
        this.locationCallBack = locationCallBack
    }

    abstract class LocationCallBack {
        fun onLocationResult(latitude: Double, longitude: Double) {}
        fun onGoogleapisFail(error: String?) {}
        fun onCompleted() {}
    }
}