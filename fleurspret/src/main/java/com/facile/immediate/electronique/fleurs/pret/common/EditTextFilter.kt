package com.facile.immediate.electronique.fleurs.pret.common

import android.text.InputFilter

object EditTextFilter {
    fun getPhoneEditFilter(): InputFilter {
        return InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                val c = source[i]
                if (!Character.isDigit(c)) {
                    return@InputFilter ""
                }
            }
            null
        }
    }

    fun getEditLengthFilter(length: Int): InputFilter {
        return InputFilter.LengthFilter(length)
    }
}