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
        ordId = getArgumentsIntent()?.getStringExtra("ordId")?:""
    }

    private fun gatheringInfo() {
        launchNet {
            mModel.gatheringInfo()
        }.success {
            gatheringInfo = it.aggressiveParentMethod
            gatheringLiveData.value = it.aggressiveParentMethod
        }.launch()
    }

    /**
    @Field("necessaryRapidCustoms") pageType: Int,
    @Field("safeVirtueClinicNewJewel") collectionType: String,
    @Field("looseManSauce") bankAccountType: String,
    @Field("lastBuildingTroublesomeRainbowChapter") bankAccountNumber: String,
    @Field("unablePolePacificShop") bankCode: String,
    @Field("nativeShirtGrocerYesterday") bankName: String,

    @Field("beautifulFlashManyEasyKey") orderFaildAddFlag: String,
    @Field("normalBillClinicMercifulBay") orderId: String,
    @Field("gratefulTourismFool") curp: String
     */
    fun saveGatheringInfo(
        collectionType: String,
        bankAccountNumber: String,
        bankAccountType: String = "",
        bankCode: String = "",
        bankName: String = "",
    ) {
        launchNet {
            val fieldMap = hashMapOf(
                "necessaryRapidCustoms" to "5",
                "safeVirtueClinicNewJewel" to collectionType,
                "lastBuildingTroublesomeRainbowChapter" to bankAccountNumber,
                "looseManSauce" to bankAccountType,
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
            startActivity(ChooseGoldActivity::class.java)
        }.launch()
    }
}