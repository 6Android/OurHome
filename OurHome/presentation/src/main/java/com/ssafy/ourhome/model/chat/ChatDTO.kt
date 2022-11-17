package com.ssafy.ourhome.model.chat

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.ssafy.ourhome.R

data class ChatDTO(
    var email: String = "",
    var name: String = "",
    var img: Painter? = null,
    var content: String = "",
    var date: String = "",
    var year: Int = 0,
    var month: Int = 0,
    var day: Int = 0,
    var hour: Int = 0,
    var minute: Int = 0,
    var mapping_date: String = ""
)
