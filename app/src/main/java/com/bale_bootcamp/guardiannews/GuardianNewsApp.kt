package com.bale_bootcamp.guardiannews

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GuardianNewsApp: Application() {
    val name = "GuardianNewsApp"
}