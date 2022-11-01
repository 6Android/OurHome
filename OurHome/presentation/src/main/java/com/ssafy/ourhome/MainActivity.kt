package com.ssafy.ourhome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ssafy.ourhome.navigation.BottomNavItem
import com.ssafy.ourhome.navigation.BottomNavigation
import com.ssafy.ourhome.navigation.OurHomeNavGraph
import com.ssafy.ourhome.ui.theme.OurHomeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OurHomeTheme {
                MyApp()
//                EditTextPreView()
            }
        }
    }
}

@Composable
fun MyApp() {
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

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
            }
            "${BottomNavItem.Question.screenRoute}" -> {
                // Show BottomBar and TopBar
                bottomBarState.value = true
            }
            "${BottomNavItem.Album.screenRoute}" -> {
                // Show BottomBar and TopBar
                bottomBarState.value = true
            }
            "${BottomNavItem.MyPage.screenRoute}" -> {
                // Hide BottomBar and TopBar
                bottomBarState.value = true
            }
            else -> {
                bottomBarState.value = false
            }
        }

        Scaffold(
            bottomBar = {
                if (bottomBarState.value) {
                    BottomNavigation(navController = navController, bottomBarState = bottomBarState)
                }
            }
        ) {
            Box(modifier = Modifier
                .padding(it)) {
                OurHomeNavGraph(navController)

            }
        }
//        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
//            RoundedButton(label = "우리집")
//        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OurHomeTheme {
        MyApp()
    }
}