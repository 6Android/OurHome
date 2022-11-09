package com.ssafy.data.datasource.user

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ssafy.data.utils.FAMILY
import com.ssafy.data.utils.USER
import com.ssafy.domain.model.user.DomainUserDTO
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val fireStore : FirebaseFirestore
): UserDataSource {
    override fun getFamilyUsers(familyCode: String)
        = fireStore.collection(FAMILY).document(familyCode).collection(USER)

    override fun getProfile(familyCode: String, email: String)
        = fireStore.collection(FAMILY).document(familyCode).collection(USER).document(email)

    override fun editProfile(familyCode: String, user: DomainUserDTO): Task<Void>
        = fireStore.collection(FAMILY).document(familyCode).collection(USER).document(user.email).set(user)
}