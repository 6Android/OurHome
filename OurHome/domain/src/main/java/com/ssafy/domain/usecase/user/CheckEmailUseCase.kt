package com.ssafy.domain.usecase.user

import com.ssafy.domain.repository.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckEmailUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    fun execute(email: String) = userRepository.checkEmail(email)
}