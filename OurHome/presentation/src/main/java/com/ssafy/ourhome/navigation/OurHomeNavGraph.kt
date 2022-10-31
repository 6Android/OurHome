package com.ssafy.ourhome.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ssafy.ourhome.screens.*


@Composable
fun OurHomeNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.screenRoute
    ) {
        composable(BottomNavItem.Home.screenRoute) {
            HomeScreen(navController = navController)
        }
        composable(BottomNavItem.Question.screenRoute) {
            QuestionScreen(navController = navController)
        }
        composable(BottomNavItem.Album.screenRoute) {
            AlbumScreen(navController = navController)
        }
        composable(BottomNavItem.MyPage.screenRoute) {
            MyPageScreen(navController = navController)
        }

        composable(OurHomeScreens.NextScreen.name) {
            NextScreen(navController = navController)
        }


    }
}