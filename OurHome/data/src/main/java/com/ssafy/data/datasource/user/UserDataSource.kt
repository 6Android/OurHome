package com.ssafy.data.datasource.user

import com.google.firebase.firestore.Query

interface UserDataSource {
    fun getFamilyUsers(familyCode: String): Query
}