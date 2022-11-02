package com.ssafy.ourhome.screens.login.join

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ssafy.ourhome.R
import com.ssafy.ourhome.navigation.BottomNavItem
import com.ssafy.ourhome.ui.theme.MainColor
import com.ssafy.ourhome.ui.theme.OurHomeTheme

@Composable
fun EnterHomeScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.LightGray
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
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
            }
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