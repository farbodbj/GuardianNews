package com.bale_bootcamp.guardiannews.ui.settings

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bale_bootcamp.guardiannews.R
import com.bale_bootcamp.guardiannews.databinding.AlertDialogItemCountBinding
import com.bale_bootcamp.guardiannews.databinding.AlertDialogOrderByBinding
import com.bale_bootcamp.guardiannews.databinding.AlertDialogThemeChoiceBinding
import com.bale_bootcamp.guardiannews.databinding.FragmentSettingsBinding
import com.bale_bootcamp.guardiannews.ui.DefaultFragment
import com.bale_bootcamp.guardiannews.ui.settings.model.ColorTheme
import com.bale_bootcamp.guardiannews.ui.settings.model.OrderBy
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private val TAG = "SettingsFragment"
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModel.SettingsViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: started")
        setUiComponents()
    }

    private fun setUiComponents() {
        setBackArrow()
        setSettingsMenu()
    }

    private fun setBackArrow() {
        binding.settingsToolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, DefaultFragment())
                .commit()
        }
    }

    private fun setSettingsMenu() {
        Log.d(TAG, "setUiComponents: started")
        setSettingTitles()
        startObservingSettings()
        setSettingsOnClickListeners()
    }

    private fun setSettingTitles() {
        binding.apply {
            itemCountSetting.settingTitle.text = getString(R.string.number_of_items)
            orderBySetting.settingTitle.text = getString(R.string.order_by)
            fromDateSetting.settingTitle.text = getString(R.string.from_date)
            themeSetting.settingTitle.text = getString(R.string.color_theme)
            textSizeSetting.settingTitle.text = getString(R.string.text_size)
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


    private fun setSettingsOnClickListeners() = binding.apply {
        themeSetting.root
            .setOnClickListener {
                val (alertDialogViewBinding, themeChangeAlertDialog) = showThemeAlertDialog()
                handleThemeSelection(alertDialogViewBinding, themeChangeAlertDialog)
            }

        itemCountSetting.root
            .setOnClickListener {
                val (itemCountAlertDialogBinding, itemCountAlertDialog) = showItemCountAlertDialog()
                handleItemCountDataEntry(itemCountAlertDialogBinding, itemCountAlertDialog)
            }

        orderBySetting.root
            .setOnClickListener {
            val (orderByAlertDialogBinding, orderByAlertDialog) = showOrderByAlertDialog()
            handleOrderBySelection(orderByAlertDialogBinding, orderByAlertDialog)
        }
    }


    private fun showItemCountAlertDialog(): Pair<AlertDialogItemCountBinding, AlertDialog>{
        val itemCountAlertDialog = AlertDialog.Builder(requireContext()).create()
        val itemCountAlertDialogView = layoutInflater.inflate(R.layout.alert_dialog_item_count, null)
        val itemCountAlertDialogBinding = AlertDialogItemCountBinding.bind(itemCountAlertDialogView)
        itemCountAlertDialog.setView(itemCountAlertDialogView)
        itemCountAlertDialog.setCanceledOnTouchOutside(true)
        itemCountAlertDialog.show()

        return Pair(itemCountAlertDialogBinding, itemCountAlertDialog)
    }

    private fun showOrderByAlertDialog(): Pair<AlertDialogOrderByBinding, AlertDialog> {
        val orderByAlertDialog = AlertDialog.Builder(requireContext()).create()
        val orderByAlertDialogView = layoutInflater.inflate(R.layout.alert_dialog_order_by, null)
        val orderByAlertDialogBinding = AlertDialogOrderByBinding.bind(orderByAlertDialogView)
        orderByAlertDialog.setView(orderByAlertDialogView)
        orderByAlertDialog.setCanceledOnTouchOutside(true)
        orderByAlertDialog.show()

        return Pair(orderByAlertDialogBinding, orderByAlertDialog)
    }

    private fun handleOrderBySelection(orderByAlertDialogBinding: AlertDialogOrderByBinding, orderByAlertDialog: AlertDialog) {
        Log.d(TAG, "handleOrderBySelection: started")
        orderByAlertDialogBinding.apply {
            val orderBy = OrderBy.findByStr(viewModel.orderBy.value.value)
            val orderByButtonId = orderByButtonId(orderBy)
            orderByRadioGroup.check(orderByButtonId)

            orderByOkButton.setOnClickListener {
                val selectedOrderBy = buttonIdOrderBy(orderByRadioGroup.checkedRadioButtonId)
                viewModel.saveOrderBy(selectedOrderBy)
                orderByAlertDialog.dismiss()

                orderByAlertDialog.dismiss()
            }
        }
    }


    @IdRes
    private fun orderByButtonId(orderBy: OrderBy): Int = when(orderBy) {
        OrderBy.NEWEST -> R.id.newest_radio_button
        OrderBy.OLDEST -> R.id.oldest_radio_button
        OrderBy.RELEVANCE -> R.id.relevance_radio_button
    }

    private fun buttonIdOrderBy(@IdRes buttonId: Int): OrderBy = when(buttonId) {
        R.id.newest_radio_button -> OrderBy.NEWEST
        R.id.oldest_radio_button -> OrderBy.OLDEST
        R.id.relevance_radio_button -> OrderBy.RELEVANCE
        else -> throw IllegalStateException("no such button id")
    }


    private fun handleItemCountDataEntry(itemCountBinding: AlertDialogItemCountBinding, itemCountAlertDialog: AlertDialog) {
        Log.d(TAG, "handleItemCountDataEntry: started")
        itemCountBinding.apply {
            itemCountOkButton.setOnClickListener {
                val itemCount = itemCountEditText.text.toString().toInt()
                viewModel.saveItemCount(itemCount)
                itemCountAlertDialog.dismiss()
            }
        }
    }


    private fun showThemeAlertDialog(): Pair<AlertDialogThemeChoiceBinding, AlertDialog> {
        val themeChangeAlertDialog = AlertDialog.Builder(requireContext()).create()
        val themeChangeAlertDialogView = layoutInflater.inflate(R.layout.alert_dialog_theme_choice, null)
        val alertDialogViewBinding = AlertDialogThemeChoiceBinding.bind(themeChangeAlertDialogView)
        themeChangeAlertDialog.setView(themeChangeAlertDialogView)
        themeChangeAlertDialog.setCanceledOnTouchOutside(true)
        themeChangeAlertDialog.show()
        return Pair(alertDialogViewBinding, themeChangeAlertDialog)
    }

    private fun colorTheme(choiceButtonId: Int): ColorTheme = when(choiceButtonId) {
            R.id.white_button -> ColorTheme.WHITE
            R.id.sky_blue_button -> ColorTheme.SKY_BLUE
            R.id.dark_blue_button -> ColorTheme.DARK_BLUE
            R.id.green_button -> ColorTheme.GREEN
            R.id.light_green_button -> ColorTheme.LIGHT_GREEN
            else -> throw IllegalStateException("unknown button chosen")
    }

    private fun handleThemeSelection(alertDialogViewBinding: AlertDialogThemeChoiceBinding, themeChangeAlertDialog: AlertDialog) {
        Log.d(TAG, "handleThemeSelection: started")
        alertDialogViewBinding.apply {
            okButton.setOnClickListener {
                themeSelectionToggleGroup.checkedButtonId
                    .let {
                        Log.d(TAG, "checked button id: $it")
                        val selectedTheme = colorTheme(it)
                        Log.d(TAG, "checked button related theme color: ${selectedTheme.value}")
                        viewModel.saveColorTheme(selectedTheme)
                    }
                Log.d(TAG, "recreating the activity")
                themeChangeAlertDialog.dismiss()
                activity?.recreate()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}