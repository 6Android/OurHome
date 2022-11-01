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
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImagePainter
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import com.ssafy.ourhome.R
import com.ssafy.ourhome.navigation.BottomNavItem
import com.ssafy.ourhome.ui.theme.Gray
import com.ssafy.ourhome.ui.theme.MainColor
import com.ssafy.ourhome.ui.theme.OurHomeTheme
import com.ssafy.ourhome.ui.theme.nanum

@Composable
fun QuestionScreen(navController : NavController) {
    val painter = rememberAsyncImagePainter("https://i.pinimg.com/222x/36/30/f7/3630f7d930f91e495d93c02833b4abfc.jpg")
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {

            PetDetail(petName = "고라파덕", painter = painter, petLevel = "Lv. 2")

            Spacer(modifier = Modifier.height(24.dp))

            TodayQuestion(questionNumber = "Q4. ", questionContent = "오늘 점심 뭐 드셨나요?")

            Spacer(modifier = Modifier.height(16.dp))

            ReplyQuestionButton(buttonWidth = 120, buttonHeight = 40, fontSize = 20,
                label = "답변 하기")
        }

        Spacer(modifier = Modifier.height(36.dp))

        LastQuestionHeader()

        Spacer(modifier = Modifier.height(16.dp))

        QuestionLazyColumn(240)
    }



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

/** 펫 정보 (이름, 이미지, 레벨) **/
@Composable
private fun PetDetail(petName : String, painter: AsyncImagePainter, petLevel: String) {
    Text(
        text = petName,
        fontFamily = nanum,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )

    Image(
        modifier = Modifier.size(250.dp),
        painter = painter,
        contentDescription = "펫 이미지"
    )
    Text(
        text = petLevel,
        fontFamily = nanum,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
}

/** 지난 질문 헤더 **/
@Composable
fun LastQuestionHeader(){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom) {
        Text("지난 질문", fontFamily = nanum, fontWeight = FontWeight.Bold, fontSize = 20.sp)

        Text("더보기", fontFamily = nanum, color = Gray, fontWeight = FontWeight.Normal)
    }
}

/** 지난 질문 리스트  **/
@Composable
fun QuestionLazyColumn(height: Int, size : Int = 3){
    LazyColumn(modifier = Modifier.height(height.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(size) {
            QuestionItem()
        }
    }
}

/** 지난 질문 리스트 lazyColumn의 item **/
@Composable
fun QuestionItem(modifier: Modifier = Modifier.fillMaxWidth()) {
    Card(modifier = modifier
        .height(70.dp)
        , elevation = 4.dp) {
        Column(modifier = modifier
            .fillMaxHeight()) {
            Row(modifier = modifier
                .padding(12.dp)) {
                Image(modifier = Modifier.size(12.dp),painter = painterResource(id = R.drawable.img_calendar_circle), contentDescription = "")

                Text(modifier = Modifier.padding(start = 8.dp), text = "2022.10.22", fontFamily = nanum, color = Gray, fontWeight = FontWeight.Normal, fontSize = 12.sp)
            }

            Text(modifier = modifier
                .padding(start = 12.dp, bottom = 16.dp)
                , text = "Q3. 가족들에게 당신은 어떤 존재인가요?", fontFamily = nanum, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OurHomeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            val navController = rememberNavController()
            QuestionScreen(navController)
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
            Text(text = label, style = TextStyle(
                fontFamily = nanum,
                fontWeight = FontWeight.ExtraBold,
                fontSize = fontSize.sp,
                letterSpacing = 0.15.sp
            ))
        }
    }
}
