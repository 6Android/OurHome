package com.ssafy.ourhome.screens.login.join

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.ourhome.components.EmailInput
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.screens.login.LoginViewModel

@Composable
fun JoinEmailScreen(
    navController: NavController = NavController(LocalContext.current),
    vm: LoginViewModel
) {
    val context = LocalContext.current
    val onSuccessListener: () -> Unit = {
        navigateToPasswordScreen(navController)
    }
    val onFailListener: () -> Unit = {
        Toast.makeText(context, "이미 사용중인 아이디 입니다.", Toast.LENGTH_SHORT).show()
    }

    OurHomeSurface {
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
                    style = MaterialTheme.typography.subtitle1
                )

                Spacer(modifier = Modifier.height(48.dp))

                /** 아이디 입력창 */
                EmailInput(
                    emailState = vm.joinIdState,
                    labelId = "이메일 아이디",
                    enabled = true,
                    onAction = KeyboardActions(onNext = {
                        vm.checkEmail(
                            onSuccess = onSuccessListener,
                            onFail = onFailListener
                        )
                    })
                )
            }
        }
        /** 다음 버튼 */
        NextButton("다음") {
            vm.checkEmail(
                onSuccess = onSuccessListener,
                onFail = onFailListener
            )
        }
    }
}

fun navigateToPasswordScreen(navController: NavController) {
    navController.navigate(OurHomeScreens.JoinPasswordScreen.name)
}
