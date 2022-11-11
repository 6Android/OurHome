package com.ssafy.domain.repository.pet

import com.ssafy.domain.model.pet.DomainFamilyPetDTO
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow

typealias PetResponse = ResultType<DomainFamilyPetDTO?>

interface PetRepository {

    fun getFamilyPet(familyCode: String): Flow<PetResponse>

}