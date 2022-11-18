package com.ssafy.domain.model.question

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DomainQuestionDTO(
    var question_content : String = "",
    var question_seq: Int = 1,
    var completed_date: String = "",
    var completed_year: Int = 0,
    var completed_month: Int = 0,
    var completed_day: Int = 0
) : Parcelable