package com.facile.immediate.electronique.fleurs.pret.input.view

import android.Manifest
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.text.SpannableStringBuilder
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.arthur.baselib.structure.mvvm.view.BaseMVVMActivity
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.ability.Loading
import com.arthur.commonlib.ability.Logger
import com.arthur.commonlib.ability.Toaster
import com.arthur.commonlib.file.FileUtil
import com.arthur.commonlib.utils.DensityUtils.Companion.dp2px
import com.arthur.commonlib.utils.SystemUtils
import com.arthur.commonlib.utils.image.DisplayUtils
import com.arthur.network.withMain
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.camera.CameraActivity
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.common.consumer.ConsumerActivity
import com.facile.immediate.electronique.fleurs.pret.common.event.NetErrorRefresh
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityInputIdentityInformationBinding
import com.facile.immediate.electronique.fleurs.pret.dialog.widget.BaseCountDownDialog
import com.facile.immediate.electronique.fleurs.pret.input.InputConstant
import com.facile.immediate.electronique.fleurs.pret.input.InputUtil
import com.facile.immediate.electronique.fleurs.pret.input.view.fragment.PicResGuideFragment
import com.facile.immediate.electronique.fleurs.pret.input.vm.IdentityInputVM
import com.facile.immediate.electronique.fleurs.pret.utils.cacheFileFromUri
import com.facile.immediate.electronique.fleurs.pret.utils.compressImage
import com.facile.immediate.electronique.fleurs.pret.web.WebLoadTimeoutDialog
import com.gyf.immersionbar.ImmersionBar
import com.permissionx.guolindev.PermissionX
import com.wld.mycamerax.util.CameraConstant
import com.wld.mycamerax.util.CameraParam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.atomic.AtomicBoolean


class InputIdentityInformationActivity :
    BaseMVVMActivity<ActivityInputIdentityInformationBinding, IdentityInputVM>() {

    private var isCardFrontPicUp = false
    private var isCardBackPicUp = false
    private var isFacePicUp = false

    private var picGuideType = 0

    private var showingDialog = AtomicBoolean(false)
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
            ConsumerActivity.goBranch(this)
        }

        mBinding.ivCardFront.setOnClickListener {
            showPicGuidePanel(InputConstant.ReqCode.CARD_FRONT_CODE)
        }
        mBinding.ivCardBack.setOnClickListener {
            showPicGuidePanel(InputConstant.ReqCode.CARD_BACK_CODE)
        }
        mBinding.ivFaceAfr.setOnClickListener {
            requestTakePhotoPermission {
                startActivityForResult(
                    Intent(this, CameraActivity::class.java),
                    InputConstant.ReqCode.FACE_PIC_CODE
                )
            }
        }

        mBinding.etIdentityCard.addTextChangedListener(mViewModel.textWatcher)
        mBinding.etIdentityCard.setOnEditorActionListener { v, actionId, event ->
            Logger.logD("arthur_action","actionId $actionId")
            if (actionId == EditorInfo.IME_FLAG_NO_ENTER_ACTION) {
                // 按下Enter/Return键时，将焦点切换到下一个EditText
                mBinding.etNin.requestFocus();
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
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
                mBinding.etNin.text = SpannableStringBuilder(it.energeticRudePollutionVisitor)
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
                        radius = 6f.dp2px(this),
                        loadingView = mBinding.ivCardFrontLoading
                    )
                    isCardFrontPicUp = true
                }

                if (it.festivalUndividedDoctor == "1"
                    && it.beautifulTelephoneFamiliarPicturePorridge?.isNotEmpty() == true
                ) {
                    DisplayUtils.displayImageAsRound(
                        it.beautifulTelephoneFamiliarPicturePorridge,
                        mBinding.ivCardBack,
                        radius = 6f.dp2px(this),
                        loadingView = mBinding.ivCardBackLoading
                    )
                    isCardBackPicUp = true
                }

                if (it.scottishQuiltStillSunday == "1"
                    && it.everydayRainfallCookieGuidance?.isNotEmpty() == true
                ) {
                    DisplayUtils.displayImageAsRound(
                        it.everydayRainfallCookieGuidance,
                        mBinding.ivFaceAfr,
                        radius = 6f.dp2px(this),
                        loadingView = mBinding.ivFaceAfrLoading
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
                WebLoadTimeoutDialog.with(this)
                    .confirm(getString(R.string.text_actualizar)) {
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
            setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_pic_upload_suc, 0, 0, 0)
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
            setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_pic_upload_fail, 0, 0, 0)
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
                        if (picGuideType == 1) {
                            lifecycleScope.launchWhenStarted {
                                mViewModel.cardFrontPicPath =
                                    intent.getStringExtra(CameraConstant.PICTURE_PATH_KEY)
                                        ?.let { path ->
                                            compressImage(
                                                path,
                                                mBinding.ivCardFront.measuredWidth,
                                                mBinding.ivCardFront.measuredHeight
                                            ).path
                                        } ?: ""
                                withContext(Dispatchers.Main) {
                                    DisplayUtils.displayImageAsRound(
                                        mViewModel.cardFrontPicPath,
                                        mBinding.ivCardFront,
                                        radius = 6f.dp2px(this@InputIdentityInformationActivity)
                                    )
                                    setUploadStateInVisible(mBinding.tvCardFrontUploadState)
                                }
                            }
                        } else if (picGuideType == 2) {
                            cacheFileFromUri(this, intent.data) {
                                it?.let {
                                    lifecycleScope.launchWhenStarted {
                                        mViewModel.cardFrontPicPath =
                                            compressImage(
                                                it.path,
                                                mBinding.ivCardFront.measuredWidth,
                                                mBinding.ivCardFront.measuredHeight
                                            ).path
                                        withContext(Dispatchers.Main) {
                                            DisplayUtils.displayImageAsRound(
                                                mViewModel.cardFrontPicPath,
                                                mBinding.ivCardFront,
                                                radius = 6f.dp2px(this@InputIdentityInformationActivity)
                                            )
                                            setUploadStateInVisible(mBinding.tvCardFrontUploadState)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    InputConstant.ReqCode.CARD_BACK_CODE -> {
                        if (picGuideType == 1) {
                            lifecycleScope.launchWhenStarted {
                                mViewModel.cardBackPicPath =
                                    intent.getStringExtra(CameraConstant.PICTURE_PATH_KEY)
                                        ?.let { path ->
                                            compressImage(
                                                path,
                                                mBinding.ivCardBack.measuredWidth,
                                                mBinding.ivCardBack.measuredHeight
                                            ).path
                                        } ?: ""
                                withContext(Dispatchers.Main) {
                                    DisplayUtils.displayImageAsRound(
                                        mViewModel.cardBackPicPath,
                                        mBinding.ivCardBack,
                                        radius = 6f.dp2px(this@InputIdentityInformationActivity)
                                    )
                                    setUploadStateInVisible(mBinding.tvCardBackUploadState)
                                }
                            }
                        } else if (picGuideType == 2) {
                            cacheFileFromUri(this, intent.data) {
                                it?.let {
                                    lifecycleScope.launchWhenStarted {
                                        mViewModel.cardBackPicPath =
                                            compressImage(
                                                it.path,
                                                mBinding.ivCardBack.measuredWidth,
                                                mBinding.ivCardBack.measuredHeight
                                            ).path
                                        withContext(Dispatchers.Main) {
                                            DisplayUtils.displayImageAsRound(
                                                mViewModel.cardBackPicPath,
                                                mBinding.ivCardBack,
                                                radius = 6f.dp2px(this@InputIdentityInformationActivity)
                                            )
                                            setUploadStateInVisible(mBinding.tvCardBackUploadState)
                                        }
                                    }
                                }
                            }
                        }

                    }

                    InputConstant.ReqCode.FACE_PIC_CODE -> {
                        lifecycleScope.launchWhenStarted {
                            mViewModel.facePicPath =
                                intent.getStringExtra(CameraConstant.PICTURE_PATH_KEY)
                                    ?.let { path ->
                                        compressImage(
                                            path,
                                            mBinding.ivFaceAfr.measuredWidth,
                                            mBinding.ivFaceAfr.measuredHeight
                                        ).path
                                    } ?: ""
                            withContext(Dispatchers.Main) {
                                DisplayUtils.displayImageAsRound(
                                    mViewModel.facePicPath,
                                    mBinding.ivFaceAfr,
                                    radius = 6f.dp2px(this@InputIdentityInformationActivity)
                                )
                                setUploadStateInVisible(mBinding.tvFaceAfrUploadState)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showPicGuidePanel(requestCode: Int) {
        if (showingDialog.get()) return
        PicResGuideFragment.show(this,
            {
                showingDialog.set(true)
            },
            {
                showingDialog.set(false)
            }) {
            picGuideType = it
            when (picGuideType) {
                1 -> {
                    requestTakePhotoPermission {
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
                    }
                }

                2 -> {
                    requestSelectPhotoPermission {
                        val galleryIntent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(galleryIntent, requestCode)
                    }
                }
            }
        }
    }

    private fun requestTakePhotoPermission(allPermissionGranted: () -> Unit) {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .request { allGranted: Boolean, grantedList: List<String?>?, deniedList: List<String?>? ->
                if (allGranted) {
                    allPermissionGranted.invoke()
                }
            }
    }

    private fun requestSelectPhotoPermission(allPermissionGranted: () -> Unit) {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .request { allGranted: Boolean, grantedList: List<String?>?, deniedList: List<String?>? ->
                if (allGranted) {
                    allPermissionGranted.invoke()
                }
            }
    }

    private fun isNextBtnEnable(): Boolean {
        mBinding.tvNext.isSelected =
            InputUtil.nextBtnEnable(mBinding.etIdentityCard, mBinding.etNin)
        return mBinding.tvNext.isSelected
    }
}