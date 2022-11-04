package com.ssafy.ourhome.screens.home.schedule

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.squaredem.composecalendar.ComposeCalendar
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.components.TextInput
import java.time.LocalDate

/** 더미 데이터 */
data class Person(
    val imgUrl: String, val name: String
)

@Composable
fun AddScheduleScreen(navController: NavController) {

    /** 더미 데이터 */
    val url =
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRxZScLkNntCccfde87I4ZcUM45MzxUb9FcmA&usqp=CAU"
    val person = Person(url, "유지니")
    val personList = arrayListOf(person, person, person)

    /** 더미 데이터 */

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
            onIconClick = {
                // todo: 일정 추가 버튼 클릭시
            })
    }) {
        OurHomeSurface() {
            Column(modifier = Modifier.padding(24.dp)) {

                /** 일정 추가 헤더 */
                Text(text = "가족과 함께하는\n일정을 추가해보세요!", style = MaterialTheme.typography.subtitle1)

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        /** 날짜 */
                        Column(
                        ) {
                            Text(text = "날짜", style = MaterialTheme.typography.subtitle2)
                            Button(
                                border = BorderStroke(width = 2.dp, color = Color.LightGray),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                                elevation = ButtonDefaults.elevation(0.dp),
                                onClick = { showDialog.value = true }) {
                                Text(
                                    text = "${dateState.value}",
                                    style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }


                        Spacer(modifier = Modifier.height(16.dp))

                        /** 제목 */
                        Column(
                        ) {
                            Text(text = "제목", style = MaterialTheme.typography.subtitle2)
                            TextInput(
                                valueState = titleState,
                                labelId = "제목을 입력해주세요.",
                                enabled = true
                            )
                        }


                        Spacer(modifier = Modifier.height(16.dp))

                        /** 내용 */
                        Text(text = "내용", style = MaterialTheme.typography.subtitle2)
                        TextInput(
                            valueState = contentState,
                            labelId = "간단한 내용을 작성해주세요.",
                            enabled = true
                        )

//                Spacer(modifier = Modifier.height(32.dp))
//                Divider(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(4.dp), color = Color.Gray
//                )

                        Spacer(modifier = Modifier.height(16.dp))

                        /** 함께하는 가족들 */
                        Text(text = "함께하는 가족들", style = MaterialTheme.typography.subtitle2)

                        Spacer(modifier = Modifier.height(8.dp))

                        PersonList(personList = personList)
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

/** 함께하는 가족들 리스트 */
@Composable
private fun PersonList(personList: ArrayList<Person>) {

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 12.dp)
    ) {
        Row(
        ) {
            LazyRow(
                modifier = Modifier
                    .layout() { measurable, constraints ->
                        val placeable = measurable.measure(
                            constraints.copy(
                                maxWidth = constraints.maxWidth + 52.dp.roundToPx(), //add the end padding 16.dp
                            )
                        )
                        layout(placeable.width, placeable.height) {
                            placeable.place(18.dp.roundToPx(), 0)
                        }
                    }
                    .offset(x = (-18).dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.width(4.dp))
                }
                items(items = personList, itemContent = { item ->

                    /** 구성원 리스트 아이템 */
                    PersonListItem(item)
                })

                item {
                    /** 가족 추가 버튼 */
                    Image(
                        modifier = Modifier
                            .size(64.dp)
                            .border(width = 1.dp, color = Color.Black, shape = CircleShape)
                            .clip(CircleShape)
                            .clickable {
                                // todo: 가족 구성원 추가 화면으로 이동
                            }
                            .padding(12.dp),
                        contentScale = ContentScale.Crop,
                        painter = rememberVectorPainter(Icons.Default.Add),
                        contentDescription = "Profile Image"
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    }
}

/** 함께하는 가족들 리스트 아이템 */
@Composable
private fun PersonListItem(item: Person) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        /** 프사 */
        Image(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            painter = rememberAsyncImagePainter(item.imgUrl),
            contentDescription = "Profile Image"
        )

        Spacer(modifier = Modifier.height(12.dp))

        /** 이름 */
        Text(
            text = item.name,
            style = MaterialTheme.typography.caption.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )
    }

    /** 가족 구성원 삭제 버튼 */
    Box() {
        Image(
            modifier = Modifier
                .size(12.dp)
                .border(width = 1.dp, color = Color.Black, shape = CircleShape)
                .clip(CircleShape)
                .clickable {
                    // todo: 가족 구성원 추가 화면으로 이동
                },
            contentScale = ContentScale.Crop,
            painter = rememberVectorPainter(Icons.Default.Close),
            contentDescription = "Profile Image"
        )
    }
}