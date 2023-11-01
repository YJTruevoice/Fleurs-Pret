package com.facile.immediate.electronique.fleurs.pret.main

import android.content.Intent
import androidx.fragment.app.Fragment
import com.arthur.baselib.structure.base.view.BaseBindingActivity
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.UserManager
import com.facile.immediate.electronique.fleurs.pret.common.loadFragments
import com.facile.immediate.electronique.fleurs.pret.common.showFragmentAndHideOthers
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityMainBinding
import com.facile.immediate.electronique.fleurs.pret.home.view.FirstFragment
import com.facile.immediate.electronique.fleurs.pret.login.LogUpActivity
import com.facile.immediate.electronique.fleurs.pret.mine.ThirdFragment
import com.facile.immediate.electronique.fleurs.pret.order.SecondFragment
import com.gyf.immersionbar.ImmersionBar


class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    private val fragments = mutableMapOf<Int,Fragment>()
    override fun setStatusBar() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(statusBarDarkMode)
            .fitsSystemWindows(true)
            .init()
    }

    override fun buildView() {
        super.buildView()

        fragments[mBinding.inTabFirst.navigationOne.id] = FirstFragment()
        fragments[mBinding.inTabSecond.navigationTwo.id] = SecondFragment()
        fragments[mBinding.inTabThird.navigationThree.id] = ThirdFragment()

        loadFragments(
            R.id.nav_host_fragment_activity_main,
            showPosition = 0,
            fragments[mBinding.inTabFirst.navigationOne.id],
            fragments[mBinding.inTabSecond.navigationTwo.id],
            fragments[mBinding.inTabThird.navigationThree.id]
        )

        initTab()
    }

    private fun initTab(){
        mBinding.inTabFirst.navigationOne.isSelected = true
        mBinding.inTabSecond.navigationTwo.isSelected = false
        mBinding.inTabThird.navigationThree.isSelected = false

        showFragmentAndHideOthers(fragments[R.id.navigation_one])
    }

    override fun setListener() {
        super.setListener()
        mBinding.inTabFirst.navigationOne.setOnClickListener { item ->
            mBinding.inTabFirst.navigationOne.isSelected = true
            mBinding.inTabSecond.navigationTwo.isSelected = false
            mBinding.inTabThird.navigationThree.isSelected = false

            showFragmentAndHideOthers(fragments[item.id])
        }
        mBinding.inTabSecond.navigationTwo.setOnClickListener { item ->
            mBinding.inTabSecond.navigationTwo.isSelected = true
            mBinding.inTabFirst.navigationOne.isSelected = false
            mBinding.inTabThird.navigationThree.isSelected = false

            showFragmentAndHideOthers(fragments[item.id])
        }

        mBinding.inTabThird.navigationThree.setOnClickListener { item ->
            if (item.id == R.id.navigation_three) {
                if (!UserManager.isLogUp()) {
                    startActivity(Intent(this@MainActivity, LogUpActivity::class.java))
                    return@setOnClickListener
                }
            }
            mBinding.inTabSecond.navigationTwo.isSelected = false
            mBinding.inTabFirst.navigationOne.isSelected = false
            mBinding.inTabThird.navigationThree.isSelected = true

            showFragmentAndHideOthers(fragments[item.id])
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.getIntExtra("selectedItemId", R.id.navigation_one)?.let {
            mBinding.inTabFirst.navigationOne.isSelected = true
        }
    }
}