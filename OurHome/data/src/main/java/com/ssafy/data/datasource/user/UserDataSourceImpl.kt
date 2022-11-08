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

    // 이메일 중복 검사
    override fun checkEmail(email: String): Task<DocumentSnapshot> =
        fireStore.collection(USER).document(email).get()

    override fun insertUser(user: DomainUserDTO) =
        fireStore.collection(USER).document(user.email).set(user)
}