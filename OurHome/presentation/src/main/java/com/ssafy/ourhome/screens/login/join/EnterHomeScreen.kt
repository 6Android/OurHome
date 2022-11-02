package com.ssafy.ourhome.screens.login.join

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.ssafy.ourhome.R
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.components.RoundedButton
import com.ssafy.ourhome.components.TextInput
import com.ssafy.ourhome.navigation.BottomNavItem
import com.ssafy.ourhome.ui.theme.MainColor
import com.ssafy.ourhome.ui.theme.OurHomeTheme

@Composable
fun EnterHomeScreen(navController: NavController) {

    val visibleState = remember {
        mutableStateOf(false)
    }

    OurHomeSurface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            /** 우리 집 생성 카드 */
            EnterHomeCard(
                title = "우리집 생성하기",
                titleColor = Color.Black,
                content = "처음 가입하셨나요?\n우리집을 새로 생성하고\n가족을 초대하세요!",
                contentColor = Color.Gray,
                image = painterResource(id = R.drawable.img_house),
                backgroundColor = Color.White
            ) {
                // todo: 우리 집 생성
                navController.navigate(BottomNavItem.Home.screenRoute)
            }

            /** 입주 생성 카드 */
            EnterHomeCard(
                title = "입주하기",
                titleColor = Color.White,
                content = "이미 우리집이 있나요?\n우리집 코드를 입력하고\n입주해보세요!",
                contentColor = Color.White,
                image = painterResource(id = R.drawable.img_truck),
                backgroundColor = MainColor
            ) {
                // todo: 우리 집 코드 입력 다이얼로그 생성
                visibleState.value = true
            }
        }

        /** 참여코드 다이얼로그 창 */
        if (visibleState.value) {
            EnterDialog(onDismissRequest = { visibleState.value = false }) {
                // todo: 입주하기 버튼 클릭
                navController.navigate(BottomNavItem.Home.screenRoute)
            }
        }
    }
}

/** 참여코드 다이얼로그 창 */
@Composable
fun EnterDialog(
    onDismissRequest: () -> Unit = {},
    onEnterClick: (String) -> Unit = {}
) {

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            /** 참여코드 다이얼로그 내용 */
            DialogContent(onEnterClick, onDismissRequest)
        }
    }
}

/** 참여코드 다이얼로그 내용 */
@Composable
fun DialogContent(onEnterClick: (String) -> Unit, onDismissRequest: () -> Unit) {

    val codeState = remember {
        mutableStateOf("")
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            /** 다이얼로그 헤더 */
            Text(text = "우리집 코드를\n입력해주세요", style = MaterialTheme.typography.h5)

            /** 다이얼로그 닫기 버튼 */
            Icon(
                modifier = Modifier
                    .offset(y = 10.dp)
                    .clickable { onDismissRequest() },
                imageVector = Icons.Default.Close,
                contentDescription = "close"
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        /** 참여코드 입력창 */
        TextInput(valueState = codeState, labelId = "코드", enabled = true)

        Spacer(modifier = Modifier.height(60.dp))

        /** 입주하기 버튼 */
        RoundedButton(modifier = Modifier.fillMaxWidth(), label = "입주하기") {
            onEnterClick(codeState.value)
        }
    }
}

/** 카드 */
@Composable
fun EnterHomeCard(
    title: String,
    titleColor: Color,
    content: String,
    contentColor: Color,
    image: Painter,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        backgroundColor = backgroundColor,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() },
//        elevation = 16.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.h5.copy(color = titleColor))
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.body1.copy(
                    color = contentColor,
                    lineHeight = 24.sp
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    modifier = Modifier.size(104.dp),
                    painter = image,
                    contentDescription = "logo"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEnterHomeScreen() {
    OurHomeTheme {
        EnterHomeScreen(navController = NavController(LocalContext.current))
    }
}