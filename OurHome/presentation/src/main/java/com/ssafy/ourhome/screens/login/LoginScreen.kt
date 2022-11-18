package com.ssafy.ourhome.screens.login

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ssafy.ourhome.R
import com.ssafy.ourhome.components.EmailInput
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.components.PasswordInput
import com.ssafy.ourhome.components.RoundedButton
import com.ssafy.ourhome.navigation.BottomNavItem
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.startLoading
import com.ssafy.ourhome.stopLoading
import com.ssafy.ourhome.utils.SocialState
import com.ssafy.ourhome.utils.State
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Preview(showBackground = true)
@Composable
fun LoginScreen(
    navController: NavController = NavController(LocalContext.current),
    vm: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val token = stringResource(R.string.default_web_client_id)
    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result ->
            vm.checkSocialEmail(result.user?.email!!)
        },
        onAuthError = {
        }
    )

    val passWordVisibility = remember {
        mutableStateOf(false)
    }
    val scrollState = rememberScrollState()

    when (vm.loginProcessState.value) {
        State.SUCCESS -> {
            when (vm.hasFamilyState.value) {
                // 이미 가족방이 있을 때
                true -> {
                    navigateToHomeScreen(navController)
                }
                // 가족방이 없을 때
                else -> {
                    navigateToEnterHomeScreen(navController)
                }
            }
            vm.loginProcessState.value = State.COMPLETED
        }
        State.FAIL -> {
            Toast.makeText(context, "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
            vm.loginProcessState.value = State.COMPLETED
        }
        State.LOADING -> {
            startLoading()
        }
        State.COMPLETED -> {
            stopLoading()
            vm.loginProcessState.value = State.DEFAULT
        }
    }

    when (vm.socialProcessState.value) {
        SocialState.MOVE_ENTER_HOME -> {
            navigateToEnterHomeScreen(navController)
            vm.socialProcessState.value = SocialState.DEFAULT
        }
        SocialState.MOVE_HOME -> {
            navigateToHomeScreen(navController)
            vm.socialProcessState.value = SocialState.DEFAULT
        }
        SocialState.MOVE_JOIN_NICKNAME -> {
            navigateToJoinNickNameScreen(navController)
            vm.socialProcessState.value = SocialState.DEFAULT
        }
        SocialState.ERROR -> {
            Toast.makeText(context, "에러가 발생했습니다", Toast.LENGTH_SHORT).show()
            vm.socialProcessState.value = SocialState.DEFAULT
        }
    }

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
            Login(vm.loginIdState, vm.loginPasswordState, passWordVisibility) {
                vm.signInEmail()
            }

            /** 회원가입 */
            Join {
                navController.navigate(OurHomeScreens.JoinEmailScreen.name)
            }

            /** 소셜 로그인 버튼 */
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SocialLoginButton(
                    painterResource =
                    painterResource(id = R.drawable.ic_google)
                ) {
                    val gso =
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(token)
                            .requestEmail()
                            .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    launcher.launch(googleSignInClient.signInIntent)
                }
                SocialLoginButton(
                    painterResource =
                    painterResource(id = R.drawable.ic_facebook)
                ) {
                    Toast.makeText(context, "현재 버전에서 지원하지 않는 기능입니다.", Toast.LENGTH_SHORT).show()
                }
                SocialLoginButton(
                    painterResource =
                    painterResource(id = R.drawable.ic_twiter)
                ) {
                    Toast.makeText(context, "현재 버전에서 지원하지 않는 기능입니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
private fun navigateToJoinNickNameScreen(navController: NavController) {
    navController.navigate(OurHomeScreens.JoinNickNameScreen.name + "/${OurHomeScreens.LoginScreen.name}")
}

@Composable
private fun navigateToEnterHomeScreen(navController: NavController) {
    navController.navigate(OurHomeScreens.EnterHomeScreen.name) {
        popUpTo(OurHomeScreens.LoginScreen.name) {
            inclusive = true
        }
    }
}

@Composable
private fun navigateToHomeScreen(navController: NavController) {
    navController.navigate(BottomNavItem.Home.screenRoute) {
        popUpTo(OurHomeScreens.LoginScreen.name) {
            inclusive = true
        }
    }
}

@Composable
private fun Join(onClick: () -> Unit) {
    Text(
        modifier = Modifier
            .clickable {
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

// 구글 로그인
@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            scope.launch {
                val authResult = Firebase.auth.signInWithCredential(credential).await()
                onAuthComplete(authResult)
            }
        } catch (e: ApiException) {
            onAuthError(e)
        }
    }
}