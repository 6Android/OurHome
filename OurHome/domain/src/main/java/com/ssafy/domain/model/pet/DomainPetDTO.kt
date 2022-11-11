package com.ssafy.domain.model.pet

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DomainFamilyPetDTO(
    var pet_level: Int = 1,
    var image: String = "https://i.pinimg.com/222x/36/30/f7/3630f7d930f91e495d93c02833b4abfc.jpg",
    var name: String = "이름을 정해주세요",
    var exp: Int = 0,
    var next_level_exp: Int = 100,
    var description: String = "고라파덕은 물 속성 포켓몬이다.\n골덕으로 진화한다."
) : Parcelable