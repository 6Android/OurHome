package com.ssafy.domain.usecase.user

import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.repository.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditUserContribution @Inject constructor(
    private val userRepository: UserRepository
) {
    fun execute(familyCode: String, email: String, point: Long) = userRepository.editUserContribution(familyCode, email, point)
}