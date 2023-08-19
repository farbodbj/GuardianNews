package com.bale_bootcamp.guardiannews.utility

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.viewbinding.ViewBinding
import com.bale_bootcamp.guardiannews.R
import com.bale_bootcamp.guardiannews.ui.settings.model.ColorTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass

object Utils {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun getColorThemeOverlayId(themeName: String) = when(ColorTheme.findByStr(themeName)) {
        ColorTheme.WHITE -> R.style.OverlayThemeWhite
        ColorTheme.SKY_BLUE -> R.style.OverlayThemeSkyBlue
        ColorTheme.DARK_BLUE -> R.style.OverlayThemeDarkBlue
        ColorTheme.GREEN -> R.style.OverlayThemeGreen
        ColorTheme.LIGHT_GREEN -> R.style.OverlayThemeLightGreen
    }

    fun <T: ViewBinding> Context.showAlertDialog(AlertDialogBinding: KClass<T>,
                                                 @LayoutRes layoutId: Int,
                                                 cancelOnTouchOutside: Boolean = true
    ): Pair<T, AlertDialog> {
        val alertDialog = AlertDialog.Builder(this).create()
        val alertDialogView = LayoutInflater.from(this).inflate(layoutId, null)
        val alertDialogBinding = AlertDialogBinding.java.getMethod("bind", (View::class.java)).invoke(null, alertDialogView) as T
        alertDialog.setView(alertDialogView)
        alertDialog.setCanceledOnTouchOutside(cancelOnTouchOutside)
        alertDialog.show()

        return Pair(alertDialogBinding, alertDialog)
    }

    fun Context.showDatePickerDialog(onDateSetListener: OnDateSetListener,
                                     cancelOnTouchOutside: Boolean
    ) {
        val today = LocalDate.now()
        val datePickerDialog = DatePickerDialog(this,
            onDateSetListener,
            today.year, today.monthValue, today.dayOfMonth)

        datePickerDialog.apply {
            setCanceledOnTouchOutside(cancelOnTouchOutside)
            show()
        }
    }
}