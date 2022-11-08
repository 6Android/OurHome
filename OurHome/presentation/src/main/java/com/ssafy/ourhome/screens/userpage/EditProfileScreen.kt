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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.utils.ResultType
import com.ssafy.ourhome.R
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.ui.theme.OurHomeTheme

//data class tmpUserDTO(
//    val imageUrl: String = "default",
//    val nickname: String,
//    val email: String,
//    val phone: String,
//    val birth: String,
//    val bloodType: String,
//    val MBTI: String,
//    val job: String,
//    val interest: String,
//    val hobby: String
//)

@Composable
fun EditProfileScreen(
    navController: NavController = NavController(LocalContext.current),
    userDTO: DomainUserDTO = DomainUserDTO(),
    num: Int = 10
) {
    val scrollState = rememberScrollState()

    val vm: UserPageViewModel = hiltViewModel()

    // 에디트 성공 여부
    val editSuccess = remember {
        mutableStateOf(false)
    }

    var user = userDTO

    when (val editResponse = vm.editResponse) {
        is ResultType.Uninitialized -> {}
        is ResultType.Success -> {
            editSuccess.value = true
        }
        is ResultType.Error -> print(editResponse.exception)
    }

    val nicknameState = remember {
        mutableStateOf(userDTO.name)
    }
    val phoneState = remember {
        mutableStateOf(userDTO.phone)
    }
    val bloodTypeState = remember {
        mutableStateOf(userDTO.blood_type)
    }

    val MBTIState = remember {
        mutableStateOf(userDTO.mbti)
    }

    val jobState = remember {
        mutableStateOf(userDTO.job)
    }

    val interestState = remember {
        mutableStateOf(userDTO.interest)
    }

    val hobbyState = remember {
        mutableStateOf(userDTO.hobby)
    }

    // 에디트 성공
    if(editSuccess.value){
        Log.d("Edit", "EditProfileScreen: ${editSuccess.value}")
        Toast.makeText(LocalContext.current, "정보 수정에 성공하였습니다.", Toast.LENGTH_SHORT).show()
        navController.popBackStack()
    }

    OurHomeSurface() {
        Scaffold(topBar = {
            MainAppBar(
                title = "내 정보 수정",
                backIconEnable = true,
                onBackClick = { navController.popBackStack() },
                icon = painterResource(R.drawable.ic_check),
                onIconClick = {
                    user.name = nicknameState.value
                    user.phone = phoneState.value
                    user.blood_type = bloodTypeState.value
                    user.mbti = MBTIState.value
                    user.job = jobState.value
                    user.interest = interestState.value
                    user.hobby = hobbyState.value

                    // TODO : 패밀리코드
                    vm.editProfile("EX7342",user)
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
                    UserImage(userDTO.image)
                    TextFieldWithClear(nicknameState)
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
                    TextWithText("이메일", userDTO.email)
                    TextWithTextField("전화번호", phoneState)
                    TextWithText(title = "생일", content = userDTO.birthday)
                    TextWithDropDown(
                        title = "혈액형", textState = bloodTypeState, itemList =
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
                        title = "MBTI", textState = MBTIState, itemList =
                        listOf(
                            "ENFP", "ENFJ", "ENTP", "ENTJ", "ESFP", "ESFJ", "ESTP", "ESTJ",
                            "INFP", "INFJ", "INTP", "INTJ", "ISFP", "ISFJ", "ISTP", "ISTJ",
                        )
                    )
                    TextWithTextField("직업", jobState)
                    TextWithTextField("관심사", interestState)
                    TextWithTextField("취미", hobbyState)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
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
            text = title, modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(4f)
        ) {

//            ExposedDropdownMenuBox(
//                expanded = expanded,
//                onExpandedChange = {
//                    expanded = !expanded
//                },
//                modifier = Modifier.wrapContentHeight()
//            ) {
//                TextField(
//                    readOnly = true,
//                    value = itemList[selectedIndex],
//                    onValueChange = { },
//                    trailingIcon = {
//                        ExposedDropdownMenuDefaults.TrailingIcon(
//                            expanded = expanded
//                        )
//                    },
//                    colors = ExposedDropdownMenuDefaults.textFieldColors()
//                )
//                ExposedDropdownMenu(
//                    expanded = expanded,
//                    onDismissRequest = {
//                        expanded = false
//                    }
//                ) {
//                    itemList.forEachIndexed { idx, selectionOption ->
//                        DropdownMenuItem(
//                            onClick = {
//                               selectedIndex = idx
//                                expanded = false
//                            }
//                        ) {
//                            Text(text = selectionOption, style = MaterialTheme.typography.h6)
//                        }
//                    }
//                }
//            }
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
    textState: MutableState<String>
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