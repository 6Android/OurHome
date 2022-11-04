package com.ssafy.ourhome.screens.userpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ssafy.ourhome.R
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.NoLabelTextInput
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.ui.theme.OurHomeTheme

data class tmpUserDTO(
    val imageUrl: String = "default",
    val nickname: String,
    val email: String,
    val phone: String,
    val birth: String,
    val bloodType: String,
    val MBTI: String,
    val job: String,
    val interest: String,
    val hobby: String
)

@Composable
fun EditProfileScreen(navController: NavController = NavController(LocalContext.current)) {
    val scrollState = rememberScrollState()

    // TODO : userDTO
    val user = tmpUserDTO(
        "default", "서경원", "skw@ssafy.com", "010-1234-5678",
        "1997년 1월 1일", "Rh+ B", "ENFP", "개발자", "운동, 요리", "글쓰기"
    )

    val nicknameState = remember {
        mutableStateOf(user.nickname)
    }
    val phoneState = remember {
        mutableStateOf(user.phone)
    }



    OurHomeSurface() {
        Scaffold(topBar = {
            MainAppBar(
                title = "내 정보 수정",
                backIconEnable = true,
                onBackClick = { navController.popBackStack() },
                icon = painterResource(R.drawable.ic_check),
                onIconClick = {
                    navController.navigate(OurHomeScreens.SettingScreen.name)
                }
            )
        }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    UserImage(user.imageUrl)
                    TextFieldWithClear(nicknameState)
                }

                Divider(thickness = 8.dp, color = Color.LightGray.copy(alpha = 0.8f))

                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    TextWithText("이메일", user.email)
                    TextWithTextField("전화번호", phoneState)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun TextFieldWithClear(
    textState: MutableState<String>
) {
    Box(modifier = Modifier.width(240.dp),) {

        NoLabelTextInput(
            modifier = Modifier.align(Alignment.Center),
            valueState = textState,
            isAlignCenter = true,
            enabled = true,
            maxLength = 10
        )

        Icon(imageVector = Icons.Default.Clear,
            contentDescription = "Clear Button",
            modifier = Modifier.align(Alignment.CenterEnd).clickable {
                textState.value = ""
            }
        )
    }

}

@Composable
private fun TextWithText(
    title: String,
    content: String
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = title, modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold))
        Text(text = content, modifier = Modifier.weight(4f), style = MaterialTheme.typography.h6)
    }
}

@Composable
private fun TextWithTextField(
    title: String,
    textState: MutableState<String>
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = title, modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold))
        NoLabelTextInput(
            modifier = Modifier.weight(4f),
            valueState = textState,
            isAlignCenter = false,
            enabled = true,
            maxLength = 10
        )
    }
}

@Composable
private fun UserImage(
    imageUrl: String
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
}

/** 라벨이 없고, 길이 제한이 있는 한줄 editText **/
@Composable
private fun NoLabelTextInput(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    enabled: Boolean,
    maxLength: Int,
    isSingleLine: Boolean = true,
    isAlignCenter: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    BasicTextField(
        value = valueState.value,
        onValueChange = {
            valueState.value = if (it.length < maxLength) {
                it
            } else {
                it.substring(0, maxLength)
            }
        },
        singleLine = isSingleLine,
        textStyle =
        if (isAlignCenter) LocalTextStyle.current.copy(textAlign = TextAlign.Center)
        else LocalTextStyle.current.copy(textAlign = TextAlign.Start),
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
    )
}

@Preview(showBackground = true)
@Composable
private fun EditProfilePreview() {
    OurHomeTheme {
        EditProfileScreen()
    }
}