package com.ssafy.ourhome.screens.userpage

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.squaredem.composecalendar.ComposeCalendar
import com.ssafy.ourhome.R
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import kotlinx.coroutines.delay
import java.time.LocalDate

@Composable
fun EditProfileScreen(
    navController: NavController = NavController(LocalContext.current),
    vm: UserPageViewModel
) {
    val scrollState = rememberScrollState()

    val showDialog = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(true){
        // 에디트텍스트 초기화
        vm.setData()
    }


    // 에디트 성공
    if (vm.editSuccess) {
        Toast.makeText(LocalContext.current, "정보 수정에 성공하였습니다.", Toast.LENGTH_SHORT).show()
        vm.setEditSuccess()
        navController.popBackStack()
    }

    if (showDialog.value) {
        ComposeCalendar(
            onDone = { it: LocalDate ->
                // Hide dialog
                Log.d("test5", "EditProfileScreen: $it")
                vm.birthDayState.value = it
                showDialog.value = false
                // Do something with the date
            },
            onDismiss = {
                // Hide dialog
                showDialog.value = false
            }
        )
    }

    OurHomeSurface() {
        Scaffold(topBar = {
            MainAppBar(
                title = "내 정보 수정",
                backIconEnable = true,
                onBackClick = { navController.popBackStack() },
                icon = painterResource(R.drawable.ic_check),
                onIconClick = {
                    vm.editProfile()
                }
            )
        }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    UserImage(vm.user.image)
                    TextFieldWithClear(vm.nicknameState)
                }

                Divider(
                    modifier = Modifier.padding(bottom = 24.dp),
                    thickness = 4.dp, color = Color.LightGray.copy(alpha = 0.8f)
                )

                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    TextWithText("이메일", vm.user.email)
                    TextWithTextField("전화번호", vm.phoneState, KeyboardType.Number)
                    BirthDaySelect(title = "생일", vm.birthDayState, showDialog)
                    TextWithDropDown(
                        title = "혈액형", textState = vm.bloodTypeState, itemList =
                        listOf(
                            "Rh+ A",
                            "Rh- A",
                            "Rh+ B",
                            "Rh- B",
                            "Rh+ O",
                            "Rh- O",
                            "Rh+ AB",
                            "Rh- AB"
                        )
                    )
                    TextWithDropDown(
                        title = "MBTI", textState = vm.MBTIState, itemList =
                        listOf(
                            "ENFP", "ENFJ", "ENTP", "ENTJ", "ESFP", "ESFJ", "ESTP", "ESTJ",
                            "INFP", "INFJ", "INTP", "INTJ", "ISFP", "ISFJ", "ISTP", "ISTJ", "MBTI",
                        )
                    )
                    TextWithTextField("직업", vm.jobState)
                    TextWithTextField("관심사", vm.interestState)
                    TextWithTextField("취미", vm.hobbyState)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun BirthDaySelect(
    title: String,
    birthDayState: MutableState<LocalDate>,
    showDialog: MutableState<Boolean>

) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title, modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
        )
        Button(
            modifier = Modifier.weight(2f),
            border = BorderStroke(width = 2.dp, color = Color.LightGray),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            elevation = ButtonDefaults.elevation(0.dp),
            onClick = { showDialog.value = true }) {
            Text(
                text = "${birthDayState.value}",
                style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun TextWithDropDown(
    title: String,
    textState: MutableState<String>,
    itemList: List<String>
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    var selectedIndex by remember {
        mutableStateOf(itemList.indexOf(textState.value))
    }

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = title, modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(2f)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = itemList[selectedIndex], modifier = Modifier,
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
                )

                IconButton(
                    onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop Down Button",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }


            DropdownMenu(
                expanded = expanded, onDismissRequest = { expanded = false },
                modifier = Modifier
                    .wrapContentHeight()
                    .heightIn(max = 235.dp)

            ) {
                itemList.forEachIndexed { index, item ->
                    DropdownMenuItem(modifier = Modifier.wrapContentHeight(), onClick = {
                        selectedIndex = index
                        textState.value = item
                        expanded = false
                    }) {
                        Text(text = item, style = MaterialTheme.typography.h6)
                    }
                }
            }
        }


    }
}

@Composable
private fun TextFieldWithClear(
    textState: MutableState<String>
) {
    Box(modifier = Modifier.width(240.dp)) {

        NoLabelTextInput(
            modifier = Modifier.align(Alignment.Center),
            valueState = textState,
            isAlignCenter = true,
            enabled = true,
            maxLength = 10
        )

        Icon(imageVector = Icons.Default.Clear,
            contentDescription = "Clear Button",
            modifier = Modifier

                .align(Alignment.CenterEnd)
                .clickable {
                    textState.value = ""
                }
                .size(20.dp)
        )
    }

}

@Composable
private fun TextWithText(
    title: String,
    content: String
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title, modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
        )
        Text(text = content, modifier = Modifier.weight(4f), style = MaterialTheme.typography.h6)
    }
}

@Composable
private fun TextWithTextField(
    title: String,
    textState: MutableState<String>,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title, modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
        )
        NoLabelTextInput(
            modifier = Modifier.weight(4f),
            valueState = textState,
            isAlignCenter = false,
            keyboardType = keyboardType,
            enabled = true,
            maxLength = 20
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
        contentScale = ContentScale.Crop,
        painter =
        if (imageUrl == "default") painterResource(R.drawable.img_default_user)
        else rememberAsyncImagePainter(imageUrl),
        contentDescription = "Profile Image"
    )
}

/** 라벨이 없고, 길이 제한이 있는 한줄 editText **/
@OptIn(ExperimentalComposeUiApi::class)
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
    val keyboardController = LocalSoftwareKeyboardController.current
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
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        }),
        decorationBox = { innerTextField ->

            Box(
                modifier = Modifier
                    .height(25.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Divider(Modifier.height(1.dp), color = Color.LightGray)
            }
            innerTextField()

        }
    )
}

//@Preview(showBackground = true)
//@Composable
//private fun EditProfilePreview() {
//    OurHomeTheme {
//        EditProfileScreen()
//    }
//}