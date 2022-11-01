package com.ssafy.ourhome.screens.login.join

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.ourhome.components.EmailInput
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.navigation.OurHomeScreens

@Composable
fun JoinEmailScreen(navController: NavController = NavController(LocalContext.current)) {

    val idState = remember {
        mutableStateOf("")
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxHeight()) {

            /** 툴바 */
            MainAppBar(title = "회원가입", onBackClick = { navController.popBackStack() })

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                /** 인디케이터 */
                JoinIndicator(step = 1)

                /** 헤더 */
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "이메일 아이디를\n입력해주세요!",
                    style = MaterialTheme.typography.h5
                )

                Spacer(modifier = Modifier.height(48.dp))

                /** 아이디 입력창 */
                EmailInput(
                    emailState = idState,
                    labelId = "이메일 아이디",
                    enabled = true,
                    onAction = KeyboardActions(onNext = {
                        // todo: 다음 버튼
                        navigateToPasswordScreen(navController)
                    })
                )
            }
        }
        /** 다음 버튼 */
        NextButton("다음") {
            // todo: 다음 버튼
            navigateToPasswordScreen(navController)
        }
    }
}

fun navigateToPasswordScreen(navController: NavController) {
    navController.navigate(OurHomeScreens.JoinPasswordScreen.name)
}
