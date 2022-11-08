package com.ssafy.ourhome.screens.login.join

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.components.TextInput
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.screens.login.LoginViewModel
import com.ssafy.ourhome.utils.JOIN_PASSWORD

@Composable
fun JoinNickNameScreen(
    navController: NavController = NavController(LocalContext.current),
    vm: LoginViewModel,
    prev_type: String
) {
    val context = LocalContext.current
    val onSuccessListener: () -> Unit = {
        navigateToEnterHomeScreen(navController)
    }
    val onFailListener: () -> Unit = {
        Toast.makeText(context, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
    }

    OurHomeSurface {
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
                    style = MaterialTheme.typography.subtitle1
                )

                Spacer(modifier = Modifier.height(48.dp))

                /** 닉네임 입력창 */
                TextInput(
                    valueState = vm.joinNickNameState,
                    labelId = "닉네임",
                    enabled = true,
                    imeAction = ImeAction.Done,
                    onAction = KeyboardActions(onDone = {
                        vm.joinEmail(onSuccess = onSuccessListener, onFail = onFailListener)
                    })
                )
            }
        }

        /** 가입 버튼 */
        NextButton("가입") {
            if(vm.joinNickNameState.value.isNullOrBlank()) {
                Toast.makeText(context, "닉네임을 입력해주세요!", Toast.LENGTH_SHORT).show()
                return@NextButton
            }
            vm.joinEmail(onSuccess = onSuccessListener, onFail = onFailListener)
        }
    }
}

fun navigateToEnterHomeScreen(navController: NavController) {
    navController.navigate(OurHomeScreens.EnterHomeScreen.name) {
        popUpTo(OurHomeScreens.LoginScreen.name) {
            inclusive = true
        }
    }
}