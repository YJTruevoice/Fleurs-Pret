package com.facile.immediate.electronique.fleurs.pret.input.vm

import android.app.Application
import androidx.lifecycle.launchNet
import com.arthur.baselib.structure.mvvm.SingleLiveEvent
import com.facile.immediate.electronique.fleurs.pret.input.model.GatheringInfo

class GatheringInputVM(app: Application) : BaseInputViewModel(app) {
    val GatheringLiveData = SingleLiveEvent<GatheringInfo?>()
    val saveGatheringLiveData = SingleLiveEvent<GatheringInfo?>()

    override fun processLogic() {
        super.processLogic()
        gatheringInfo()
    }

    fun gatheringInfo() {
        launchNet {
            mModel.gatheringInfo()
        }.success {

        }.launch()
    }

    fun saveGatheringInfo(
        bankCode: String,
        bankName: String,
        bankAccountType: String,
        bankAccountNumber: String,
    ) {
        launchNet {
            mModel.saveGatheringInfo(
                pageType = 5,
                bankCode = bankCode,
                bankName,
                bankAccountType,
                bankAccountNumber
            )
        }.success {

        }.launch()
    }
}