package com.facile.immediate.electronique.fleurs.pret.input.vm

import android.app.Application
import androidx.lifecycle.launchNet
import com.facile.immediate.electronique.fleurs.pret.input.view.InputContactInformationActivity

class BasicInputVM(app: Application) : BaseInputViewModel(app) {

    override fun processLogic() {
        super.processLogic()
        preInputInfo(1)
    }

    fun savePersonalInfo(
        names: String,
        surnames: String,
        birthDay: String,
        sex: String,
        email: String,
        fullAddress: String
    ) {
        launchNet {
            mModel.savePersonalInfo(
                pageType = 1,
                names,
                surnames,
                birthDay,
                sex,
                email,
                fullAddress
            )
        }.success {
            startActivity(InputContactInformationActivity::class.java)
        }.showLoading(true).launch()
    }
}