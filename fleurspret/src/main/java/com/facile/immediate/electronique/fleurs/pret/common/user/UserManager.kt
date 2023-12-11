package com.facile.immediate.electronique.fleurs.pret.common.user

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

    fun savePhone(phone: String) {
        SPUtils.putData(AppConstants.KEY_USER_PHONE, phone)
    }

    fun phoneNumber(): String {
        return SPUtils.getString(AppConstants.KEY_USER_PHONE, "")
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

    fun isTestAccount(): Boolean {
        return "1" == user()?.thoroughBriefGreatSomebody
    }

    fun logout() {
        SPUtils.remove(AppConstants.KEY_USER)
        SPUtils.remove(AppConstants.KEY_USER_ID)
        SPUtils.remove(AppConstants.KEY_TOKEN)
    }

}