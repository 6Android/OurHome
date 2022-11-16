package com.ssafy.ourhome.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import com.google.firebase.auth.FirebaseAuth
import com.ssafy.ourhome.navigation.BottomNavItem
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.screens.login.LoginViewModel
import com.ssafy.ourhome.utils.SocialState

@Composable
fun SplashScreen(navController: NavHostController, vm: LoginViewModel = hiltViewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val context = LocalContext.current
        val composition by rememberLottieComposition(LottieCompositionSpec.Asset("loading.json"))
        val logoAnimationState =
            animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever
            )
        LottieAnimation(
            modifier = Modifier.align(Alignment.Center),
            composition = composition,
            progress = { logoAnimationState.progress }
        )

        // 자동 로그인
        LaunchedEffect("") {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                vm.checkSocialEmail(user.email!!)
            } else {
                navController.navigate(OurHomeScreens.LoginScreen.name) {
                    popUpTo(OurHomeScreens.SplashScreen.name) {
                        inclusive = true
                    }
                }
            }
        }

        when (vm.socialProcessState.value) {
            SocialState.MOVE_ENTER_HOME -> {
                navController.navigate(OurHomeScreens.EnterHomeScreen.name) {
                    popUpTo(OurHomeScreens.SplashScreen.name) {
                        inclusive = true
                    }
                }
                vm.socialProcessState.value = SocialState.DEFAULT
            }

            SocialState.MOVE_HOME -> {
                navController.navigate(BottomNavItem.Home.screenRoute) {
                    popUpTo(OurHomeScreens.SplashScreen.name) {
                        inclusive = true
                    }
                }
                vm.socialProcessState.value = SocialState.DEFAULT
            }

            SocialState.MOVE_JOIN_NICKNAME -> {
                navController.navigate(OurHomeScreens.JoinNickNameScreen.name + "/${OurHomeScreens.LoginScreen.name}")
                vm.socialProcessState.value = SocialState.DEFAULT
            }

            SocialState.ERROR -> {
                Toast.makeText(context, "에러가 발생했습니다", Toast.LENGTH_SHORT).show()
                vm.socialProcessState.value = SocialState.DEFAULT
            }
        }
    }
}