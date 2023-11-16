package com.facile.immediate.electronique.fleurs.pret.input.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/*
cardFrontFlag	[string]	否	"1" 已上传身份证正面
cardBackFlag	[string]	否	"1" 已上传身份证反面
faceVerified	[string]	否	"1" 已上传人脸照片
cardFrontUrl	[string]	否	身份证正面图片url
cardBackUrl	[string]	否	身份证反面图片url
afaceUrl	[string]	否	人脸照片url(我的界面头像)
cardFrontOgUrl	[string]	否	身份证正面后台相对地址(正面未修改, 修改反面时, 保存图片地址使用此参数返回的数据)
cardBackOgUrl	[string]	否	身份证反面后台相对地址(反面未修改, 修改正面时, 保存图片地址使用此参数返回的数据)
afaceOgUrl	[string]	否	人脸照片后台相对地址
cardVoterUrl	[string]	否	选民卡照片url

 */
@Parcelize
data class IdentityPic(
    val liveExcellentTaxEquality: String,
    val festivalUndividedDoctor: String,
    val scottishQuiltStillSunday: String,

    val broadImportantBelief: String,
    val beautifulTelephoneFamiliarPicturePorridge: String,
    val everydayRainfallCookieGuidance: String,
) : Parcelable
