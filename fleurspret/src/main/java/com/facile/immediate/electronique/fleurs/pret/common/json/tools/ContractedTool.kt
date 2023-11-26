package com.facile.immediate.electronique.fleurs.pret.common.json.tools

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.CallLog
import android.provider.Telephony
import android.text.TextUtils
import com.arthur.commonlib.ability.AppKit
import com.facile.immediate.electronique.fleurs.pret.common.json.bean.ElectricCigarRareSeriousField
import com.facile.immediate.electronique.fleurs.pret.common.json.bean.LivePolice
import com.facile.immediate.electronique.fleurs.pret.common.json.bean.MountainousBrunchLuckySecondChemist
import org.json.JSONArray
import org.json.JSONObject


object ContractedTool {
    fun generateCn(): List<MountainousBrunchLuckySecondChemist> {
        // 暂时不要
        return listOf()
    }

    fun generateSsMSs(): List<ElectricCigarRareSeriousField> {
        val smArr = arrayListOf<ElectricCigarRareSeriousField>()
        val SMS_URI_ALL = "content://sms/"
        var cursor: Cursor? = null
        try {
            val resolver: ContentResolver = AppKit.context.contentResolver
            val projection = arrayOf(
                Telephony.Sms._ID,
                Telephony.Sms.ADDRESS,
                Telephony.Sms.PERSON,
                Telephony.Sms.BODY,
                Telephony.Sms.DATE,
                Telephony.Sms.TYPE,
                Telephony.Sms.READ,
                Telephony.Sms.STATUS,
                Telephony.Sms.SEEN,
                Telephony.Sms.DATE_SENT
            )
            val uri = Uri.parse(SMS_URI_ALL)
            cursor = resolver.query(uri, projection, null, null, "date desc")
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    smArr.add(
                        ElectricCigarRareSeriousField(
                            taxCafeteriaMildFamilyConclusion = cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                    Telephony.Sms.ADDRESS
                                )
                            ),
                            importantGladSchoolmateGreekPurse = cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                    Telephony.Sms.BODY
                                )
                            ),
                            steadyTeacherPersonalPioneerDirectory = cursor.getLong(
                                cursor.getColumnIndexOrThrow(
                                    Telephony.Sms.DATE
                                )
                            ),
                            boringSufferingLooseReasonAggressivePhysicist = cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                    Telephony.Sms.TYPE
                                )
                            ),
                            rareApartmentKindLanternScholarship = cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                    Telephony.Sms._ID
                                )
                            ),
                            specialHusbandActualMrs = cursor.getLong(
                                cursor.getColumnIndexOrThrow(
                                    Telephony.Sms.DATE_SENT
                                )
                            ),
                            realKeeperEastChiefSkyscraper = cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                    Telephony.Sms.READ
                                )
                            ),
                            thickCupLameTravel = cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                    Telephony.Sms.SEEN
                                )
                            ),
                            cruelSugarHugePolicemanMexicanKitchen = cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                    Telephony.Sms.STATUS
                                )
                            ),
                            contentQueenPartBasketball = cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                    Telephony.Sms.PERSON
                                )
                            ),
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return smArr
    }

    fun generateGPS(): LivePolice {
        val locationData = LivePolice()

        try {
//            val location: GoogleLocation = LocationInformation.getLocalLocation(AppKit.context)
//            var latitude = ""
//            var longitude = ""
//            if (location != null) {
//                latitude = location.getGoogleLatitude()
//                longitude = location.getGoogleLongitude()
//            }
//            val latlng = "$latitude,$longitude"
//
//            locationData.put("gps_address_city", latlng)
//            locationData.put("gps_address_province", latlng)
//            locationData.put("gps_address_street", latlng)
//            val gpsJSONject = JSONObject()
//            gpsJSONject.put("latitude", latitude)
//            gpsJSONject.put("longitude", longitude)
//            locationData.put("gps", gpsJSONject)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return locationData
    }

    fun generateCallls(): List<MountainousBrunchLuckySecondChemist> {
        val arr = arrayListOf<MountainousBrunchLuckySecondChemist>()
        var cursor: Cursor? = null
        try {
            val uri = CallLog.Calls.CONTENT_URI
            val projection = arrayOf(
                CallLog.Calls.DATE,  // 日期
                CallLog.Calls.NUMBER,  // 号码
                CallLog.Calls.TYPE,  // 类型
                CallLog.Calls.CACHED_NAME,  // 名字
                CallLog.Calls._ID,  // id
                CallLog.Calls.DURATION,
                CallLog.Calls.NEW,
                CallLog.Calls._COUNT,
                CallLog.Calls.DATA_USAGE
            )
            cursor = AppKit.context.contentResolver
                .query(uri, projection, null, null, CallLog.Calls.DEFAULT_SORT_ORDER)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val number =
                        cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER))
                    val cachedName =
                        cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME)) // 缓存的名称与电话号码，如果它的存在
                    val obj = JSONObject()
                    val thoseInterestingCourtyard = if (TextUtils.isEmpty(cachedName)) {
                        number
                    } else {
                        cachedName
                    }

                    arr.add(
                        MountainousBrunchLuckySecondChemist(
                            thoseInterestingCourtyard = thoseInterestingCourtyard,
                            privateLivelyEveningTheseDesert = number,
                            importantValuablePet = "${
                                cursor.getInt(
                                    cursor.getColumnIndexOrThrow(
                                        CallLog.Calls._COUNT
                                    )
                                )
                            }",
                            atlanticHopefulSoccer = cursor.getLong(
                                cursor.getColumnIndexOrThrow(
                                    CallLog.Calls.DATE
                                )
                            ),
                            firstTextScottishHusband = cursor.getLong(
                                cursor.getColumnIndexOrThrow(
                                    CallLog.Calls.DATA_USAGE
                                )
                            )
                        )
                    )
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return arr
    }
}