package com.ssafy.domain.repository.user

import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow

typealias Users = List<DomainUserDTO>
typealias UsersResponse = ResultType<Users>

interface UserRepository {
    fun getFamilyUsers(familyCode: String): Flow<UsersResponse>
}