package com.ssafy.domain.usecase.user

import com.ssafy.domain.repository.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFamilyUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    fun getFamilyUsers(familyCode: String) = userRepository.getFamilyUsers(familyCode)
}