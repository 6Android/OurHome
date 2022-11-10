package com.ssafy.ourhome.work

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ssafy.domain.usecase.user.SendLatLngUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import kotlin.random.Random

@HiltWorker
class MapWorkManager @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val sendLatLngUseCase: SendLatLngUseCase
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {

        Log.d("test5", "doWork: Start!")

        // 1. 위치 받기 OOOOOOOO
        // 2. 주기별로 보내기
        // 3. 위치 공유 조건에 따라 처리

//        val location = getLatLng(appContext)

//        sendLatLngUseCase.execute(
//            "EX7342",
//            "a@naver.com",
//            location.latitude,
//            location.longitude
//        ).collect {
//            Log.d("test5", "doWork: $it")
//        }

        sendLatLngUseCase.execute(
            "EX7342",
            "a@naver.com",
            Random.nextDouble(100.0),
            Random.nextDouble(100.0),
            System.currentTimeMillis()
        ).collect {
            Log.d("test5", "doWork: $it")
        }
        Log.d("test5", "doWork: End")
        Result.success()
    }

    private fun getLatLng(appContext: Context): Location {
        val locationManager = appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val PERMISSIONS_REQUEST_CODE = 100

        var currentLatLng: Location? = null
        var hasFineLocationPermission = ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        var hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED
        ) {
            val locationProvider = LocationManager.GPS_PROVIDER
            currentLatLng = locationManager?.getLastKnownLocation(locationProvider)
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    appContext as Activity,
                    REQUIRED_PERMISSIONS[0]
                )
            ) {
//                Toast.makeText(this, "앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(
                    appContext,
                    REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    appContext,
                    REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
            currentLatLng = getLatLng(appContext)
        }
        return currentLatLng!!
    }
}