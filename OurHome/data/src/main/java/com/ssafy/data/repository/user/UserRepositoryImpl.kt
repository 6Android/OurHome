package com.ssafy.data.repository.user

import android.util.Log
import com.ssafy.data.datasource.user.UserDataSource
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.repository.user.UserRepository
import com.ssafy.domain.repository.user.UserResponse
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

    override fun getProfile(familyCode: String, email: String): Flow<UserResponse> = callbackFlow {
        val snapshotListener =
            userDataSource.getProfile(familyCode, email).addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null) {
                    val user = snapshot.toObject(DomainUserDTO::class.java)!!
                    ResultType.Success(user)
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
    override fun joinEmail(email: String, password: String, nickname: String) =
        callbackFlow {
            userDataSource.joinEmail(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userDataSource.insertUser(DomainUserDTO(email = email, name = nickname))
                        .addOnCompleteListener {
                            val response = if (task.isSuccessful) {
                                ResultType.Success(Unit)
                            } else {
                                ResultType.Error(Exception())
                            }
                            trySend(response)
                        }
                } else {
                    trySend(ResultType.Fail)
                }
            }
            awaitClose {}
        }

    // 이메일 로그인
    override fun signInEmail(email: String, password: String): Flow<UserResponse> =
        callbackFlow {
            userDataSource.signInEmail(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userDataSource.getUser(email)
                        .addOnSuccessListener {
                            val user = it.toObject(DomainUserDTO::class.java) ?: DomainUserDTO()
                            trySend(ResultType.Success(user))
                        }
                        .addOnFailureListener {
                            trySend(ResultType.Error(it))
                        }
                } else {
                    trySend(ResultType.Fail)
                }
            }
            awaitClose {}
        }

    // 이메일 중복 검사
    override fun checkEmail(email: String): Flow<ResultType<Unit>> = callbackFlow {
        val snapshotListener =
            userDataSource.checkEmail(email).addOnSuccessListener { documentSnapshot ->
                val response =
                    // 가입한 이력이 없는 유저
                    if (documentSnapshot?.data == null) {
                        ResultType.Success(Unit)
                    }
                    // 가입한 이력이 있는 유저
                    else {
                        ResultType.Fail
                    }
                trySend(response)
            }
        awaitClose { }
    }

    override fun editProfile(familyCode: String, user: DomainUserDTO): Flow<ResultType<Unit>> =
        callbackFlow {
            userDataSource.editProfile(familyCode, user).addOnCompleteListener {
                val response = if (it.isSuccessful) {
                    ResultType.Success(Unit)
                } else if (it.exception != null) {
                    ResultType.Error(it.exception)
                } else {
                    ResultType.Loading
                }
                trySend(response)
            }
            awaitClose {

            }
        }

    override fun sendLatLng(
        familyCode: String,
        email: String,
        latitude: Double,
        longitude: Double,
        time: Long
    ): Flow<ResultType<Unit>> =
        callbackFlow {
            userDataSource.sendLatLng(familyCode, email, latitude, longitude, time)
                .addOnCompleteListener {
                    Log.d("test5", "sendLatLng: $it")
                    val response = if (it.isSuccessful) {
                        ResultType.Success(Unit)
                    } else if (it.exception != null) {
                        ResultType.Error(it.exception)
                    } else {
                        ResultType.Loading
                    }
                    trySend(response)
                }
            awaitClose {

            }
        }

}