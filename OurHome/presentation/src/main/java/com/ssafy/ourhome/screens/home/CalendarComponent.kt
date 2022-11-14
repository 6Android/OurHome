package com.ssafy.ourhome.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.*
import com.ssafy.domain.model.schedule.DomainScheduleDTO
import com.ssafy.ourhome.R
import com.ssafy.ourhome.ui.theme.Gray
import com.ssafy.ourhome.ui.theme.Green
import com.ssafy.ourhome.ui.theme.MainColor
import com.ssafy.ourhome.ui.theme.nanum
import com.ssafy.ourhome.utils.displayText
import com.ssafy.ourhome.utils.rememberFirstVisibleMonthAfterScroll
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

/** 오늘 일정 리스트  **/
@Composable
fun TodayScheduleList(
    modifier: Modifier = Modifier,
    list: List<DomainScheduleDTO>,
    onScheduleClick: (DomainScheduleDTO) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        items(list) { item ->
            TodayScheduleListItem(schedule = item, onScheduleClick = onScheduleClick)
        }
    }
}

/** 오늘 일정 리스트 아이템 **/
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TodayScheduleListItem(
    modifier: Modifier = Modifier.fillMaxWidth(),
    schedule: DomainScheduleDTO,
    onScheduleClick: (DomainScheduleDTO) -> Unit
) {
    Card(
        modifier = modifier
            .padding(vertical = 8.dp)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = modifier.clickable {
                // todo: 일정 상세로 이동
                onScheduleClick(schedule)
            }
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
                    text = schedule.date,
                    fontFamily = nanum,
                    color = Gray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
            }

            Text(
                modifier = modifier
                    .padding(start = 12.dp, bottom = 16.dp),
                text = schedule.title,
                fontFamily = nanum,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }

}

/** 달력 카드 */
@Composable
fun CalendarCard(
    modifier: Modifier = Modifier,
    visibleBottomSheetState: MutableState<Boolean>,
    selection: MutableState<CalendarDay?>,
    map: MutableMap<String, List<DomainScheduleDTO>>,
    onMonthChange: (String) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(430.dp)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            /** 달력 */
            /** 달력 */
            /** 달력 */
            /** 달력 */
            Calendar(
                visibleBottomSheetState = visibleBottomSheetState,
                selection = selection,
                map = map,
                onMonthChange = onMonthChange
            )
        }
    }
}

/** 달력 */
@SuppressLint("UnrememberedMutableState")
@Composable
fun Calendar(
    visibleBottomSheetState: MutableState<Boolean>,
    selection: MutableState<CalendarDay?>,
    map: MutableMap<String, List<DomainScheduleDTO>>,
    onMonthChange: (String) -> Unit
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(500) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(500) } // Adjust as needed
    val daysOfWeek = remember { daysOfWeek() }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )
    val coroutineScope = rememberCoroutineScope()
    val visibleMonth = rememberFirstVisibleMonthAfterScroll(state = state, onChange = onMonthChange)

//    visibleMonth.let {
//        // 같은 날짜로 묶기
//        map.putAll(getSchedules(it).groupBy { schedule -> schedule.date })
//    }

    CalendarTitle(
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
        currentMonth = visibleMonth,
        goToPrevious = {
            coroutineScope.launch {
                state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
            }
        },
        goToNext = {
            coroutineScope.launch {
                state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
            }
        }
    )
    Spacer(modifier = Modifier.height(24.dp))
    HorizontalCalendar(
        state = state,
        dayContent = { day ->
            Day(
                day = day,
                isSelected = selection.value == day,
                hasSchedule = map.containsKey("${day.date.year}-${day.date.monthValue}-${day.date.dayOfMonth}"),
            ) { clickedDay ->
                selection.value = clickedDay
                visibleBottomSheetState.value = true
            }
        },
        monthHeader = {
            DaysOfWeekTitle(daysOfWeek = daysOfWeek)
        }
    )
}

/** 현재 년, 월, 이전, 다음 버튼 */
@Composable
fun CalendarTitle(
    modifier: Modifier,
    currentMonth: YearMonth,
    goToPrevious: () -> Unit,
    goToNext: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(shape = CircleShape)
                .clickable(role = Role.Button, onClick = goToPrevious)
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .align(Alignment.Center),
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Previous",
                tint = Color.Gray
            )
        }
        Text(
            modifier = Modifier
                .weight(1f)
                .testTag("MonthTitle"),
            text = currentMonth.displayText(),
            style = MaterialTheme.typography.subtitle2,
            textAlign = TextAlign.Center,
        )
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(shape = CircleShape)
                .clickable(role = Role.Button, onClick = goToNext)
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .align(Alignment.Center),
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Next",
                tint = Color.Gray
            )
        }
    }
}

/** 요일 타이틀 표시 */
@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/** 일 */
@Composable
fun Day(
    day: CalendarDay,
    isSelected: Boolean,
    hasSchedule: Boolean,
    onClick: (CalendarDay) -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(color = if (isSelected) MainColor else Color.Transparent)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) }
            ),
        contentAlignment = Alignment.Center
    ) {
        val textColor = when (day.position) {
            DayPosition.MonthDate ->
                if (isSelected) {
                    Color.White
                } else {
                    Color.Black
                }
            else -> Color.LightGray
        }
        Text(
            text = day.date.dayOfMonth.toString(),
            style = MaterialTheme.typography.body2,
            color = textColor
        )
        if (hasSchedule) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(shape = CircleShape)
                    .background(Green)
                    .align(alignment = Alignment.BottomCenter)
            ) { }
        }
    }
}