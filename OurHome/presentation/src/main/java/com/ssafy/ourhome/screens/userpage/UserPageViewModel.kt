package com.ssafy.ourhome.screens.userpage

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.usecase.user.*
import com.ssafy.domain.utils.ResultType
import com.ssafy.ourhome.utils.Prefs
import com.ssafy.ourhome.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
    private val editUserProfileUseCase: EditUserProfileUseCase
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
    var imageUri = mutableStateOf(user.image)


    var getProfileFail by mutableStateOf(false) // true = 실패
        private set

    var editProcessState by mutableStateOf(State.DEFAULT)
        private set

    var errorState by mutableStateOf(false)

    var delegateSuccess by mutableStateOf(false)
        private set

    var transferSuccess by mutableStateOf(false)
        private set

    lateinit var job: Job
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
        editProcessState = State.LOADING

        val tmp = user.copy()
        tmp.name = nicknameState.value
        tmp.phone = phoneState.value
        tmp.birthday = birthDayState.value.toString()
        tmp.blood_type = bloodTypeState.value
        tmp.mbti = MBTIState.value
        tmp.job = jobState.value
        tmp.interest = interestState.value
        tmp.hobby = hobbyState.value

        editUserProfileUseCase.execute(imageUri.value.toUri(), tmp).collect {
            if (it is ResultType.Success) {
                editProcessState = State.SUCCESS
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
                        delegateSuccess = true
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
    fun setData() {
        nicknameState.value = user.name
        phoneState.value = user.phone
        birthDayState.value = LocalDate.parse(user.birthday)
        bloodTypeState.value = user.blood_type
        MBTIState.value = user.mbti
        jobState.value = user.job
        interestState.value = user.interest
        hobbyState.value = user.hobby
        imageUri.value = user.image
    }

    fun logout() {
        firebaseAuth.signOut()
        Prefs.email = ""
        Prefs.familyCode = ""
    }

    fun setJob(job: Job) {
        this.job = job
    }

    fun setDelegateSuccess() {
        delegateSuccess = false
    }

    fun setTransferSuccess() {
        transferSuccess = false
    }

    fun setEditSuccess() {
        editProcessState = State.SUCCESS
    }

    fun setGetProfileFail() {
        editProcessState = State.FAIL
    }

    fun seteditProcessStateDefault(){
        editProcessState = State.DEFAULT

    }
}