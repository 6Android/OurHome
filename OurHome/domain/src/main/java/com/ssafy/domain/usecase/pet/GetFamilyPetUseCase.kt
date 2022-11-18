package com.ssafy.domain.usecase.pet

import com.ssafy.domain.repository.pet.PetRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFamilyPetUseCase @Inject constructor(
    private val petRepository: PetRepository
){
    fun execute(familyCode: String) = petRepository.getFamilyPet(familyCode)
}