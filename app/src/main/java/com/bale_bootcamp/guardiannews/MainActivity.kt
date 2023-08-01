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



    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: starts")
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        Log.d(TAG, "onCreate: binding created")
        setContentView(binding.root)

        // set fragment in the fragment container to the fragment_default
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, DefaultFragment())
            .commit()
    }
}