package com.ssafy.ourhome.screens.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.repository.user.UsersResponse
import com.ssafy.domain.usecase.user.CheckEmailUseCase
import com.ssafy.domain.usecase.user.GetFamilyUsersUseCase
import com.ssafy.domain.usecase.user.JoinEmailUseCase
import com.ssafy.domain.utils.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getFamilyUsersUseCase: GetFamilyUsersUseCase,
    private val joinEmailUseCase: JoinEmailUseCase,
    private val checkEmailUseCase: CheckEmailUseCase
) : ViewModel() {
    var usersResponse by mutableStateOf<UsersResponse>(ResultType.Uninitialized)
        private set

    var result by mutableStateOf<ResultType<Unit>>(ResultType.Uninitialized)

    fun getFamilyUsers() = viewModelScope.launch(Dispatchers.IO) {
        getFamilyUsersUseCase.execute("EX7342").collect { response ->
            usersResponse = response
        }
    }

    fun joinEmail() = viewModelScope.launch(Dispatchers.IO) {
        joinEmailUseCase.execute("c@ssafy.com", "123456").collect { response ->
            Log.d("TAG", "joinEmail: $response")
            result = response
        }
    }

    fun checkEmail()= viewModelScope.launch(Dispatchers.IO) {
        checkEmailUseCase.execute("a@ssafy.com").collect { response ->
        }
    }
}