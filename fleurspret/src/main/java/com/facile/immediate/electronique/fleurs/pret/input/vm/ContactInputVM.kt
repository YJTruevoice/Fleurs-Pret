package com.facile.immediate.electronique.fleurs.pret.input.vm

import android.app.Application
import androidx.lifecycle.launchNet
import com.facile.immediate.electronique.fleurs.pret.input.view.InputIdentityInformationActivity

class ContactInputVM(app: Application) : BaseInputViewModel(app) {
    override fun processLogic() {
        super.processLogic()
        preInputInfo(3)
    }

    fun saveContactInfo(
        phoneNumber: String,
        name: String,
        relationship: String,
        phoneNumberSec: String,
        nameSec: String,
        relationshipSec: String
    ) {
        launchNet {
            mModel.saveContactInfo(
                pageType = 3,
                phoneNumber,
                name,
                relationship,
                phoneNumberSec,
                nameSec,
                relationshipSec
            )
        }.success {
            startActivity(InputIdentityInformationActivity::class.java)
        }.showLoading(true).launch()
    }
}