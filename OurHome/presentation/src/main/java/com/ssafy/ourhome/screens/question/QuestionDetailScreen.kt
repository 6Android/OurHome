package com.ssafy.ourhome.screens.question

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.ui.theme.Gray
import com.ssafy.ourhome.ui.theme.MainColor


@Composable
fun QuestionDetailScreen(navController: NavController){
    val scrollState = rememberScrollState()

    // TODO NestedScrollView 필요
    Scaffold(topBar = {
        MainAppBar(title = "질문 상세",
            onBackClick = { navController.popBackStack() }
        ) }
    ) {
        OurHomeSurface {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                TodayQuestion(questionNumber = "Q4. ", questionContent = "오늘 점심 뭐 드셨나요?")

                Spacer(modifier = Modifier.height(16.dp))

                MyAnswer()

                Spacer(modifier = Modifier.height(24.dp))

                Divider(modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp), color = Gray)

                Spacer(modifier = Modifier.height(24.dp))

                FamilyAnswer()

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/** 가족 답변 카드 **/
@Composable
fun FamilyAnswer(){
    val scrollState = rememberScrollState()
    val painter =
        rememberAsyncImagePainter("https://i.pinimg.com/222x/36/30/f7/3630f7d930f91e495d93c02833b4abfc.jpg")

    LazyColumn(
        modifier = Modifier.height(600.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        items(30){
            FamilyAnswerItem(painter = painter,
                name = "아빠",
                content = "순대국밥 먹었습니다.순대국밥 먹었습니다.순대국밥 먹었습니다." +
                        "순대국밥 먹었습니다.순대국밥 먹었습니다.순대국밥 먹었습니다.순대국밥 먹었습니다." +
                        "순대국밥 먹었습니다.순대국밥 먹었습니다.순대국밥 먹었습니다.순대국밥 먹었습니다." +
                        "순대국밥 먹었습니다.순대국밥 먹었습니다.순대국밥 먹었습니다.순대국밥 먹었습니다." +
                        "순대국밥 먹었습니다.순대국밥 먹었습니다.순대국밥 먹었습니다.")
        }
    }

}

/** 가족 답변 lazycolumn item **/
@Composable
fun FamilyAnswerItem(painter: AsyncImagePainter, name: String, content: String){
    GrayBorderBox {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape),
                painter = painter,
                contentDescription = "가족 프로필 이미지"
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(text = name, style = MaterialTheme.typography.h5.copy(
                    fontWeight = FontWeight.Bold))
        }
        
        Text(modifier = Modifier.padding(16.dp), text = content,
            style = MaterialTheme.typography.body2
        )
        
        Spacer(modifier = Modifier.height(12.dp))
    }
}

/** 내 답변 카드 **/
@Composable
fun MyAnswer(){
    val myAnswerState = remember {
        mutableStateOf("")
    }

    GrayBorderBox {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "내 답변",
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.ExtraBold)
                )
                SmallRoundedButton(boolean = myAnswerState.value.isNotEmpty() && myAnswerState.value.length <= 100)
            }

            NoUnderLineTextInput(myAnswerState = myAnswerState, height = 150)

            Spacer(modifier = Modifier.height(12.dp))

            ShowTextSize(myAnswerState)
        }
    }

}

/** 텍스트 길이 표시 **/
@Composable
fun ShowTextSize(myAnswerState: MutableState<String>){

    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Text(text = myAnswerState.value.length.toString(),
            style = MaterialTheme.typography.caption.copy(color = Gray)
        )

        Text(text = "/ 100",
            style = MaterialTheme.typography.caption.copy(color = Gray)
        )
    }
}

/** 언더바 없는 TextInput **/
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NoUnderLineTextInput(myAnswerState: MutableState<String>, height: Int){
        TextField(modifier = Modifier.fillMaxWidth(),
            value = myAnswerState.value,
            onValueChange = {
                if(it.length < 100){
                    myAnswerState.value = it
                }
                else {
                    myAnswerState.value = it.substring(0, 100)
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
        )
}


/** Rounded 버튼 스몰 버전 **/
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SmallRoundedButton(boolean : Boolean){
    val keyboardController = LocalSoftwareKeyboardController.current

    val addState : MutableState<Boolean> = remember {
        mutableStateOf(true)
    }

    Surface(
        modifier = Modifier.clip(
            RoundedCornerShape(CornerSize(20))
        ),
        color = if(boolean) MainColor else Gray
    ) {
        Column(
            modifier = Modifier.width(52.dp).heightIn(28.dp)
                .clickable(enabled = boolean) {
                    addState.value = !addState.value
                    keyboardController?.hide()
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if(addState.value) "등록" else "수정",
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.ExtraBold, color = Color.White)
            )
        }
    }
}

/** 회색 Rounded shape 카드, 높이 변경 시 Modifier에 height만 담아서 넘겨주기 **/
@Composable
fun GrayBorderBox(modifier: Modifier = Modifier, content : @Composable() (ColumnScope.() -> Unit)){
    Column(modifier = modifier.fillMaxWidth()
        .border(width = 2.dp,
            color = Gray,
            shape = RoundedCornerShape(12.dp)
        ),
        content = content
    )
}