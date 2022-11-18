package com.ssafy.ourhome.screens.question

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ssafy.domain.model.question.DomainQuestionAnswerDTO
import com.ssafy.ourhome.R
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.startLoading
import com.ssafy.ourhome.stopLoading
import com.ssafy.ourhome.ui.theme.Gray
import com.ssafy.ourhome.ui.theme.MainColor
import com.ssafy.ourhome.utils.State


@Composable
fun QuestionDetailScreen(navController: NavController, vm: QuestionViewModel) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true){
        initQuestionDetailScreen(vm)
    }
    initQuestionDetailViewModelCallback(vm, context, navController)

    Scaffold(topBar = {
        MainAppBar(title = "질문 상세",
            onBackClick = { navController.popBackStack() }
        )
    }
    ) {
        OurHomeSurface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                TodayQuestion(
                    questionNumber = vm.detailQuestion.question_seq.toString(),
                    questionContent = vm.detailQuestion.question_content
                )

                Spacer(modifier = Modifier.height(16.dp))

                MyAnswer(vm)

                Spacer(modifier = Modifier.height(24.dp))

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp), color = Color.LightGray
                )

                Spacer(modifier = Modifier.height(24.dp))

                FamilyAnswer(vm) { email ->
                    navController.navigate(OurHomeScreens.UserPageScreen.name + "/$email")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

const val FIRST_ANSWER_POINT = 100
fun initQuestionDetailViewModelCallback(
    vm: QuestionViewModel,
    context: Context,
    navController: NavController
) {
    when (vm.answerCompleteState) {
        State.ERROR -> {
            Toast.makeText(context, "답변 등록하는데 실패했습니다", Toast.LENGTH_SHORT).show()
            vm.answerCompleteState = State.DEFAULT
        }
        State.SUCCESS -> {

            Toast.makeText(context, "답변 등록 성공했습니다", Toast.LENGTH_SHORT).show()
            vm.answerCompleteState = State.DEFAULT
        }
    }

    when (vm.updateExpCompleteState) {
        State.ERROR -> {
            stopLoading()
            Toast.makeText(context, "경험치 업데이트에 실패했습니다.\n문의사항에서 건의해주세요.", Toast.LENGTH_SHORT).show()
            vm.updateExpCompleteState = State.DEFAULT
        }
        State.SUCCESS -> {
            stopLoading()
            vm.updateExpCompleteState = State.DEFAULT
            navController.popBackStack()
        }
    }
}

fun initQuestionDetailScreen(vm: QuestionViewModel) {
    vm.myAnswer.value = ""
    vm.myAnswerPoint = 0
//    vm.getFamilyUsers()
    vm.getQuestionAnswers()
    vm.getDetailQuestion()
}

/** 가족 답변 카드 **/
@Composable
fun FamilyAnswer(vm: QuestionViewModel, onClick: (String) -> (Unit)) {
    LazyColumn(
        modifier = Modifier.height(600.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(vm.familyAnswers) {
            FamilyAnswerItem(vm, it) { email ->
                onClick(email)
            }
        }
    }

}

/** 가족 답변 lazycolumn item **/
@Composable
fun FamilyAnswerItem(
    vm: QuestionViewModel,
    familyAnswers: DomainQuestionAnswerDTO,
    onClick: (String) -> (Unit)
) {

    var painter = painterResource(R.drawable.img_default_user)
    var name = "가족 정보 없음"

    if (vm.familyUsers.contains(familyAnswers.email)) {
        painter =
            if (vm.familyUsers[familyAnswers.email]!!.image == "default") painterResource(R.drawable.img_default_user)
            else rememberAsyncImagePainter(vm.familyUsers[familyAnswers.email]!!.image)
        name = vm.familyUsers[familyAnswers.email]!!.name
    }

    CenterHorizontalColumn {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable {
                        onClick(familyAnswers.email)
                    },
                contentScale = ContentScale.Crop,
                painter = painter,
                contentDescription = "가족 프로필 이미지"
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = name,
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), text = familyAnswers.content,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}

/** 내 답변 카드 **/
@Composable
fun MyAnswer(vm: QuestionViewModel) {

    CenterHorizontalColumn {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "내 답변",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.ExtraBold)
            )
            SmallRoundedButton(
                vm = vm,
                colorFlag = vm.myAnswer.value.isNotEmpty() && vm.myAnswer.value.length <= 100
            )
        }

        NoUnderLineTextInput(myAnswerState = vm.myAnswer, height = 150)

        Spacer(modifier = Modifier.height(12.dp))

        ShowTextSize(vm.myAnswer)
    }
}

/** 텍스트 길이 표시 **/
@Composable
fun ShowTextSize(myAnswerState: MutableState<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = myAnswerState.value.length.toString(),
            style = MaterialTheme.typography.caption.copy(color = Gray)
        )

        Text(
            text = "/ 100",
            style = MaterialTheme.typography.caption.copy(color = Gray)
        )
    }
}

/** 언더바 없는 TextInput **/
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NoUnderLineTextInput(myAnswerState: MutableState<String>, height: Int) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = myAnswerState.value,
        onValueChange = {
            if (it.length < 100) {
                myAnswerState.value = it
            } else {
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
fun SmallRoundedButton(vm: QuestionViewModel, colorFlag: Boolean) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .clip(
                RoundedCornerShape(CornerSize(20))
            ),
        color = if (colorFlag) MainColor else Gray
    ) {
        Column(
            modifier = Modifier
                .width(52.dp)
                .heightIn(28.dp)
                .clickable(enabled = colorFlag) {
                    if (vm.myAnswerAddedState) {
                        vm.modifyAnswer()
                        keyboardController?.hide()
                    } else {
                        vm.answer()
                        keyboardController?.hide()
                    }
                    startLoading()
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (vm.myAnswerAddedState) "수정" else "등록",
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            )
        }
    }
}

/** 회색 Rounded shape 카드, 높이 변경 시 Modifier에 height만 담아서 넘겨주기 **/
@Composable
fun GrayBorderBox(modifier: Modifier = Modifier, content: @Composable() (ColumnScope.() -> Unit)) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Gray,
                shape = RoundedCornerShape(12.dp)
            ),
        content = content
    )
}