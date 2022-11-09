package com.ssafy.ourhome.screens.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.usecase.user.CheckEmailUseCase
import com.ssafy.domain.usecase.user.JoinEmailUseCase
import com.ssafy.domain.utils.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val joinEmailUseCase: JoinEmailUseCase,
    private val checkEmailUseCase: CheckEmailUseCase
) : ViewModel() {
    val joinIdState = mutableStateOf("")
    val joinPasswordState = mutableStateOf("")
    val joinPasswordConfirmState = mutableStateOf("")
    val joinNickNameState = mutableStateOf("")


    fun joinEmail(onSuccess: () -> Unit, onFail: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        joinEmailUseCase.execute(joinIdState.value, joinPasswordState.value, joinNickNameState.value).collect { response ->
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
}