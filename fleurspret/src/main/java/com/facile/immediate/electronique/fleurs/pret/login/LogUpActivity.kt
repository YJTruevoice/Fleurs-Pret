package com.facile.immediate.electronique.fleurs.pret.login

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.arthur.baselib.structure.mvvm.view.BaseMVVMActivity
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityLogUpBinding
import com.facile.immediate.electronique.fleurs.pret.login.vm.LogUpViewModel
import com.facile.immediate.electronique.fleurs.pret.main.MainActivity

class LogUpActivity : BaseMVVMActivity<ActivityLogUpBinding, LogUpViewModel>() {

    override fun buildView() {
        super.buildView()
        PrivacyPolicyDisplayUtil.displayPrivacyPolicyGuide(this, mBinding.tvReadPrivacyPolicyGuide)
    }

    override fun setListener() {
        super.setListener()
        mBinding.ivBack.setOnClickListener {
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
        }
        mBinding.ivClearVerify.setOnClickListener {
            mBinding.etVerifyCode.text.clear()
            mBinding.btnSendVerifyCode.isEnabled = false
        }

        mBinding.etPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                mBinding.btnSendVerifyCode.isEnabled = s?.isNotEmpty() == true
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
                mBinding.btnLogUp.isEnabled = s?.isNotEmpty() == true
                mViewModel.code = s.toString()
                mBinding.ivClearVerify.visibility =
                    if (s?.isNotEmpty() == true) View.VISIBLE else View.GONE
            }
        })
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        mViewModel.verifyCodeLiveData.observe(this) {

        }

        mViewModel.logUpSuccessLiveData.observe(this) {
            startActivity(Intent(this@LogUpActivity, MainActivity::class.java))
        }
    }
}