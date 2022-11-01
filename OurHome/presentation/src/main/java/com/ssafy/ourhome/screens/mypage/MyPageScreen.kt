package com.ssafy.ourhome.screens.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ssafy.ourhome.R
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.ui.theme.BirthDayColor
import com.ssafy.ourhome.ui.theme.MBTIColor
import com.ssafy.ourhome.ui.theme.OurHomeTheme
import com.ssafy.ourhome.utils.SETTING_ICON

@Composable
fun MyPageScreen(navController: NavController) {
    Text(text = "MyPageScreen")
}

@Preview(showBackground = true)
@Composable
fun MyPagePreview() {
    OurHomeTheme {
        Column() {
            MainAppBar(
                icon = painterResource(id = SETTING_ICON)
            )
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.padding(top = 16.dp))
                UserInfoCard(
                    userName = "한상엽",
                    userEmail = "super7615@naver.com",
                    userPhone = "010-1234-5678",
                    isMyPage = true,
                    isManager = true
                )

                Spacer(modifier = Modifier.height(16.dp))
                UserColorCardList()

                Spacer(modifier = Modifier.height(16.dp))
                UserCommonCardList("안드로이드 개발자", "운동", "달리기")
            }
        }
    }
}

@Composable
private fun UserColorCardList() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BirthDayCard("1111")
        BirthDayCard("1111")
        MBTICard("ENFP")
    }
}

@Composable
private fun UserCommonCard(
    title: String,
    content: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(8.dp)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 12.dp)
        ) {
            Text(
                text = title, modifier = Modifier.align(Alignment.TopStart),
                style = MaterialTheme.typography.body1
            )
            Text(
                text = content, modifier = Modifier.align(Alignment.BottomEnd),
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Composable
private fun UserCommonCardList(
    jobContent: String,
    likeContent: String,
    hobbyContent: String
) {
    Column(
    ) {
        UserCommonCard(title = "직업", content = jobContent)
        Spacer(modifier = Modifier.height(16.dp))
        UserCommonCard(title = "관심사", content = likeContent)
        Spacer(modifier = Modifier.height(16.dp))
        UserCommonCard(title = "취미", content = hobbyContent)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun BirthDayCard(
    content: String,
) {
    Card(
        modifier = Modifier
            .size(110.dp)
            .clip(RoundedCornerShape(8.dp)),
        backgroundColor = BirthDayColor
    ) {
        Box() {
            
        }
    }
}

@Composable
private fun MBTICard(
    content: String,
) {
    Card(
        modifier = Modifier
            .size(110.dp)
            .clip(RoundedCornerShape(8.dp)),
        backgroundColor = MBTIColor
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(top = 10.dp, bottom = 4.dp, start = 10.dp, end = 10.dp)
        ) {
            Text(text = "MBTI",
                modifier = Modifier.align(Alignment.TopStart),
            style = MaterialTheme.typography.body1.copy(color = Color.White, fontWeight = FontWeight.Bold))
            Text(text = content,
                modifier = Modifier.align(Alignment.BottomEnd),
                style = MaterialTheme.typography.h5.copy(color = Color.White))
        }
    }
}

@Composable
private fun UserInfoCard(
    imageUrl: String = "default",
    userName: String,
    userEmail: String,
    userPhone: String,
    isMyPage: Boolean,
    isManager: Boolean = false,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(100.dp)
            ) {
                Image(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    painter =
                    if (imageUrl == "default") painterResource(R.drawable.img_default_user)
                    else rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Profile Image"
                )

                if (isManager) {
                    Image(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .align(Alignment.BottomEnd),
                        painter = painterResource(R.drawable.img_manager),
                        contentDescription = "Manager Image",
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = userName,
                style = MaterialTheme.typography.body1
                    .copy(fontSize = 22.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = userEmail, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                // TODO : 전화걸기 기능 추가
                modifier = Modifier.clickable {

                }
            ) {
                Image(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 4.dp),
                    painter = painterResource(R.drawable.img_phone),
                    contentDescription = "Phone Image"
                )
                Text(text = userPhone, style = MaterialTheme.typography.body1)
            }

            Spacer(modifier = Modifier.height(12.dp))


            if (isMyPage) {
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    // TODO : 프로필 편집 기능 추가
                    onClick = { },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, Color.LightGray)
                ) {
                    Text(
                        text = "프로필 편집",
                        style = MaterialTheme.typography.button.copy(color = Color.Black)
                    )
                }
            }
        }
    }
}
