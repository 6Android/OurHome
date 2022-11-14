package com.ssafy.ourhome.screens.home.schedule

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.screens.home.HomeViewModel
import com.ssafy.ourhome.utils.personList
import java.time.LocalDate

@Composable
fun ScheduleDetailScreen(navController: NavHostController, vm: HomeViewModel) {
    val titleState = remember {
        mutableStateOf(vm.scheduleDetailState.value.title)
    }
    val contentState = remember {
        mutableStateOf(vm.scheduleDetailState.value.content)
    }
    val dateState = remember {
        mutableStateOf(LocalDate.parse(vm.scheduleDetailState.value.date))
    }



    Scaffold(topBar = {
        MainAppBar(
            title = "일정 상세보기",
            backIconEnable = true,
            icon = rememberVectorPainter(Icons.Default.Delete),
            onBackClick = { navController.popBackStack() },
            onIconClick = {
                // todo: 일정 사게 버튼 클릭시

            })
    }) {
        OurHomeSurface() {
            Column(
                modifier = Modifier.padding(
                    top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp
                )
            ) {

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
                            SelectDate(dateState = dateState)

                            Spacer(modifier = Modifier.height(16.dp))

                            /** 제목 */
                            WriteTitle(titleState, false)

                            Spacer(modifier = Modifier.height(16.dp))

                            /** 내용 */
                            if (vm.scheduleDetailState.value.content.isNotBlank()) {
                                WriteContent(contentState, false)
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            /** 함께하는 가족들 헤더 */
                            Text(
                                modifier = Modifier.offset(x = 8.dp),
                                text = "함께하는 가족들",
                                style = MaterialTheme.typography.subtitle2
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        /** 함께하는 가족들 리스트 */
                        PersonList(personList = vm.scheduleDetailPeople, isEditable = false) {
                            navController.navigate(OurHomeScreens.AddMemberScreen.name)
                        }
                    }
                }
            }
        }
    }
}