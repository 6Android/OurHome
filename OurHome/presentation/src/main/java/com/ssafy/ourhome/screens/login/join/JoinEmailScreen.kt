package com.ssafy.ourhome.screens.login.join

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.ssafy.ourhome.components.RoundedButton
import com.ssafy.ourhome.components.TextInput

@Composable
fun JoinEmailScreen(navController: NavController = NavController(LocalContext.current)) {

    val idState = remember {
        mutableStateOf("")
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Text(text = "이메일 아이디를\n입력해주세요!")
            TextInput(valueState = idState, labelId = "이메일 아이디", enabled = true)
            RoundedButton(label = "다음")
        }
    }
}