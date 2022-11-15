package com.ssafy.domain.model.chat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DomainChatDTO (
    var email: String = "",
    var content: String = "",
    var date: String = "",
    var year: Int = 0,
    var month: Int = 0,
    var day: Int = 0,
    var hour: Int = 0,
    var minute: Int = 0,
    var mapping_date: String = ""
        ) : Parcelable