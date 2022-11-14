package com.ssafy.ourhome.model.schedule

import com.ssafy.domain.model.user.DomainUserDTO

data class ParticipantDTO(
    var name: String = "",
    var email: String = "",
    var image: String = "",
    var checked: Boolean = false
) {
    fun toDomainUserDTO() : DomainUserDTO =
        DomainUserDTO(
            name = name,
            email = email,
            image = image
        )
}
