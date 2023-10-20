package com.facile.immediate.electronique.fleurs.pret.home

import android.widget.TextView
import com.arthur.baselib.structure.mvvm.view.BaseMVVMFragment
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentHomeBinding

class FirstFragment : BaseMVVMFragment<FragmentHomeBinding, FirstViewModel>() {

    override fun buildView() {
        super.buildView()
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        val textView: TextView = mBinding.textHome
        mViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
    }
}