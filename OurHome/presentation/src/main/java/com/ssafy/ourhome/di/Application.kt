package com.ssafy.ourhome.di

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.ssafy.ourhome.utils.OUR_HOME
import com.ssafy.ourhome.utils.Prefs
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ApplicationClass : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        Prefs.prefs = this.getSharedPreferences(OUR_HOME, MODE_PRIVATE)
        super.onCreate()
    }
}
