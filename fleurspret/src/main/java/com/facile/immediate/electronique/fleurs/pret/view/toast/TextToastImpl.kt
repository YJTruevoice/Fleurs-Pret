package com.facile.immediate.electronique.fleurs.pret.view.toast

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.ability.IToaster
import com.arthur.commonlib.ability.Logger
import com.arthur.commonlib.ability.ToastLocationInfo
import com.facile.immediate.electronique.fleurs.pret.R

class TextToastImpl : IToaster {

    private val TAG = "Toast"

    override fun getToastView(message: String): View {
        val view = LayoutInflater.from(AppKit.context)
            .inflate(R.layout.layout_toast_text, null) as TextView
        view.text = message
        return view
    }

    override fun onToast(message: String) {
        Logger.logI(TAG, "showToast: $message")
    }

    override fun getLocationInfo(): ToastLocationInfo {
        return ToastLocationInfo()
    }
}