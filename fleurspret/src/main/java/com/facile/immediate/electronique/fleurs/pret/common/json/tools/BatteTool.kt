package com.facile.immediate.electronique.fleurs.pret.common.json.tools

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.arthur.commonlib.ability.AppKit
import com.facile.immediate.electronique.fleurs.pret.common.json.bean.SplendidGrapeAloneFierceLesson


object BatteTool {

    fun generateSplendidGrapeAloneFierceLesson(): SplendidGrapeAloneFierceLesson {
        val splendidGrapeAloneFierceLesson = SplendidGrapeAloneFierceLesson()
        val manager = AppKit.context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val dianliang = manager.getIntProperty(4)
        splendidGrapeAloneFierceLesson.differentCarrierConsiderateYouthGarden = dianliang.toString()

        val intent: Intent? = AppKit.context.registerReceiver(
            null as BroadcastReceiver?,
            IntentFilter("android.intent.action.BATTERY_CHANGED")
        )
        return when (intent?.getIntExtra("plugged", -1)) {
            1 -> {
                splendidGrapeAloneFierceLesson.arcticProtectionCarpet = "1"
                splendidGrapeAloneFierceLesson.naturalTheseDiscussion = "0"
                splendidGrapeAloneFierceLesson.localPreciousQuizCheerfulCigar = "1"
                splendidGrapeAloneFierceLesson
            }

            2 -> {
                splendidGrapeAloneFierceLesson.arcticProtectionCarpet = "1"
                splendidGrapeAloneFierceLesson.naturalTheseDiscussion = "1"
                splendidGrapeAloneFierceLesson.localPreciousQuizCheerfulCigar = "0"
                splendidGrapeAloneFierceLesson
            }

            else -> {
                splendidGrapeAloneFierceLesson.arcticProtectionCarpet = "0"
                splendidGrapeAloneFierceLesson.naturalTheseDiscussion = "0"
                splendidGrapeAloneFierceLesson.localPreciousQuizCheerfulCigar = "0"
                splendidGrapeAloneFierceLesson
            }
        }
    }
}