package com.bale_bootcamp.guardiannews.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHost
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
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

    private var backPressedTime: Long = 0

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
        addBackButtonCallback()
        binding = ActivityMainBinding.inflate(layoutInflater)
        Log.d(TAG, "onCreate: binding created")
        setContentView(binding.root)
    }

    private fun addBackButtonCallback() {
        val navController = Navigation.findNavController(this, R.id.fragment_container)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentId = navController.currentDestination?.id

                if(currentId == R.id.defaultFragment) {
                    if (System.currentTimeMillis() - backPressedTime < 2000) {
                        isEnabled = false
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.press_back_to_exit),
                            Toast.LENGTH_SHORT)
                        .show()
                    }
                    backPressedTime = System.currentTimeMillis()
                } else {
                    navController.navigateUp()
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)
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