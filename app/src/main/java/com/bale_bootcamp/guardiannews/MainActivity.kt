package com.bale_bootcamp.guardiannews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.bale_bootcamp.guardiannews.adapter.NewsPagerAdapter
import com.bale_bootcamp.guardiannews.databinding.ActivityMainBinding
import com.bale_bootcamp.guardiannews.network.NewsApiService
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.IllegalStateException

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
        binding.viewPager.adapter =
            NewsPagerAdapter(this,
                NewsApiService.Category
                    .values()
                    .map { it.categoryName }
            )

        Log.d(TAG, "onCreate: view pager adapter set")
    }


    private fun setTabLayout() {
        val navDrawer = binding.navView
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = navDrawer.menu.getItem(position).title
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
        setShowSelectedItem()
        syncDrawerWithViewPager()
    }

    private fun setShowSelectedItem() {
        val navDrawer = binding.navView
        binding.tabLayout.addOnTabSelectedListener(object: OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d(TAG, tab!!.position.toString())
                navDrawer.setCheckedItem(navDrawer.menu.getItem(tab.position).itemId)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // No implementation needed
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // No implementation needed
            }
        })
    }

    private fun syncDrawerWithViewPager() {
        val viewPager = binding.viewPager
        val navDrawer = binding.navView

        navDrawer.setNavigationItemSelectedListener {
            try {
                viewPager.currentItem =
                    when(it.itemId) {
                        R.id.nav_home ->  0
                        R.id.nav_world ->  1
                        R.id.nav_science -> 2
                        R.id.nav_environment -> 3
                        R.id.nav_sport -> 4
                        else -> throw IllegalStateException("unknown error")
                    }
                true
            } catch (e: IllegalStateException) {
                false
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState()
    }
}