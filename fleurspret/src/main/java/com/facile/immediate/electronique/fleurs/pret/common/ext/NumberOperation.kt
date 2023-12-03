package com.facile.immediate.electronique.fleurs.pret.common.ext


fun String?.addThousandSeparator(): String {
    return try {
        val number = this?.replace(",", "")?.toDouble()
        val formattedNumber = String.format("%,.0f", number)
        formattedNumber
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