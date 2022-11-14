package com.ssafy.ourhome.screens.home.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.squaredem.composecalendar.ComposeCalendar
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.ui.theme.OurHomeTheme
import com.ssafy.ourhome.utils.personList
import java.time.LocalDate

@Composable
fun AddScheduleScreen(navController: NavController) {
    val showDialog = rememberSaveable { mutableStateOf(false) }
    val titleState = remember {
        mutableStateOf("")
    }
    val contentState = remember {
        mutableStateOf("")
    }
    val dateState = remember {
        mutableStateOf(LocalDate.now())
    }

    Scaffold(topBar = {
        MainAppBar(
            title = "일정 추가",
            backIconEnable = true,
            icon = rememberVectorPainter(Icons.Default.Check),
            onBackClick = { navController.popBackStack() },
            onIconClick = {
                // todo: 일정 추가 버튼 클릭시

            })
    }) {
        OurHomeSurface() {
            Column(
                modifier = Modifier.padding(
                    top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp
                )
            ) {
                /** 일정 추가 헤더 */
                Text(
                    modifier = Modifier.offset(x = 12.dp),
                    text = "가족과 함께하는\n일정을 추가해보세요!",
                    style = MaterialTheme.typography.subtitle1
                )

                Spacer(modifier = Modifier.height(16.dp))

                /** 일정 등록 카드 */
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
                ) {
                    Column {

                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {

                            /** 날짜 */
                            SelectDate(dateState) {
                                showDialog.value = true
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            /** 제목 */
                            WriteTitle(titleState)

                            Spacer(modifier = Modifier.height(16.dp))

                            /** 내용 */
                            WriteContent(contentState)

                            Spacer(modifier = Modifier.height(16.dp))

                            /** 함께하는 가족들 헤더 */
                            Text(
                                modifier = Modifier.offset(x = 8.dp),
                                text = "함께하는 가족들",
                                style = MaterialTheme.typography.subtitle2
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

//                        /** 함께하는 가족들 리스트 */
//                        PersonList(personList = personList) {
//                            navController.navigate(OurHomeScreens.AddMemberScreen.name)
//                        }
                    }
                }

                /** 날짜 선택 다이얼로그 */
                if (showDialog.value) {
                    ComposeCalendar(
                        onDone = { it: LocalDate ->
                            // Hide dialog
                            dateState.value = it
                            showDialog.value = false
                            // Do something with the date
                        },
                        onDismiss = {
                            // Hide dialog
                            showDialog.value = false
                        }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PrevAddScheduleScreen() {
    OurHomeTheme {
        AddScheduleScreen(navController = NavController(LocalContext.current))
    }
}