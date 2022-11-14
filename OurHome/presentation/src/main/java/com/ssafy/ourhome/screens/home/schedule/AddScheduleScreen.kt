package com.ssafy.ourhome.screens.home.schedule

import android.widget.Toast
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.squaredem.composecalendar.ComposeCalendar
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.navigation.OurHomeScreens
import com.ssafy.ourhome.screens.home.HomeViewModel
import com.ssafy.ourhome.utils.State
import java.time.LocalDate

@Composable
fun AddScheduleScreen(navController: NavController, vm: HomeViewModel) {
    val context = LocalContext.current
    val showDialog = rememberSaveable { mutableStateOf(false) }

    when (vm.addScheduleProcessState.value) {
        State.SUCCESS -> {
            Toast.makeText(context, "일정을 등록했습니다", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
            vm.addScheduleProcessState.value = State.DEFAULT
        }
        State.FAIL -> {
            Toast.makeText(context, "일정 등록 실패했습니다", Toast.LENGTH_SHORT).show()
            vm.addScheduleProcessState.value = State.DEFAULT
        }
    }

    Scaffold(topBar = {
        MainAppBar(
            title = "일정 추가",
            backIconEnable = true,
            icon = rememberVectorPainter(Icons.Default.Check),
            onBackClick = { navController.popBackStack() },
            onIconClick = {
                // todo: 일정 추가 버튼 클릭시
                if (vm.addScheduleTitleState.value.isBlank()) {
                    Toast.makeText(context, "일정 제목을 입력해주세요", Toast.LENGTH_SHORT).show()
                    return@MainAppBar
                }
                vm.addSchedule()
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
                            SelectDate(vm.addScheduleDateState) {
                                showDialog.value = true
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            /** 제목 */
                            WriteTitle(vm.addScheduleTitleState)

                            Spacer(modifier = Modifier.height(16.dp))

                            /** 내용 */
                            WriteContent(vm.addScheduleContentState)

                            Spacer(modifier = Modifier.height(16.dp))

                            /** 함께하는 가족들 헤더 */
                            Text(
                                modifier = Modifier.offset(x = 8.dp),
                                text = "함께하는 가족들",
                                style = MaterialTheme.typography.subtitle2
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        /** 함께하는 가족들 리스트 */
                        PersonList(
                            personList = vm.addScheduleParticipantsState
                                .filter { it.checked }
                                .map { it.toDomainUserDTO() })
                        {
                            navController.navigate(OurHomeScreens.AddMemberScreen.name)
                        }
                    }
                }

                /** 날짜 선택 다이얼로그 */
                if (showDialog.value) {
                    ComposeCalendar(
                        onDone = { it: LocalDate ->
                            // Hide dialog
                            vm.addScheduleDateState.value = it
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