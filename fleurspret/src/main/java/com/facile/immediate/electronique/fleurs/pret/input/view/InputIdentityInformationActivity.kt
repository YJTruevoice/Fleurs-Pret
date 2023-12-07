package com.facile.immediate.electronique.fleurs.pret.input.view

import android.Manifest
import android.content.Intent
import android.content.res.ColorStateList
import android.provider.Settings
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.arthur.baselib.structure.mvvm.view.BaseMVVMActivity
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.ability.Loading
import com.arthur.commonlib.ability.Toaster
import com.arthur.commonlib.utils.DensityUtils.Companion.dp2px
import com.arthur.commonlib.utils.SystemUtils
import com.arthur.commonlib.utils.image.DisplayUtils
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.common.consumer.ConsumerActivity
import com.facile.immediate.electronique.fleurs.pret.common.event.NetErrorRefresh
import com.facile.immediate.electronique.fleurs.pret.common.user.UserManager
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityInputIdentityInformationBinding
import com.facile.immediate.electronique.fleurs.pret.dialog.widget.BaseCountDownDialog
import com.facile.immediate.electronique.fleurs.pret.input.InputConstant
import com.facile.immediate.electronique.fleurs.pret.input.InputUtil
import com.facile.immediate.electronique.fleurs.pret.input.vm.IdentityInputVM
import com.gyf.immersionbar.ImmersionBar
import com.permissionx.guolindev.PermissionX
import com.wld.mycamerax.util.CameraConstant
import com.wld.mycamerax.util.CameraParam
import org.greenrobot.eventbus.EventBus

class InputIdentityInformationActivity :
    BaseMVVMActivity<ActivityInputIdentityInformationBinding, IdentityInputVM>() {

    private var isCardFrontPicUp = false
    private var isCardBackPicUp = false
    private var isFacePicUp = false
    override fun setStatusBar() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(statusBarDarkMode)
            .fitsSystemWindows(true)
            .init()
    }

    override fun buildView() {
        super.buildView()
        initTitleBar()

        PrivacyPolicyDisplayUtil.displayPrivacyPolicyGuide(this, mBinding.tvReadPrivacyPolicyGuide)
    }

    override fun setListener() {
        super.setListener()
        mBinding.inTitleBar.ivBack.setOnClickListener {
            finish()
        }
        mBinding.inTitleBar.ivCustomer.setOnClickListener {
            ConsumerActivity.go(this)
        }

        mBinding.ivCardFront.setOnClickListener {
            requestPermission(InputConstant.ReqCode.CARD_FRONT_CODE)
        }
        mBinding.ivCardBack.setOnClickListener {
            requestPermission(InputConstant.ReqCode.CARD_BACK_CODE)
        }
        mBinding.ivFaceAfr.setOnClickListener {
            requestPermission(InputConstant.ReqCode.FACE_PIC_CODE)
        }

        mBinding.etIdentityCard.addTextChangedListener(mViewModel.textWatcher)
        mBinding.etNin.addTextChangedListener(mViewModel.textWatcher)

        mBinding.tvNext.setOnClickListener {
            if (!isCardFrontPicUp || !isCardBackPicUp) {
                Toaster.showToast(getString(R.string.text_veuillez_t_l_charger_une_photo_d_identit))
                return@setOnClickListener
            }
            if (!isFacePicUp) {
                Toaster.showToast(getString(R.string.text_veuillez_t_l_charger_une_photo_de_votre_visage))
                return@setOnClickListener
            }
            if (!isNextBtnEnable()) {
                Toaster.showToast(getString(R.string.veuilltext_ez_compl_ter_toutes_les_informations))
                return@setOnClickListener
            }
            mViewModel.idNo = mBinding.etIdentityCard.text.toString()
            mViewModel.ninNo = mBinding.etNin.text.toString()
            mViewModel.saveIdentityInfo()
        }

    }

    private fun initTitleBar() {
        mBinding.inTitleBar.tvTitle.text = getString(R.string.text_informations_d_identification)
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        mViewModel.userBasicLiveData.observe(this) {
            it?.let {
                mBinding.etIdentityCard.text = SpannableStringBuilder(it.gratefulTourismFool)
                mBinding.etNin.text = SpannableStringBuilder(UserManager.phoneNumber())
                isNextBtnEnable()
            }
        }

        mViewModel.cardFaceInfoLiveData.observe(this) {
            it?.let {
                if (it.liveExcellentTaxEquality == "1"
                    && it.broadImportantBelief?.isNotEmpty() == true
                ) {
                    DisplayUtils.displayImageAsRound(
                        it.broadImportantBelief,
                        mBinding.ivCardFront,
                        radius = 6f.dp2px(this)
                    )
                    isCardFrontPicUp = true
                }

                if (it.festivalUndividedDoctor == "1"
                    && it.beautifulTelephoneFamiliarPicturePorridge?.isNotEmpty() == true
                ) {
                    DisplayUtils.displayImageAsRound(
                        it.beautifulTelephoneFamiliarPicturePorridge,
                        mBinding.ivCardBack,
                        radius = 6f.dp2px(this)
                    )
                    isCardBackPicUp = true
                }

                if (it.scottishQuiltStillSunday == "1"
                    && it.everydayRainfallCookieGuidance?.isNotEmpty() == true
                ) {
                    DisplayUtils.displayImageAsRound(
                        it.everydayRainfallCookieGuidance,
                        mBinding.ivFaceAfr,
                        radius = 6f.dp2px(this)
                    )
                    isFacePicUp = true
                }
                isNextBtnEnable()
            }
        }

        mViewModel.textWatcherLiveData.observe(this) {
            isNextBtnEnable()
        }

        mViewModel.updateStartLiveData.observe(this) {
            Loading.startLoading(this)
        }
        mViewModel.UpdateFinishLiveData.observe(this) {
            Loading.closeLoading()
        }

        mViewModel.uploadPicSuccessLiveData.observe(this) {
            it?.let {
                when (it) {
                    "cardFrontPicPath" -> {
                        mBinding.apply {
                            ivPhotoLabelFront.setImageResource(R.mipmap.icon_photo_label)
                            tvFront.text = getString(R.string.text_recto)
                            displayUpdateSuc(tvCardFrontUploadState)
                        }
                        isCardFrontPicUp = true
                    }

                    "cardBackPicPath" -> {
                        mBinding.apply {
                            ivPhotoLabelBack.setImageResource(R.mipmap.icon_photo_label)
                            tvBack.text = getString(R.string.text_recto)
                            displayUpdateSuc(tvCardBackUploadState)
                        }
                        isCardBackPicUp = true
                    }

                    "facePicPath" -> {
                        mBinding.apply {
                            ivPhotoLabelAfr.setImageResource(R.mipmap.icon_photo_label)
                            tvAfr.text = getString(R.string.text_recto)
                            displayUpdateSuc(tvFaceAfrUploadState)
                        }
                        isFacePicUp = true
                    }
                }
                isNextBtnEnable()
            }
        }

        mViewModel.uploadPicFailedLiveData.observe(this) {
            if (!SystemUtils.isNetworkAvailable(AppKit.context)) {
                BaseCountDownDialog.with(this)
                    .img(R.mipmap.pic_net_404)
                    .countDown(5)
                    .content(getString(R.string.text_erreur_de_connexion))
                    .confirm(getString(R.string.text_actualiser)) {
                        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                        startActivity(intent)
                    }.build().show()
            }
            it?.let {
                when (it) {
                    "cardFrontPicPath" -> {
                        mBinding.apply {
                            ivPhotoLabelFront.setImageResource(R.mipmap.icon_pic_load_failed)
                            tvFront.text = getString(R.string.text_fallo_la_carga)
                            displayUpdateFailed(tvCardFrontUploadState)
                        }
                    }

                    "cardBackPicPath" -> {
                        mBinding.apply {
                            ivPhotoLabelBack.setImageResource(R.mipmap.icon_pic_load_failed)
                            tvBack.text = getString(R.string.text_fallo_la_carga)
                            displayUpdateFailed(tvCardBackUploadState)
                        }
                    }

                    "facePicPath" -> {
                        mBinding.apply {
                            ivPhotoLabelAfr.setImageResource(R.mipmap.icon_pic_load_failed)
                            tvAfr.text = getString(R.string.text_fallo_la_carga)
                            displayUpdateFailed(tvFaceAfrUploadState)
                        }
                    }
                }
            }
        }
    }

    private fun displayUpdateSuc(tv: TextView) {
        tv.apply {
            text = getString(R.string.text_t_l_chargement_r_ussi)
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.color_B316CF36
                )
            )
            visibility = View.VISIBLE
        }
    }

    private fun displayUpdateFailed(tv: TextView) {
        tv.apply {
            text = getString(R.string.text_le_t_l_chargement_a_chou)
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.color_B3CF1616
                )
            )
            visibility = View.VISIBLE
        }
    }

    private fun setUploadStateInVisible(tv: TextView) {
        tv.visibility = View.INVISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let { intent ->
            if (resultCode == RESULT_OK) {
                when (requestCode) {
                    InputConstant.ReqCode.CARD_FRONT_CODE -> {
                        mViewModel.cardFrontPicPath =
                            intent.getStringExtra(CameraConstant.PICTURE_PATH_KEY) ?: ""
                        setUploadStateInVisible(mBinding.tvCardFrontUploadState)
                        DisplayUtils.displayImageAsRound(
                            mViewModel.cardFrontPicPath,
                            mBinding.ivCardFront,
                            radius = 6f.dp2px(this)
                        )
                    }

                    InputConstant.ReqCode.CARD_BACK_CODE -> {
                        mViewModel.cardBackPicPath =
                            intent.getStringExtra(CameraConstant.PICTURE_PATH_KEY) ?: ""
                        setUploadStateInVisible(mBinding.tvCardBackUploadState)
                        DisplayUtils.displayImageAsRound(
                            mViewModel.cardBackPicPath,
                            mBinding.ivCardBack,
                            radius = 6f.dp2px(this)
                        )
                    }

                    InputConstant.ReqCode.FACE_PIC_CODE -> {
                        mViewModel.facePicPath =
                            intent.getStringExtra(CameraConstant.PICTURE_PATH_KEY) ?: ""
                        setUploadStateInVisible(mBinding.tvFaceAfrUploadState)
                        DisplayUtils.displayImageAsRound(
                            mViewModel.facePicPath,
                            mBinding.ivFaceAfr,
                            radius = 6f.dp2px(this)
                        )
                    }
                }
            }
        }
    }

    private fun requestPermission(requestCode: Int) {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .request { allGranted: Boolean, grantedList: List<String?>?, deniedList: List<String?>? ->
                if (allGranted) {
                    if (requestCode == InputConstant.ReqCode.FACE_PIC_CODE) {
                        CameraParam.Builder()
                            .setShowFocusTips(false)
                            .setShowMask(false)
                            .setFront(true)
                            .setActivity(this@InputIdentityInformationActivity)
                            .setRequestCode(requestCode)
                            .build()
                    } else {
                        CameraParam.Builder()
                            .setShowFocusTips(false)
                            .setActivity(this@InputIdentityInformationActivity)
                            .setRequestCode(requestCode)
                            .build()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "These permissions are denied: \$deniedList",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun isNextBtnEnable(): Boolean {
        mBinding.tvNext.isSelected =
            InputUtil.nextBtnEnable(mBinding.etIdentityCard, mBinding.etNin)
        return mBinding.tvNext.isSelected
    }

    private fun isPicUpdateAll(): Boolean {
        return mBinding.tvCardFrontUploadState.text.toString() == getString(R.string.text_t_l_chargement_r_ussi)
                && mBinding.tvCardBackUploadState.text.toString() == getString(R.string.text_t_l_chargement_r_ussi)
                && mBinding.tvFaceAfrUploadState.text.toString() == getString(R.string.text_t_l_chargement_r_ussi)
    }
}