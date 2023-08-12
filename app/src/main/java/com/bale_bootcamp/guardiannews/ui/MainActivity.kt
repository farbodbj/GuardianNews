package com.bale_bootcamp.guardiannews.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bale_bootcamp.guardiannews.R
import com.bale_bootcamp.guardiannews.data.local.datastore.SettingsDataStore
import com.bale_bootcamp.guardiannews.data.repository.SettingsRepository
import com.bale_bootcamp.guardiannews.databinding.ActivityMainBinding
import com.bale_bootcamp.guardiannews.utility.Utils.getColorThemeOverlayId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val settingsRepository by lazy {
        SettingsRepository.getInstance(SettingsDataStore
            .SettingsDataStoreFactory(this)
            .create())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeFromPrefs()
        Log.d(TAG, "onCreate: starts")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        Log.d(TAG, "onCreate: binding created")
        setContentView(binding.root)
    }

    private fun setThemeFromPrefs() = runBlocking {
        val it = settingsRepository.getColorTheme().first()
            runOnUiThread {
                val themeId = getColorThemeOverlayId(it)
                setTheme(themeId)
                setStatusBarColorFromTheme(themeId)
            }

    }

    private fun setStatusBarColorFromTheme(themeId: Int) =
        theme.obtainStyledAttributes(themeId, intArrayOf(com.google.android.material.R.attr.colorPrimary))
            .apply {
                val colorPrimary = getColor(0, 0)
                window.statusBarColor = colorPrimary
            }

}