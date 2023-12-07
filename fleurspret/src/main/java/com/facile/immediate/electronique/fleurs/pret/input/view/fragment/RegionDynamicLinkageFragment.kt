package com.facile.immediate.electronique.fleurs.pret.input.view.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.arthur.baselib.structure.base.view.BaseBindingFragment
import com.arthur.commonlib.ability.AppKit
import com.arthur.commonlib.utils.DensityUtils
import com.arthur.network.ext.scopeNetLife
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.bottomsheet.BottomSheetDialog
import com.facile.immediate.electronique.fleurs.pret.bottomsheet.IDialogContent
import com.facile.immediate.electronique.fleurs.pret.common.CommonItemDecoration
import com.facile.immediate.electronique.fleurs.pret.common.config.CommonConfigItem
import com.facile.immediate.electronique.fleurs.pret.common.config.ConfigAPI
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentThreeLevelDynamicLinkageBinding
import com.facile.immediate.electronique.fleurs.pret.input.model.Region
import com.facile.immediate.electronique.fleurs.pret.net.NetMgr

class RegionDynamicLinkageFragment(private var selectConfirmed: ((Region?, Region?, Region?) -> Unit)? = null) :
    BaseBindingFragment<FragmentThreeLevelDynamicLinkageBinding>(), IDialogContent {

    private val configService = NetMgr.get().service<ConfigAPI>()

    private var regionSelected: CommonConfigItem? = null

    private var selectedProvince: Region? = null
    private var selectedCity: Region? = null
    private var selectedDistrict: Region? = null

    private val provinceAdapter: LevelListAdapter by lazy {
        LevelListAdapter() {
            selectedProvince = it
            cities(it.eastBasicFavouriteSupermarket)
        }
    }
    private val cityAdapter: LevelListAdapter by lazy {
        LevelListAdapter() {
            selectedCity = it
            district(it.eastBasicFavouriteSupermarket)
        }
    }
    private val districtAdapter: LevelListAdapter by lazy {
        LevelListAdapter() { selectedDistrict = it }
    }

    override fun buildView() {
        super.buildView()
        initRv()
    }

    override fun setListener() {
        super.setListener()
        mBinding.tvDialogBottomSheetConfirm.setOnClickListener {
            dismiss()
            selectConfirmed?.invoke(selectedProvince, selectedCity, selectedDistrict)
        }
        mBinding.tvDialogBottomSheetClose.setOnClickListener {
            dismiss()
        }
    }

    override fun processLogic() {
        super.processLogic()
        regionSelected = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("regionSelected", CommonConfigItem::class.java)
        } else {
            arguments?.getParcelable("regionSelected")
        }
        province()
    }

    override val current: Fragment = this

    private fun initRv() {
        mBinding.rvLevel1st.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = provinceAdapter
            addItemDecoration(CommonItemDecoration(1f))
        }
        mBinding.rvLevel2st.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cityAdapter
            addItemDecoration(CommonItemDecoration(1f))
            visibility = View.GONE
        }
        mBinding.rvLevel3st.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = districtAdapter
            addItemDecoration(CommonItemDecoration(1f))
            visibility = View.GONE
        }
    }

    private fun province() {
        scopeNetLife {
            configService.region("-1", "1")
        }.success { res ->
            res.aggressiveParentMethod?.let { province ->
                val region = regionSelected?.let { regionSelected ->
                    province.find { regionSelected.code == it.eastBasicFavouriteSupermarket }
                        ?.apply {
                            selected = true
                        }
                }
                provinceAdapter.clear()
                provinceAdapter.addAll(province)
                if (region != null) {
                    mBinding.rvLevel1st.post {
                        if (isValid()) {
                            mBinding.rvLevel1st.scrollToPosition(province.indexOf(region))
                        }
                    }
                    cities(region.eastBasicFavouriteSupermarket)
                }
            }
        }.showLoading(true).launch()
    }

    private fun cities(provinceId: String) {
        scopeNetLife {
            configService.region(provinceId, "2")
        }.success { res ->
            res.aggressiveParentMethod?.let { cities ->
                if (cities.isNotEmpty()) {
                    val region = regionSelected?.let { regionSelected ->
                        cities.find { regionSelected.code == it.eastBasicFavouriteSupermarket }
                            ?.apply {
                                selected = true
                            }
                    }
                    cityAdapter.clear()
                    cityAdapter.addAll(cities)
                    mBinding.vLine2st.visibility = View.VISIBLE
                    mBinding.rvLevel2st.visibility = View.VISIBLE
                    if (region != null) {
                        mBinding.rvLevel2st.post {
                            if (isValid()) {
                                mBinding.rvLevel2st.scrollToPosition(cities.indexOf(region))
                            }
                        }
                        district(region.eastBasicFavouriteSupermarket)
                    }
                }
            }
        }.showLoading(true).launch()
    }

    private fun district(cityId: String) {
        scopeNetLife {
            configService.region(cityId, "3")
        }.success { res ->
            res.aggressiveParentMethod?.let { district ->
                if (district.isNotEmpty()) {
                    val region = regionSelected?.let { regionSelected ->
                        district.find { regionSelected.code == it.eastBasicFavouriteSupermarket }
                            ?.apply {
                                selected = true
                            }
                    }
                    districtAdapter.clear()
                    districtAdapter.addAll(district)
                    mBinding.vLine3st.visibility = View.VISIBLE
                    mBinding.rvLevel3st.visibility = View.VISIBLE
                    if (region != null) {
                        mBinding.rvLevel3st.post {
                            if (isValid()) {
                                mBinding.rvLevel3st.scrollToPosition(district.indexOf(region))
                            }
                        }
                    }
                }
            }
        }.showLoading(true).launch()
    }

    companion object {
        fun show(
            ac: FragmentActivity,
            regionSelected: CommonConfigItem? = null,
            selectConfirmed: ((Region?, Region?, Region?) -> Unit)? = null
        ) {
            show(ac.supportFragmentManager, regionSelected, selectConfirmed)
        }

        fun show(
            fm: FragmentManager,
            regionSelected: CommonConfigItem? = null,
            selectConfirmed: ((Region?, Region?, Region?) -> Unit)? = null
        ) {
            BottomSheetDialog.withFixedHeight()
                .height(DensityUtils.dp2px(AppKit.context, 500f))
                .wrapHeight(true)
                .content(RegionDynamicLinkageFragment(selectConfirmed).apply {
                    arguments = Bundle().apply { putParcelable("regionSelected", regionSelected) }
                })
                .cancelOnTouchOutsize(false)
                .build()
                .show(fm, "SelectRegionPanel")
        }
    }

    class LevelListAdapter(private val selectedItem: (Region) -> Unit) :
        BaseQuickAdapter<Region, QuickViewHolder>() {
        override fun onCreateViewHolder(
            context: Context,
            parent: ViewGroup,
            viewType: Int
        ): QuickViewHolder {
            return QuickViewHolder(R.layout.item_choose_layout, parent)
        }

        override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: Region?) {
            item?.apply {
                holder.setText(R.id.tv_choose_item, item.normalAppointmentHeadmistressMachine)


                holder.getView<View>(R.id.fl_item_root).apply {
                    isSelected = item.selected
                    setOnClickListener {
                        items.forEach {
                            it.selected = it == item
                        }

                        notifyItemRangeChanged(0, items.size)
                        selectedItem.invoke(item)
                    }
                }
            }
        }

        fun clear() {
            for (i in items) {
                remove(i)
            }
        }
    }
}