package com.ssafy.domain.repository.pet

import com.google.android.gms.tasks.Task
import com.ssafy.domain.model.pet.DomainFamilyPetDTO
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow

typealias PetResponse = ResultType<DomainFamilyPetDTO?>

interface PetRepository {

    fun getFamilyPet(familyCode: String): Flow<PetResponse>

    fun updatePetExp(familyCode: String, exp: Int): Flow<ResultType<Unit>>

    fun levelUp(familyCode: String, nextPetLevel: Int): Flow<ResultType<Unit>>
}