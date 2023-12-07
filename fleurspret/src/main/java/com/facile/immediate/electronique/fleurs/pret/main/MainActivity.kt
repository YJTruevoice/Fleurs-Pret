package com.facile.immediate.electronique.fleurs.pret.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.arthur.baselib.structure.base.view.BaseBindingActivity
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.user.UserManager
import com.facile.immediate.electronique.fleurs.pret.common.event.HomeOrdState
import com.facile.immediate.electronique.fleurs.pret.common.loadFragments
import com.facile.immediate.electronique.fleurs.pret.common.showFragmentAndHideOthers
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityMainBinding
import com.facile.immediate.electronique.fleurs.pret.home.view.FirstFragment
import com.facile.immediate.electronique.fleurs.pret.login.LogUpActivity
import com.facile.immediate.electronique.fleurs.pret.mine.ThirdFragment
import com.facile.immediate.electronique.fleurs.pret.order.view.SecondFragment
import com.gyf.immersionbar.ImmersionBar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    private val fragments = mutableMapOf<Int, Fragment>()
    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        EventBus.getDefault().register(this)
    }
    override fun setStatusBar() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(statusBarDarkMode)
            .fitsSystemWindows(true)
            .init()
    }

    override fun buildView() {
        super.buildView()

        fragments[R.id.navigation_one] = FirstFragment()
        fragments[R.id.navigation_two] = SecondFragment()
        fragments[R.id.navigation_three] = ThirdFragment()

        loadFragments(
            R.id.nav_host_fragment_activity_main,
            showPosition = 0,
            fragments[R.id.navigation_one],
            fragments[R.id.navigation_two],
            fragments[R.id.navigation_three]
        )

        initTab()
    }

    private fun initTab() {
        mBinding.inTabFirst.navigationOne.isSelected = true
        mBinding.inTabSecond.navigationTwo.isSelected = false
        mBinding.inTabThird.navigationThree.isSelected = false

        showFragmentAndHideOthers(fragments[R.id.navigation_one])
    }

    override fun setListener() {
        super.setListener()
        mBinding.inTabFirst.navigationOne.setOnClickListener {
            mBinding.inTabFirst.navigationOne.isSelected = true
            mBinding.inTabSecond.navigationTwo.isSelected = false
            mBinding.inTabThird.navigationThree.isSelected = false

            showFragmentAndHideOthers(fragments[R.id.navigation_one])
        }
        mBinding.inTabSecond.navigationTwo.setOnClickListener {
            if (!UserManager.isLogUp()) {
                startActivity(Intent(this@MainActivity, LogUpActivity::class.java))
                return@setOnClickListener
            }
            mBinding.inTabSecond.navigationTwo.isSelected = true
            mBinding.inTabFirst.navigationOne.isSelected = false
            mBinding.inTabThird.navigationThree.isSelected = false

            showFragmentAndHideOthers(fragments[R.id.navigation_two])
        }

        mBinding.inTabThird.navigationThree.setOnClickListener {
            if (!UserManager.isLogUp()) {
                startActivity(Intent(this@MainActivity, LogUpActivity::class.java))
                return@setOnClickListener
            }
            mBinding.inTabSecond.navigationTwo.isSelected = false
            mBinding.inTabFirst.navigationOne.isSelected = false
            mBinding.inTabThird.navigationThree.isSelected = true

            showFragmentAndHideOthers(fragments[R.id.navigation_three])
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.getIntExtra("selectedItemId", R.id.navigation_one)?.let {

            when (it) {
                R.id.navigation_one -> {
                    mBinding.inTabFirst.navigationOne.isSelected = true
                    mBinding.inTabSecond.navigationTwo.isSelected = false
                    mBinding.inTabThird.navigationThree.isSelected = false
                }

                R.id.navigation_two -> {
                    mBinding.inTabFirst.navigationOne.isSelected = false
                    mBinding.inTabSecond.navigationTwo.isSelected = true
                    mBinding.inTabThird.navigationThree.isSelected = false
                }

                R.id.navigation_three -> {
                    mBinding.inTabFirst.navigationOne.isSelected = false
                    mBinding.inTabSecond.navigationTwo.isSelected = false
                    mBinding.inTabThird.navigationThree.isSelected = true
                }
            }

            showFragmentAndHideOthers(fragments[it])
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(homeOrdState: HomeOrdState){
        mBinding.inTabFirst.navigationOne.performClick()
    }
}