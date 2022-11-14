package com.ssafy.domain.usecase.user

import com.google.firebase.firestore.FirebaseFirestore
import com.ssafy.domain.model.user.DomainUserDTO
import javax.inject.Inject
import javax.inject.Singleton

/** 테스트 코드 **/

@Singleton
class InsertUserUseCase @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun execute(userDTO: DomainUserDTO)
    = firestore.collection("family").document("TEST").collection("user").document(userDTO.email).set(userDTO)


}