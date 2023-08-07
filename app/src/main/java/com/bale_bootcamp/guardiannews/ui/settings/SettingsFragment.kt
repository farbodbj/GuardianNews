package com.bale_bootcamp.guardiannews.ui.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bale_bootcamp.guardiannews.databinding.FragmentSettingsBinding
import com.bale_bootcamp.guardiannews.ui.settings.model.Setting
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private val TAG = "SettingsFragment"
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModel.SettingsViewModelFactory()
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
        setSettingsRecyclerView()
    }

    private fun setSettingsRecyclerView() {
        Log.d(TAG, "setUiComponents: started")
        val adapter = createRecyclerViewAdapter()
        binding.settingsList.adapter = adapter

        val settings: MutableList<Setting> = mutableListOf(
            Setting("Item Count", "10"),
            Setting("Order By", "Newest"),
            Setting("From Date", "2021-01-01"),
            Setting("Color Theme", "Light"),
            Setting("Text Size", "Medium"))

        adapter.submitList(settings)
        startObserving(settings)
    }


    private fun createRecyclerViewAdapter(): SettingsAdapter =
        SettingsAdapter { setting -> viewModel.onSettingClicked(setting) }


    private fun startObserving(currentSettings: MutableList<Setting>) {
        lifecycleScope.launch {
            viewModel.apply {
                val settingsFlows = listOf(itemCount, orderBy, fromDate, colorTheme, textSize)
                settingsFlows.forEachIndexed { index, flow ->
                    flow.collect { setting ->
                        val current = currentSettings[index]
                        currentSettings[index] = Setting(current.title, setting.toString())
                        binding.settingsList.adapter?.notifyItemChanged(index)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}