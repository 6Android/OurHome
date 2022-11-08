package com.ssafy.domain.usecase.user

import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.repository.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    fun execute(familyCode: String, user: DomainUserDTO) = userRepository.editProfile(familyCode, user)
}