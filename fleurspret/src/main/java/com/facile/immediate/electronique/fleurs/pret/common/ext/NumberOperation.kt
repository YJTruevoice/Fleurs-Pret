package com.facile.immediate.electronique.fleurs.pret.common.ext

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.regex.Pattern


fun String?.addThousandSeparator(decimalPlaces: Int = 0): String {
    return try {
        val number = this?.replace(",", "")
        number?.let {
            val bd = BigDecimal(it.toDouble())
            val scaled = bd.setScale(decimalPlaces, RoundingMode.UP)
            val tmp = StringBuffer().append(scaled).reverse()
            val retNum =
                Pattern.compile("(\\d{3})(?=\\d)").matcher(tmp.toString()).replaceAll("$1,")

            StringBuffer().append(retNum).reverse().toString()
        } ?: ""
    } catch (e: NumberFormatException) {
        this ?: ""
    }
}

fun Int.tryCompleteZero(): String {
    return if (this < 10) {
        "0$this"
    } else {
        this.toString()
    }
}

fun Long.tryCompleteZero(): String {
    return if (this < 10) {
        "0$this"
    } else {
        this.toString()
    }
}