package com.ssafy.ourhome.screens.userpage

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.repository.user.UserResponse
import com.ssafy.domain.usecase.user.EditProfileUseCase
import com.ssafy.domain.usecase.user.GetProfileUseCase
import com.ssafy.domain.utils.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPageViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val editProfileUseCase: EditProfileUseCase
): ViewModel(){

    var userResponse by mutableStateOf<UserResponse>(ResultType.Uninitialized)
        private set

    var editSuccess by mutableStateOf(false)
        private set

    fun getProfile(familyCode: String, email: String) = viewModelScope.launch(Dispatchers.IO) {
        getProfileUseCase.execute(familyCode, email).collect{
            Log.d("test5", "getMyProfile: $it")
            userResponse = it
        }
    }

    fun editProfile(familyCode: String, user: DomainUserDTO) = viewModelScope.launch(Dispatchers.IO) {
        editProfileUseCase.execute(familyCode, user).collect{
            if(it is ResultType.Success){
                editSuccess = true
            }
        }
    }
}