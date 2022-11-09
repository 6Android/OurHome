package com.ssafy.domain.repository.user

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

    fun getProfile(familyCode: String, email: String): Flow<UserResponse>

    fun editProfile(familyCode: String, user: DomainUserDTO): Flow<ResultType<Unit>>
}