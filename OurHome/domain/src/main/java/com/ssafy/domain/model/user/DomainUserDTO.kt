package com.ssafy.domain.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DomainUserDTO(
    var birthday: String = "2000.01.01",
    var blood_type: String = "RH+ A",
    var contribute_point: Long = 0L,
    var email: String = "",
    var family_code: String = "",
    var hobby: String = "",
    var image: String = "default",
    var interest: String = "",
    var job: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var location_permit: Boolean = false,
    var location_updated: Long = 0L,
    var manager: Boolean = false,
    var mbti: String = "",
    var name: String = "",
    var phone: String = "",
    var role: String = ""
): Parcelable
