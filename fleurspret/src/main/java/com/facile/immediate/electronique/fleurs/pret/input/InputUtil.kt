package com.facile.immediate.electronique.fleurs.pret.input

import android.widget.TextView

object InputUtil {
    fun nextBtnEnable(vararg tvs: TextView?): Boolean {
        for (index in tvs.indices) {
            val tv = tvs[index]
            if (tv?.text.toString().trim().isEmpty()) {
                return false
            }
        }
        return true
    }
}