package com.ssafy.ourhome.screens.userpage

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.utils.ResultType
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.ui.theme.OurHomeTheme
import com.ssafy.ourhome.utils.SETTING_ICON


@Composable
fun MyPageScreen(navController: NavController = NavController(LocalContext.current), vm : UserPageViewModel) {
    val scrollState = rememberScrollState()


    // TODO : 패밀리코드, 본인 이메일
    vm.getProfile("EX7342","a@naver.com")

    val user = remember {
        mutableStateOf(DomainUserDTO())
    }

    when(val userResponse = vm.userResponse) {
        is ResultType.Uninitialized -> {}
        is ResultType.Success -> {
            user.value = userResponse.data
            Log.d("test5", "MyPageScreen: ${userResponse.data}")
        }
        is ResultType.Error -> print(userResponse.exception)
    }

    Scaffold(topBar = {
        MainAppBar(
            title = "마이페이지",
            backIconEnable = false,
            icon = painterResource(id = SETTING_ICON),
            onIconClick = {
                navController.navigate(OurHomeScreens.UserPageScreen.name + "/b@naver.com")
//                navController.navigate(OurHomeScreens.SettingScreen.name)
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

                UserInfoCard(userDTO = user.value, navController)

                Spacer(modifier = Modifier.height(16.dp))
                UserColorCardList(userDTO = user.value)

                Spacer(modifier = Modifier.height(16.dp))
                UserCommonCardList(userDTO = user.value)
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


