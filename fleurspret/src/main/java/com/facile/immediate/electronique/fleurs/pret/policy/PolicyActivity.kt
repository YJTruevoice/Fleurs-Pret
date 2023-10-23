package com.facile.immediate.electronique.fleurs.pret.policy

import android.content.Intent
import android.text.method.ScrollingMovementMethod
import com.arthur.baselib.structure.base.view.BaseBindingActivity
import com.facile.immediate.electronique.fleurs.pret.common.PrivacyPolicyDisplayUtil
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityPolicyBinding
import com.facile.immediate.electronique.fleurs.pret.main.MainActivity
import com.facile.immediate.electronique.fleurs.pret.utils.PolicyUtil

/**
 * @Author arthur
 * @Date 2023/10/17
 * @Description
 */
class PolicyActivity : BaseBindingActivity<ActivityPolicyBinding>() {

    override fun buildView() {
        super.buildView()

        mBinding.tvPermissionDeclare.movementMethod = ScrollingMovementMethod.getInstance()


        PrivacyPolicyDisplayUtil.displayPrivacyPolicyGuide(this, mBinding.tvReadPrivacyPolicyGuide)
    }

    override fun setListener() {
        super.setListener()
        mBinding.tvSkip.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        mBinding.tvAccept.setOnClickListener {
            PolicyUtil.policyAccepted()
            // 申请权限
        }
    }
}