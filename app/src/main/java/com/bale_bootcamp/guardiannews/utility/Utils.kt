package com.bale_bootcamp.guardiannews.utility

import android.os.Bundle
import androidx.datastore.preferences.core.Preferences

object Utils {
    fun buildStringBundle(pairList: List<Pair<String, String>>): Bundle {
        val bundle = Bundle()
        pairList.forEach { bundle.putString(it.first, it.second) }
        return bundle
    }
}