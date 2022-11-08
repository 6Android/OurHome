package com.ssafy.data.datasource.user

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query

interface UserDataSource {
    fun getFamilyUsers(familyCode: String): Query

    // 이메일 회원 가입
    fun joinEmail(email: String, password: String): Task<AuthResult>

    // 이메일 중복 검사
    fun checkEmail(email: String): DocumentReference
}