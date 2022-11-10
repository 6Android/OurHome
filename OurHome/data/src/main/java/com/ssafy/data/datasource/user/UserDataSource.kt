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

    // user Doc (user Collection -> email Doc)
    fun getUserDoc(email: String): DocumentReference

    // 유저 정보 가져오기
    fun getProfile(familyCode: String, email: String): DocumentReference

    // 유저 정보 수정하기
    fun editProfile(familyCode: String, user: DomainUserDTO): Task<Void>

    // 현재 위치 전송하기
    fun sendLatLng(familyCode: String, email :String, latitude : Double, longitude : Double, time: Long) : Task<Void>

    // 위치 공유 동의 여부 수정하기
    fun editLocationPermission(familyCode: String, email: String, permission: Boolean): Task<Void>
}