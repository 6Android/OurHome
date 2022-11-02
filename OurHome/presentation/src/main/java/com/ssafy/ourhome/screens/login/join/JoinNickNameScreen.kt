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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.TextInput
import com.ssafy.ourhome.navigation.BottomNavItem
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.utils.JOIN_PASSWORD

@Composable
fun JoinNickNameScreen(
    navController: NavController = NavController(LocalContext.current),
    prev_type: String
) {
    val nickNameState = remember {
        mutableStateOf("")
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxHeight()) {

            /** 툴바 */
            MainAppBar(title = "회원가입", onBackClick = { navController.popBackStack() })

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                /** 인디케이터 */
                if (prev_type == JOIN_PASSWORD) JoinIndicator(step = 3)

                /** 헤더 */
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "닉네임을 입력해주세요!",
                    style = MaterialTheme.typography.h5
                )

                Spacer(modifier = Modifier.height(48.dp))

                /** 닉네임 입력창 */
                TextInput(
                    valueState = nickNameState,
                    labelId = "닉네임",
                    enabled = true,
                    imeAction = ImeAction.Done,
                    onAction = KeyboardActions(onDone = {
                        // todo: 다음 버튼
                        navigateToHomeScreen(navController)
                    })
                )
            }
        }

        /** 가입 버튼 */
        NextButton("가입") {
            // todo: 가입 버튼
            navigateToHomeScreen(navController)
        }
    }
}

fun navigateToHomeScreen(navController: NavController) {
    navController.navigate(OurHomeScreens.EnterHomeScreen.name)
}