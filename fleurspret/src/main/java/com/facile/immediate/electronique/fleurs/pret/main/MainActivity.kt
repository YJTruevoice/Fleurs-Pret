package com.facile.immediate.electronique.fleurs.pret.main

import android.content.Context
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.arthur.baselib.structure.base.view.BaseSimpleActivity
import com.facile.immediate.electronique.fleurs.pret.R
import com.facile.immediate.electronique.fleurs.pret.databinding.ActivityMainBinding
import com.facile.immediate.electronique.fleurs.pret.utils.AppLanguageUtil
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseSimpleActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun buildView() {
        super.buildView()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        binding.toolBar.visibility = View.GONE

        val navView: BottomNavigationView = binding.navView

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