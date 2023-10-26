package com.facile.immediate.electronique.fleurs.pret.main

import android.content.Intent
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

    private val fragments = hashMapOf(
        R.id.navigation_one to FirstFragment(),
        R.id.navigation_two to SecondFragment(),
        R.id.navigation_three to ThirdFragment()
    )

    override fun setStatusBar() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(statusBarDarkMode)
            .fitsSystemWindows(true)
            .init()
    }

    override fun buildView() {
        super.buildView()
        loadFragments(
            R.id.nav_host_fragment_activity_main,
            showPosition = 0,
            fragments[R.id.navigation_one],
            fragments[R.id.navigation_two],
            fragments[R.id.navigation_three]
        )
    }

    override fun setListener() {
        super.setListener()
        mBinding.navView.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.navigation_three) {
                if (!UserManager.isLogUp()) {
                    startActivity(Intent(this@MainActivity, LogUpActivity::class.java))
                    return@setOnItemSelectedListener false
                }
            }

            showFragmentAndHideOthers(fragments[item.itemId])

            return@setOnItemSelectedListener true
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.getIntExtra("selectedItemId", R.id.navigation_one)?.let {
            mBinding.navView.selectedItemId = it
        }
    }
}