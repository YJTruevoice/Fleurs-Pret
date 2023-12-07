package com.facile.immediate.electronique.fleurs.pret.input.vm

import android.app.Application
import androidx.lifecycle.launchNet
import com.arthur.baselib.structure.mvvm.SingleLiveEvent
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.ability.Logger
import com.arthur.commonlib.ability.Toaster
import com.arthur.network.download.UploadListener
import com.arthur.network.download.Uploader
import com.facile.immediate.electronique.fleurs.pret.BuildConfig
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.input.model.IdentityPic
import com.facile.immediate.electronique.fleurs.pret.input.view.InputGatheringInformationActivity
import java.io.File

class IdentityInputVM(app: Application) : BaseInputViewModel(app) {

    val cardFaceInfoLiveData: SingleLiveEvent<IdentityPic?> = SingleLiveEvent()
    val updateStartLiveData: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val UpdateFinishLiveData: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val uploadPicSuccessLiveData: SingleLiveEvent<String> = SingleLiveEvent()
    val uploadPicFailedLiveData: SingleLiveEvent<String> = SingleLiveEvent()

    var cardFrontPicPath = ""
        set(value) {
            field = value
            uploadImg(field, "00", "cardFrontPicPath")
        }
    var cardBackPicPath = ""
        set(value) {
            field = value
            uploadImg(field, "01", "cardBackPicPath")
        }
    var facePicPath = ""
        set(value) {
            field = value
            uploadImg(field, "02", "facePicPath")
        }

    var idNo = ""
    var ninNo = ""

    override fun processLogic() {
        super.processLogic()
        preInputInfo(4)
        identityPic()
    }

    private fun uploadImg(imgPath: String, type: String, tag: String) {
        Uploader(object : UploadListener {
            override fun onStart(totalLength: Long) {
                updateStartLiveData.value = true
                Logger.logE("Uploader", "onStart totalLength:$totalLength")
            }

            override fun onProgress(progress: Int) {
                Logger.logE("Uploader", "onProgress progress:$progress")
            }

            override fun onProgress(uploadLength: Long, totalLength: Long) {
            }

            override fun onFinish(response: String?) {
                Logger.logE("Uploader", "onFinish response:$response")
                uploadPicSuccessLiveData.value = tag
                UpdateFinishLiveData.value = true
            }

            override fun onCancel() {
                UpdateFinishLiveData.value = true
                Logger.logE("Uploader", "onCancel")
            }

            override fun onError(message: String?) {
                Toaster.showToast(
                    message
                        ?: AppKit.context.getString(R.string.text_upload_failed_please_upload_again_carga_fallida_por_favor_sube_de_nuevo)
                )
                uploadPicFailedLiveData.value = tag
                UpdateFinishLiveData.value = true
            }

        }).upload(
            url = "${BuildConfig.HOST}/fleurspret/few/presentUnpleasantLid",
            formFileName = "illChart",
            file = File(imgPath),
            params = hashMapOf("boringSufferingLooseReasonAggressivePhysicist" to type)
        )
    }

    private fun identityPic() {
        launchNet {
            mModel.identityAfrPic(pageType = 4)
        }.success { res ->
            cardFaceInfoLiveData.value = res.aggressiveParentMethod
        }.showLoading(true).launch()
    }

    fun saveIdentityInfo() {
        launchNet {
            mModel.saveIdentityInfo(pageType = 4, idCardNo = idNo, phoneNo = ninNo)
        }.success {
            startActivity(InputGatheringInformationActivity::class.java)
        }.showLoading(true).launch()
    }
}