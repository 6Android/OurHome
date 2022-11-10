package com.ssafy.ourhome.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.ourhome.screens.NextScreen
import com.ssafy.ourhome.screens.album.AlbumDetailScreen
import com.ssafy.ourhome.screens.album.AlbumScreen
import com.ssafy.ourhome.screens.chat.ChatScreen
import com.ssafy.ourhome.screens.home.HomeScreen
import com.ssafy.ourhome.screens.home.map.MapScreen
import com.ssafy.ourhome.screens.home.schedule.AddMemberScreen
import com.ssafy.ourhome.screens.home.schedule.AddScheduleScreen
import com.ssafy.ourhome.screens.home.schedule.ScheduleDetailScreen
import com.ssafy.ourhome.screens.login.LoginScreen
import com.ssafy.ourhome.screens.login.LoginViewModel
import com.ssafy.ourhome.screens.login.join.EnterHomeScreen
import com.ssafy.ourhome.screens.login.join.JoinEmailScreen
import com.ssafy.ourhome.screens.login.join.JoinNickNameScreen
import com.ssafy.ourhome.screens.login.join.JoinPasswordScreen
import com.ssafy.ourhome.screens.question.QuestionDetailScreen
import com.ssafy.ourhome.screens.question.QuestionListScreen
import com.ssafy.ourhome.screens.question.QuestionScreen
import com.ssafy.ourhome.screens.question.pet.PetDetailScreen
import com.ssafy.ourhome.screens.userpage.EditProfileScreen
import com.ssafy.ourhome.screens.userpage.MyPageScreen
import com.ssafy.ourhome.screens.userpage.UserPageScreen
import com.ssafy.ourhome.screens.userpage.setting.ManageFamilyScreen
import com.ssafy.ourhome.screens.userpage.setting.SettingScreen


@Composable
fun OurHomeNavGraph(navController: NavHostController) {
    val loginViewModel: LoginViewModel = hiltViewModel()
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
            LoginScreen(navController = navController, loginViewModel)
        }

        composable(OurHomeScreens.JoinEmailScreen.name) {
            JoinEmailScreen(navController = navController, loginViewModel)
        }

        composable(OurHomeScreens.JoinPasswordScreen.name) {
            JoinPasswordScreen(navController = navController, loginViewModel)
        }

        composable(OurHomeScreens.QuestionListScreen.name) {
            QuestionListScreen(navController = navController)
        }

        composable(OurHomeScreens.QuestionDetailScreen.name) {
            QuestionDetailScreen(navController = navController)
        }

        composable(OurHomeScreens.PetDetailScreen.name) {
            PetDetailScreen(navController = navController)
        }

        composable(OurHomeScreens.ChatScreen.name) {
            ChatScreen(navController = navController)
        }

        composable(OurHomeScreens.AlbumScreen.name) {
            AlbumScreen(navController = navController)
        }

        composable(
            "${OurHomeScreens.AlbumDetailScreen.name}/{photoUrl}/{photoDate}",
            arguments = listOf(navArgument(name = "photoUrl") {
                type = NavType.StringType
            }, navArgument(name = "photoDate") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            AlbumDetailScreen(
                navController = navController,
                photoUrl = backStackEntry!!.arguments!!.getString("photoUrl")!!,
                photoDate = backStackEntry!!.arguments!!.getString("photoDate")!!
            )
        }

        composable(
            "${OurHomeScreens.JoinNickNameScreen.name}/{prev_type}",
            arguments = listOf(navArgument("prev_type") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("prev_type").let {
                JoinNickNameScreen(
                    navController = navController,
                    prev_type = it.toString(),
                    vm = loginViewModel
                )
            }
        }

        composable(
            "${OurHomeScreens.UserPageScreen.name}/{email}",
            arguments = listOf(navArgument("email") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("email").let {
                UserPageScreen(navController = navController, email = it.toString())
            }
        }

        composable(OurHomeScreens.EditProfileScreen.name)
        {
            var userDTO =
                navController.previousBackStackEntry?.arguments?.getParcelable<DomainUserDTO>("userDTO")
            EditProfileScreen(navController = navController, userDTO = userDTO!!)
        }

        composable(OurHomeScreens.SettingScreen.name) {
            SettingScreen(navController = navController)
        }

        composable(OurHomeScreens.ManageFamilyScreen.name) {
            ManageFamilyScreen(navController = navController)
        }

        composable(OurHomeScreens.EnterHomeScreen.name) {
            EnterHomeScreen(navController = navController, vm = loginViewModel)
        }

        composable(OurHomeScreens.AddScheduleScreen.name) {
            AddScheduleScreen(navController = navController)
        }

        composable(OurHomeScreens.AddMemberScreen.name) {
            AddMemberScreen(navController = navController)
        }

        composable(OurHomeScreens.ScheduleDetailScreen.name) {
            ScheduleDetailScreen(navController = navController)
        }

        composable(OurHomeScreens.MapScreen.name) {
            MapScreen(navController = navController)
        }
    }
}