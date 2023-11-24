package com.facile.immediate.electronique.fleurs.pret.loan.view.dialog

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.arthur.commonlib.utils.DensityUtils.Companion.dp2px
import com.arthur.commonlib.utils.image.DisplayUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.setting.RecommendPro
import com.facile.immediate.electronique.fleurs.pret.databinding.DialogRecommendProLayoutBinding
import com.facile.immediate.electronique.fleurs.pret.dialog.entity.BaseDialogConfigEntity
import com.facile.immediate.electronique.fleurs.pret.dialog.entity.CommonDialogConfigEntity
import com.facile.immediate.electronique.fleurs.pret.dialog.widget.BaseDialog
import com.facile.immediate.electronique.fleurs.pret.web.WebActivity
import kotlinx.parcelize.Parcelize

open class RecommendProDialog @JvmOverloads constructor(
    context: Context,
    themeResId: Int = R.style.CustomDialog
) : BaseDialog(context, themeResId) {

    private lateinit var mBinding: DialogRecommendProLayoutBinding


    override fun getContentView(): View {
        mBinding = DialogRecommendProLayoutBinding.inflate(LayoutInflater.from(context))
        return mBinding.root
    }

    override fun setDialogData(config: BaseDialogConfigEntity?) {
        (config as? RecommendProDialogConfigEntity)?.let {
            config.touchOutsideCancelAble = false
            // 公共初始化
            super.setDialogData(config)

            mBinding.ivClose.apply {
                setOnClickListener {
                    dismiss()
                }
            }

            // count down
            mBinding.tvCountDown.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    isCountDown = true
                }
                base = SystemClock.elapsedRealtime() + (60 + 1) * 1000
                setOnChronometerTickListener {
                    val curElapsedRealtime = SystemClock.elapsedRealtime()
                    if (base <= curElapsedRealtime) {
                        stop()
                        dismiss()
                    } else {
                        it.text = String.format("%sS", (base - curElapsedRealtime) / 1000)
                    }
                }
            }.start()


            mBinding.rvRecommendProList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = object : BaseQuickAdapter<RecommendPro, QuickViewHolder>() {
                    override fun onCreateViewHolder(
                        context: Context,
                        parent: ViewGroup,
                        viewType: Int
                    ): QuickViewHolder {
                        return QuickViewHolder(R.layout.item_recommend_pro_layout, parent)
                    }

                    override fun onBindViewHolder(
                        holder: QuickViewHolder,
                        position: Int,
                        item: RecommendPro?
                    ) {
                        item?.apply {
                            DisplayUtils.displayImageAsRound(
                                this.chineseGeographyTenseHopefulSpirit,
                                holder.getView(R.id.iv_app_logo),
                                radius = 4f.dp2px(context)
                            )

                            holder.setText(R.id.tv_app_name, this.foggyEveryoneLivingArmyPrinter)

                            holder.getView<View>(R.id.tv_btn_go).setOnClickListener {
                                WebActivity.open(context, this.arabicNephewNoisyZooNoisyBaby ?: "")
                            }
                        }
                    }

                }
            }
        }
    }

    override fun dismiss() {
        mBinding.tvCountDown.stop()
        super.dismiss()
        config?.confirmCallback?.invoke(this@RecommendProDialog)
    }

    companion object {
        fun with(context: Context): Builder {
            return Builder(context)
        }
    }

    class Builder constructor(var context: Context) : BaseDialog.Builder<Builder>(context) {
        override fun createDialog(): BaseDialog {
            return RecommendProDialog(context)
        }

        override fun createConfig(): BaseDialogConfigEntity {
            return RecommendProDialogConfigEntity()
        }

        fun recommendPros(recommendPros: List<RecommendPro>): Builder {
            (config as? RecommendProDialogConfigEntity)?.recommendPros = recommendPros
            return this
        }

        fun countDown(count: Int = 60): Builder {
            (config as? RecommendProDialogConfigEntity)?.countDown = count
            return this
        }
    }

    @Parcelize
    class RecommendProDialogConfigEntity(
        var countDown: Int = 3,
        var recommendPros: List<RecommendPro> = emptyList(),
    ) : CommonDialogConfigEntity(), Parcelable

}