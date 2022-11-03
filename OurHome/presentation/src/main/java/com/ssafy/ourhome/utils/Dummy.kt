package com.ssafy.ourhome.utils

import java.time.YearMonth

data class Schedule(
    val date: String,
    val title: String,
    val content: String,
)

fun getSchedules(yearMonth: YearMonth) : List<Schedule>  {
    when(yearMonth.month.value) {
        11 -> return listOf(
            Schedule(
                date = "2022-11-03",
                title = "외식",
                content = ""
            ),
            Schedule(
                date = "2022-11-03",
                title = "가족사진",
                content = "가족사진 촬영하기"
            ),
            Schedule(
                date = "2022-11-15",
                title = "산책",
                content = ""
            ),
            Schedule(
                date = "2022-11-21",
                title = "가족여행",
                content = "일본으로 가족 여행 출발"
            )
        )
        10 -> return listOf(
            Schedule(
                date = "2022-10-28",
                title = "코딩테스트",
                content = "싸피 코딩테스트"
            )
        )
        else -> return emptyList()
    }
}