package com.ssafy.ourhome.di

import android.app.Application
import android.widget.Toast
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.ssafy.ourhome.utils.OUR_HOME
import com.ssafy.ourhome.utils.Prefs
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import com.ssafy.ourhome.R

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

        // 카카오 SDK 초기화
        KakaoSdk.init(this, getString(R.string.KAKAO_NATIVE_APP_KEY))
        //var keyHash = Utility.getKeyHash(this)
        //Toast.makeText(this, "$keyHash", Toast.LENGTH_SHORT).show()
    }
}
