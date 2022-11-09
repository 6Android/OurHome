package com.ssafy.ourhome.screens.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.usecase.user.CheckEmailUseCase
import com.ssafy.domain.usecase.user.JoinEmailUseCase
import com.ssafy.domain.usecase.user.SignInEmailUseCase
import com.ssafy.domain.utils.ResultType
import com.ssafy.ourhome.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val joinEmailUseCase: JoinEmailUseCase,
    private val checkEmailUseCase: CheckEmailUseCase,
    private val signInEmailUseCase: SignInEmailUseCase
) : ViewModel() {

    val loginIdState = mutableStateOf("")
    val loginPasswordState = mutableStateOf("")
    val loginState = mutableStateOf(State.DEFAULT)
    val familyState = mutableStateOf(true) // true 이면 이미 가족방 있음

    val joinIdState = mutableStateOf("")
    val joinPasswordState = mutableStateOf("")
    val joinPasswordConfirmState = mutableStateOf("")
    val joinNickNameState = mutableStateOf("")


    fun joinEmail(onSuccess: () -> Unit, onFail: () -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            joinEmailUseCase.execute(
                joinIdState.value,
                joinPasswordState.value,
                joinNickNameState.value
            ).collect { response ->
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

        if (loginIdState.value.isBlank() || loginPasswordState.value.isBlank()) {
            loginState.value = State.FAIL
            return@launch
        }

        loginState.value = State.LOADING
        signInEmailUseCase.execute(loginIdState.value, loginPasswordState.value)
            .collect { response ->
                Log.d("TAG", "signInEmail response: $response")
                withContext(Dispatchers.Main) {
                    when (response) {
                        is ResultType.Success -> {
                            // 가족방이 없을 때
                            if (response.data.family_code.isBlank()) {
                                familyState.value = false
                            }

                            loginState.value = State.SUCCESS
                        }
                        else -> {
                            loginState.value = State.FAIL
                        }
                    }
                }
            }
    }
}