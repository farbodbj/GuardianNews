package com.bale_bootcamp.guardiannews.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.bale_bootcamp.guardiannews.R
import com.bale_bootcamp.guardiannews.ui.news.NewsPagerAdapter
import com.bale_bootcamp.guardiannews.data.network.NewsApiService
import com.bale_bootcamp.guardiannews.databinding.FragmentDefaultBinding
import com.bale_bootcamp.guardiannews.ui.settings.SettingsFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "DefaultFragment"
@AndroidEntryPoint
class DefaultFragment : Fragment() {

    private var _binding: FragmentDefaultBinding? = null
    private val binding get() = _binding!!

    private val navDrawerItems = mapOf(
         R.id.nav_home to 0,
         R.id.nav_world to 1,
         R.id.nav_science to 2,
         R.id.nav_environment to 3,
         R.id.nav_sport to 4,
         R.id.nav_settings to 5)


    private val toggle by lazy {
        ActionBarDrawerToggle(
            activity,
            binding.root,
            R.string.drawer_open,
            R.string.drawer_close
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDefaultBinding.inflate(inflater, container, false)
        if (savedInstanceState == null)
            setUiComponents()
        return binding.root
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
            activity?.let { it ->
                NewsPagerAdapter(
                    it,
                    NewsApiService.Category
                        .values()
                        .map { it.categoryName }
                )
            }

        binding.viewPager.offscreenPageLimit = 5
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
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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
                val selectedNavItem = navDrawerItems[it.itemId]

                if(selectedNavItem in 0..4) {
                    viewPager.currentItem = selectedNavItem!!
                } else {

                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.fragment_container, SettingsFragment())
                        ?.commit()
                }
                true
            } catch (e: IllegalStateException) {
                false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}