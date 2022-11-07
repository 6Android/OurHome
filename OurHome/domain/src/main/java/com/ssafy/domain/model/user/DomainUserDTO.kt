package com.ssafy.domain.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DomainUserDTO(
    val birthday: String = "",
    val blood_type: String = "",
    val contribute_point: Long = 0L,
    val email: String = "",
    val family_code: String = "",
    val hobby: String = "",
    val image: String = "",
    val interest: String = "",
    val job: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val location_permit: Boolean = false,
    val location_updated: Long = 0L,
    val manager: Boolean = false,
    val mbti: String = "",
    val name: String = "",
    val phone: String = "",
    val role: String = ""
): Parcelable
