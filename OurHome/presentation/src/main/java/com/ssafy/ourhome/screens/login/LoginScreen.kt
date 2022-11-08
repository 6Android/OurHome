package com.ssafy.ourhome.screens.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.domain.utils.ResultType
import com.ssafy.ourhome.R
import com.ssafy.ourhome.StatusBarColorUpdateEffect
import com.ssafy.ourhome.components.EmailInput
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.components.PasswordInput
import com.ssafy.ourhome.components.RoundedButton
import com.ssafy.ourhome.navigation.BottomNavItem
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.utils.LOGIN

@Preview(showBackground = true)
@Composable
fun LoginScreen(navController: NavController = NavController(LocalContext.current)) {

    val vm: LoginViewModel = hiltViewModel()
    vm.getFamilyUsers()

    when(val usersResponse = vm.usersResponse) {
        is ResultType.Loading -> {}
        is ResultType.Success -> {
            Log.d("test5", "LoginScreen: ${usersResponse.data}")
        }
        is ResultType.Error -> print(usersResponse.exception)
    }


    var idState = remember {
        mutableStateOf("")
    }
    var passwordState = remember {
        mutableStateOf("")
    }
    val passWordVisibility = remember {
        mutableStateOf(false)
    }
    val scrollState = rememberScrollState()

    OurHomeSurface {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /** 우리집 로고, 타이틀 */
            Logo()

            /** 로그인 */
            Login(idState, passwordState, passWordVisibility) {
                navController.navigate(BottomNavItem.Home.screenRoute)
            }

            /** 회원가입 */
            Join {
//                navController.navigate(OurHomeScreens.JoinEmailScreen.name)
                vm.joinEmail()
            }

            /** 소셜 로그인 버튼 */
            SocialLogin {
                // todo: 닉네임 화면으로 이동
                navController.navigate(OurHomeScreens.JoinNickNameScreen.name + "/$LOGIN")
            }
        }
    }
}

/** 소셜 로그인 버튼 */
// todo: 뷰모델 인자로 받기
@Composable
private fun SocialLogin(onDone: () -> Unit) {
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
            onDone()
        }
        SocialLoginButton(
            painterResource =
            painterResource(id = R.drawable.ic_facebook)
        ) {
            // todo: 페이스북 로그인
            onDone()
        }
        SocialLoginButton(
            painterResource =
            painterResource(id = R.drawable.ic_twiter)
        ) {
            // todo: 트위터 로그인
            onDone()
        }
    }
}

@Composable
private fun Join(onClick: () -> Unit) {
    Text(
        modifier = Modifier
            .clickable {
                // todo: 회원가입 클릭
                onClick()
            }
            .padding(8.dp),
        text = "회원가입",
        style = MaterialTheme.typography.body2,
        color = Color.Gray
    )
    Spacer(modifier = Modifier.height(48.dp))
}

@Composable
private fun Login(
    idState: MutableState<String>,
    passwordState: MutableState<String>,
    passWordVisibility: MutableState<Boolean>,
    onClick: () -> Unit
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp),
        text = "로그인",
        style = MaterialTheme.typography.subtitle1
    )
    EmailInput(emailState = idState, labelId = "아이디", enabled = true)
    PasswordInput(
        passwordState = passwordState,
        labelId = "비밀번호",
        enabled = true,
        passwordVisibility = passWordVisibility,
        onAction = KeyboardActions(onDone = { onClick() })
    )
    Spacer(modifier = Modifier.height(16.dp))
    RoundedButton(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(48.dp), label = "로그인"
    ) {
        // todo: 로그인 클릭
        onClick()

    }
    Spacer(modifier = Modifier.height(20.dp))
}

/** 우리집 로고, 타이틀 */
@Composable
private fun Logo() {
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
        style = MaterialTheme.typography.subtitle2
    )
    Spacer(modifier = Modifier.height(32.dp))
}

/** 소셜 로그인 버튼 */
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