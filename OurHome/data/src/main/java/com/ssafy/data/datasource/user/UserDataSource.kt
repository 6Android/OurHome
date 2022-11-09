package com.ssafy.data.datasource.user

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.ssafy.domain.model.user.DomainUserDTO

interface UserDataSource {
    fun getFamilyUsers(familyCode: String): Query

    // 이메일 회원 가입
    fun joinEmail(email: String, password: String): Task<AuthResult>

    // 이메일 로그인
    fun signInEmail(email: String, password: String): Task<AuthResult>

    // 이메일 중복 검사
    fun checkEmail(email: String): Task<DocumentSnapshot>

    // 유저 Document 등록
    fun insertUser(user: DomainUserDTO): Task<Void>

    // 유저 Document 가져오기
    fun getUser(email: String): Task<DocumentSnapshot>

    fun getProfile(familyCode: String, email: String): DocumentReference

    fun editProfile(familyCode: String, user: DomainUserDTO): Task<Void>

}