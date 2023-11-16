package com.facile.immediate.electronique.fleurs.pret.input.model

import com.arthur.baselib.structure.base.IBaseModel
import com.facile.immediate.electronique.fleurs.pret.mine.model.UserBasicEntity
import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr
import retrofit2.http.Field

class InputModel : IBaseModel {

    private val service = NetMgr.get().service<InputAPI>()

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
        fullAddress: String
    ): BaseResponse<Any?> {
        return service.savePersonalInfo(
            pageType,
            names,
            surnames,
            birthDay,
            sex,
            email,
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

    suspend fun gatheringInfo(): BaseResponse<GatheringInfo?> {
        return service.gatheringInfo()
    }

    suspend fun saveGatheringInfo(
        pageType: Int,
        bankCode: String,
        bankName: String,
        bankAccountNumber: String,
        bankAccountType: String,
    ): BaseResponse<Any?> {
        return service.saveGatheringInfo(
            pageType,
            bankCode,
            bankName,
            bankAccountNumber,
            bankAccountType
        )
    }

    suspend fun identityAfrPic(pageType: Int): BaseResponse<IdentityPic?> {
        return service.identityAfrPic(pageType)
    }
}