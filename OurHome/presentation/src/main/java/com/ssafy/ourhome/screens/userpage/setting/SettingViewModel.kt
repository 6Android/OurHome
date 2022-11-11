package com.ssafy.ourhome.screens.userpage.setting

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.usecase.user.EditLocationPermissionUseCase
import com.ssafy.domain.usecase.user.EditManagerUseCase
import com.ssafy.domain.usecase.user.GetFamilyUsersUseCase
import com.ssafy.domain.utils.ResultType
import com.ssafy.ourhome.utils.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val editLocationPermissionUseCase: EditLocationPermissionUseCase,
    private val getFamilyUsersUseCase: GetFamilyUsersUseCase,
    private val editManagerUseCase: EditManagerUseCase
) : ViewModel() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    var users by mutableStateOf(listOf<DomainUserDTO>())
        private set

    var errorState by mutableStateOf(false)

    var editSuccess by mutableStateOf(false)
        private set

    fun editLocationPermission(permit : Boolean){
        viewModelScope.launch(Dispatchers.IO) {

            editLocationPermissionUseCase.execute(Prefs.familyCode,Prefs.email,permit).collect{
                when(it){
                    is ResultType.Success -> {
                        Log.d("SettingViewModel", "editLocationPermissionSuccess: ")
                    }
                    else ->{
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

    fun editManager(otherEmail: String){
        viewModelScope.launch(Dispatchers.IO) {
            editManagerUseCase.execute(Prefs.familyCode,Prefs.email, otherEmail).collect{
                when(it){
                    is ResultType.Success -> {
                        Log.d("SettingViewModel", "yes: ")
                        editSuccess = true
                    }
                    else ->{
                        Log.d("SettingViewModel", "no: ")
                    }
                }

            }
        }
    }

    fun logout(){
        firebaseAuth.signOut()
        Prefs.email = ""
        Prefs.familyCode = ""
    }

    fun setEditSuccess(){
        editSuccess = false
    }
}