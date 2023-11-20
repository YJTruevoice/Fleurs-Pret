package com.facile.immediate.electronique.fleurs.pret.choosegold.vm

import android.app.Application
import androidx.lifecycle.launchNet
import com.arthur.baselib.structure.mvvm.BaseViewModel
import com.arthur.baselib.structure.mvvm.SingleLiveEvent
import com.facile.immediate.electronique.fleurs.pret.BuildConfig
import com.facile.immediate.electronique.fleurs.pret.choosegold.model.ChooseGoldModel
import com.facile.immediate.electronique.fleurs.pret.choosegold.model.OrdInfor
import com.facile.immediate.electronique.fleurs.pret.choosegold.model.PreCompResult
import com.facile.immediate.electronique.fleurs.pret.choosegold.model.ProdInfo
import com.facile.immediate.electronique.fleurs.pret.choosegold.model.ProdList

class ChooseGoldVM(app: Application) : BaseViewModel<ChooseGoldModel>(app) {
    val prodListLiveData: SingleLiveEvent<ProdList?> = SingleLiveEvent()
    val preComputeResultLiveData: SingleLiveEvent<PreCompResult?> = SingleLiveEvent()
    val preOdrLiveData: SingleLiveEvent<OrdInfor?> = SingleLiveEvent()
    val submitOdrLiveData: SingleLiveEvent<Boolean?> = SingleLiveEvent()

    var prodList: ProdList? = null
    var curProDate: ProdInfo? = null
    var preOrd: OrdInfor? = null

    override fun processLogic() {
        super.processLogic()
        prodList()
    }

    fun prodList() {
        launchNet {
            mModel.prodList()
        }.success { res ->
            prodList = res.aggressiveParentMethod
            prodList?.bornSunglassesRipeProblemFalseHeadmaster?.let {
                for (i in it.indices) {
                    it[i].dampCabbageMaximumSorryCabbage =
                        prodList?.dampCabbageMaximumSorryCabbage ?: ""
                    it[i].isLocked = false
                    it[i].selected = i == 0
                }
                if (it.size == 1) {
                    it.add(ProdInfo(
                        neatPhysicsPeasantCommonSport = System.currentTimeMillis().toString(),
                        plainLungAppleGale = it[0].plainLungAppleGale * 2
                    ).apply {
                        this.dampCabbageMaximumSorryCabbage =
                            prodList?.dampCabbageMaximumSorryCabbage ?: ""
                        this.isLocked = true
                        this.selected = false
                    })
                    it.add(ProdInfo(
                        neatPhysicsPeasantCommonSport = System.currentTimeMillis().toString(),
                        plainLungAppleGale = it[0].plainLungAppleGale * 3
                    ).apply {
                        this.dampCabbageMaximumSorryCabbage =
                            prodList?.dampCabbageMaximumSorryCabbage ?: ""
                        this.isLocked = true
                        this.selected = false
                    })
                } else if (it.size > 1) {
                    it.add(ProdInfo(
                        neatPhysicsPeasantCommonSport = System.currentTimeMillis().toString(),
                        plainLungAppleGale = it[0].plainLungAppleGale + it[it.size - 1].plainLungAppleGale
                    ).apply {
                        this.dampCabbageMaximumSorryCabbage =
                            prodList?.dampCabbageMaximumSorryCabbage ?: ""
                        this.isLocked = true
                        this.selected = false
                    })
                    it.add(ProdInfo(
                        neatPhysicsPeasantCommonSport = System.currentTimeMillis().toString(),
                        plainLungAppleGale = 2 * it[0].plainLungAppleGale + it[it.size - 1].plainLungAppleGale
                    ).apply {
                        this.dampCabbageMaximumSorryCabbage =
                            prodList?.dampCabbageMaximumSorryCabbage ?: ""
                        this.isLocked = true
                        this.selected = false
                    })
                } else {

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
        }.launch()
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
        }.failed {
            if (BuildConfig.DEBUG) {
                preOdrLiveData.value = null
            }
        }.launch()
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
        }.failed {
            if (BuildConfig.DEBUG) {
                submitOdrLiveData.value = false
            }
        }.launch()
    }
}