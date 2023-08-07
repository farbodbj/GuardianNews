package com.bale_bootcamp.guardiannews.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.datastore.dataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bale_bootcamp.guardiannews.R
import com.bale_bootcamp.guardiannews.data.local.datastore.SettingsDataStore
import com.bale_bootcamp.guardiannews.databinding.AlertDialogThemeChoiceBinding
import com.bale_bootcamp.guardiannews.databinding.FragmentSettingsBinding
import com.bale_bootcamp.guardiannews.ui.MainActivity
import com.bale_bootcamp.guardiannews.ui.settings.model.ColorTheme
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class SettingsFragment : Fragment() {
    private val TAG = "SettingsFragment"
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModel.SettingsViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)
        setUiComponents()
    }

    private fun setUiComponents() {
        setSettingsView()
    }

    private fun setSettingsView() {
        Log.d(TAG, "setUiComponents: started")
        startObservingSettings()
        setSettingsOnClickListeners()
    }

    private fun setSettingsOnClickListeners() {
        Log.d(TAG, "setSettingsOnClickListeners: started")
        binding.themeSetting.root.setOnClickListener {
            Log.d(TAG, "themeSetting clicked")
            val themeChangeAlertDialogBuilder = AlertDialog.Builder(requireContext()).create()
            val themeChangeAlertDialogView = layoutInflater.inflate(R.layout.alert_dialog_theme_choice, null)
            val alertDialogViewBinding = AlertDialogThemeChoiceBinding.bind(themeChangeAlertDialogView)
            themeChangeAlertDialogBuilder.setView(themeChangeAlertDialogView)
            themeChangeAlertDialogBuilder.show()
            alertDialogViewBinding.apply {
                okButton.setOnClickListener {
                    themeSelectionToggleGroup.checkedButtonId.let {
                        Log.d(TAG, "checked button id: $it")
                        val selectedTheme = when(it) {
                            R.id.white_button -> ColorTheme.WHITE
                            R.id.sky_blue_button -> ColorTheme.SKY_BLUE
                            R.id.dark_blue_button -> ColorTheme.DARK_BLUE
                            R.id.green_button -> ColorTheme.GREEN
                            R.id.light_green_button -> ColorTheme.LIGHT_GREEN
                            else -> throw IllegalStateException("unknown button chosen")
                        }
                        Log.d(TAG, "checked button related theme color: ${selectedTheme.value}")
                        viewModel.saveColorTheme(selectedTheme)
                    }
                    Log.d(TAG, "recreating the activity")
                    activity?.recreate()
                    themeChangeAlertDialogBuilder.dismiss()
                }
            }
        }
    }

    private fun startObservingSettings() {
        lifecycleScope.launch {
            Log.d(TAG, "launching observation on all theme items")
            awaitAll(observerItemCountAsync(),
                    observerOrderByAsync(),
                    observerFromDateAsync(),
                    observerColorThemeAsync(),
                    observerTextSizeAsync())
        }
    }

    private suspend fun observerItemCountAsync() = lifecycleScope.async {
        viewModel.itemCount.collect {
        binding.itemCountSetting.settingValue.text = it.toString()
        }
    }

    private suspend fun observerOrderByAsync() = lifecycleScope.async {
        viewModel.orderBy.collect {
        binding.orderBySetting.settingValue.text = it.value
        }
    }

    private suspend fun observerFromDateAsync() = lifecycleScope.async {
        viewModel.fromDate.collect {
        binding.fromDateSetting.settingValue.text = it
        }
    }

    private suspend fun observerColorThemeAsync() = lifecycleScope.async {
        viewModel.colorTheme.collect {
        binding.themeSetting.settingValue.text = it.value
        }
    }

    private suspend fun observerTextSizeAsync() = lifecycleScope.async {
        viewModel.textSize.collect {
        binding.textSizeSetting.settingValue.text = it.value
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}