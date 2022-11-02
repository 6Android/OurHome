package com.ssafy.ourhome.screens.question

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.ssafy.ourhome.R
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.ui.theme.Gray
import com.ssafy.ourhome.ui.theme.MainColor
import com.ssafy.ourhome.ui.theme.nanum
import com.ssafy.ourhome.utils.SETTING_ICON

@Composable
fun QuestionScreen(navController: NavController) {
    val painter =
        rememberAsyncImagePainter("https://i.pinimg.com/222x/36/30/f7/3630f7d930f91e495d93c02833b4abfc.jpg")
    val scrollState = rememberScrollState()

    Scaffold(topBar = { // TODO 세팅 아이콘 -> 채팅 아이콘
        MainAppBar(title = "질문", backIconEnable = false, icon = painterResource(id = SETTING_ICON), onIconClick = {
            navigateChatScreen(navController)
        })
    }) {
        OurHomeSurface() {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                CenterHorizontalColumn {

                    PetSemiDetail(petName = "고라파덕", painter = painter, petLevel = "Lv. 2"){
                        navController.navigate(OurHomeScreens.PetDetailScreen.name)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    TodayQuestion(questionNumber = "Q4. ", questionContent = "오늘 점심 뭐 드셨나요?")

                    Spacer(modifier = Modifier.height(16.dp))

                    ReplyQuestionButton(
                        buttonWidth = 120, buttonHeight = 40, fontSize = 20,
                        label = "답변 하기"
                    )
                }

                Spacer(modifier = Modifier.height(36.dp))

                LastQuestionHeader(){
                    navController.navigate(OurHomeScreens.QuestionListScreen.name)
                }

                QuestionLazyColumn(Modifier.height(260.dp))

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

}

/** 채팅 스크린 이동 **/
fun navigateChatScreen(navController: NavController){
    navController.navigate(OurHomeScreens.ChatScreen.name)
}

/** 중앙 정렬 Column **/
@Composable
fun CenterHorizontalColumn(content : @Composable() (ColumnScope.() -> Unit) ){
    Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp), horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
}

/** 오늘의 질문 내용 **/
@Composable
private fun TodayQuestion(questionNumber: String, questionContent: String) {
    Text(
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = MainColor,
                    fontFamily = nanum,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 34.sp
                )
            ) {
                append(questionNumber)
            }
            withStyle(
                style = SpanStyle(
                    fontFamily = nanum,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp
                )
            ) {
                append(questionContent)
            }
        }
    )
}

/** 펫 정보 (이름, 이미지, 레벨), 클릭 시 펫 상헤 화면 이동 **/
@Composable
fun PetSemiDetail(petName: String, painter: AsyncImagePainter, petLevel: String, onClick: () -> Unit = {}) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onClick.invoke()
        }, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = petName,
            fontFamily = nanum,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Image(
            modifier = Modifier.size(250.dp),
            painter = painter,
            contentDescription = "펫 이미지"
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = petLevel,
            fontFamily = nanum,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }

}

/** 지난 질문 헤더 **/
@Composable
fun LastQuestionHeader(onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text("지난 질문", fontFamily = nanum, fontWeight = FontWeight.Bold, fontSize = 20.sp)

        Text(
            "더보기",
            modifier = Modifier.clickable(onClick = onClick),
            fontFamily = nanum,
            color = Gray,
            fontWeight = FontWeight.Normal
        )
    }
}

/** 지난 질문 리스트  **/
@Composable
fun QuestionLazyColumn(modifier: Modifier = Modifier, size: Int = 3) {
    LazyColumn(modifier = modifier) {
        items(size) {
            QuestionItem()
        }
    }
}

/** 지난 질문 리스트 lazyColumn의 item **/
@Composable
fun QuestionItem(modifier: Modifier = Modifier.fillMaxWidth()) {
    Card(
        modifier = modifier.padding(vertical = 8.dp), elevation = 4.dp
    ) {
        Column(
            modifier = modifier
        ) {
            Row(
                modifier = modifier
                    .padding(12.dp)
            ) {
                Image(
                    modifier = Modifier.size(12.dp),
                    painter = painterResource(id = R.drawable.img_calendar_circle),
                    contentDescription = ""
                )

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "2022.10.22",
                    fontFamily = nanum,
                    color = Gray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
            }

            Text(
                modifier = modifier
                    .padding(start = 12.dp, bottom = 16.dp),
                text = "Q3. 가족들에게 당신은 어떤 존재인가요?",
                fontFamily = nanum,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }

}

/** 답변 하기 버튼 **/
@Composable
fun ReplyQuestionButton(
    label: String = "Button",
    radius: Int = 20,
    onClick: () -> Unit = {},
    buttonWidth: Int,
    buttonHeight: Int,
    fontSize: Int
) {

    Surface(
        modifier = Modifier.clip(
            RoundedCornerShape(CornerSize(radius))
        ),
        color = MainColor
    ) {
        Column(
            modifier = Modifier
                .width(buttonWidth.dp)
                .heightIn(buttonHeight.dp)
                .clickable { onClick.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label, style = TextStyle(
                    fontFamily = nanum,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = fontSize.sp,
                    letterSpacing = 0.15.sp
                )
            )
        }
    }
}
