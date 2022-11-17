package com.ssafy.ourhome.screens.question

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface


/** 지난 질문 목록 재사용 **/
@Composable
fun QuestionListScreen(navController: NavController, vm: QuestionViewModel) {

    LaunchedEffect(key1 = true) {
        initQuestionListScreen(vm)
    }

    Scaffold(topBar = {
        MainAppBar(title = "지난 질문", onBackClick = {
            navController.popBackStack()
        })
    }) {
        OurHomeSurface() {
            Column(modifier = Modifier.padding(horizontal = 16.dp))
            {
                //QuestionLazyColumn 에 위아래 패딩 8dp 있어서 8dp만
                Spacer(modifier = Modifier.height(8.dp))

                QuestionLazyColumn(
                    questionsList = vm.lastAllQuestions,
                    navController = navController,
                    vm = vm
                )

                Spacer(modifier = Modifier.height(8.dp))

            }
        }
    }

}

fun initQuestionListScreen(vm: QuestionViewModel) {
    vm.getLastAllQuestions()
}