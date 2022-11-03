package com.ssafy.ourhome.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.ssafy.ourhome.R
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.ui.theme.OurHomeTheme
import java.time.YearMonth


data class Person(
    val imgUrl: String, val name: String
)

@Composable
fun HomeScreen(navController: NavController) {

    val scrollState = rememberScrollState()

    /** 더미 데이터 */
    val url =
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQvGknHPBUegusBb26NtSy4y47-yxr2Q_-Hwg&usqp=CAU"
    val person = Person(url, "아이유")
    val personList = arrayListOf(person, person, person, person)
    /** 더미 데이터 */

    Scaffold(topBar = {

        /** 상단 바 */
        TopAppBar(
            modifier = Modifier
                .height(200.dp)
                .shadow(
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                    elevation = 4.dp
                ),
        ) {
            Column() {

                /** 홈 툴바 */
                HomeToolBar {
                    // todo: 채팅 아이콘 클릭

                }

                Spacer(modifier = Modifier.height(32.dp))

                /** 구성원 리스트 */
                PersonList(personList)
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }) {
        OurHomeSurface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    /** 가족위치 카드 */
                    HomeCard(
                        modifier = Modifier.weight(1f),
                        title = "가족위치",
                        content = "지도에서 가족위치를 확인하세요!",
                        image = painterResource(
                            id = R.drawable.ic_map
                        )
                    ) {
                        // todo: 가족위치 클릭
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    /** 초대하기 카드 */
                    HomeCard(
                        modifier = Modifier.weight(1f),
                        title = "초대하기",
                        content = "우리집에 가족을 초대해보세요!",
                        image = painterResource(
                            id = R.drawable.ic_invite
                        )
                    ) {
                        // todo: 초대하기 클릭
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                /** 달력 카드 */
                CalendarCard(onAddScheduleClick = {
                    // todo: 일정 추가 버튼 클릭
                })

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/** 홈 툴바 */
@Composable
private fun HomeToolBar(
    onChatClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        /** 로고 */
        Row(verticalAlignment = Alignment.CenterVertically) {

            /** 로고 아이콘 */
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.Home,
                tint = Color.White,
                contentDescription = "icon home"
            )
            Spacer(modifier = Modifier.width(8.dp))

            /** 앱 이름 */
            Text(
                modifier = Modifier.offset(y = (-2).dp),
                text = "우리집",
                style = MaterialTheme.typography.subtitle1.copy(color = Color.White)
            )
        }

        /** 채팅 아이콘 */
        Icon(
            modifier = Modifier
                .size(32.dp)
                .clickable { onChatClick() },
            painter = painterResource(id = R.drawable.ic_chat_white),
            contentDescription = "icon chat",
            tint = Color.White
        )
    }
}

/** 구성원 리스트 */
@Composable
private fun PersonList(personList: ArrayList<Person>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 12.dp)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = personList, itemContent = { item ->
                /** 구성원 리스트 아이템 */
                PersonListItem(item)
            })
        }
    }
}

/** 구성원 리스트 아이템 */
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
                color = Color.White
            )
        )
    }
}


/** 상단 카드 */
@Composable
fun HomeCard(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    image: Painter,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() },
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            /** 헤더 */
            Text(text = title, style = MaterialTheme.typography.subtitle2)
            Spacer(modifier = Modifier.height(12.dp))

            /** 내용 */
            Text(
                text = content,
                style = MaterialTheme.typography.body2.copy(
                    lineHeight = 20.sp,
                    color = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            /** 아이콘 */
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    modifier = Modifier.size(40.dp),
                    painter = image,
                    contentDescription = "logo"
                )
            }
        }
    }
}

/** 달력 카드 */
@Composable
fun CalendarCard(
    modifier: Modifier = Modifier,
    onAddScheduleClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                /** 헤더 */
                Text(text = "공유 일정", style = MaterialTheme.typography.subtitle2)

                /** 일정 추가 아이콘 */
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onAddScheduleClick() },
                    imageVector = Icons.Default.Add, contentDescription = "일정 추가"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            /** 달력 */
            Calendar()

            /**
            todo
            월화수목 금토일 넣기
            오늘 날짜 표시하기 (2022년 10월)
            < > 버튼 누르면 달력 이동
            날짜 누르면 바텀sheet으로 해당하는 날짜의 일정 뜸
            이번달 일 색만 검은색 나머지는 회색
             */
        }
    }
}

/** 달력 */
@Composable
fun Calendar() {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    HorizontalCalendar(
        state = state,
        dayContent = { Day(it) }
    )

//    If you need a vertical calendar.
//    VerticalCalendar(
//        state = state,
//        dayContent = { Day(it) }
//    )
}

/** 일 */
@Composable
fun Day(day: CalendarDay) {
    Box(
        modifier = Modifier
            .aspectRatio(1f), // This is important for square sizing!
        contentAlignment = Alignment.Center
    ) {
        Text(text = day.date.dayOfMonth.toString(), style = MaterialTheme.typography.body2)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    OurHomeTheme {
        HomeScreen(navController = NavController(LocalContext.current))
    }
}