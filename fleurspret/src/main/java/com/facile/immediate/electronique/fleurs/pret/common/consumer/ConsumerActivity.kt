package com.facile.immediate.electronique.fleurs.pret.common.consumer

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.arthur.baselib.structure.base.view.BaseBindingActivity
import com.arthur.commonlib.ability.Toaster
import com.arthur.commonlib.utils.ClipBoardUtil
import com.arthur.network.ext.scopeNetLife
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.setting.SettingAPI
import com.facile.immediate.electronique.fleurs.pret.common.user.UserManager
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityConsumerLayoutBinding
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


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
            settingService.consumerConfig()
        }.success {
            it.aggressiveParentMethod?.let { consumers ->
                setData(consumers)
            }
        }.showLoading(true).launch()
    }


    private fun setData(consumerEntity: ConsumerEntity) {

        mBinding.rvPhoneList.apply {
            layoutManager = LinearLayoutManager(context)
            consumerEntity.lazyNervousToiletHam?.let { phones ->
                adapter = TextAdapter(ConsumeType.PHONE).apply { addAll(phones) }
            }
        }
        mBinding.rvWhatsAppList.apply {
            layoutManager = LinearLayoutManager(context)
            consumerEntity.favouriteHungerLazyCoach?.let { apps ->
                adapter = TextAdapter(ConsumeType.WHATSAPP).apply { addAll(apps) }
            }
        }
        mBinding.rvEmailList.apply {
            layoutManager = LinearLayoutManager(context)
            consumerEntity.freezingFieldPostmanSpaghetti?.let { emails ->
                adapter = TextAdapter(ConsumeType.EMAIL).apply { addAll(emails) }
            }
        }

        mBinding.tvConsumerDesc.text = consumerEntity.looseArabicCustom
    }

    inner class TextAdapter(private val type: ConsumeType) :
        BaseQuickAdapter<ItemEntity, QuickViewHolder>() {
        override fun onCreateViewHolder(
            context: Context,
            parent: ViewGroup,
            viewType: Int
        ): QuickViewHolder {
            return QuickViewHolder(R.layout.item_simple_text_list, parent)
        }

        override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: ItemEntity?) {
            item?.apply {
                holder.getView<TextView>(R.id.tv_text).apply {
                    text = item.egyptianHeight
                    setOnClickListener {
                        ClipBoardUtil.copyText(context, item.egyptianHeight ?: "")
                        Toaster.showToast(context.getString(R.string.text_copie_r_ussie))
                        lifecycleScope.launchWhenStarted {
                            delay(800)
                            withContext(Dispatchers.Main){
                                when (type) {
                                    ConsumeType.PHONE -> {
                                        val intent =
                                            Intent(Intent.ACTION_DIAL, Uri.parse("tel:${item.egyptianHeight}"))
                                        if (intent.resolveActivity(context.packageManager) != null) {
                                            context.startActivity(intent)
                                        }
                                    }

                                    ConsumeType.WHATSAPP -> {
                                        // 检查设备上是否安装了WhatsApp
                                        if (isWhatsAppInstalled()) {
                                            // 如果已安装WhatsApp，直接跳转到WhatsApp内部
                                            openWhatsApp(item.egyptianHeight ?: "")
                                        } else {
                                            // 如果未安装WhatsApp，打开浏览器访问WhatsApp网页
                                            openWhatsAppWeb(item.egyptianHeight ?: "")
                                        }
                                    }

                                    ConsumeType.EMAIL -> {

                                        // 创建一个Intent，指定动作为发送邮件
                                        val emailIntent = Intent(Intent.ACTION_SENDTO)
                                        emailIntent.data = Uri.parse("mailto: ${item.egyptianHeight}")

                                        // 检查是否有能够处理该Intent的应用程序
                                        if (emailIntent.resolveActivity(packageManager) != null) {
                                            // 启动邮件应用程序
                                            startActivity(emailIntent);
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

    private fun isWhatsAppInstalled(): Boolean {
        val packageManager = packageManager
        return try {
            // 使用WhatsApp的包名检查是否安装
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun openWhatsApp(phone: String?) {
        // 如果已安装WhatsApp，直接跳转到WhatsApp内部
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("whatsapp://send?phone=$phone")
        startActivity(intent)
    }

    private fun openWhatsAppWeb(phone: String?) {
        // 如果未安装WhatsApp，打开浏览器访问WhatsApp网页
        val url = "https://web.whatsapp.com/send?phone=$phone"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}