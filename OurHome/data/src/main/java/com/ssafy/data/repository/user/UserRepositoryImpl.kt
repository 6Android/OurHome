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

    override fun joinEmail(email: String, password: String): Flow<ResultType<Unit>> = callbackFlow {
        val listener = userDataSource.joinEmail(email, password).addOnCompleteListener { task ->
            val response = if (task.isSuccessful) {
                ResultType.Success(Unit)
            } else {
                ResultType.Error(Exception())
            }
            trySend(response)
        }
    }
}