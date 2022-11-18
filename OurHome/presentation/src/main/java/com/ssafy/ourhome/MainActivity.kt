package com.ssafy.ourhome

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.airbnb.lottie.compose.*
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.ssafy.ourhome.MainActivity.Companion.loadingState
import com.ssafy.ourhome.navigation.BottomNavItem
import com.ssafy.ourhome.navigation.BottomNavigation
import com.ssafy.ourhome.navigation.OurHomeNavGraph
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.ui.theme.MainColor
import com.ssafy.ourhome.ui.theme.OurHomeTheme
import com.ssafy.ourhome.utils.LOCATION
import com.ssafy.ourhome.utils.findActivity
import com.ssafy.ourhome.work.MapWorkManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        var loadingState = mutableStateOf(
            false
        )

        private val locationWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<MapWorkManager>(15, TimeUnit.MINUTES)
                .build()
        lateinit var workManager: WorkManager

        // 위치 정보 갱신 워크매니저 시작
        fun startWorkManager() {
            workManager.enqueueUniquePeriodicWork(
                LOCATION,
                ExistingPeriodicWorkPolicy.KEEP,
                locationWorkRequest
            )
        }

        // 위치 정보 갱신 워크매니저 종료
        fun stopWorkManager() {
            workManager.cancelUniqueWork(LOCATION)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workManager = WorkManager.getInstance(applicationContext)

        setContent {
            OurHomeTheme {
                MyApp()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MyApp() {
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    val navController = rememberAnimatedNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {

        // Subscribe to navBackStackEntry, required to get current route
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        /** 로딩 다이얼로그 */
        if (loadingState.value) {
            LoadingDialog()
        }

        // Control TopBar and BottomBar
        when (navBackStackEntry?.destination?.route) {
            BottomNavItem.Home.screenRoute -> {
                // Show BottomBar and TopBar
                bottomBarState.value = true
                StatusBarColorUpdateEffect(MainColor, false)
            }
            BottomNavItem.Question.screenRoute -> {
                // Show BottomBar and TopBar
                bottomBarState.value = true
                StatusBarColorUpdateEffect(Color.White, true)
            }
            BottomNavItem.Album.screenRoute -> {
                // Show BottomBar and TopBar
                bottomBarState.value = true
                StatusBarColorUpdateEffect(Color.White, true)
            }
            BottomNavItem.MyPage.screenRoute -> {
                // Hide BottomBar and TopBar
                bottomBarState.value = true
                StatusBarColorUpdateEffect(Color.White, true)
            }
            OurHomeScreens.LoginScreen.name -> {
                bottomBarState.value = false
                StatusBarColorUpdateEffect(Color(0xFFF8F8FB), true)
            }
            else -> {
                bottomBarState.value = false
                StatusBarColorUpdateEffect(Color.White, true)
            }
        }

        Scaffold(
            bottomBar = {
                if (bottomBarState.value) {
                    BottomNavigation(navController = navController, bottomBarState = bottomBarState)
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
            ) {
                OurHomeNavGraph(navController)
            }
        }
    }
}

@Composable
fun StatusBarColorUpdateEffect(color: Color, isLight: Boolean = true) {
    val activity = LocalContext.current.findActivity()
    val defaultStatusBarColor = Color.White
    DisposableEffect(LocalLifecycleOwner.current) {
        activity.window.apply {
            statusBarColor = color.toArgb()
            if (isLight && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
        onDispose {
            activity.window.apply {
                statusBarColor = defaultStatusBarColor.toArgb()
                if (isLight) decorView.systemUiVisibility = 0
            }
        }
    }
}

/** 로딩 다이얼로그 */
@Composable
private fun LoadingDialog() {
    Dialog(
        onDismissRequest = { loadingState.value = false },
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Box(
            contentAlignment = Center,
            modifier = Modifier
                .size(200.dp)
                .background(White, shape = CircleShape)
        ) {
            val context = LocalContext.current
            val composition by rememberLottieComposition(LottieCompositionSpec.Asset("loading.json"))
            val logoAnimationState =
                animateLottieCompositionAsState(
                    composition = composition,
                    iterations = LottieConstants.IterateForever
                )
            LottieAnimation(
                modifier = Modifier.align(Center),
                composition = composition,
                progress = { logoAnimationState.progress }
            )
        }
    }
}

fun startLoading() {
    loadingState.value = true
}

fun stopLoading() {
    loadingState.value = false
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OurHomeTheme {
        MyApp()
    }
}
