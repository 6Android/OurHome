package com.ssafy.data.repository.user

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ssafy.data.datasource.family.FamilyDataSource
import com.ssafy.data.datasource.pet.PetDataSource
import com.ssafy.data.datasource.question.QuestionDataSource
import com.ssafy.data.datasource.user.UserDataSource
import com.ssafy.data.model.question.DataQuestionDTO
import com.ssafy.data.utils.*
import com.ssafy.domain.model.family.DomainFamilyDTO
import com.ssafy.domain.model.pet.DomainFamilyPetDTO
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.repository.user.UserRepository
import com.ssafy.domain.repository.user.UserResponse
import com.ssafy.domain.repository.user.UsersResponse
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.time.LocalDateTime
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val userDataSource: UserDataSource,
    private val familyDataSource: FamilyDataSource,
    private val questionDataSource: QuestionDataSource,
    private val petDataSource: PetDataSource
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

    // 유저 정보 가져오기
    override fun getMyProfile(familyCode: String, email: String): Flow<UserResponse> =
        callbackFlow {
            val snapshotListener =
                userDataSource.getMyProfile(familyCode, email).addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val user = snapshot.toObject(DomainUserDTO::class.java)!!
                        ResultType.Success(user)
                    } else {
                        ResultType.Error(e)
                    }
                    trySend(response)
                }
            awaitClose {
                Log.d("test5", "getProfile: Cancel")
                snapshotListener.remove()
            }

        }

    //다른 유저 정보 가져오기
    override fun getOtherProfile(familyCode: String, email: String): Flow<UserResponse> =
        callbackFlow {
                userDataSource.getOtherProfile(familyCode, email).addOnSuccessListener {
                    val otherUser = it.toObject(DomainUserDTO::class.java) ?: DomainUserDTO()
                    trySend(ResultType.Success(otherUser))
                }.addOnFailureListener {
                    trySend(ResultType.Error(it))

                }
            awaitClose {

            }

        }

    // 이메일 회원 가입
    override fun joinEmail(email: String, password: String, nickname: String, birthday: String) =
        callbackFlow {
            userDataSource.joinEmail(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userDataSource.insertUser(
                        DomainUserDTO(
                            email = email,
                            name = nickname,
                            birthday = birthday
                        )
                    )
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

    override fun joinSocial(
        email: String,
        nickname: String,
        birthday: String
    ): Flow<ResultType<Unit>> =
        callbackFlow {
            userDataSource.insertUser(
                DomainUserDTO(
                    email = email,
                    name = nickname,
                    birthday = birthday
                )
            )
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

            // question_info Doc
            val questionInfoDocRef = questionDataSource.getQuestionInfoDoc("1")

            // pet_info Doc
            val petInfoDocRef = petDataSource.getPetInfoDoc("1")

            // family/user Doc
            val familyUserDocRef =
                familyDataSource.getFamilyUserDoc(familyCode = familyCode, email = userEmail)

            // family/question Doc
            // 최초에는 seq 1 넣음
            val familyQuestionDocRef =
                familyDataSource.getFamilyQuestionDoc(familyCode = familyCode, seq = "1")

            // family/pet Doc
            val familyPetDocRef =
                familyDataSource.getFamilyPetDoc(familyCode = familyCode)

            // family/album
            // init_date 초기화
            val familyAlbumDocRef =
                familyDataSource.getFamilyAlbumDoc(familyCode = familyCode)

            // family/chat
            // init_date 초기화
            val familyChatDocRef =
                familyDataSource.getFamilyChatDoc(familyCode = familyCode)

            // init_date 담기위한 현재 시간
            val date = LocalDateTime.now()
//            var dateString = date.toString().split("T")[0] + "T"
//
//            dateString += date.hour.toFillZeroString() + "-"
//            dateString += date.minute.toFillZeroString() + "-"
//            dateString += date.second.toFillZeroString()

            val dateMap: Map<String, Any> = mapOf(
//                DATE to dateString,
                YEAR to date.year,
                MONTH to date.monthValue.toFillZeroString(),
                DAY to date.dayOfMonth.toFillZeroString()
            )

            fireStore.runTransaction { transaction ->
                val userSnapshot = transaction.get(userDocRef)
                var user = userSnapshot.toObject(DomainUserDTO::class.java)!!

                val questionInfoSnapshot = transaction.get(questionInfoDocRef)
                val questionInfo = questionInfoSnapshot.toObject(DataQuestionDTO::class.java)!!

                val petInfoSnapshot = transaction.get(petInfoDocRef)
                val petInfo = petInfoSnapshot.toObject(DomainFamilyPetDTO::class.java)!!

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
                transaction.set(familyQuestionDocRef, questionInfo)

                // family/pet 안에 doc 새로 추가
                transaction.set(familyPetDocRef, petInfo)

                // family/album 안에 init_date doc 새로 추가
                transaction.set(familyAlbumDocRef, dateMap)

                // family/char 안에 init_date doc 새로 추가
                transaction.set(familyChatDocRef, dateMap)


                null
            }.addOnSuccessListener {
                trySend(ResultType.Success(Unit))
            }.addOnFailureListener {
                Log.d("TAG", "insetFamily: $it")
                trySend(ResultType.Error(Exception()))
            }

            awaitClose {}
        }

    // 가족방 참여
    override fun enterFamily(familyCode: String, email: String): Flow<ResultType<Unit>> =
        callbackFlow {

            // family Doc
            val familyDocRef = familyDataSource.getFamilyDoc(familyCode)

            // user Doc
            val userDocRef = userDataSource.getUserDoc(email)

            // family/user Doc
            val familyUserDocRef =
                familyDataSource.getFamilyUserDoc(familyCode = familyCode, email = email)

            // 유저 정보 업데이트 시킬 맵
            val map = mapOf<String, Any>(
                FAMILY_CODE to familyCode
            )

            // 패밀리 코드가 이미 있는지 확인
            familyDataSource.checkFamily(familyCode).addOnSuccessListener { documentSnapshot ->

                // 가족방이 없는 코드인 경우
                if (documentSnapshot?.data == null) {
                    trySend(ResultType.Fail)
                }
                // 이미 있는 가족코드인 경우
                else {
                    fireStore.runTransaction { transaction ->

                        // 유저 정보 doc에서 가져와서
                        val userSnapshot = transaction.get(userDocRef)
                        var user = userSnapshot.toObject(DomainUserDTO::class.java)!!

                        // 유저 정보 doc 업데이트 (familycode update)
                        transaction.update(userDocRef, map)

                        // family/user 안에 doc 새로 추가
                        user.apply {
                            family_code = map[FAMILY_CODE].toString()
                        }
                        transaction.set(familyUserDocRef, user)

                        null
                    }.addOnSuccessListener {
                        trySend(ResultType.Success(Unit))
                    }.addOnFailureListener {
                        trySend(ResultType.Error(Exception()))
                    }
                }
            }

            awaitClose {}
        }

    // 유저 정보 수정하기
    override fun editUserProfile(imageUri: Uri, user: DomainUserDTO): Flow<ResultType<Unit>> =
        callbackFlow {

            if (imageUri.toString() != user.image) {
                userDataSource.editProfileImage(user.email, imageUri).addOnSuccessListener {
                    it.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                        Log.d("test5", "editUserProfile: $uri")
                        userDataSource.editUserInfo(
                            user.family_code,
                            user.copy(image = uri.toString())
                        )
                            .addOnCompleteListener {
                                Log.d("test5", "successWithImage")
                                trySend(ResultType.Success(Unit))
                            }
                    }
                }
            } else {
                userDataSource.editUserInfo(user.family_code, user)
                    .addOnCompleteListener {
                        Log.d("test5", "successWithOutImage")
                        trySend(ResultType.Success(Unit))
                    }
            }
            awaitClose {

            }
        }


    // 현재 위치 전송하기
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

    // 위치 공유 동의 여부 수정하기
    override fun editLocationPermission(
        familyCode: String,
        email: String,
        permission: Boolean
    ): Flow<ResultType<Unit>> =
        callbackFlow {
            userDataSource.editLocationPermission(familyCode, email, permission)
                .addOnCompleteListener {
                    Log.d("test5", "editLocationPermission: $it")
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

    // 펫 기여도 수정
    override fun editUserContribution(
        familyCode: String,
        email: String,
        point: Long
    ): Flow<ResultType<Unit>> = callbackFlow {
        userDataSource.editUserContribution(familyCode, email, point).addOnCompleteListener {
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

    // 가족장 변경하기
    override fun editManager(
        familyCode: String,
        myEmail: String,
        otherEmail: String
    ): Flow<ResultType<Unit>> =
        callbackFlow {

            // 내 가족장 위임
            userDataSource.editManager(familyCode, myEmail, false).addOnCompleteListener { my ->
                if (my.isSuccessful) {
                    // 가족장 전달받음
                    userDataSource.editManager(familyCode, otherEmail, true)
                        .addOnCompleteListener { other ->
                            if (other.isSuccessful) {
                                familyDataSource.updateFamilyManager(familyCode, otherEmail)
                                    .addOnSuccessListener {
                                        val response2 = ResultType.Success(Unit)
                                        trySend(response2)
                                    }.addOnFailureListener {
                                        trySend(ResultType.Error(it))
                                    }
                            }
                        }
                } else {
                    trySend(ResultType.Fail)
                }
            }
            awaitClose {
            }
        }


    // 가족 정보 이전 후 삭제
    override fun transferUserData(user: DomainUserDTO): Flow<ResultType<Unit>> = callbackFlow {
        userDataSource.moveUserData(user.copy(family_code = "", contribute_point = 0L, manager = false)).addOnCompleteListener { move ->
            // 정보 이동 성공 시
            if (move.isSuccessful) {
                // 가족 정보에 있는 유저 정보 지움
                userDataSource.outUsers(user.family_code, user.email).addOnSuccessListener {
                    val response = ResultType.Success(Unit)
                    trySend(response)
                }.addOnFailureListener {
                    trySend(ResultType.Error(it))
                }
            } else {
                trySend(ResultType.Fail)
            }
        }
        awaitClose {}
    }
}