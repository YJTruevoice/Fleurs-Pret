package com.facile.immediate.electronique.fleurs.pret.common

import com.arthur.commonlib.utils.SPUtils
import com.arthur.commonlib.utils.json.JsonUtils
import com.facile.immediate.electronique.fleurs.pret.AppConstants
import com.facile.immediate.electronique.fleurs.pret.login.model.UserInfoEntity

object UserManager {
    fun saveLocal(user: UserInfoEntity) {
        SPUtils.putData(AppConstants.KEY_USER, JsonUtils.toJsonString(user) ?: "")
        SPUtils.putData(
            AppConstants.KEY_USER_ID,
            user.radioactiveTeacherBasicBankLaserSuspect ?: ""
        )
        SPUtils.putData(AppConstants.KEY_TOKEN, user.dirtyCompositionUpsetRealBelief ?: "")
    }

    fun user(): UserInfoEntity? {
        val jsonStr = SPUtils.getString(AppConstants.KEY_USER, "")
        return JsonUtils.fromJson(jsonStr, UserInfoEntity::class.java)
    }

    fun userId(): String {
        return SPUtils.getString(AppConstants.KEY_USER_ID, "")
    }

    fun token(): String {
        return SPUtils.getString(AppConstants.KEY_TOKEN, "")
    }

    fun isLogUp(): Boolean {
        return SPUtils.getString(AppConstants.KEY_TOKEN, "").isNotEmpty()
    }
}