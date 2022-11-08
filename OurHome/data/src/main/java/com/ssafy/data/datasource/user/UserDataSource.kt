package com.ssafy.data.datasource.user

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.ssafy.domain.model.user.DomainUserDTO

interface UserDataSource {
    fun getFamilyUsers(familyCode: String): Query
    fun getProfile(familyCode: String, email: String): DocumentReference
    fun editProfile(familyCode: String, user: DomainUserDTO): Task<Void>
}