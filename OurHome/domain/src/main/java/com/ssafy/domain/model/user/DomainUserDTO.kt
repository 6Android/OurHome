package com.ssafy.domain.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DomainUserDTO(
    var birthday: String = "2000-01-01",
    var blood_type: String = "Rh+ A",
    var contribute_point: Long = 0L,
    var email: String = "",
    var family_code: String = "",
    var hobby: String = "취미",
    var image: String = "default",
    var interest: String = "관심사",
    var job: String = "직업",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var location_permit: Boolean = false,
    var location_updated: Long = 0L,
    var manager: Boolean = false,
    var mbti: String = "MBTI",
    var name: String = "",
    var phone: String = "010-1234-5678",
    var role: String = ""
): Parcelable
