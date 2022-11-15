package com.ssafy.ourhome.model.chat

data class ChatDTO(
    var email: String = "",
    var name: String = "",
    var img: String = "",
    var content: String = "",
    var date: String = "",
    var year: Int = 0,
    var month: Int = 0,
    var day: Int = 0,
    var hour: Int = 0,
    var minute: Int = 0,
    var mapping_date: String = ""
)
