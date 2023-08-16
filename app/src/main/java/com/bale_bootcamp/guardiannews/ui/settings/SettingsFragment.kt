package com.bale_bootcamp.guardiannews.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.annotation.IdRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bale_bootcamp.guardiannews.R
import com.bale_bootcamp.guardiannews.databinding.AlertDialogFontSizeChoiceBinding
import com.bale_bootcamp.guardiannews.databinding.AlertDialogItemCountBinding
import com.bale_bootcamp.guardiannews.databinding.AlertDialogOrderByBinding
import com.bale_bootcamp.guardiannews.databinding.AlertDialogThemeChoiceBinding
import com.bale_bootcamp.guardiannews.databinding.FragmentSettingsBinding
import com.bale_bootcamp.guardiannews.ui.settings.model.ColorTheme
import dagger.hilt.android.AndroidEntryPoint
import com.bale_bootcamp.guardiannews.ui.settings.model.OrderBy
import com.bale_bootcamp.guardiannews.ui.settings.model.TextSize
import com.bale_bootcamp.guardiannews.utility.Utils.showAlertDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException


private const val TAG = "SettingsFragment"
@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

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
            val directions = SettingsFragmentDirections.actionSettingsFragmentToDefaultFragment()
            findNavController().navigate(directions)
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
            observerItemCount()
            observerColorTheme()
            observerFromDate()
            observerOrderBy()
            observerTextSize()
        }
    }

    private suspend fun observerItemCount() = lifecycleScope.launch {
        viewModel.itemCount.collect {
            binding.itemCountSetting.settingValue.text = it.toString()
        }
    }

    private suspend fun observerOrderBy() = lifecycleScope.launch {
        viewModel.orderBy.collect {
            binding.orderBySetting.settingValue.text = it.value
        }
    }

    private suspend fun observerFromDate() = lifecycleScope.launch {
        viewModel.fromDate.collect {
            binding.fromDateSetting.settingValue.text = it
        }
    }

    private suspend fun observerColorTheme() = lifecycleScope.launch {
        viewModel.colorTheme.collect {
            binding.themeSetting.settingValue.text = it.value
        }
    }

    private suspend fun observerTextSize() = lifecycleScope.launch {
        viewModel.textSize.collect {
            binding.textSizeSetting.settingValue.text = it.value
        }
    }


    private fun setSettingsOnClickListeners() = binding.apply {
        themeSetting.root
            .setOnClickListener {
                val (itemCountAlertDialogViewBinding, themeChangeAlertDialog) = requireContext()
                    .showAlertDialog(
                        AlertDialogThemeChoiceBinding::class,
                        R.layout.alert_dialog_theme_choice)

                handleThemeSelection(itemCountAlertDialogViewBinding, themeChangeAlertDialog)
            }

        itemCountSetting.root
            .setOnClickListener {
                val (itemCountAlertDialogBinding, itemCountAlertDialog) = requireContext()
                    .showAlertDialog(
                        AlertDialogItemCountBinding::class,
                        R.layout.alert_dialog_item_count)

                handleItemCountDataEntry(itemCountAlertDialogBinding, itemCountAlertDialog)
            }


        orderBySetting.root
            .setOnClickListener {
            val (orderByAlertDialogBinding, orderByAlertDialog) = requireContext()
                .showAlertDialog(
                    AlertDialogOrderByBinding::class,
                    R.layout.alert_dialog_order_by)
            handleOrderBySelection(orderByAlertDialogBinding, orderByAlertDialog)
        }

        textSizeSetting.root
            .setOnClickListener {
                val (fontSizeAlertDialogViewBinding, fontSizeAlertDialog) = requireContext()
                    .showAlertDialog(
                        AlertDialogFontSizeChoiceBinding::class,
                        R.layout.alert_dialog_font_size_choice)

                handleFontSizeSelection(fontSizeAlertDialogViewBinding, fontSizeAlertDialog)
        }
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

    private fun handleFontSizeSelection(alertDialogViewBinding: AlertDialogFontSizeChoiceBinding, fontSizeAlertDialog: AlertDialog) {
        alertDialogViewBinding.apply {
            checkCurrentFontSize(fontSizeRadioGroup)
            fontSizeOkButton.setOnClickListener {
                fontSizeRadioGroup.checkedRadioButtonId.let {
                    Log.d(TAG, "checked button id: ${it}, small:${R.id.font_size_small}, medium: ${R.id.font_size_medium}, large:${R.id.font_size_large}")
                    val selectedFontSize = fontSizeFromButton(it)
                    Log.d(TAG, "selected font size: ${selectedFontSize.value}")
                    viewModel.saveFontSize(selectedFontSize)
                    fontSizeAlertDialog.dismiss()
                }
                requireActivity().recreate()
            }
        }
    }


    private fun checkCurrentFontSize(fontSizeRadioGroup: RadioGroup) {
        lifecycleScope.launch {
            val current = viewModel.textSize.first()
            val buttonId = buttonIdFromFontSize(current)

            withContext(Dispatchers.Main) {
                fontSizeRadioGroup.check(buttonId)
            }
        }
    }

    private fun buttonIdFromFontSize(fontSize: TextSize): Int = when(fontSize) {
        TextSize.SMALL -> R.id.font_size_small
        TextSize.MEDIUM -> R.id.font_size_medium
        TextSize.LARGE -> R.id.font_size_large
    }


    private fun fontSizeFromButton(@IdRes buttonId: Int): TextSize = when(buttonId) {
        R.id.font_size_small -> TextSize.SMALL
        R.id.font_size_medium -> TextSize.MEDIUM
        R.id.font_size_large -> TextSize.LARGE
        else -> throw IllegalStateException("unknown button chosen")
    }


    private fun colorTheme(choiceButtonId: Int): ColorTheme = when(choiceButtonId) {
            R.id.white_button -> ColorTheme.WHITE
            R.id.sky_blue_button -> ColorTheme.SKY_BLUE
            R.id.dark_blue_button -> ColorTheme.DARK_BLUE
            R.id.green_button -> ColorTheme.GREEN
            R.id.light_green_button -> ColorTheme.LIGHT_GREEN
            else -> throw IllegalStateException("unknown button chosen")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}