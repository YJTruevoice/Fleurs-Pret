package com.facile.immediate.electronique.fleurs.pret.choosegold.vm

import android.app.Application
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.launchNet
import com.arthur.baselib.structure.mvvm.BaseViewModel
import com.arthur.baselib.structure.mvvm.SingleLiveEvent
import com.facile.immediate.electronique.fleurs.pret.choosegold.model.ChooseGoldModel
import com.facile.immediate.electronique.fleurs.pret.choosegold.model.OrdInfor
import com.facile.immediate.electronique.fleurs.pret.choosegold.model.PreCompResult
import com.facile.immediate.electronique.fleurs.pret.choosegold.model.ProdInfo
import com.facile.immediate.electronique.fleurs.pret.choosegold.model.ProdList
import com.facile.immediate.electronique.fleurs.pret.common.config.ConfigAPI
import com.facile.immediate.electronique.fleurs.pret.common.user.UserManager
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr

class ChooseGoldVM(app: Application) : BaseViewModel<ChooseGoldModel>(app) {
    val configService: ConfigAPI = NetMgr.get().service()

    val prodListLiveData: SingleLiveEvent<ProdList?> = SingleLiveEvent()
    val preComputeResultLiveData: SingleLiveEvent<PreCompResult?> = SingleLiveEvent()
    val preOdrLiveData: SingleLiveEvent<OrdInfor?> = SingleLiveEvent()
    val submitOdrLiveData: SingleLiveEvent<Boolean?> = SingleLiveEvent()

    var optTerm = 0
    var isMulti = false
    var prodList: ProdList? = null
    var curProDate: ProdInfo? = null
    var preOrd: OrdInfor? = null

    override fun processLogic() {
        super.processLogic()
        launchNet {
            configService.config("newrealterm")
        }.success {
            it.aggressiveParentMethod?.let { configs ->
                if (configs.isNotEmpty()) {
                    val optTermItem = configs[0].code
                    try {
                        optTerm = optTermItem.toInt()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }.finished {
            prodList()
        }.launch()
    }

    private fun prodList() {
        launchNet {
            mModel.prodList()
        }.success { res ->
            prodList = res.aggressiveParentMethod
            prodList?.bornSunglassesRipeProblemFalseHeadmaster?.let {
                if (UserManager.isTestAccount() && it.size > 1) {
                    val prodInfo = it[0]
                    it.clear()
                    it.add(prodInfo)
                }
                isMulti = it.size > 1
                for (i in it.indices) {
                    it[i].dampCabbageMaximumSorryCabbage =
                        prodList?.dampCabbageMaximumSorryCabbage ?: ""
                    it[i].optTerm = optTerm
                    it[i].isLocked = false
                    if (!isMulti) {
                        it[i].selected = i == 0
                    }
                }
                if (it.size == 1) {
                    val originFist = it.first()
                    it.add(ProdInfo(
                        neatPhysicsPeasantCommonSport = System.currentTimeMillis().toString(),
                        plainLungAppleGale = originFist.plainLungAppleGale * 2
                    ).apply {
                        this.dampCabbageMaximumSorryCabbage =
                            prodList?.dampCabbageMaximumSorryCabbage ?: ""
                        this.optTerm = this@ChooseGoldVM.optTerm
                        this.isLocked = true
                        this.selected = false
                    })
                    it.add(ProdInfo(
                        neatPhysicsPeasantCommonSport = System.currentTimeMillis().toString(),
                        plainLungAppleGale = originFist.plainLungAppleGale * 3
                    ).apply {
                        this.dampCabbageMaximumSorryCabbage =
                            prodList?.dampCabbageMaximumSorryCabbage ?: ""
                        this.optTerm = this@ChooseGoldVM.optTerm
                        this.isLocked = true
                        this.selected = false
                    })
                } else if (it.size > 1) {
                    val originFist = it.first()
                    val originLast = it.last()
                    it.add(ProdInfo(
                        neatPhysicsPeasantCommonSport = System.currentTimeMillis().toString(),
                        plainLungAppleGale = originFist.plainLungAppleGale + originLast.plainLungAppleGale
                    ).apply {
                        this.dampCabbageMaximumSorryCabbage =
                            prodList?.dampCabbageMaximumSorryCabbage ?: ""
                        this.optTerm = this@ChooseGoldVM.optTerm
                        this.isLocked = true
                        this.selected = false
                    })
                    it.add(ProdInfo(
                        neatPhysicsPeasantCommonSport = System.currentTimeMillis().toString(),
                        plainLungAppleGale = 2 * originFist.plainLungAppleGale + originLast.plainLungAppleGale
                    ).apply {
                        this.dampCabbageMaximumSorryCabbage =
                            prodList?.dampCabbageMaximumSorryCabbage ?: ""
                        this.optTerm = this@ChooseGoldVM.optTerm
                        this.isLocked = true
                        this.selected = false
                    })
                }

                // 测试账号 在计算基础上加 91*n （n代表元素顺序位）
                if (UserManager.isTestAccount()) {
                    it.forEachIndexed { index, prodInfo ->
                        prodInfo.optTerm += 91 * (index + 1)
                    }
                }
            }
            prodListLiveData.value = res.aggressiveParentMethod
        }.launch()
    }

    fun preCompute(
        neatPhysicsPeasantCommonSport: String,
        rudeHungryActionInformation: String
    ) {
        launchNet {
            mModel.preCompute(
                prodList?.activeAsianBookcase.toString(),
                neatPhysicsPeasantCommonSport,
                rudeHungryActionInformation
            )
        }.success {
            preComputeResultLiveData.value = it.aggressiveParentMethod
        }.showLoading(true).launch()
    }

    fun preOdr(
        neatPhysicsPeasantCommonSport: String,
        rudeHungryActionInformation: String
    ) {
        launchNet {
            mModel.preOrd(
                prodList?.activeAsianBookcase.toString(),
                neatPhysicsPeasantCommonSport,
                rudeHungryActionInformation
            )
        }.success {
            preOrd = it.aggressiveParentMethod
            preOdrLiveData.value = it.aggressiveParentMethod
        }.showLoading(true).launch()
    }

    fun submitOrd(
        neatPhysicsPeasantCommonSport: String,
        rudeHungryActionInformation: String
    ) {
        launchNet {
            mModel.submitOrd(
                prodList?.activeAsianBookcase.toString(),
                neatPhysicsPeasantCommonSport,
                rudeHungryActionInformation,
                preOrd?.normalBillClinicMercifulBay ?: ""
            )
        }.success {
            submitOdrLiveData.value = true
        }.showLoading(true).launch()
    }
}