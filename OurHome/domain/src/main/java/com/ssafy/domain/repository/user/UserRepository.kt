package com.ssafy.domain.repository.user

import android.net.Uri
import com.ssafy.domain.model.family.DomainFamilyDTO
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow

typealias Users = List<DomainUserDTO>
typealias UsersResponse = ResultType<Users>

typealias UserResponse = ResultType<DomainUserDTO>

/** 2개 이상의 메서드를 레포에서 묶는다. **/
interface UserRepository {
    fun getFamilyUsers(familyCode: String): Flow<UsersResponse>

    // 이메일 회원 가입
    fun joinEmail(
        email: String,
        password: String,
        nickname: String,
        birthday: String
    ): Flow<ResultType<Unit>>

    // 소셜 회원 가입
    fun joinSocial(email: String, nickname: String, birthday: String): Flow<ResultType<Unit>>

    // 이메일 로그인
    fun signInEmail(email: String, password: String): Flow<UserResponse>

    // 이메일 중복 검사
    fun checkEmail(email: String): Flow<ResultType<Unit>>

    // 유저 정보 가져오기
    fun getUser(email: String): Flow<UserResponse>

    // 가족방 생성
    fun insetFamily(
        familyCode: String,
        familyDTO: DomainFamilyDTO,
        map: Map<String, Any>
    ): Flow<ResultType<Unit>>

    // 가족방 참여
    fun enterFamily(
        familyCode: String,
        email: String
    ): Flow<ResultType<Unit>>

    // 유저 정보 가져오기
    fun getProfile(familyCode: String, email: String): Flow<UserResponse>

    // 유저 정보 수정하기
    fun editUserProfile(imageUri : Uri,user: DomainUserDTO) : Flow<ResultType<Unit>>

    // 현재 위치 전송하기
    fun sendLatLng(
        familyCode: String,
        email: String,
        latitude: Double,
        longitude: Double,
        time: Long
    ): Flow<ResultType<Unit>>

    // 위치 공유 동의 여부 수정하기
    fun editLocationPermission(
        familyCode: String,
        email: String,
        permission: Boolean
    ): Flow<ResultType<Unit>>

    // 펫 기여도 수정
    fun editUserContribution(familyCode: String, email: String, point: Long): Flow<ResultType<Unit>>

    // 가족장 변경하기
    fun editManager(familyCode: String,
                    myEmail: String,
                    otherEmail: String): Flow<ResultType<Unit>>

    // 가족 정보 이전 후 삭제
    fun transferUserData(user: DomainUserDTO): Flow<ResultType<Unit>>
}