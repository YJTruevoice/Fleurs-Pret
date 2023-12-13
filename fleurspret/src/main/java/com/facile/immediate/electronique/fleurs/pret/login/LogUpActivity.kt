package com.facile.immediate.electronique.fleurs.pret.login

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.ContentInfo
import android.view.KeyEvent
import android.view.OnReceiveContentListener
import android.view.View
import androidx.core.text.isDigitsOnly
import com.arthur.baselib.structure.mvvm.view.BaseMVVMActivity
import com.arthur.commonlib.ability.Logger
import com.arthur.commonlib.ability.Toaster
import com.arthur.commonlib.utils.PhoneUtils
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.EditTextFilter
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.common.ext.tryCompleteZero
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityLogUpBinding
import com.facile.immediate.electronique.fleurs.pret.login.vm.LogUpViewModel
import com.facile.immediate.electronique.fleurs.pret.main.MainActivity
import com.permissionx.guolindev.PermissionX
import java.lang.StringBuilder


class LogUpActivity : BaseMVVMActivity<ActivityLogUpBinding, LogUpViewModel>() {

    private var isCountDown = false

    override fun buildView() {
        super.buildView()
        PrivacyPolicyDisplayUtil.displayPrivacyPolicyGuide(this, mBinding.tvReadPrivacyPolicyGuide)
        mBinding.btnLogUp.isSelected = false
        mBinding.btnSendVerifyCode.isSelected = false
        mBinding.btnSendVerifyCode.text = getString(R.string.text_obtenir_otp)
    }

    override fun getViewBelowStatusBar(): View {
        return mBinding.clLogUpHead
    }

    override fun setListener() {
        super.setListener()
        mBinding.ivBack.setOnClickListener {
            gotoMain()
            finish()
        }

        mBinding.btnSendVerifyCode.setOnClickListener {
            val inputNumber = mBinding.etPhone.text.toString()
            if (this.isCountDown) {
                return@setOnClickListener
            }
            if (!PhoneUtils.isOverSeaPhone("+221${inputNumber}") || inputNumber.length < 9) {
                Toaster.showToast(getString(R.string.text_veuillez_d_abord_remplir_le_bon_num_ro_de_t_l_phone_portable))
                return@setOnClickListener
            }
            mViewModel.getVerifyCode()
        }
        mBinding.btnLogUp.setOnClickListener {
            val inputPhone = mBinding.etPhone.text.toString()
            if (!PhoneUtils.isOverSeaPhone("+221${inputPhone}") || inputPhone.length < 9) {
                Toaster.showToast(getString(R.string.text_veuillez_d_abord_remplir_le_bon_num_ro_de_t_l_phone_portable))
                return@setOnClickListener
            }

            val inputCode = mBinding.etVerifyCode.text.toString()
            if (inputCode.isEmpty()) {
                Toaster.showToast(getString(R.string.text_le_code_de_v_rification_ne_peut_pas_tre_vide))
                return@setOnClickListener
            }
            if (!inputCode.isDigitsOnly() || inputCode.length != 4) {
                Toaster.showToast(getString(R.string.text_erreur_de_format_du_code_de_v_rification))
                return@setOnClickListener
            }
            mViewModel.logUp()
        }

        mBinding.ivClearPhone.setOnClickListener {
            mBinding.etPhone.text.clear()
            mBinding.btnLogUp.isSelected = false
            mBinding.btnSendVerifyCode.isSelected = false
        }
        mBinding.ivClearVerify.setOnClickListener {
            mBinding.etVerifyCode.text.clear()
            mBinding.btnLogUp.isSelected = false
            if (mBinding.etPhone.text.isEmpty()) {
                mBinding.btnSendVerifyCode.isSelected = false
            }
        }

        mBinding.etPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val afterText = SpannableStringBuilder(s).toString()
                val resultBuilder = StringBuilder()
                var needReplace = false
                for (c in afterText) {
                    if (Character.isDigit(c)) {
                        resultBuilder.append(c)
                    } else {
                        needReplace = true
                    }
                }
                val result = resultBuilder.toString()
                if (needReplace) {
                    mBinding.etPhone.text = SpannableStringBuilder(result)
                    mBinding.etPhone.setSelection(result.length)
                }
                mBinding.btnSendVerifyCode.isSelected =
                    result.isNotEmpty() && !isCountDown

                mBinding.btnLogUp.isSelected =
                    result.isNotEmpty() && mBinding.etVerifyCode.text.toString().isNotEmpty()
                mViewModel.phone = result
                mBinding.ivClearPhone.visibility =
                    if (result.isNotEmpty()) View.VISIBLE else View.GONE
            }
        })

        mBinding.etVerifyCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val afterText = SpannableStringBuilder(s).toString()
                val resultBuilder = StringBuilder()
                var needReplace = false
                var curLen = 0
                for (c in afterText) {
                    if (curLen < 4 && Character.isDigit(c)) {
                        resultBuilder.append(c)
                        curLen++
                    } else {
                        needReplace = true
                    }
                }
                val result = resultBuilder.toString()
                if (needReplace) {
                    mBinding.etVerifyCode.text = SpannableStringBuilder(result)
                    mBinding.etVerifyCode.setSelection(result.length)
                }

                mBinding.btnLogUp.isSelected =
                    result.isNotEmpty() && mBinding.etPhone.text.toString().isNotEmpty()
                mViewModel.code = result
                mBinding.ivClearVerify.visibility =
                    if (result.isNotEmpty()) View.VISIBLE else View.GONE
            }
        })
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        mViewModel.verifyCodeLiveData.observe(this) {
            mBinding.btnSendVerifyCode.apply {
                this@LogUpActivity.isCountDown = true
                base = SystemClock.elapsedRealtime() + 60L * 1000
                setOnChronometerTickListener {
                    val curElapsedRealtime = SystemClock.elapsedRealtime()
                    if (base > curElapsedRealtime) {
                        it.text =
                            String.format(
                                "%s %ss",
                                getString(R.string.text_r_acqu_rir_otp),
                                ((base - curElapsedRealtime) / 1000).tryCompleteZero()
                            )
                    } else {
                        it.text = getString(R.string.text_obtenir_otp)
                        stop()
                        mBinding.btnSendVerifyCode.isSelected =
                            mBinding.etPhone.text.isNotEmpty()
                        this@LogUpActivity.isCountDown = false
                    }
                }
                isSelected = false
            }.start()
            mBinding.etVerifyCode.text = SpannableStringBuilder(mViewModel.code)
        }

        mViewModel.logUpSuccessLiveData.observe(this) {
            gotoMain()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            gotoMain()
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun gotoMain() {
        startActivity(Intent(this@LogUpActivity, MainActivity::class.java).apply {
            putExtra("selectedItemId", R.id.navigation_one)
        })
    }
}