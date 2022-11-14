package com.ssafy.ourhome.screens.userpage

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.repository.user.UserResponse
import com.ssafy.domain.usecase.user.*
import com.ssafy.domain.utils.ResultType
import com.ssafy.ourhome.utils.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class UserPageViewModel @Inject constructor(
    private val editLocationPermissionUseCase: EditLocationPermissionUseCase,
    private val getFamilyUsersUseCase: GetFamilyUsersUseCase,
    private val editManagerUseCase: EditManagerUseCase,
    private val transferUserDataUseCase: TransferUserDataUseCase,
    private val insertUserUseCase: InsertUserUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val editProfileUseCase: EditProfileUseCase
) : ViewModel() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    var users by mutableStateOf(listOf<DomainUserDTO>())
        private set

    var user by mutableStateOf(DomainUserDTO())
        private set

    var nicknameState = mutableStateOf(user.name)
    var phoneState = mutableStateOf(user.phone)
    var birthDayState = mutableStateOf(LocalDate.parse(user.birthday))
    var bloodTypeState = mutableStateOf(user.blood_type)
    var MBTIState = mutableStateOf(user.mbti)
    var jobState = mutableStateOf(user.job)
    var interestState = mutableStateOf(user.interest)
    var hobbyState = mutableStateOf(user.hobby)

    var getProfileFail by mutableStateOf(false) // true = 실패
        private set

    var editSuccess by mutableStateOf(false)
        private set

    var errorState by mutableStateOf(false)

    var delegateSuccess by mutableStateOf(false)
        private set

    var transferSuccess by mutableStateOf(false)
        private set

    fun getProfile(email: String) = viewModelScope.launch(Dispatchers.IO) {
        getProfileUseCase.execute(Prefs.familyCode, email).collect {
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

    fun editProfile() = viewModelScope.launch(Dispatchers.IO) {

        val tmp = user.copy()
        tmp.name = nicknameState.value
        tmp.phone = phoneState.value
        tmp.birthday = birthDayState.value.toString()
        tmp.blood_type = bloodTypeState.value
        tmp.mbti = MBTIState.value
        tmp.job = jobState.value
        tmp.interest = interestState.value
        tmp.hobby = hobbyState.value

        editProfileUseCase.execute(Prefs.familyCode, tmp).collect {
            if (it is ResultType.Success) {
                editSuccess = true
            }
        }
    }

    fun editLocationPermission(permit: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {

            editLocationPermissionUseCase.execute(Prefs.familyCode, Prefs.email, permit).collect {
                when (it) {
                    is ResultType.Success -> {
                        Log.d("SettingViewModel", "editLocationPermissionSuccess: ")
                    }
                    else -> {
                        Log.d("SettingViewModel", "editLocationPermissionError: ")
                    }
                }

            }
        }
    }

    fun getFamilyUsers() = viewModelScope.launch(Dispatchers.IO) {
        getFamilyUsersUseCase.execute(Prefs.familyCode).collect { response ->
            when (response) {
                is ResultType.Loading -> {}
                is ResultType.Success -> {
                    users = response.data
                }
                is ResultType.Error -> {
                    errorState = true
                }
                else -> {

                }
            }
        }
    }

    fun editManager(otherEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            editManagerUseCase.execute(Prefs.familyCode, Prefs.email, otherEmail).collect {
                when (it) {
                    is ResultType.Success -> {
                        Log.d("SettingViewModel", "yes: ")
                        editSuccess = true
                    }
                    else -> {
                        Log.d("SettingViewModel", "no: ")
                    }
                }

            }
        }
    }

    fun transferUserData(user: DomainUserDTO) {
        viewModelScope.launch(Dispatchers.IO) {
            transferUserDataUseCase.execute(user).collect {
                when (it) {
                    is ResultType.Success -> {
                        transferSuccess = true
                    }
                    else -> {
                    }
                }
            }
        }
    }

    // TESTCODE
    fun insertUser(user: DomainUserDTO) {
        viewModelScope.launch(Dispatchers.IO) {
            insertUserUseCase.execute(user)
        }
    }

    // 에디트 텍스트 세팅
    fun setData(){
        nicknameState = mutableStateOf(user.name)
        phoneState = mutableStateOf(user.phone)
        birthDayState = mutableStateOf(LocalDate.parse(user.birthday))
        bloodTypeState = mutableStateOf(user.blood_type)
        MBTIState = mutableStateOf(user.mbti)
        jobState = mutableStateOf(user.job)
        interestState = mutableStateOf(user.interest)
        hobbyState = mutableStateOf(user.hobby)
    }

    fun logout() {
        firebaseAuth.signOut()
        Prefs.email = ""
        Prefs.familyCode = ""
    }

    fun setDelegateSuccess() {
        delegateSuccess = false
    }

    fun setTransferSuccess() {
        transferSuccess = false
    }

    fun setEditSuccess() {
        editSuccess = false
    }

    fun setGetProfileFail() {
        editSuccess = false
    }
}