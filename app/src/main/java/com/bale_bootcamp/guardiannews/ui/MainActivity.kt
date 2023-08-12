package com.bale_bootcamp.guardiannews.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.bale_bootcamp.guardiannews.R
import com.bale_bootcamp.guardiannews.data.repository.SettingsRepository
import com.bale_bootcamp.guardiannews.databinding.ActivityMainBinding
import com.bale_bootcamp.guardiannews.utility.Utils.getColorThemeOverlayId
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private const val TAG = "MainActivity"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface ThemeAccessorEntryPoint {
        val settingsRepository: SettingsRepository
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeFromPrefs()
        Log.d(TAG, "onCreate: starts")
        super.onCreate(savedInstanceState)
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

    private fun setThemeFromPrefs() = runBlocking {
        val theme =
            EntryPointAccessors.fromApplication<ThemeAccessorEntryPoint>(this@MainActivity).settingsRepository.getColorTheme()
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