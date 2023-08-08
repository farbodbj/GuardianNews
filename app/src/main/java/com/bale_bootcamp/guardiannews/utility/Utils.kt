package com.bale_bootcamp.guardiannews.utility

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import com.bale_bootcamp.guardiannews.R
import com.bale_bootcamp.guardiannews.ui.settings.model.ColorTheme

object Utils {
    fun buildStringBundle(pairList: List<Pair<String, String>>): Bundle {
        val bundle = Bundle()
        pairList.forEach { bundle.putString(it.first, it.second) }
        return bundle
    }

    fun getColorThemeOverlayId(themeName: String) = when(ColorTheme.findByStr(themeName)) {
        ColorTheme.WHITE -> R.style.OverlayThemeWhite
        ColorTheme.SKY_BLUE -> R.style.OverlayThemeSkyBlue
        ColorTheme.DARK_BLUE -> R.style.OverlayThemeDarkBlue
        ColorTheme.GREEN -> R.style.OverlayThemeGreen
        ColorTheme.LIGHT_GREEN -> R.style.OverlayThemeLightGreen
    }
}