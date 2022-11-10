package com.ssafy.ourhome.screens.login.join

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.squaredem.composecalendar.ComposeCalendar
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.components.TextInput
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.screens.login.LoginViewModel
import com.ssafy.ourhome.utils.State
import java.time.LocalDate

@Composable
fun JoinNickNameScreen(
    navController: NavController = NavController(LocalContext.current),
    vm: LoginViewModel,
    prev_type: String
) {
    val context = LocalContext.current
    val showDialog = rememberSaveable { mutableStateOf(false) }

    when (vm.joinProcessState.value) {
        State.SUCCESS -> {
            navigateToEnterHomeScreen(navController)
            vm.joinProcessState.value = State.DEFAULT
        }
        State.FAIL -> {
            Toast.makeText(context, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
            vm.joinProcessState.value = State.DEFAULT
        }
    }

    OurHomeSurface {
        Column(modifier = Modifier.fillMaxHeight()) {

            /** 툴바 */
            MainAppBar(title = "회원가입", onBackClick = { navController.popBackStack() })

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                /** 인디케이터 */
                if (prev_type == OurHomeScreens.JoinPasswordScreen.name) JoinIndicator(step = 3)

                /** 헤더 */
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "닉네임과 생일을\n입력해주세요!",
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
                        joinUser(vm, context, prev_type)
                    })
                )

                Spacer(modifier = Modifier.height(24.dp))

                /**  생일 */
                SelectBirth(vm.joinDateState) {
                    showDialog.value = true
                }
            }
        }

        /** 가입 버튼 */
        NextButton("가입") {
            joinUser(vm, context, prev_type)
        }

        /** 날짜 선택 다이얼로그 */
        if (showDialog.value) {
            ComposeCalendar(
                onDone = { it: LocalDate ->
                    // Hide dialog
                    vm.joinDateState.value = it
                    showDialog.value = false
                    // Do something with the date
                },
                onDismiss = {
                    // Hide dialog
                    showDialog.value = false
                }
            )
        }
    }
}

fun joinUser(vm: LoginViewModel, context: Context, prev_type: String) {
    if (vm.joinNickNameState.value.isBlank()) {
        Toast.makeText(context, "닉네임을 입력해주세요!", Toast.LENGTH_SHORT).show()
    }

    when (prev_type) {
        OurHomeScreens.JoinPasswordScreen.name -> {
            vm.joinEmail()
        }
        OurHomeScreens.LoginScreen.name -> {
            vm.joinSocial()
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