package com.facile.immediate.electronique.fleurs.pret.common.consumer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.arthur.baselib.structure.base.view.BaseBindingActivity
import com.arthur.commonlib.ability.Toaster
import com.arthur.commonlib.utils.ClipBoardUtil
import com.arthur.network.ext.scopeNetLife
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.setting.GlobalSetting
import com.facile.immediate.electronique.fleurs.pret.common.setting.SettingAPI
import com.facile.immediate.electronique.fleurs.pret.common.user.UserManager
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityConsumerLayoutBinding
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr

class ConsumerActivity : BaseBindingActivity<ActivityConsumerLayoutBinding>() {

    companion object {
        fun goBranch(context: Context) {
            if (UserManager.isLogUp()) {
                CrispMgr.launchChat(context)
            } else {
                context.startActivity(Intent(context, ConsumerActivity::class.java))
            }
        }

        fun go(context: Context) {
            context.startActivity(Intent(context, ConsumerActivity::class.java))
        }
    }

    private val settingService = NetMgr.get().service<SettingAPI>()

    override fun buildView() {
        super.buildView()
        mBinding.inTitleBar.apply {
            ivCustomer.visibility = View.GONE
            ivBack.setOnClickListener {
                finish()
            }

            tvTitle.text = getString(R.string.text_centre_de_service_client)
        }
    }

    override fun processLogic() {
        super.processLogic()
        scopeNetLife {
            settingService.globalSetting("longJawLeadingInjuryGreatTongue,hopelessLoudSkill,absentMoreCandle,formerDrierKiloJunk")
        }.success {
            it.aggressiveParentMethod?.let { setting ->
                setData(setting)
            }
        }.showLoading(true).launch()
    }


    private fun setData(setting: GlobalSetting) {

        mBinding.rvPhoneList.apply {
            layoutManager = LinearLayoutManager(context)
            setting.hopelessLoudSkill?.split(",")?.let { phones ->
                adapter = TextAdapter(ConsumeType.PHONE).apply { addAll(phones) }
            }
        }
        mBinding.rvWhatsAppList.apply {
            layoutManager = LinearLayoutManager(context)
            setting.absentMoreCandle?.split(",")?.let { apps ->
                adapter = TextAdapter(ConsumeType.WHATSAPP).apply { addAll(apps) }
            }
        }
        mBinding.rvEmailList.apply {
            layoutManager = LinearLayoutManager(context)
            setting.longJawLeadingInjuryGreatTongue?.split(",")?.let { emails ->
                adapter = TextAdapter(ConsumeType.EMAIL).apply { addAll(emails) }
            }
        }

        mBinding.tvConsumerDesc.text = setting.formerDrierKiloJunk
    }

    inner class TextAdapter(private val type: ConsumeType) :
        BaseQuickAdapter<String, QuickViewHolder>() {
        override fun onCreateViewHolder(
            context: Context,
            parent: ViewGroup,
            viewType: Int
        ): QuickViewHolder {
            return QuickViewHolder(R.layout.item_simple_text_list, parent)
        }

        override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: String?) {
            holder.getView<TextView>(R.id.tv_text).apply {
                text = item
                setOnClickListener {
                    when (type) {
                        ConsumeType.PHONE -> {
                            val intent =
                                Intent(Intent.ACTION_DIAL, Uri.parse("tel:$item"))
                            if (intent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(intent)
                            } else {
                                ClipBoardUtil.copyText(context, item ?: "")
                                Toaster.showToast(context.getString(R.string.text_copie_r_ussie))
                            }
                        }

                        ConsumeType.EMAIL, ConsumeType.WHATSAPP -> {
                            ClipBoardUtil.copyText(context, item ?: "")
                            Toaster.showToast(context.getString(R.string.text_copie_r_ussie))
                        }
                    }
                }
            }
        }
    }
}