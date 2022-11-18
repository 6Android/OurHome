package com.ssafy.domain.usecase.user

import com.ssafy.domain.repository.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignInEmailUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    // 이메일 로그인
    fun execute(email: String, password: String) = userRepository.signInEmail(email, password)
}