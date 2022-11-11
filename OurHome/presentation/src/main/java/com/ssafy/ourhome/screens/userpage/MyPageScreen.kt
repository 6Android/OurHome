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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.utils.Prefs
import com.ssafy.ourhome.utils.SETTING_ICON


@Composable
fun MyPageScreen(
    navController: NavController = NavController(LocalContext.current),
    vm: UserPageViewModel
) {
    val scrollState = rememberScrollState()

    vm.getProfile(Prefs.email)

    Scaffold(topBar = {
        MainAppBar(
            title = "마이페이지",
            backIconEnable = false,
            icon = painterResource(id = SETTING_ICON),
            onIconClick = {
//                navController.navigate(OurHomeScreens.UserPageScreen.name + "/b@naver.com")
                navController.navigate(OurHomeScreens.SettingScreen.name+"/${vm.user.location_permit}")
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

                UserInfoCard(userDTO = vm.user, navController)

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
//private fun MyPagePreview() {
//    OurHomeTheme {
//        MyPageScreen()
//    }
//}
//


