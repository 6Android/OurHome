package com.ssafy.domain.usecase.user

import com.ssafy.domain.model.family.DomainFamilyDTO
import com.ssafy.domain.repository.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertFamilyUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    // 이메일 회원 가입
    fun execute(familyCode: String, familyDTO: DomainFamilyDTO, map: Map<String, Any>) =
        userRepository.insetFamily(familyCode, familyDTO, map)
}