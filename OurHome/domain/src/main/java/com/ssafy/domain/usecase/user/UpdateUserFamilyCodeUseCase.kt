package com.ssafy.domain.usecase.user

import com.ssafy.domain.repository.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateUserFamilyCodeUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    // 유저 정보 업데이트
    fun execute(map: Map<String, Any>) = userRepository.updateUserFamilyCode(map)
}