package com.ssafy.ourhome.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.ssafy.ourhome.screens.home.HomeViewModel
import com.ssafy.ourhome.screens.home.moveMap

fun getRandomString(length: Int): String {
    val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..length)
        .map { charset.random() }
        .joinToString("")
}

/** 위치 권한 요청 코드 **/
val permissions = arrayOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION
)


/** 권한 체크, 요청 코드 **/
fun checkAndRequestLocationPermissions(
    context: Context,
    permissions: Array<String>,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    isAlreadyGranted : () -> (Unit)
) {
    /** 권한이 이미 있는 경우 **/
    if (
        permissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    ) {
        isAlreadyGranted()
    }
    /** 권한이 없는 경우 **/
    else {
        launcher.launch(permissions)
    }
}