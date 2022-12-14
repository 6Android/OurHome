package com.ssafy.ourhome.screens.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ssafy.data.utils.EMAIL
import com.ssafy.data.utils.FAMILY_CODE
import com.ssafy.data.utils.MANAGER
import com.ssafy.domain.model.family.DomainFamilyDTO
import com.ssafy.domain.usecase.user.*
import com.ssafy.domain.utils.ResultType
import com.ssafy.ourhome.MainActivity
import com.ssafy.ourhome.utils.Prefs
import com.ssafy.ourhome.utils.SocialState
import com.ssafy.ourhome.utils.State
import com.ssafy.ourhome.utils.getRandomString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val joinEmailUseCase: JoinEmailUseCase,
    private val checkEmailUseCase: CheckEmailUseCase,
    private val signInEmailUseCase: SignInEmailUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val joinSocialUseCase: JoinSocialUseCase,
    private val insertFamilyUseCase: InsertFamilyUseCase,
    private val enterFamilyUseCase: EnterFamilyUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    val loginIdState = mutableStateOf("")
    val loginPasswordState = mutableStateOf("")
    val loginProcessState = mutableStateOf(State.DEFAULT)
    val hasFamilyState = mutableStateOf(true) // true 이면 이미 가족방 있음

    val joinIdState = mutableStateOf("")
    val joinPasswordState = mutableStateOf("")
    val joinPasswordConfirmState = mutableStateOf("")
    val joinNickNameState = mutableStateOf("")
    val joinDateState = mutableStateOf(LocalDate.now())

    val socialProcessState = mutableStateOf(SocialState.DEFAULT)
    var socialEmail = ""
    val joinProcessState = mutableStateOf(State.DEFAULT)

    val insertFamilyProcessState = mutableStateOf(State.DEFAULT)

    val enterFamilyCodeState = mutableStateOf("")

    fun joinEmail() =
        viewModelScope.launch(Dispatchers.IO) {
            joinProcessState.value = State.LOADING

            joinEmailUseCase.execute(
                joinIdState.value,
                joinPasswordState.value,
                joinNickNameState.value,
                "${joinDateState.value}"
            ).collect { response ->
                when (response) {
                    is ResultType.Success -> {
                        joinProcessState.value = State.SUCCESS
                        Prefs.email = joinIdState.value
                    }
                    else -> {
                        joinProcessState.value = State.FAIL
                    }
                }
            }
        }

    fun joinSocial() =
        viewModelScope.launch(Dispatchers.IO) {
            joinProcessState.value = State.LOADING

            joinSocialUseCase.execute(
                socialEmail,
                joinNickNameState.value,
                "${joinDateState.value}"
            ).collect { response ->
                when (response) {
                    is ResultType.Success -> {
                        joinProcessState.value = State.SUCCESS
                        Prefs.email = socialEmail
                    }
                    else -> {
                        joinProcessState.value = State.FAIL
                    }
                }
            }
        }

    fun checkSocialEmail(email: String) =
        viewModelScope.launch(Dispatchers.IO) {
            checkEmailUseCase.execute(email).collect { response ->
                when (response) {
                    is ResultType.Success -> {
                        // 아이디 없을 때
                        socialEmail = email
                        socialProcessState.value = SocialState.MOVE_JOIN_NICKNAME
                    }
                    else -> {
                        // 아이디 있을 때
                        getUserUseCase.execute(email).collect { response2 ->
                            when (response2) {
                                is ResultType.Success -> {
                                    // 유저 정보 가져올 때
                                    Prefs.email = email
                                    if (response2.data.family_code.isBlank()) {
                                        socialProcessState.value = SocialState.MOVE_ENTER_HOME
                                    } else {
                                        socialProcessState.value = SocialState.MOVE_HOME
                                        Prefs.familyCode = response2.data.family_code
                                    }
                                }
                                else -> {
                                    // 유저 정보 가져오기 실패
                                    socialProcessState.value = SocialState.ERROR
                                }
                            }
                        }
                    }
                }
            }
        }

    fun checkEmail(onSuccess: () -> Unit, onFail: () -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            checkEmailUseCase.execute(joinIdState.value).collect { response ->
                withContext(Dispatchers.Main) {
                    when (response) {
                        is ResultType.Success -> {
                            onSuccess()
                        }
                        else -> {
                            onFail()
                        }
                    }
                }
            }
        }

    // 이메일 로그인
    fun signInEmail() = viewModelScope.launch(Dispatchers.IO) {

        loginProcessState.value = State.LOADING

        if (loginIdState.value.isBlank() || loginPasswordState.value.isBlank()) {
            loginProcessState.value = State.FAIL
            return@launch
        }

        loginProcessState.value = State.LOADING
        signInEmailUseCase.execute(loginIdState.value, loginPasswordState.value)
            .collect { response ->
                withContext(Dispatchers.Main) {
                    when (response) {
                        is ResultType.Success -> {
                            // 가족방이 없을 때
                            Prefs.email = loginIdState.value
                            if (response.data.family_code.isBlank()) {
                                hasFamilyState.value = false
                            } else {
                                Prefs.familyCode = response.data.family_code
                            }

                            loginProcessState.value = State.SUCCESS
                        }
                        else -> {
                            loginProcessState.value = State.FAIL
                        }
                    }
                }
            }
    }

    // 가족방 생성
    fun insertFamily() =
        viewModelScope.launch(Dispatchers.IO) {

            val user_email = Prefs.email

            // Family Doc
            val randomCode = getRandomString(8)
            val family = DomainFamilyDTO(family_code = randomCode, manager = user_email)

            // User Doc
            val map =
                mapOf<String, Any>(EMAIL to user_email, FAMILY_CODE to randomCode, MANAGER to true)

            insertFamilyUseCase.execute(randomCode, family, map).collect { response ->
                withContext(Dispatchers.Main) {
                    when (response) {
                        is ResultType.Success -> {
                            insertFamilyProcessState.value = State.SUCCESS
                            Prefs.familyCode = randomCode
                        }
                        else -> {
                            insertFamilyProcessState.value = State.FAIL
                        }
                    }
                }

            }
        }

    // 가족방 참여
    fun enterFamily() =
        viewModelScope.launch(Dispatchers.IO) {

            val user_email = Prefs.email
            val family_code = enterFamilyCodeState.value

            enterFamilyUseCase.execute(family_code, user_email).collect { response ->
                withContext(Dispatchers.Main) {
                    when (response) {
                        is ResultType.Success -> {
                            insertFamilyProcessState.value = State.SUCCESS
                            Prefs.familyCode = family_code
                        }
                        else -> {
                            insertFamilyProcessState.value = State.FAIL
                        }
                    }
                }

            }
        }

    // 로그아웃
    fun logout() {
        MainActivity.stopWorkManager()
        firebaseAuth.signOut()
        Prefs.email = ""
        Prefs.familyCode = ""
    }
}