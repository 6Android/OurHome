package com.ssafy.ourhome.di

import android.app.Application
import com.ssafy.ourhome.utils.OUR_HOME
import com.ssafy.ourhome.utils.Prefs
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationClass : Application() {
    override fun onCreate() {
        Prefs.prefs = this.getSharedPreferences(OUR_HOME, MODE_PRIVATE)
        super.onCreate()
    }
}