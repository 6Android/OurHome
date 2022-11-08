package com.ssafy.domain.usecase.user

import com.ssafy.domain.repository.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JoinEmailUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    // 이메일 회원 가입
    fun joinEmail(email: String, password: String) = userRepository.joinEmail(email, password)
}