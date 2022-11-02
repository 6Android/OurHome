package com.ssafy.ourhome.screens.question

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface


/** 지난 질문 목록 재사용 **/
@Composable
fun QuestionListScreen(navController: NavController){
    Scaffold(topBar = {
        MainAppBar(title = "지난 질문", onBackClick = {
            navController.popBackStack()
        })
    }) {
        OurHomeSurface() {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                QuestionLazyColumn(size = 30)
            }
        }
    }

}