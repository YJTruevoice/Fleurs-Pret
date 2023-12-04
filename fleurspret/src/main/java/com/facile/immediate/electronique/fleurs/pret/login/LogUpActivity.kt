package com.facile.immediate.electronique.fleurs.pret.login

import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import com.arthur.baselib.structure.mvvm.view.BaseMVVMActivity
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.EditTextFilter
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.common.ext.tryCompleteZero
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityLogUpBinding
import com.facile.immediate.electronique.fleurs.pret.login.vm.LogUpViewModel
import com.facile.immediate.electronique.fleurs.pret.main.MainActivity

class LogUpActivity : BaseMVVMActivity<ActivityLogUpBinding, LogUpViewModel>() {

    private var isCountDown = false

    override fun buildView() {
        super.buildView()
        PrivacyPolicyDisplayUtil.displayPrivacyPolicyGuide(this, mBinding.tvReadPrivacyPolicyGuide)
        mBinding.btnSendVerifyCode.text = getString(R.string.text_obtenir_otp)
        mBinding.etPhone.filters =
            arrayOf(EditTextFilter.getPhoneEditFilter(), EditTextFilter.getEditLengthFilter(10))
        mBinding.etVerifyCode.filters =
            arrayOf(EditTextFilter.getPhoneEditFilter(), EditTextFilter.getEditLengthFilter(4))
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
            mViewModel.getVerifyCode()
        }
        mBinding.btnLogUp.setOnClickListener {
            mViewModel.logUp()
        }

        mBinding.ivClearPhone.setOnClickListener {
            mBinding.etPhone.text.clear()
            mBinding.btnLogUp.isEnabled = false
            mBinding.btnSendVerifyCode.isEnabled = false
        }
        mBinding.ivClearVerify.setOnClickListener {
            mBinding.etVerifyCode.text.clear()
            mBinding.btnLogUp.isEnabled = false
            if (mBinding.etPhone.text.isEmpty()) {
                mBinding.btnSendVerifyCode.isEnabled = false
            }
        }

        mBinding.etPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mBinding.btnSendVerifyCode.isEnabled =
                        s?.isNotEmpty() == true && !mBinding.btnSendVerifyCode.isCountDown
                } else {
                    mBinding.btnSendVerifyCode.isEnabled =
                        s?.toString()?.isNotEmpty() == true && !isCountDown
                }
                mBinding.btnLogUp.isEnabled =
                    s?.isNotEmpty() == true && mBinding.etVerifyCode.text.toString().isNotEmpty()
                mViewModel.phone = s.toString()
                mBinding.ivClearPhone.visibility =
                    if (s?.isNotEmpty() == true) View.VISIBLE else View.GONE
            }
        })

        mBinding.etVerifyCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                mBinding.btnLogUp.isEnabled =
                    s?.isNotEmpty() == true && mBinding.etPhone.text.toString().isNotEmpty()
                mViewModel.code = s.toString()
                mBinding.ivClearVerify.visibility =
                    if (s?.isNotEmpty() == true) View.VISIBLE else View.GONE
            }
        })
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        mViewModel.verifyCodeLiveData.observe(this) {
            mBinding.btnSendVerifyCode.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    isCountDown = true
                } else {
                    this@LogUpActivity.isCountDown = true
                }
                base = SystemClock.elapsedRealtime() + 60L * 1000
                setOnChronometerTickListener {
                    val curElapsedRealtime = SystemClock.elapsedRealtime()
                    if (base >= curElapsedRealtime) {
                        it.text =
                            String.format(
                                "%s %ss",
                                getString(R.string.text_r_acqu_rir_otp),
                                ((base - curElapsedRealtime) / 1000).tryCompleteZero()
                            )
                    } else {
                        it.text = getString(R.string.text_obtenir_otp)
                        stop()
                        mBinding.btnSendVerifyCode.isEnabled =
                            mBinding.etPhone.text.isNotEmpty()
                        this@LogUpActivity.isCountDown = false
                    }
                }
                isEnabled = false
            }.start()
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