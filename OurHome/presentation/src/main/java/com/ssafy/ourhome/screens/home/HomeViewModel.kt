package com.ssafy.ourhome.screens.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.usecase.user.EditLocationPermissionUseCase
import com.ssafy.domain.usecase.user.GetFamilyUsersUseCase
import com.ssafy.domain.utils.ResultType
import com.ssafy.ourhome.utils.Prefs
import com.ssafy.ourhome.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val editLocationPermissionUseCase: EditLocationPermissionUseCase,
    private val getFamilyUsersUseCase: GetFamilyUsersUseCase
): ViewModel() {
    val familyUsersState = mutableStateOf<List<DomainUserDTO>>(emptyList())
    val familyUsersProcessState = mutableStateOf(State.DEFAULT)

    fun editLocationPermission(permission: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        editLocationPermissionUseCase.execute(Prefs.familyCode, Prefs.email, permission).collect { response ->
            when (response) {
                is ResultType.Loading -> {}
                is ResultType.Success -> {
                    Log.d("test5", "editLocationPermission: 잘 됐음.")
                }
                is ResultType.Error -> {
                    Log.d("test5", "editLocationPermission: 에러낫음!!!!!!.")
                }
                else -> {

                }
            }
        }
    }

    fun getFamilyUsers() = viewModelScope.launch(Dispatchers.IO) {
        getFamilyUsersUseCase.execute(Prefs.familyCode).collect { response ->
            when(response) {
                is ResultType.Loading -> {}
                is ResultType.Success -> {
                    familyUsersState.value = response.data
                }
                is ResultType.Error -> {
                    familyUsersProcessState.value = State.ERROR
                }
                else -> {

                }
            }
        }
    }
}