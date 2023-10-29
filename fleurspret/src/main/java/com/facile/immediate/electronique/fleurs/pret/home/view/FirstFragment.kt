package com.facile.immediate.electronique.fleurs.pret.home.view

import androidx.lifecycle.Lifecycle
import com.arthur.baselib.structure.mvvm.view.BaseMVVMFragment
import com.facile.immediate.electronique.fleurs.pret.databinding.FragmentHomeHostBinding
import com.facile.immediate.electronique.fleurs.pret.home.ProductType
import com.facile.immediate.electronique.fleurs.pret.home.vm.FirstViewModel

class FirstFragment : BaseMVVMFragment<FragmentHomeHostBinding, FirstViewModel>() {

    override fun onLazyInit() {
        super.onLazyInit()
        mViewModel.sOrM()
    }

    override fun initLiveDataObserver() {
        super.initLiveDataObserver()
        mViewModel.prodTypeLiveData.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    ProductType.S -> {
                        childFragmentManager.beginTransaction().apply {
                            val singleProHFragment = SingleProHFragment()
                            replace(mBinding.fcvHomeContainer.id, singleProHFragment)
                            setMaxLifecycle(singleProHFragment, Lifecycle.State.RESUMED)
                        }.commitAllowingStateLoss()
                    }

                    ProductType.M -> {
                        childFragmentManager.beginTransaction().apply {
                            val multiProHFragment = MultiProHFragment()
                            replace(mBinding.fcvHomeContainer.id, multiProHFragment)
                            setMaxLifecycle(multiProHFragment, Lifecycle.State.RESUMED)
                        }.commitAllowingStateLoss()
                    }

                    else -> {
                        isLoaded = false
                    }
                }
            }
        }
    }
}