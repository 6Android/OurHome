package com.ssafy.ourhome.utils

import java.time.YearMonth

data class Schedule(
    val date: String,
    val title: String,
    val content: String,
)

fun getSchedules(yearMonth: YearMonth): List<Schedule> {
    when (yearMonth.month.value) {
        11 -> return listOf(
            Schedule(
                date = "2022-11-3",
                title = "외식",
                content = ""
            ),
            Schedule(
                date = "2022-11-3",
                title = "가족사진",
                content = "가족사진 촬영하기"
            ),
            Schedule(
                date = "2022-11-3",
                title = "가족사진",
                content = "가족사진 촬영하기"
            ),
            Schedule(
                date = "2022-11-3",
                title = "가족사진",
                content = "가족사진 촬영하기"
            ),
            Schedule(
                date = "2022-11-3",
                title = "가족사진",
                content = "가족사진 촬영하기"
            ),
            Schedule(
                date = "2022-11-3",
                title = "가족사진",
                content = "가족사진 촬영하기"
            ),
            Schedule(
                date = "2022-11-3",
                title = "가족사진",
                content = "가족사진 촬영하기"
            ),
            Schedule(
                date = "2022-11-3",
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

/** 일정 추가에서 사용하는 프로필 */
data class Person(
    val id: Int, val imgUrl: String, val name: String, var checked: Boolean = false
)

val personList = arrayListOf(
    Person(
        1,
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRxZScLkNntCccfde87I4ZcUM45MzxUb9FcmA&usqp=CAU",
        "유지니"
    ),
    Person(
        2,
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQvGknHPBUegusBb26NtSy4y47-yxr2Q_-Hwg&usqp=CAU",
        "아이유"
    ),
    Person(
        3,
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRxZScLkNntCccfde87I4ZcUM45MzxUb9FcmA&usqp=CAU",
        "유지니"
    ),
    Person(
        4,
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQvGknHPBUegusBb26NtSy4y47-yxr2Q_-Hwg&usqp=CAU",
        "아이유"
    ),
    Person(
        5,
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRxZScLkNntCccfde87I4ZcUM45MzxUb9FcmA&usqp=CAU",
        "유지니"
    ),
    Person(
        6,
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQvGknHPBUegusBb26NtSy4y47-yxr2Q_-Hwg&usqp=CAU",
        "아이유"
    )
)