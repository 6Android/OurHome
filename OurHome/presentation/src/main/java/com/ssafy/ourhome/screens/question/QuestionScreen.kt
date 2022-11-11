package com.ssafy.ourhome.screens.question

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import com.ssafy.domain.model.pet.DomainFamilyPetDTO
import com.ssafy.domain.model.question.DomainQuestionDTO
import com.ssafy.ourhome.R
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.ui.theme.Gray
import com.ssafy.ourhome.ui.theme.MainColor
import com.ssafy.ourhome.ui.theme.nanum
import com.ssafy.ourhome.utils.CHATTING_ICON_BLACK
import com.ssafy.ourhome.utils.SETTING_ICON
import java.time.LocalDate

@Composable
fun QuestionScreen(navController: NavController, vm : QuestionViewModel) {

    val scrollState = rememberScrollState()

    initQuestionScreen(vm)


    Scaffold(topBar = { // TODO 세팅 아이콘 -> 채팅 아이콘
        MainAppBar(title = "질문", backIconEnable = false, icon = painterResource(id = CHATTING_ICON_BLACK), onIconClick = {
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
                Spacer(modifier = Modifier.height(16.dp))

                CenterHorizontalColumn {

                    PetSemiDetail(vm.pet){
                        navController.navigate(OurHomeScreens.PetDetailScreen.name)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    TodayQuestion(questionNumber = vm.todayQuestion.question_seq.toString(), questionContent = vm.todayQuestion.question_content)

                    Spacer(modifier = Modifier.height(16.dp))

                    ReplyQuestionButton(
                        buttonWidth = 120, buttonHeight = 40, fontSize = 20,
                        label = "답변 하기",
                        onClick = {
                            navigateQuestionDetailScreen(navController)
                        })
                }

                Spacer(modifier = Modifier.height(36.dp))

                LastQuestionHeader(){
                    navController.navigate(OurHomeScreens.QuestionListScreen.name)
                }

                QuestionLazyColumn(Modifier.height(260.dp), questionsList = vm.last3Questions)

                Spacer(modifier = Modifier.height(16.dp))            }
        }
    }

}

fun initQuestionScreen(vm: QuestionViewModel){
    vm.getFamiliyPet()
    vm.getTodayQuestion()
    vm.getLast3Questions()
}

fun navigateQuestionDetailScreen(navController: NavController){
    navController.navigate(OurHomeScreens.QuestionDetailScreen.name)
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
            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
}

/** 오늘의 질문 내용 **/
@Composable
fun TodayQuestion(questionNumber: String, questionContent: String) {
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
                append("Q" + questionNumber + ". ")
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
fun PetSemiDetail(pet: DomainFamilyPetDTO, onClick: () -> Unit = {}) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onClick.invoke()
        }, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = pet.name,
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Image(
            modifier = Modifier.size(250.dp),
            painter = rememberAsyncImagePainter(pet.image),
            contentDescription = "펫 이미지"
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Lv. " + pet.pet_level.toString(),
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
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
        Text("지난 질문", style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold))

        Text(
            "더보기",
            modifier = Modifier.clickable(onClick = onClick),
            style = MaterialTheme.typography.body2,
            color = Gray
        )
    }
}

/** 지난 질문 리스트  **/
@Composable
fun QuestionLazyColumn(modifier: Modifier = Modifier, questionsList: List<DomainQuestionDTO>) {
    LazyColumn(modifier = modifier) {
        items(questionsList) {
            QuestionItem(question = it)
        }
    }
}

/** 지난 질문 리스트 lazyColumn의 item **/
@Composable
fun QuestionItem(modifier: Modifier = Modifier.fillMaxWidth(), question: DomainQuestionDTO) {
    Card(
        modifier = modifier.padding(vertical = 8.dp), elevation = 2.dp
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
                    text = question.completed_date,
                    style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Normal, letterSpacing = 0.sp),
                    color = Gray
                )
            }

            Text(
                modifier = modifier
                    .padding(start = 12.dp, bottom = 16.dp),
                text = "Q" + question.question_seq + ". " + question.question_content,
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
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
