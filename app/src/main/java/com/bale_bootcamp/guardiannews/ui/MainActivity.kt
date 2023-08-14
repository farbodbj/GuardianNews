package com.bale_bootcamp.guardiannews.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.bale_bootcamp.guardiannews.GuardianNewsApp
import com.bale_bootcamp.guardiannews.R
import com.bale_bootcamp.guardiannews.data.repository.SettingsRepository
import com.bale_bootcamp.guardiannews.databinding.ActivityMainBinding
import com.bale_bootcamp.guardiannews.ui.settings.model.TextSize
import com.bale_bootcamp.guardiannews.utility.Utils.getColorThemeOverlayId
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.lang.Exception


private const val TAG = "MainActivity"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @ApplicationContext lateinit var appContext: Context

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface ThemeAccessorEntryPoint {
        val settingsRepository: SettingsRepository
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeFromPrefs()
        Log.d(TAG, "onCreate: starts")
        super.onCreate(savedInstanceState)
        setFontScaleFromPrefs(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        Log.d(TAG, "onCreate: binding created")
        setContentView(binding.root)
    }

    override fun attachBaseContext(newBase: Context?) {
        Log.d(TAG, "attachBaseContext called, is newBase null: ${newBase == null}")
        val config = setFontScaleFromPrefs(newBase!!)
        Log.d(TAG, "super called with config font scale: ${config.resources.configuration.fontScale}")
        super.attachBaseContext(config)

    }

    private fun setFontScaleFromPrefs(context: Context): Context {
        val config = context.resources.configuration
        config.fontScale = getFontSizeFromPrefs(context)
        Log.d(TAG, "setting scaling all fonts to ${config.fontScale}")
        return context.createConfigurationContext(config)
    }

    private fun getFontSizeFromPrefs(context: Context) = runBlocking {
        Log.d(TAG, "getFontSizeFromPrefs called")
        val it = EntryPointAccessors.fromApplication<ThemeAccessorEntryPoint>(context)
            .settingsRepository
            .getFontSize()
            .first()

        Log.d(TAG, it)
        when(TextSize.findByStr(it)) {
            TextSize.SMALL -> 0.85F
            TextSize.MEDIUM -> 1.0F
            TextSize.LARGE -> 1.15F
        }
    }


    private fun setThemeFromPrefs() = runBlocking {
        val theme = EntryPointAccessors.fromApplication<ThemeAccessorEntryPoint>(this@MainActivity)
                .settingsRepository
                .getColorTheme()
                .first()
        runOnUiThread {
            val themeId = getColorThemeOverlayId(theme)
            setTheme(themeId)
            setStatusBarColorFromTheme(themeId)
        }
    }

    private fun setStatusBarColorFromTheme(themeId: Int) =
        theme.obtainStyledAttributes(
            themeId,
            intArrayOf(com.google.android.material.R.attr.colorPrimary)
        )
            .apply {
                val colorPrimary = getColor(0, 0)
                window.statusBarColor = colorPrimary
            }

}