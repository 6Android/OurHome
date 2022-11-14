package com.ssafy.domain.usecase.user

import com.ssafy.domain.repository.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditManagerUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    fun execute(familyCode: String,
                myEmail: String,
                otherEmail: String) = userRepository.editManager(familyCode, myEmail, otherEmail)
}