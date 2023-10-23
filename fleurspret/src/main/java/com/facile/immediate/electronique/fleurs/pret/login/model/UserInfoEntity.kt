package com.facile.immediate.electronique.fleurs.pret.login.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * newCustFlag    [string]	是	"1" 为首次注册成功
testCustFlag    [string]	是	"1" 为测试账号
token    [string]	是	登录令牌
userId    [string]	是	用户ID
 */
@Parcelize
data class UserInfoEntity(
    val modernThisBoxing: String? = null,//newCustFlag
    val thoroughBriefGreatSomebody: String? = null,//testCustFlag
    val dirtyCompositionUpsetRealBelief: String? = null,//token
    val radioactiveTeacherBasicBankLaserSuspect: String? = null,//userId
) : Parcelable
