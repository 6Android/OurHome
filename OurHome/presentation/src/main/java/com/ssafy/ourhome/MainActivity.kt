package com.ssafy.ourhome

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ssafy.ourhome.navigation.BottomNavItem
import com.ssafy.ourhome.navigation.BottomNavigation
import com.ssafy.ourhome.navigation.OurHomeNavGraph
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.ui.theme.MainColor
import com.ssafy.ourhome.ui.theme.OurHomeTheme
import com.ssafy.ourhome.utils.findActivity
import com.ssafy.ourhome.work.MapWorkManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locationWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<MapWorkManager>(15, TimeUnit.MINUTES)
                .build()

        val workManager = WorkManager.getInstance(applicationContext)

//        workManager.cancelUniqueWork("location")

        var a = workManager.getWorkInfosForUniqueWork("location").get()
        for(i in a){
            Log.d("test5", "onCreate: $i")
        }

        workManager.enqueueUniquePeriodicWork(
            "location",
            ExistingPeriodicWorkPolicy.KEEP,
            locationWorkRequest
        )

        a = workManager.getWorkInfosForUniqueWork("location").get()
        for(i in a){
            Log.d("test5", "onCreate: $i")
        }


        setContent {
            OurHomeTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    val auth = Firebase.auth.currentUser
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {

        val navController = rememberNavController()

        // Subscribe to navBackStackEntry, required to get current route
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        // Control TopBar and BottomBar
        when (navBackStackEntry?.destination?.route) {
            "${BottomNavItem.Home.screenRoute}" -> {
                // Show BottomBar and TopBar
                bottomBarState.value = true
                StatusBarColorUpdateEffect(MainColor, false)
            }
            "${BottomNavItem.Question.screenRoute}" -> {
                // Show BottomBar and TopBar
                bottomBarState.value = true
                StatusBarColorUpdateEffect(Color.White, true)
            }
            "${BottomNavItem.Album.screenRoute}" -> {
                // Show BottomBar and TopBar
                bottomBarState.value = true
                StatusBarColorUpdateEffect(Color.White, true)
            }
            "${BottomNavItem.MyPage.screenRoute}" -> {
                // Hide BottomBar and TopBar
                bottomBarState.value = true
                StatusBarColorUpdateEffect(Color.White, true)
            }
            "${OurHomeScreens.LoginScreen.name}" -> {
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
//        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
//            RoundedButton(label = "우리집")
//        }
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OurHomeTheme {
        MyApp()
    }
}
