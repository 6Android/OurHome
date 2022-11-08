package com.ssafy.data.repository.user

import com.ssafy.data.datasource.user.UserDataSource
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.repository.user.UserRepository
import com.ssafy.domain.repository.user.UsersResponse
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {
    override fun getFamilyUsers(familyCode: String): Flow<UsersResponse> = callbackFlow {
        val snapshotListener =
            userDataSource.getFamilyUsers(familyCode).addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null) {
                    val users = snapshot.toObjects(DomainUserDTO::class.java)
                    ResultType.Success(users)
                } else {
                    ResultType.Error(e)
                }
                trySend(response)
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    // 이메일 회원 가입
    override fun joinEmail(email: String, password: String): Flow<ResultType<Unit>> = callbackFlow {
        userDataSource.joinEmail(email, password).addOnCompleteListener { task ->
            val response = if (task.isSuccessful) {
                ResultType.Success(Unit)
            } else {
                ResultType.Error(Exception())
            }
            trySend(response)
        }
        awaitClose {}
    }

    // 이메일 중복 검사
    override fun checkEmail(email: String): Flow<ResultType<Unit>> = callbackFlow {
        val snapshotListener =
            userDataSource.checkEmail(email).addSnapshotListener { snapshot, e ->
                val response =
                    // 가입한 이력이 없는 유저
                    if (snapshot?.data == null) {
                        ResultType.Success(Unit)
                    }
                    // 가입한 이력이 있는 유저
                    else {
                        ResultType.Fail(Unit)
                    }
                trySend(response)
            }
        awaitClose {
            snapshotListener.remove()
        }
    }
}