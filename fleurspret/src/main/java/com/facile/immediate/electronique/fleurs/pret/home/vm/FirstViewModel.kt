package com.facile.immediate.electronique.fleurs.pret.home.vm

import android.app.Application
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.launchNet
import com.arthur.baselib.structure.mvvm.BaseViewModel
import com.arthur.baselib.structure.mvvm.SingleLiveEvent
import com.arthur.commonlib.utils.DateUtil
import com.arthur.commonlib.utils.SPUtils
import com.facile.immediate.electronique.fleurs.pret.common.user.UserManager
import com.facile.immediate.electronique.fleurs.pret.common.setting.GlobalSetting
import com.facile.immediate.electronique.fleurs.pret.home.Constant
import com.facile.immediate.electronique.fleurs.pret.home.model.FirstModel
import com.facile.immediate.electronique.fleurs.pret.home.model.MultiP
import com.facile.immediate.electronique.fleurs.pret.home.model.ProInfo
import java.util.Date

class FirstViewModel(application: Application) : BaseViewModel<FirstModel>(application) {

    val singleProHLiveData: SingleLiveEvent<ProInfo?> = SingleLiveEvent()
    val multiProHLiveData: SingleLiveEvent<List<MultiP>?> = SingleLiveEvent()

    val ordStateLiveData: MutableLiveData<ProInfo?> = MutableLiveData()

    val refreshCompleteLiveData: MutableLiveData<Boolean?> = MutableLiveData()
    val globalSettingLiveData: SingleLiveEvent<GlobalSetting?> = SingleLiveEvent()

    var globalSetting: GlobalSetting? = null

    fun getHomeData() {
        if (UserManager.isLogUp()) {
            multiProH()
        } else {
            globalSetting("afraidDecemberSlimClassicalTechnology,brownTopic")
        }
    }

    fun globalSetting(keys: String) {
        launchNet {
            mModel.globalSetting(keys)
        }.success { res ->
            globalSetting = res.aggressiveParentMethod
            globalSettingLiveData.value = res.aggressiveParentMethod
        }.failed {
            singleProHLiveData.value = ProInfo(afraidDecemberSlimClassicalTechnology = "--")
        }.finished {
            refreshCompleteLiveData.value = true
        }.launch()
    }

    fun verifyIsNetworkAvailable(availableHandle: () -> Unit) {
        launchNet {
            mModel.globalSetting("")
        }.success { res ->
            availableHandle.invoke()
        }.showLoading(true).launch()
    }

    fun multiProH() {
        launchNet {
            mModel.multiProH()
        }.success { res ->
            res.aggressiveParentMethod?.let {
                if (it.size > 1) {
                    for (e in it) {
                        val normalBillClinicMercifulBay = e.normalBillClinicMercifulBay
                        if (normalBillClinicMercifulBay?.isNotEmpty() == true
                            && normalBillClinicMercifulBay.isDigitsOnly()
                            && normalBillClinicMercifulBay.toLong() > 0
                        ) {
                            ordStateLiveData.value = e
                            return@success
                        }
                    }
                    multiProHLiveData.value = it
                } else if (it.size == 1) {
                    val pro = it[0]
                    val normalBillClinicMercifulBay = pro.normalBillClinicMercifulBay
                    if (normalBillClinicMercifulBay?.isNotEmpty() == true
                        && normalBillClinicMercifulBay.isDigitsOnly()
                        && normalBillClinicMercifulBay.toLong() > 0
                    ) {
                        ordStateLiveData.value = pro
                    } else {
                        singleProHLiveData.value = pro
                    }
                }

                // 消息公告弹窗
                val curTime = System.currentTimeMillis()
                val lastShowTime = SPUtils.getLong(Constant.KEY_NOTIFY_SHOW_TIME)
                if (DateUtil.getDayDiffer(Date(lastShowTime), Date(curTime)) >= 1) {
                    globalSetting("peacefulPartBrain,toughHydrogenMedicalTriangleSuffering,honestDessertUnableReceiptHotIceland")
                }
            }
        }.failed {
            singleProHLiveData.value = ProInfo(afraidDecemberSlimClassicalTechnology = "--")
        }.finished {
            refreshCompleteLiveData.value = true
        }.launch()
    }
}