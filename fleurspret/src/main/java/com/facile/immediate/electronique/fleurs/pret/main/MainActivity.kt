package com.facile.immediate.electronique.fleurs.pret.main

import android.content.Context
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.arthur.baselib.structure.base.view.BaseBindingActivity
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityMainBinding
import com.facile.immediate.electronique.fleurs.pret.utils.AppLanguageUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gyf.immersionbar.ImmersionBar

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    override fun setStatusBar() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(statusBarDarkMode)
            .fitsSystemWindows(true)
            .titleBar(mBinding.toolBar)
            .init()
    }

    override fun buildView() {
        super.buildView()
        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.visibility = View.GONE

        val navView: BottomNavigationView = mBinding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_one, R.id.navigation_two, R.id.navigation_three
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun attachBaseLanguageContext(context: Context?): Context? {
        return super.attachBaseLanguageContext(context?.let {
            AppLanguageUtil.getAttachBaseContext(it)
        })
    }
}