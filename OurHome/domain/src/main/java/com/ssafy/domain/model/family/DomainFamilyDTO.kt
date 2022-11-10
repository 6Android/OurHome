package com.ssafy.domain.model.family

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DomainFamilyDTO(
    var family_code: String = "",
) : Parcelable