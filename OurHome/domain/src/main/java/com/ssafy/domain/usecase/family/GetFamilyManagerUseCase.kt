package com.ssafy.domain.usecase.family

import com.ssafy.domain.model.question.DomainQuestionAnswerDTO
import com.ssafy.domain.repository.family.FamilyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFamilyManagerUseCase @Inject constructor(
    private val familiyRepository: FamilyRepository
){
    fun execute(familyCode: String) = familiyRepository.getFamilyManager(familyCode)
}