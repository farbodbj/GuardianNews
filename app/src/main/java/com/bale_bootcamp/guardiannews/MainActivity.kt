package com.bale_bootcamp.guardiannews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bale_bootcamp.guardiannews.adapter.NewsPagerAdapter
import com.bale_bootcamp.guardiannews.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding


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
    }

    private fun setViewPager() {
        binding.viewPager.adapter = NewsPagerAdapter(this@MainActivity)
            .addFragment(NewsFragment(), listOf("category" to "home"))
            .addFragment(NewsFragment(), listOf("category" to "world"))
            .addFragment(NewsFragment(), listOf("category" to "science"))
            .addFragment(NewsFragment(), listOf("category" to "environment"))
            .addFragment(NewsFragment(), listOf("category" to "sport"))
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
}