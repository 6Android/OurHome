package com.ssafy.ourhome.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.ssafy.ourhome.screens.NextScreen
import com.ssafy.ourhome.screens.album.AlbumScreen
import com.ssafy.ourhome.screens.chat.ChatScreen
import com.ssafy.ourhome.screens.home.HomeScreen
import com.ssafy.ourhome.screens.userpage.MyPageScreen
import com.ssafy.ourhome.screens.login.LoginScreen
import com.ssafy.ourhome.screens.login.join.EnterHomeScreen
import com.ssafy.ourhome.screens.login.join.JoinEmailScreen
import com.ssafy.ourhome.screens.login.join.JoinNickNameScreen
import com.ssafy.ourhome.screens.login.join.JoinPasswordScreen
import com.ssafy.ourhome.screens.question.QuestionDetailScreen
import com.ssafy.ourhome.screens.question.QuestionListScreen
import com.ssafy.ourhome.screens.question.QuestionScreen
import com.ssafy.ourhome.screens.question.pet.PetDetailScreen
import com.ssafy.ourhome.screens.userpage.setting.ManageFamilyScreen
import com.ssafy.ourhome.screens.userpage.setting.SettingScreen


@Composable
fun OurHomeNavGraph(navController: NavHostController) {
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

        composable(OurHomeScreens.JoinEmailScreen.name) {
            JoinEmailScreen(navController = navController)
        }

        composable(OurHomeScreens.JoinPasswordScreen.name) {
            JoinPasswordScreen(navController = navController)
        }

        composable(OurHomeScreens.QuestionListScreen.name){
            QuestionListScreen(navController = navController)
        }

        composable(OurHomeScreens.QuestionDetailScreen.name){
            QuestionDetailScreen(navController = navController)
        }

        composable(OurHomeScreens.PetDetailScreen.name){
            PetDetailScreen(navController = navController)
        }

        composable(OurHomeScreens.ChatScreen.name){
            ChatScreen(navController = navController)
        }

        composable(
            "${OurHomeScreens.JoinNickNameScreen.name}/{prev_type}",
            arguments = listOf(navArgument("prev_type") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("prev_type").let {

                JoinNickNameScreen(navController = navController, prev_type = it.toString())
            }
        }

        composable(OurHomeScreens.SettingScreen.name){
            SettingScreen(navController = navController)
        }

        composable(OurHomeScreens.ManageFamilyScreen.name){
            ManageFamilyScreen(navController = navController)
        }

        composable(OurHomeScreens.EnterHomeScreen.name) {
            EnterHomeScreen(navController = navController)
        }
    }
}