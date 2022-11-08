package com.ssafy.data.datasource.user

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query

interface UserDataSource {
    fun getFamilyUsers(familyCode: String): Query
    fun getMyProfile(familyCode: String, email: String): DocumentReference
}