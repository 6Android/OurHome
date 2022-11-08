package com.ssafy.ourhome.screens.userpage.mypage

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.repository.user.UserResponse
import com.ssafy.domain.usecase.user.GetMyProfileUseCase
import com.ssafy.domain.utils.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getMyProfileUseCase: GetMyProfileUseCase
): ViewModel(){

    var userResponse by mutableStateOf<UserResponse>(ResultType.Uninitialized)
        private set

    fun getMyProfile(familyCode: String, email: String) = viewModelScope.launch(Dispatchers.IO) {
        getMyProfileUseCase.execute(familyCode, email).collect{
            Log.d("test5", "getMyProfile: $it")
            userResponse = it
        }
    }
}