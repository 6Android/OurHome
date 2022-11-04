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
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.components.PasswordInput
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.utils.JOIN_PASSWORD


@Composable
fun JoinPasswordScreen(navController: NavController = NavController(LocalContext.current)) {

    val passwordState = remember {
        mutableStateOf("")
    }
    val passwordConfirmState = remember {
        mutableStateOf("")
    }

    OurHomeSurface {
        Column(modifier = Modifier.fillMaxHeight()) {

            /** 툴바 */
            /** 툴바 */
            MainAppBar(title = "회원가입", onBackClick = { navController.popBackStack() })

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                /** 인디케이터 */
                /** 인디케이터 */
                JoinIndicator(step = 2)

                /** 헤더 */

                /** 헤더 */
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "비밀번호를 입력해주세요!",
                    style = MaterialTheme.typography.subtitle1
                )

                Spacer(modifier = Modifier.height(48.dp))

                /** 비밀번호 입력창 */

                /** 비밀번호 입력창 */
                PasswordInput(
                    passwordState = passwordState,
                    labelId = "비밀번호",
                    enabled = true,
                    imeAction = ImeAction.Next
                )

                /** 비밀번호 확인창 */

                /** 비밀번호 확인창 */
                PasswordInput(
                    passwordState = passwordConfirmState,
                    labelId = "비밀번호 확인",
                    enabled = true,
                    imeAction = ImeAction.Next,
                    onAction = KeyboardActions(onNext = {
                        // todo: 다음 버튼
                        navigateToNickNameScreen(navController)
                    })
                )
            }
        }

        /** 다음 버튼 */

        /** 다음 버튼 */
        NextButton(title = "다음") {
            // todo: 다음 버튼
            navigateToNickNameScreen(navController)
        }
    }
}

fun navigateToNickNameScreen(navController: NavController) {
    navController.navigate(OurHomeScreens.JoinNickNameScreen.name + "/$JOIN_PASSWORD")
}