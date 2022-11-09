package com.ssafy.domain.repository.user

import com.google.android.gms.tasks.Task
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow

typealias Users = List<DomainUserDTO>
typealias UsersResponse = ResultType<Users>

typealias UserResponse = ResultType<DomainUserDTO>

interface UserRepository {
    fun getFamilyUsers(familyCode: String): Flow<UsersResponse>
    fun getProfile(familyCode: String, email: String): Flow<UserResponse>
    fun editProfile(familyCode: String, user: DomainUserDTO): Flow<ResultType<Unit>>
}