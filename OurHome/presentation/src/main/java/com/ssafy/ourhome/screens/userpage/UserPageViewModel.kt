package com.ssafy.ourhome.screens.userpage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.repository.user.UserResponse
import com.ssafy.domain.usecase.user.EditProfileUseCase
import com.ssafy.domain.usecase.user.GetProfileUseCase
import com.ssafy.domain.utils.ResultType
import com.ssafy.ourhome.utils.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPageViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val editProfileUseCase: EditProfileUseCase
): ViewModel(){

    var user by mutableStateOf(DomainUserDTO())
        private set

    var getProfileFail by mutableStateOf(false) // true = 실패
        private set

    var editSuccess by mutableStateOf(false)
        private set

    fun getProfile(email: String) = viewModelScope.launch(Dispatchers.IO) {
        getProfileUseCase.execute(Prefs.familyCode, email).collect{
            when (it) {
                is ResultType.Uninitialized -> {}
                is ResultType.Success -> {
                    user = it.data
                }
                is ResultType.Error -> {
                    getProfileFail = true
                }
            }
        }
    }

    fun editProfile(user: DomainUserDTO) = viewModelScope.launch(Dispatchers.IO) {
        editProfileUseCase.execute(Prefs.familyCode, user).collect{
            if(it is ResultType.Success){
                editSuccess = true
            }
        }
    }

    fun setEditSuccess(){
        editSuccess = false
    }
    fun setGetProfileFail(){
        editSuccess = false
    }
}