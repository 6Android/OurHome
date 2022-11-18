package com.ssafy.domain.model.question

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DomainQuestionAnswerDTO(
    var email: String = "",
    var content: String = "",
    var date: String = "",
    var year: Int = 0,
    var month: Int = 0,
    var day: Int = 0
) : Parcelable