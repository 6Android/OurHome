package com.ssafy.domain.usecase.user

import android.net.Uri
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.repository.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    fun execute(imageUri: Uri, user: DomainUserDTO) = userRepository.editUserProfile(imageUri, user)
}