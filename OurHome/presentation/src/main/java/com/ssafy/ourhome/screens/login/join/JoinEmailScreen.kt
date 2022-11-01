package com.ssafy.ourhome.screens.login.join

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.RoundedButton
import com.ssafy.ourhome.components.TextInput
import com.ssafy.ourhome.ui.theme.MainColor

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
                TextInput(valueState = idState, labelId = "이메일 아이디", enabled = true)
            }
        }

        /** 다음 버튼 */
        Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.BottomCenter) {
            RoundedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp), label = "다음"
            ) {
                // todo: 다음 버튼 클릭
            }
        }
    }
}

/** 인디케이터들 */
@Composable
fun JoinIndicator(step: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        for (i in 1..3) {
            if (i <= step) Indicator(MainColor)
            else Indicator()
        }
    }
}

/** 인디케이터 */
@Composable
private fun Indicator(color: Color = Color.LightGray) {
    Box(
        Modifier
            .padding(4.dp)
            .background(color = color, shape = CircleShape)
            .size(12.dp)
    ) {
    }
}