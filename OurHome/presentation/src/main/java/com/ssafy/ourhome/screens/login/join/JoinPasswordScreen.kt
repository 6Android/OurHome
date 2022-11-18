package com.ssafy.ourhome.screens.login.join

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.components.PasswordInput
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.screens.login.LoginViewModel


@SuppressLint("UnrememberedMutableState")
@Composable
fun JoinPasswordScreen(
    navController: NavController = NavController(LocalContext.current),
    vm: LoginViewModel
) {
    val joinPasswordSameState =
        mutableStateOf(
            vm.joinPasswordState.value == vm.joinPasswordConfirmState.value
                    && vm.joinPasswordState.value.isNotEmpty()
                    && vm.joinPasswordConfirmState.value.isNotEmpty()
        )

    OurHomeSurface {
        Column(modifier = Modifier.fillMaxHeight()) {

            /** 툴바 */
            MainAppBar(title = "회원가입", onBackClick = { navController.popBackStack() })

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                /** 인디케이터 */
                JoinIndicator(step = 2)

                /** 헤더 */
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "비밀번호를 입력해주세요!",
                    style = MaterialTheme.typography.subtitle1
                )

                Spacer(modifier = Modifier.height(48.dp))

                /** 비밀번호 입력창 */
                PasswordInput(
                    passwordState = vm.joinPasswordState,
                    labelId = "비밀번호",
                    enabled = true,
                    imeAction = ImeAction.Next
                )

                /** 비밀번호 확인창 */
                PasswordInput(
                    passwordState = vm.joinPasswordConfirmState,
                    labelId = "비밀번호 확인",
                    enabled = true,
                    imeAction = ImeAction.Next,
                    onAction = KeyboardActions(onNext = {
                        if (vm.joinPasswordState.value.length >= 6 &&joinPasswordSameState.value) {
                            navigateToNickNameScreen(navController)
                        }
                    })
                )

                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 10.dp),
                    style = MaterialTheme.typography.caption.copy(color = Color.Red),
                    text = if (vm.joinPasswordState.value.length < 6) {
                        "비밀번호는 6자리 이상이어야 합나디."
                    } else if (!joinPasswordSameState.value) {
                        "비밀번호와 비밀번호 확인이 일치하지 않습니다."
                    } else {
                        ""
                    }
                )
            }
        }

        if (vm.joinPasswordState.value.length >= 6 &&joinPasswordSameState.value) {
            /** 다음 버튼 */
            NextButton(title = "다음") {
                navigateToNickNameScreen(navController)
            }
        }
    }
}

fun navigateToNickNameScreen(navController: NavController) {
    navController.navigate(OurHomeScreens.JoinNickNameScreen.name + "/${OurHomeScreens.JoinPasswordScreen.name}")
}