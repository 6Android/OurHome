package com.ssafy.domain.repository.user

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.ssafy.domain.model.family.DomainFamilyDTO
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow

typealias Users = List<DomainUserDTO>
typealias UsersResponse = ResultType<Users>

typealias UserResponse = ResultType<DomainUserDTO>

interface UserRepository {
    fun getFamilyUsers(familyCode: String): Flow<UsersResponse>

    // 이메일 회원 가입
    fun joinEmail(email: String, password: String, nickname: String): Flow<ResultType<Unit>>

    // 소셜 회원 가입
    fun joinSocial(email: String, nickname: String): Flow<ResultType<Unit>>

    // 이메일 로그인
    fun signInEmail(email: String, password: String): Flow<UserResponse>

    // 이메일 중복 검사
    fun checkEmail(email: String): Flow<ResultType<Unit>>

    // 유저 정보 가져오기
    fun getUser(email: String): Flow<UserResponse>

    // 가족방 생성시
    // 유저 정보(familyCode, manager)업데이트
    fun updateUserFamilyCode(map: Map<String, Any>): Flow<ResultType<Unit>>

    // 가족방 생성
    fun insetFamily(familyCode: String, familyDTO: DomainFamilyDTO): Flow<ResultType<Unit>>

    // 유저 정보 가져오기
    fun getProfile(familyCode: String, email: String): Flow<UserResponse>

    // 유저 정보 수정하기
    fun editProfile(familyCode: String, user: DomainUserDTO): Flow<ResultType<Unit>>

    // 현재 위치 전송하기
    fun sendLatLng(
        familyCode: String,
        email: String,
        latitude: Double,
        longitude: Double,
        time: Long
    ): Flow<ResultType<Unit>>

    // 위치 공유 동의 여부 수정하기
    fun editLocationPermission(familyCode: String, email: String, permission: Boolean): Flow<ResultType<Unit>>
}