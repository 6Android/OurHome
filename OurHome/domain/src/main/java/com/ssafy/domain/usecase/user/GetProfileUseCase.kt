package com.ssafy.domain.usecase.user

import com.ssafy.domain.repository.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    fun execute(familyCode: String, email: String) = userRepository.getProfile(familyCode, email)
}