package com.facile.immediate.electronique.fleurs.pret.input.vm

import android.app.Application
import androidx.lifecycle.launchNet
import com.arthur.baselib.structure.mvvm.SingleLiveEvent
import com.facile.immediate.electronique.fleurs.pret.common.config.CommonConfigItem
import com.facile.immediate.electronique.fleurs.pret.common.consumer.CrispMgr
import com.facile.immediate.electronique.fleurs.pret.common.event.UserInfoUpdate
import com.facile.immediate.electronique.fleurs.pret.input.view.InputContactInformationActivity
import org.greenrobot.eventbus.EventBus

class BasicInputVM(app: Application) : BaseInputViewModel(app) {

    val regionGetMatchLiveData: SingleLiveEvent<Boolean> = SingleLiveEvent()

    var region: CommonConfigItem? = null

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
                region?.code ?: "",
                region?.next?.code ?: "",
                region?.next?.next?.code ?: "",
                fullAddress
            )
        }.success {
            EventBus.getDefault().post(UserInfoUpdate())
            CrispMgr.setEmail(email)
            startActivity(InputContactInformationActivity::class.java)
        }.showLoading(true).launch()
    }

    fun matchRegion(provinceId: String?, cityId: String?, districtId: String?) {
        if (!provinceId.isNullOrEmpty()) {
            province() { regions ->
                regions?.run {
                    val province = find { it.eastBasicFavouriteSupermarket == provinceId }
                    if (province != null) {
                        region = CommonConfigItem(
                            code = province.eastBasicFavouriteSupermarket,
                            value = province.normalAppointmentHeadmistressMachine
                        )
                        regionGetMatchLiveData.value = true

                        if (!cityId.isNullOrEmpty()) {
                            cities(provinceId) { regions ->
                                regions?.run {
                                    val city =
                                        find { it.eastBasicFavouriteSupermarket == cityId }
                                    if (city != null) {
                                        region?.next = CommonConfigItem(
                                            code = city.eastBasicFavouriteSupermarket,
                                            value = city.normalAppointmentHeadmistressMachine
                                        )
                                        regionGetMatchLiveData.value = true

                                        if (!districtId.isNullOrEmpty()) {
                                            district(cityId) { regions ->
                                                regions?.run {
                                                    val district =
                                                        find { it.eastBasicFavouriteSupermarket == districtId }
                                                    if (district != null) {
                                                        region?.next?.next = CommonConfigItem(
                                                            code = district.eastBasicFavouriteSupermarket,
                                                            value = district.normalAppointmentHeadmistressMachine
                                                        )
                                                        regionGetMatchLiveData.value = true
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}