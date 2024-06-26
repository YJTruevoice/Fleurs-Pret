package com.facile.immediate.electronique.fleurs.pret.input.vm

import android.app.Application
import androidx.lifecycle.launchNet
import com.arthur.baselib.structure.mvvm.SingleLiveEvent
import com.facile.immediate.electronique.fleurs.pret.choosegold.view.ChooseGoldActivity
import com.facile.immediate.electronique.fleurs.pret.input.model.GatheringInfo

class GatheringInputVM(app: Application) : BaseInputViewModel(app) {
    val gatheringLiveData = SingleLiveEvent<List<GatheringInfo>?>()
    val saveGatheringLiveData = SingleLiveEvent<GatheringInfo?>()

    var gatheringInfo: List<GatheringInfo>? = null

    var ordId: String = ""

    override fun processLogic() {
        super.processLogic()
        gatheringInfo()
        ordId = getArgumentsIntent()?.getStringExtra("ordId") ?: ""
    }

    private fun gatheringInfo() {
        launchNet {
            mModel.gatheringInfo()
        }.success {
            gatheringInfo = it.aggressiveParentMethod
            gatheringLiveData.value = it.aggressiveParentMethod
        }.launch()
    }

    fun saveGatheringInfo(
        collectionType: String = "2",
        bankAccountNumber: String,
        bankAccountType: String = "",
        bankCode: String = "",
        bankName: String = "",
    ) {
        launchNet {
            val fieldMap = hashMapOf(
                "necessaryRapidCustoms" to "5",
                "safeVirtueClinicNewJewel" to "2",
                "lastBuildingTroublesomeRainbowChapter" to bankAccountNumber,
//                "looseManSauce" to bankAccountType,
                "unablePolePacificShop" to bankCode,
                "nativeShirtGrocerYesterday" to bankName,
            )
            if (ordId.isNotEmpty()) {
                fieldMap.putAll(
                    hashMapOf(
                        "beautifulFlashManyEasyKey" to "1",
                        "normalBillClinicMercifulBay" to ordId,
                        "gratefulTourismFool" to "RUT",
                    )
                )
            }
            mModel.saveGatheringInfo(fieldMap)
        }.success {
            if (ordId.isNotEmpty()) {
                finish()
            } else {
                startActivity(ChooseGoldActivity::class.java)
            }
        }.showLoading(true).launch()
    }
}