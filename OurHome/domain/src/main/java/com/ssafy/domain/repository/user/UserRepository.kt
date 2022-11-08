package com.ssafy.domain.repository.user

import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow

typealias Users = List<DomainUserDTO>
typealias UsersResponse = ResultType<Users>

interface UserRepository {
    fun getFamilyUsers(familyCode: String): Flow<UsersResponse>

    // 이메일 회원 가입
    fun joinEmail(email: String, password: String, nickname: String): Flow<ResultType<Unit>>

    // 이메일 중복 검사
    fun checkEmail(email: String): Flow<ResultType<Unit>>
}