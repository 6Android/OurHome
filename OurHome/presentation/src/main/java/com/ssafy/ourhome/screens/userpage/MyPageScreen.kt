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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.ui.theme.OurHomeTheme
import com.ssafy.ourhome.utils.SETTING_ICON

@Composable
fun MyPageScreen(navController: NavController = NavController(LocalContext.current)) {
    val scrollState = rememberScrollState()
    Scaffold(topBar = {
        MainAppBar(
            title = "마이페이지",
            backIconEnable = false,
            icon = painterResource(id = SETTING_ICON),
            onIconClick = {
                navController.navigate(OurHomeScreens.SettingScreen.name)
            }
        )
    }) {

        // TODO : 데이터 통신
        OurHomeSurface {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.padding(top = 16.dp))
                UserInfoCard(
                    userName = "한상엽",
                    userEmail = "super7615@naver.com",
                    userPhone = "010-1234-5678",
                    isMyPage = true,
                    isManager = true
                )

                Spacer(modifier = Modifier.height(16.dp))
                UserColorCardList()

                Spacer(modifier = Modifier.height(16.dp))
                UserCommonCardList("안드로이드 개발자", "운동", "달리기")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyPagePreview() {
    OurHomeTheme {
        MyPageScreen()
    }
}



