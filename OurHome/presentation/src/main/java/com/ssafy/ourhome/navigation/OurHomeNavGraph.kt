package com.ssafy.ourhome.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ssafy.ourhome.screens.*
import com.ssafy.ourhome.screens.album.AlbumScreen
import com.ssafy.ourhome.screens.home.HomeScreen
import com.ssafy.ourhome.screens.login.LoginScreen
import com.ssafy.ourhome.screens.mypage.MyPageScreen
import com.ssafy.ourhome.screens.question.QuestionScreen


@Composable
fun OurHomeNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = OurHomeScreens.LoginScreen.name
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

        composable(OurHomeScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }

    }
}