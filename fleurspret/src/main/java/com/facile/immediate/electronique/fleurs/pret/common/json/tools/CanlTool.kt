package com.facile.immediate.electronique.fleurs.pret.common.json.tools

import android.database.Cursor
import android.net.Uri
import android.telephony.TelephonyManager
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.utils.DateUtil
import com.facile.immediate.electronique.fleurs.pret.common.json.bean.MagicSoftRussianForm


object CanlTool {
    fun generateCanl(): List<MagicSoftRussianForm> {
        val CALENDER_EVENT_URL = "content://com.android.calendar/events"
        var mTm: TelephonyManager
        val CALANDER_URL = "content://com.android.calendar/calendars"
        val CALANDER_EVENT_URL = "content://com.android.calendar/events"
        val CALANDER_REMIDER_URL = "content://com.android.calendar/reminders"


        var startTime: String? = ""
        var endTime: String? = ""
        var eventTitle = ""
        var description = ""
        var location = ""

        val arr = arrayListOf<MagicSoftRussianForm>()
        try {
            val eventCursor: Cursor? = AppKit.context.contentResolver.query(
                Uri.parse(CALENDER_EVENT_URL), null,
                null, null, null
            )
            eventCursor?.let {
                while (eventCursor.moveToNext()) {
                    eventTitle = eventCursor.getString(eventCursor.getColumnIndexOrThrow("title"))
                    description =
                        eventCursor.getString(eventCursor.getColumnIndexOrThrow("description"))
                    location =
                        eventCursor.getString(eventCursor.getColumnIndexOrThrow("eventLocation"))
                    startTime = DateUtil.getSecondFormatStr(
                        eventCursor.getString(eventCursor.getColumnIndexOrThrow("dtstart")).toLong()
                    )
                    endTime =
                        DateUtil.getSecondFormatStr(
                            eventCursor.getString(
                                eventCursor.getColumnIndexOrThrow(
                                    "dtend"
                                )
                            ).toLong()
                        )
                    arr.add(
                        MagicSoftRussianForm(
                            excellentYouSnowyThinkingChess = eventTitle,
                            trueNearbyShanghaiContentFootball = description,
                            livePolice = location,
                            strictBoyFlamingCoughNovember = startTime ?: "",
                            challengingDinnerSmellySalesgirlPurpose = endTime ?: "",
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return arr

    }
}