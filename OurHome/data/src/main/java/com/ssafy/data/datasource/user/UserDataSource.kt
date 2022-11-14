package com.ssafy.data.datasource.user

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.storage.UploadTask
import com.ssafy.domain.model.user.DomainUserDTO
import java.net.URL

interface UserDataSource {
    fun getFamilyUsers(familyCode: String): Query

    // 이메일 회원 가입
    fun joinEmail(email: String, password: String): Task<AuthResult>

    // 이메일 로그인
    fun signInEmail(email: String, password: String): Task<AuthResult>

    // 이메일 중복 검사
    fun checkEmail(email: String): Task<DocumentSnapshot>

    // 유저 Document 등록
    fun insertUser(user: DomainUserDTO): Task<Void>

    // 유저 Document 가져오기
    fun getUser(email: String): Task<DocumentSnapshot>

    // user Doc (user Collection -> email Doc)
    fun getUserDoc(email: String): DocumentReference

    // 유저 정보 가져오기
    fun getProfile(familyCode: String, email: String): DocumentReference

    // 유저 정보 수정하기
    fun editUserInfo(familyCode: String, user: DomainUserDTO): Task<Void>

    //유저 프로필 사진 업로드
    fun editProfileImage(email : String, imageUrl : Uri ) : UploadTask

    // 현재 위치 전송하기
    fun sendLatLng(familyCode: String, email :String, latitude : Double, longitude : Double, time: Long) : Task<Void>

    // 위치 공유 동의 여부 수정하기
    fun editLocationPermission(familyCode: String, email: String, permission: Boolean): Task<Void>

    // 펫 기여도 수정
    fun editUserContribution(familyCode: String, email: String, point: Long):Task<Void>

    // 가족장 변경하기
    fun editManager(familyCode: String, email: String, isManager: Boolean): Task<Void>

    // 가족 데이터 추가하기 (family => user)
    fun moveUserData(userDTO: DomainUserDTO): Task<Void>

    // 가족원 제외 (family 삭제)
    fun outUsers(familyCode: String, email: String): Task<Void>

}