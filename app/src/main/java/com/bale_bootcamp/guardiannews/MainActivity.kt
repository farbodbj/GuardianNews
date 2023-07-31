package com.bale_bootcamp.guardiannews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bale_bootcamp.guardiannews.adapter.NewsPagerAdapter
import com.bale_bootcamp.guardiannews.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var _binding: ActivityMainBinding
    val binding get() = _binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUiComponents()
    }

    private fun setUiComponents() {
        setViewPager()
        setTabLayout()
    }

    private fun setViewPager() {
        val bundle = Bundle()
        bundle.putString("category", "world")

        binding.viewPager.adapter = NewsPagerAdapter(this@MainActivity)
            .addFragment(NewsFragment(), listOf("category" to "home"))
            .addFragment(NewsFragment(), listOf("category" to "world"))
            .addFragment(NewsFragment(), listOf("category" to "science"))
            .addFragment(NewsFragment(), listOf("category" to "environment"))
            .addFragment(NewsFragment(), listOf("category" to "sport"))
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
    }
}