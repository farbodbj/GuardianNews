package com.bale_bootcamp.guardiannews.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bale_bootcamp.guardiannews.R
import com.bale_bootcamp.guardiannews.data.local.datastore.SettingsDataStore
import com.bale_bootcamp.guardiannews.data.repository.UserPreferencesRepository
import com.bale_bootcamp.guardiannews.databinding.ActivityMainBinding
import com.bale_bootcamp.guardiannews.ui.settings.model.ColorTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeFromPrefs()
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

    private fun setThemeFromPrefs() {
        Log.d(TAG, "setThemeFromPrefs: started")
        val preferencesRepository = UserPreferencesRepository
            .getInstance(
                SettingsDataStore
                    .SettingsDataStoreFactory(this)
                    .create()
            )
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                preferencesRepository.getColorTheme().collectLatest {
                    val themeId = when(ColorTheme.findByStr(it)) {
                        ColorTheme.WHITE -> R.style.OverlayThemeWhite
                        ColorTheme.SKY_BLUE -> R.style.OverlayThemeSkyBlue
                        ColorTheme.DARK_BLUE -> R.style.OverlayThemeDarkBlue
                        ColorTheme.GREEN -> R.style.OverlayThemeGreen
                        ColorTheme.LIGHT_GREEN -> R.style.OverlayThemeLightGreen
                    }
                    Log.d(TAG, "theme res Id: $themeId for theme name: $it")
                    withContext(Dispatchers.Main){
                        setTheme(themeId)
                    }
                }
            }
        }
    }
}