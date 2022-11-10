package com.ssafy.ourhome.screens.userpage.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.ui.theme.MainColor
import com.ssafy.ourhome.ui.theme.OurHomeTheme
import com.ssafy.ourhome.utils.Prefs

@Composable
fun SettingScreen(navController: NavController = NavController(LocalContext.current)) {
    // TODO : 스위치 상태 통신
    val switchChecked = remember {
        mutableStateOf(false)
    }
    val scrollState = rememberScrollState()

    Scaffold(topBar = {
        MainAppBar(title = "설정", backIconEnable = true, onBackClick = {
            navController.popBackStack()
        })
    }) {
        // TODO : 옵션 로직 작성
        OurHomeSurface() {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                TextWithSwitch(title = "위치 공유 허용", isChecked = switchChecked)

                Spacer(modifier = Modifier.height(42.dp))
                OurHomeSetting(code = Prefs.familyCode,navController)

                Spacer(modifier = Modifier.height(42.dp))
                Support()

                Spacer(modifier = Modifier.height(56.dp))
                ClickableText("로그아웃"){

                }

                Spacer(modifier = Modifier.height(32.dp))
                ClickableText("회원탈퇴"){

                }
            }
        }
    }
}

@Composable
private fun OurHomeSetting(
    code: String,
    navController: NavController
){
    TextHeader(title = "가족 설정")
    Spacer(modifier = Modifier.height(26.dp))

    // TODO : 클릭 이벤트
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp)) {
        TextWithCode(title = "우리집 코드", code = code) {

        }
        Spacer(modifier = Modifier.height(32.dp))
        TextWithNext(title = "가족 관리") {
            navController.navigate(OurHomeScreens.ManageFamilyScreen.name)
        }
        Spacer(modifier = Modifier.height(32.dp))
        TextWithNext(title = "가족 끊기") {

        }
    }
}

@Composable
private fun ClickableText(
    title: String,
    onClick: () -> Unit
){
    Text(text = title,
        modifier = Modifier.clickable {
            onClick.invoke()
        },
        style = MaterialTheme.typography.body1.copy(
        fontSize = 18.sp, fontWeight = FontWeight.ExtraBold
    ))
}

@Composable
private fun Support(){
    TextHeader(title = "고객 지원")
    Spacer(modifier = Modifier.height(26.dp))

    // TODO : 클릭 이벤트
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp)) {
        TextWithNext(title = "이용 약관") {

        }
        Spacer(modifier = Modifier.height(32.dp))
        TextWithNext(title = "문의 보내기") {

        }
    }
}

@Composable
private fun TextWithSwitch(
    title: String,
    isChecked: MutableState<Boolean>,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title, style = MaterialTheme.typography.body1.copy(
                fontSize = 18.sp, fontWeight = FontWeight.ExtraBold
            )
        )

        Switch(
            checked = isChecked.value,
            onCheckedChange = { isChecked.value = it },
            modifier = Modifier
                .padding(all = 0.dp)
                .width(48.dp),
            colors = SwitchDefaults.colors(
                checkedTrackColor = MainColor,
                checkedTrackAlpha = 1.0f,
                uncheckedTrackColor = Color.Gray,
                checkedThumbColor = Color.White
            )
        )
    }
}

@Composable
private fun TextWithNext(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick.invoke()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title, style = MaterialTheme.typography.body1.copy(
                fontSize = 18.sp, fontWeight = FontWeight.Normal
            )
        )
        Icon(imageVector = Icons.Default.NavigateNext, contentDescription = "Next Button")
    }
}

@Composable
private fun TextHeader(
    title: String
){
       Text(text = title, style = MaterialTheme.typography.body1.copy(
           fontSize = 18.sp, fontWeight = FontWeight.ExtraBold
       ))
}

@Composable
private fun TextWithCode(
    title: String,
    code: String,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick.invoke()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title, style = MaterialTheme.typography.body1.copy(
                fontSize = 18.sp, fontWeight = FontWeight.Normal
            )
        )
        Text(
            text = code, style = MaterialTheme.typography.body1.copy(
                fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF707070)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingPreview() {
    OurHomeTheme {
        SettingScreen()
    }
}
