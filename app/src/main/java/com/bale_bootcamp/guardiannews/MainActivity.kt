package com.bale_bootcamp.guardiannews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.bale_bootcamp.guardiannews.adapter.NewsPagerAdapter
import com.bale_bootcamp.guardiannews.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding
    private val toggle by lazy {
        ActionBarDrawerToggle(
            this,
            binding.root,
            R.string.drawer_open,
            R.string.drawer_close
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: starts")
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        Log.d(TAG, "onCreate: binding created")
        setContentView(binding.root)

        setUiComponents()
        Log.d(TAG, "onCreate: ui components set")
    }

    private fun setUiComponents() {
        setViewPager()
        Log.d(TAG, "onCreate: view pager set")
        setTabLayout()
        Log.d(TAG, "onCreate: tab layout set")
        setNavigationDrawer()
        Log.d(TAG, "onCreate: navigation drawer set")

    }

    private fun setViewPager() {
        binding.viewPager.adapter = NewsPagerAdapter(this)
        Log.d(TAG, "onCreate: view pager adapter set")
    }


    private fun setTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) {
            tab, position ->
            when(position) {
                0 -> tab.text = "Home"
                1 -> tab.text = "World"
                2 -> tab.text = "Science"
                3 -> tab.text = "Environment"
                4 -> tab.text = "Sport"
            }
        }.attach()

        Log.d(TAG, "onCreate: tab layout mediator attached")
    }

    private fun setNavigationDrawer() {
        val drawerLayout = binding.root
        val toolbar = binding.toolbar

        drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = false
        toolbar.setNavigationIcon(R.drawable.menu_24dp)

        toolbar.setNavigationOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState()
    }
}