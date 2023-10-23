package com.facile.immediate.electronique.fleurs.pret.main

import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.arthur.baselib.structure.base.view.BaseBindingActivity
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.common.UserManager
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityMainBinding
import com.facile.immediate.electronique.fleurs.pret.login.LogUpActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
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

    override fun setListener() {
        super.setListener()
        mBinding.navView.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.navigation_three) {
                if (!UserManager.isLogUp()) {
                    startActivity(Intent(this@MainActivity, LogUpActivity::class.java))
                    return@setOnItemSelectedListener false
                }
            }
            return@setOnItemSelectedListener true
        }
    }
}