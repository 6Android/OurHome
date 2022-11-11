package com.ssafy.data.datasource.user

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ssafy.data.utils.FAMILY
import com.ssafy.data.utils.USER
import com.ssafy.domain.model.user.DomainUserDTO
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val fireAuth: FirebaseAuth,
) : UserDataSource {
    override fun getFamilyUsers(familyCode: String): Query =
        fireStore.collection(FAMILY).document(familyCode).collection(USER)

    // 이메일 회원 가입
    override fun joinEmail(email: String, password: String) =
        fireAuth.createUserWithEmailAndPassword(email, password)

    // 이메일 로그인
    override fun signInEmail(email: String, password: String) =
        fireAuth.signInWithEmailAndPassword(email, password)

    // 이메일 중복 검사
    override fun checkEmail(email: String): Task<DocumentSnapshot> =
        fireStore.collection(USER).document(email).get()

    override fun insertUser(user: DomainUserDTO) =
        fireStore.collection(USER).document(user.email).set(user)

    // 유저 Document 가져오기
    override fun getUser(email: String) =
        fireStore.collection(USER).document(email).get()

    // user Doc (user Collection -> email Doc)
    override fun getUserDoc(email: String) =
        fireStore.collection(USER).document(email)

    // 유저 정보 가져오기
    override fun getProfile(familyCode: String, email: String) =
        fireStore.collection(FAMILY).document(familyCode).collection(USER).document(email)

    // 유저 정보 수정하기
    override fun editProfile(familyCode: String, user: DomainUserDTO): Task<Void> =
        fireStore.collection(FAMILY).document(familyCode).collection(USER).document(user.email)
            .set(user)

    // 현재 위치 전송하기
    override fun sendLatLng(
        familyCode: String,
        email: String,
        latitude: Double,
        longitude: Double,
        time: Long
    ): Task<Void> =
        fireStore.collection(FAMILY).document(familyCode).collection(USER).document(email)
            .update("latitude", latitude, "longitude", longitude, "location_updated", time)

    // 위치 공유 동의 여부 수정하기
    override fun editLocationPermission(familyCode: String, email: String, permission: Boolean): Task<Void> =
        fireStore.collection(FAMILY).document(familyCode).collection(USER).document(email)
            .update("location_permit", permission)

    // 가족장 변경하기
    override fun editManager(familyCode: String, email: String, isManager: Boolean): Task<Void> =
        fireStore.collection(FAMILY).document(familyCode).collection(USER).document(email)
            .update("manager", isManager)
}