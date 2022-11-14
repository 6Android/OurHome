package com.ssafy.domain.model.schedule

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DomainScheduleDTO(
    var uid: String = "",
    var date: String = "",
    var year: Int = 0,
    var month: Int = 0,
    var day: Int = 0,
    var title: String = "",
    var content: String = "",
    var participants: List<String> = emptyList()
) : Parcelable