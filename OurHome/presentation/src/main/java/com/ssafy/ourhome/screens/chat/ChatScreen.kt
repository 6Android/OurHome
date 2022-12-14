package com.ssafy.ourhome.screens.chat

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import com.google.common.base.Strings.isNullOrEmpty
import com.ssafy.domain.model.chat.DomainChatDTO
import com.ssafy.ourhome.R
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.model.chat.ChatDTO
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.ui.theme.Gray
import com.ssafy.ourhome.ui.theme.MainColor
import com.ssafy.ourhome.utils.Prefs
import com.ssafy.ourhome.utils.Prefs.email
import com.ssafy.ourhome.utils.State
import com.ssafy.ourhome.utils.addFocusCleaner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun ChatScreen(navController: NavController, vm: ChatViewModel){

    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = true){
        initChatScreen(vm)
    }
    initChatViewModelCallback(vm)

    val listState = rememberLazyListState()
// Remember a CoroutineScope to be able to launch
    val coroutineScope = rememberCoroutineScope()

//    val today = remember {
//        LocalDate.now()
//    }
//
//    val todayDate = "${today.year}??? ${today.monthValue}??? ${today.dayOfMonth}???"

    LaunchedEffect(key1 = vm.chatSize){
        listState.scrollToItem(if(vm.chatSize == 0) vm.chatSize else vm.chatSize - 1)
    }

    Scaffold(topBar = {
        MainAppBar(title = "??????",
            onBackClick = { navController.popBackStack() }
        ) }
    ) {
        OurHomeSurface {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .addFocusCleaner(focusManager)
            ) {
                ChattingMsgList(vm, listState){ email ->
                    navController.navigate(OurHomeScreens.UserPageScreen.name + "/$email")
                }

                ChattingTextInput(vm)
            }
        }
    }

}

fun initChatScreen(vm: ChatViewModel){
    vm.getFamilyUsers()
}

fun initChatViewModelCallback(vm: ChatViewModel){
    when(vm.getFamilyProcessState.value){
        State.ERROR -> {
            vm.getFamilyProcessState.value = State.DEFAULT
        }
        State.SUCCESS ->{
            vm.getChats()
        }
    }

    when(vm.getChatsProcessState.value){
        State.ERROR -> {
            vm.getChatsProcessState.value = State.DEFAULT
        }
        State.SUCCESS ->{
            var count = 0
            vm.chats.forEach{ mapping_date, chat_list ->
                count += chat_list.size
            }
            vm.chatSize = count
        }
    }
}

/** ?????? ????????? ?????? **/
@Composable
private fun ChattingMsgList(vm: ChatViewModel, listState: LazyListState, onClick: (String) -> Unit) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f),
        state = listState
    ) {

        vm.chats.forEach(){ mapping_date, chats ->

            item{
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){

                    Divider(modifier = Modifier
                        .width(60.dp)
                        .height(2.dp), color = Gray)

                    Text(text = mapping_date, style = MaterialTheme.typography.body2, color = Gray)

                    Divider(modifier = Modifier
                        .width(60.dp)
                        .height(2.dp), color = Gray)
                }
            }

            items(chats){

                var painter = painterResource(R.drawable.img_default_user)
                var name = "?????? ?????? ??????"

                if (vm.familyUsers.contains(it.email)) {
                    painter =
                        if (vm.familyUsers[it.email]!!.image == "default") painterResource(R.drawable.img_default_user)
                        else rememberAsyncImagePainter(vm.familyUsers[it.email]!!.image)
                    name = vm.familyUsers[it.email]!!.name
                }


                var chat = ChatDTO(it.email, name, painter, it.content, it.date, it.year, it.month, it.day, it.hour, it.minute)

                if(it.email == Prefs.email){
                    MyChatItem(chat)
                }else {
                    FamilyChatItem(chat){ email ->
                        onClick(email)
                    }
                }
            }

        }

    }
}

/** ??? ?????? ????????? **/
@Composable
fun MyChatItem(chat: ChatDTO) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        val (msg, time) = createRefs()

        /** ?????? ????????? **/
        Column(
            modifier = Modifier
                .constrainAs(msg) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end, margin = 8.dp)
                }
                .clip(RoundedCornerShape(12.dp))
                .background(MainColor)
                .padding(8.dp)
                .widthIn(16.dp, 180.dp)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = chat.content,
                style = MaterialTheme.typography.body1,
                color = Color.White
            )
        }

        val chatTime = makeChatTime(chat)

        /** ?????? **/
        Text(
            text = chatTime,
            style = MaterialTheme.typography.caption,
            color = Gray,
            modifier = Modifier.constrainAs(time){
                end.linkTo(msg.start, margin = 8.dp)
                bottom.linkTo(msg.bottom)
            }
        )
    }
}

/** ?????? ?????? ????????? **/
@Composable
fun FamilyChatItem(chat: ChatDTO, onClick : (String) -> Unit) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        val (profile, msg, time) = createRefs()
        /** ????????? **/
        Column(
            modifier = Modifier.constrainAs(profile){
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable { onClick(chat.email) },
                contentScale = ContentScale.Crop,
                painter = chat.img!!,
                contentDescription = "Profile Image"
            )
        }

        Column(
            modifier = Modifier
                .constrainAs(msg) {
                    top.linkTo(profile.top)
                    start.linkTo(profile.end, margin = 8.dp)
                }
        ){
            /** ?????? **/
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = chat.name,
                style = MaterialTheme.typography.body2
            )

            /** ?????? ????????? **/
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .widthIn(16.dp, 188.dp)
                    .wrapContentHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                Text(
                    text = chat.content,
                    style = MaterialTheme.typography.body1
                )
            }
        }


        val chatTime = makeChatTime(chat)

        /** ?????? **/
        Text(
            text = chatTime,
            style = MaterialTheme.typography.caption.copy(fontSize = 10.sp),
            color = Gray,
            modifier = Modifier.constrainAs(time){
                start.linkTo(msg.end, margin = 8.dp)
                bottom.linkTo(msg.bottom)
            }
        )
    }
}

@Composable
fun makeChatTime(chat: ChatDTO): String {
    var hour = chat.hour.toString()
    if (hour.length < 2) {
        hour = "0" + hour
    }

    var minute = chat.minute.toString()
    if (minute.length < 2) {
        minute = "0" + minute
    }

    var chatTime = if (chat.hour == 24) {
        "?????? 0:" + minute
    } else if (chat.hour < 12) {
        "?????? " + hour + ":" + minute
    } else if (chat.hour == 12) {
        "?????? 12:" + minute
    } else {
        hour = (chat.hour - 12).toString()
        if (hour.length < 2) {
            hour = "0" + hour
        }

        "?????? " + hour + ":" + minute
    }
    return chatTime
}

/** ?????? ?????? ??? **/
@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ChattingTextInput(vm: ChatViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester by remember { mutableStateOf(FocusRequester()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
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
                    value = vm.content.value,
                    onValueChange = {
                        if(it.length < 500){
                            vm.content.value = it
                        }
                        else {
                            vm.content.value = it.substring(0, 500)
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
                            text = "???????????? ???????????????.",
                            style = MaterialTheme.typography.body1
                        )
                    }
                )
            }

            SendIcon(vm)
        }
    }
}

/** ????????? ?????? ????????? **/
@Composable
private fun SendIcon(vm: ChatViewModel) {
    Image(
        modifier = Modifier
            .size(32.dp)
            .fillMaxWidth()
            .padding(start = 8.dp)
            .clickable {
                if (vm.content.value.isNotBlank()) {
                    vm.chatting()
                }
            },
        alignment = Alignment.CenterEnd,
        painter = painterResource(id = R.drawable.img_chat_send),
        contentDescription = null
    )
}

