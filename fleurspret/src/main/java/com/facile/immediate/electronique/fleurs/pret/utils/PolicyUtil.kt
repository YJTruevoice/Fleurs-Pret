package com.facile.immediate.electronique.fleurs.pret.utils

import com.arthur.commonlib.utils.SPUtils
import com.facile.immediate.electronique.fleurs.pret.AppConstants

object PolicyUtil {

    fun isPolicyAccepted(): Boolean {
        return SPUtils.getBoolean(AppConstants.KEY_POLICY_ACCEPTED, false)
    }

    fun policyAccepted() {
        SPUtils.putData(AppConstants.KEY_POLICY_ACCEPTED, true)
    }
}