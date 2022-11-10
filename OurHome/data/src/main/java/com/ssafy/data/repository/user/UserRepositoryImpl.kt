package com.ssafy.data.repository.user

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ssafy.data.datasource.family.FamilyDataSource
import com.ssafy.data.datasource.user.UserDataSource
import com.ssafy.data.utils.EMAIL
import com.ssafy.data.utils.FAMILY_CODE
import com.ssafy.domain.model.family.DomainFamilyDTO
import com.ssafy.domain.model.question.DomainQuestionDTO
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
    private val fireStore: FirebaseFirestore,
    private val userDataSource: UserDataSource,
    private val familyDataSource: FamilyDataSource
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
    override fun joinEmail(email: String, password: String, nickname: String, birthday: String) =
        callbackFlow {
            userDataSource.joinEmail(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userDataSource.insertUser(DomainUserDTO(email = email, name = nickname, birthday = birthday))
                        .addOnCompleteListener {
                            val response = if (it.isSuccessful) {
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

    override fun joinSocial(email: String, nickname: String, birthday: String): Flow<ResultType<Unit>> =
        callbackFlow {
            userDataSource.insertUser(DomainUserDTO(email = email, name = nickname, birthday = birthday))
                .addOnCompleteListener { task ->
                    val response = if (task.isSuccessful) {
                        ResultType.Success(Unit)
                    } else {
                        ResultType.Error(Exception())
                    }
                    trySend(response)
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

    // 유저 정보 가져오기
    override fun getUser(email: String): Flow<UserResponse> = callbackFlow {
        userDataSource.getUser(email)
            .addOnSuccessListener {
                val user = it.toObject(DomainUserDTO::class.java) ?: DomainUserDTO()
                trySend(ResultType.Success(user))
            }
            .addOnFailureListener {
                trySend(ResultType.Error(it))
            }
        awaitClose {}
    }

    // 이메일 중복 검사
    override fun checkEmail(email: String): Flow<ResultType<Unit>> = callbackFlow {
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

    // 가족방 생성
    override fun insetFamily(
        familyCode: String,
        familyDTO: DomainFamilyDTO,
        map: Map<String, Any>
    ): Flow<ResultType<Unit>> =
        callbackFlow {

            val userEmail = map[EMAIL].toString()

            // family Doc
            val familyDocRef = familyDataSource.getFamilyDoc(familyCode)

            // user Doc
            val userDocRef = userDataSource.getUserDoc(userEmail)

            // family/user Doc
            val familyUserDocRef =
                familyDataSource.getFamilyUserDoc(familyCode = familyCode, email = userEmail)

            // family/question Doc
            // 최초에는 seq 1 넣음
            val familyQuestionDocRef =
                familyDataSource.getFamilyQuestionDoc(familyCode = familyCode, seq = "1")

            fireStore.runTransaction { transaction ->
                val userSnapshot = transaction.get(userDocRef)
                var user = userSnapshot.toObject(DomainUserDTO::class.java)!!

                // family doc 새로 추가
                transaction.set(familyDocRef, familyDTO)

                // 유저 정보 업데이트 (familycode, manager)
                transaction.update(userDocRef, map)

                // family/user 안에 doc 새로 추가
                user.apply {
                    family_code = map[FAMILY_CODE].toString()
                    manager = true
                }
                transaction.set(familyUserDocRef, user)

                // family/question 안에 doc 새로 추가
                val questionMap = mapOf<String, Any>(
                    "question_seq" to 1,
                    "email_map" to mapOf<String, DomainQuestionDTO>()
                )
                transaction.set(familyQuestionDocRef, questionMap)

                null
            }.addOnSuccessListener {
                Log.d("TAG", "addOnSuccessListener: ")
                trySend(ResultType.Success(Unit))
            }.addOnFailureListener {
                Log.d("TAG", "addOnSuccessListener: $it")
                trySend(ResultType.Error(Exception()))
            }

            awaitClose {}
        }

    override fun editProfile(familyCode: String, user: DomainUserDTO): Flow<ResultType<Unit>> =
        callbackFlow {
            val completeListener =
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
}