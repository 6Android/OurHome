package com.ssafy.ourhome.screens.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.ourhome.R
import com.ssafy.ourhome.components.RoundedButton
import com.ssafy.ourhome.components.TextInput

@Preview(showBackground = true)
@Composable
fun LoginScreen() {
    var idState = remember {
        mutableStateOf("")
    }
    var passwordState = remember {
        mutableStateOf("")
    }
    val scrollState = rememberScrollState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            Image(
                modifier = Modifier.size(104.dp),
                painter = painterResource(id = R.drawable.img_house),
                contentDescription = "home logo"
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "가족들과 함께\n추억을 남겨보세요!",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                text = "로그인",
                style = MaterialTheme.typography.h5
            )
            TextInput(valueState = idState, labelId = "아이디", enabled = true)
            TextInput(valueState = passwordState, labelId = "비밀번호", enabled = true)
            Spacer(modifier = Modifier.height(16.dp))
            RoundedButton(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(48.dp), label = "로그인"
            ) {
                // todo: 로그인 클릭
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier
                    .clickable {
                        // todo: 회원가입 클릭
                    }
                    .padding(8.dp),
                text = "회원가입",
                style = MaterialTheme.typography.subtitle2,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(48.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SocialLoginButton(
                    painterResource =
                    painterResource(id = R.drawable.ic_google)
                ) {
                    // todo: 구글 로그인
                }
                SocialLoginButton(
                    painterResource =
                    painterResource(id = R.drawable.ic_facebook)
                ) {
                    // todo: 페이스북 로그인
                }
                SocialLoginButton(
                    painterResource =
                    painterResource(id = R.drawable.ic_twiter)
                ) {
                    // todo: 트위터 로그인
                }
            }

        }
    }
}

@Composable
fun SocialLoginButton(
    modifier: Modifier = Modifier,
    painterResource: Painter,
    onClick: () -> Unit
) {
    Image(
        modifier = modifier
            .size(54.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        painter = painterResource,
        contentDescription = "social login button",
    )
}