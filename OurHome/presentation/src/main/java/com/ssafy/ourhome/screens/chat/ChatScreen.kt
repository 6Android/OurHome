package com.ssafy.ourhome.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import com.ssafy.ourhome.R
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.ui.theme.Gray
import com.ssafy.ourhome.ui.theme.MainColor
import com.ssafy.ourhome.utils.addFocusCleaner

@Composable
fun ChatScreen(navController: NavController){
    val chatContent = remember {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current
    
    Scaffold(topBar = {
        MainAppBar(title = "질문 상세",
            onBackClick = { navController.popBackStack() }
        ) }
    ) {
        OurHomeSurface {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .addFocusCleaner(focusManager)
            ) {
                ChattingMsgList()

                ChattingTextInput(chatContent)
            }
        }
    }
}

/** 채팅 메세지 목록 **/
@Composable
private fun ChattingMsgList() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f)
    ) {
        items(20) {
            //TODO 본인, 타인 분기 처리 후 표시
            FamilyChatItem()
            MyChatItem()
        }
    }
}

/** 내 채팅 메세지 **/
@Composable
fun MyChatItem() {
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
    ) {
        val (msg, time) = createRefs()

        /** 채팅 메세지 **/
        Column(
            modifier = Modifier.constrainAs(msg){
                    top.linkTo(parent.top)
                    end.linkTo(parent.end, margin = 8.dp)
                }.clip(RoundedCornerShape(12.dp))
                .background(MainColor)
                .padding(8.dp)
                .widthIn(16.dp, 180.dp)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "안녕하세요",
                style = MaterialTheme.typography.body1,
                color = Color.White
            )
        }

        /** 시간 **/
        Text(
            text = "오후 12:20 분",
            style = MaterialTheme.typography.caption,
            color = Gray,
            modifier = Modifier.constrainAs(time){
                end.linkTo(msg.start, margin = 8.dp)
                bottom.linkTo(msg.bottom)
            }
        )
    }
}

/** 타인 채팅 메세지 **/
@Composable
fun FamilyChatItem() {
    val painter =
        rememberAsyncImagePainter("https://i.pinimg.com/222x/36/30/f7/3630f7d930f91e495d93c02833b4abfc.jpg")

    ConstraintLayout(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
    ) {
        val (profile, msg, time) = createRefs()
        /** 프로필 **/
        Column(
            modifier = Modifier.constrainAs(profile){
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier.size(64.dp).clip(CircleShape),
                contentScale = ContentScale.Crop,
                painter = painter,
                contentDescription = "Profile Image"
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = "아빠",
                style = MaterialTheme.typography.body2
            )
        }
        /** 채팅 메세지 **/
        Column(
            modifier = Modifier.constrainAs(msg){
                    top.linkTo(profile.top)
                    start.linkTo(profile.end, margin = 8.dp)
                }.clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .widthIn(16.dp, 188.dp)
                .wrapContentHeight()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요",
                style = MaterialTheme.typography.body1
            )
        }
        /** 시간 **/
        Text(
            text = "오후 12:20 분",
            style = MaterialTheme.typography.caption,
            color = Gray,
            modifier = Modifier.constrainAs(time){
                start.linkTo(msg.end, margin = 8.dp)
                bottom.linkTo(msg.bottom)
            }
        )
    }
}

/** 채팅 입력 창 **/
@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ChattingTextInput(chatContent: MutableState<String>) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester by remember { mutableStateOf(FocusRequester()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .border(
                        width = 2.dp,
                        color = Gray,
                        shape = RoundedCornerShape(12.dp)
                    ),
                verticalArrangement = Arrangement.Center
            ) {
                TextField(modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester = focusRequester)
                    .onFocusChanged {
                        if (!it.hasFocus) {
                            keyboardController?.hide()
                        }
                    }
                    ,
                    value = chatContent.value,
                    onValueChange = {
                        if(it.length < 500){
                            chatContent.value = it
                        }
                        else {
                            chatContent.value = it.substring(0, 500)
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        disabledTextColor = Color.Transparent,
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(
                            text = "메세지를 입력하세요.",
                            style = MaterialTheme.typography.body1
                        )
                    }
                )
            }

            SendIcon()
        }
    }
}

/** 메세지 전송 아이콘 **/
@Composable
private fun SendIcon() {
    Image(
        modifier = Modifier
            .size(32.dp)
            .fillMaxWidth()
            .padding(start = 8.dp)
            .clickable {
                //TODO 메세지 전송
            },
        alignment = Alignment.CenterEnd,
        painter = painterResource(id = R.drawable.img_chat_send),
        contentDescription = null
    )
}

