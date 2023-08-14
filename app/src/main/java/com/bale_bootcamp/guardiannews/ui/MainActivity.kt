package com.bale_bootcamp.guardiannews.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bale_bootcamp.guardiannews.GuardianNewsApp
import com.bale_bootcamp.guardiannews.R
import com.bale_bootcamp.guardiannews.data.local.datastore.SettingsDataStore
import com.bale_bootcamp.guardiannews.data.repository.SettingsRepository
import com.bale_bootcamp.guardiannews.databinding.ActivityMainBinding
import com.bale_bootcamp.guardiannews.ui.settings.model.TextSize
import com.bale_bootcamp.guardiannews.utility.Utils.getColorThemeOverlayId
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.lang.Exception

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val settingsRepository by lazy {
        SettingsRepository.getInstance(SettingsDataStore
            .SettingsDataStoreFactory(GuardianNewsApp.getAppContext())
            .create())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeFromPrefs()
        Log.d(TAG, "onCreate: starts")
        super.onCreate(savedInstanceState)
        setFontScaleFromPrefs(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        Log.d(TAG, "onCreate: binding created")
        setContentView(binding.root)
        // set fragment in the fragment container to the fragment_default
        if(savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, DefaultFragment())
                .commit()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        Log.d(TAG, "attachBaseContext called, is newBase null: ${newBase == null}")
        try{
            val config = setFontScaleFromPrefs(newBase!!)
            Log.d(TAG, "super called with config font scale: ${config.resources.configuration.fontScale}")
            super.attachBaseContext(config)
        } catch (e: Exception) {
            super.attachBaseContext(GuardianNewsApp.getAppContext())
            Log.e(TAG, "${e.stackTrace.asList().joinToString() }}")
            Log.d(TAG, "default super called successfully")
        }
    }

    private fun setFontScaleFromPrefs(context: Context): Context {
        val config = context.resources.configuration
        config.fontScale = getFontSizeFromPrefs()
        Log.d(TAG, "setting scaling all fonts to ${config.fontScale}")
        return context.createConfigurationContext(config)
    }

    private fun getFontSizeFromPrefs() = runBlocking {
        Log.d(TAG, "getFontSizeFromPrefs called")
        val it = settingsRepository.getFontSize().first()
        Log.d(TAG, it)
        when(TextSize.findByStr(it)) {
            TextSize.SMALL -> 0.85F
            TextSize.MEDIUM -> 1.0F
            TextSize.LARGE -> 1.15F
        }
    }


    private fun setThemeFromPrefs() = runBlocking {
        Log.d(TAG, "setThemeFromPrefs called")
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