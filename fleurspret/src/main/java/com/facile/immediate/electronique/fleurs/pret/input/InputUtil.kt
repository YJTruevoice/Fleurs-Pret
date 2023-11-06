package com.facile.immediate.electronique.fleurs.pret.input

import android.widget.TextView
import com.facile.immediate.electronique.fleurs.pret.bottomsheet.bean.CommonChooseListItem
import com.facile.immediate.electronique.fleurs.pret.common.config.Sex

object InputUtil {
    fun nextBtnEnable(vararg tvs: TextView?): Boolean {
        for (index in tvs.indices) {
            val tv = tvs[index]
            if (tv?.text.toString().isEmpty()) {
                return false
            }
        }
        return true
    }

    fun sexMap(sexCode: String): CommonChooseListItem? {
        var mapItem: CommonChooseListItem? = null
        Sex.values().forEach {
            if (sexCode == it.code) {
                mapItem = CommonChooseListItem(it.value, it.code)
            }
        }
        return mapItem
    }
}