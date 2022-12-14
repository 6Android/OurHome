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
                // ?????? ???????????? ?????? ???
                true -> {
                    navigateToHomeScreen(navController)
                }
                // ???????????? ?????? ???
                else -> {
                    navigateToEnterHomeScreen(navController)
                }
            }
            vm.loginProcessState.value = State.COMPLETED
        }
        State.FAIL -> {
            Toast.makeText(context, "???????????? ??????????????? ??????????????????", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(context, "????????? ??????????????????", Toast.LENGTH_SHORT).show()
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
            /** ????????? ??????, ????????? */
            Logo()

            /** ????????? */
            Login(vm.loginIdState, vm.loginPasswordState, passWordVisibility) {
                vm.signInEmail()
            }

            /** ???????????? */
            Join {
                navController.navigate(OurHomeScreens.JoinEmailScreen.name)
            }

            /** ?????? ????????? ?????? */
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
                    Toast.makeText(context, "?????? ???????????? ???????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show()
                }
                SocialLoginButton(
                    painterResource =
                    painterResource(id = R.drawable.ic_twiter)
                ) {
                    Toast.makeText(context, "?????? ???????????? ???????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show()
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
        text = "????????????",
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
        text = "?????????",
        style = MaterialTheme.typography.subtitle1
    )
    EmailInput(emailState = idState, labelId = "?????????", enabled = true)
    PasswordInput(
        passwordState = passwordState,
        labelId = "????????????",
        enabled = true,
        passwordVisibility = passWordVisibility,
        onAction = KeyboardActions(onDone = { onClick() })
    )
    Spacer(modifier = Modifier.height(16.dp))
    RoundedButton(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(48.dp), label = "?????????"
    ) {
        onClick()

    }
    Spacer(modifier = Modifier.height(20.dp))
}

/** ????????? ??????, ????????? */
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
        text = "???????????? ??????\n????????? ???????????????!",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.subtitle2
    )
    Spacer(modifier = Modifier.height(32.dp))
}

/** ?????? ????????? ?????? */
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

// ?????? ?????????
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