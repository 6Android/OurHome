package com.ssafy.data.datasource.user

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ssafy.data.utils.FAMILY
import com.ssafy.data.utils.USER
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val fireStore : FirebaseFirestore
): UserDataSource {
    override fun getFamilyUsers(familyCode: String)
        = fireStore.collection(FAMILY).document(familyCode).collection(USER)

    override fun getMyProfile(familyCode: String, email: String)
        = fireStore.collection(FAMILY).document(familyCode).collection(USER).document(email)
}