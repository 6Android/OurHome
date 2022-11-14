package com.ssafy.domain.usecase.user

import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.repository.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransferUserDataUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    fun execute(user: DomainUserDTO) = userRepository.transferUserData(user)
}