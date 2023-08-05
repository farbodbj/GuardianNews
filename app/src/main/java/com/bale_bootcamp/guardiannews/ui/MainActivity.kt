package com.bale_bootcamp.guardiannews.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bale_bootcamp.guardiannews.R
import com.bale_bootcamp.guardiannews.databinding.ActivityMainBinding

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