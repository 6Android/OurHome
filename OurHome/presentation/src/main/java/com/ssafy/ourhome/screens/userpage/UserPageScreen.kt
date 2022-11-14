package com.ssafy.ourhome.screens.userpage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.navigation.OurHomeScreens

@Composable
fun UserPageScreen(
    navController: NavController,
    email: String,
    vm: UserPageViewModel
) {
    val scrollState = rememberScrollState()

    vm.getProfile(email)

    Scaffold(topBar = {
        MainAppBar(
            title = "유저 정보",
            backIconEnable = true,
            onBackClick = {
                navController.popBackStack()
            }
        )
    }) {

        OurHomeSurface {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.padding(top = 16.dp))

                UserInfoCard(userDTO = vm.user) {
                    navController.navigate(OurHomeScreens.EditProfileScreen.name)
                }

                Spacer(modifier = Modifier.height(16.dp))
                UserColorCardList(userDTO = vm.user)

                Spacer(modifier = Modifier.height(16.dp))
                UserCommonCardList(userDTO = vm.user)
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun UserPagePreview() {
//    OurHomeTheme {
//        UserPageScreen()
//    }
//}