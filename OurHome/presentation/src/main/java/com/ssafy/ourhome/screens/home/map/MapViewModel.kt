package com.ssafy.ourhome.screens.home.map

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.repository.user.UsersResponse
import com.ssafy.domain.usecase.user.GetFamilyUsersUseCase
import com.ssafy.domain.usecase.user.SendLatLngUseCase
import com.ssafy.domain.utils.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getFamilyUsersUseCase: GetFamilyUsersUseCase,
    private val sendLatLngUseCase: SendLatLngUseCase
) : ViewModel() {

    var users by mutableStateOf(listOf<DomainUserDTO>())
        private set

    var errorState by mutableStateOf(false)

    fun getFamilyUsers(familyCode: String) = viewModelScope.launch(Dispatchers.IO) {
        Log.d("MapScreen_", "getFamilyUsers: ")
        getFamilyUsersUseCase.execute(familyCode).collect { response ->
            when (response) {
                is ResultType.Loading -> {}
                is ResultType.Success -> {
                    Log.d("test5", "LoginScreen: ${response.data}")
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

    fun sendLatLng() = viewModelScope.launch(Dispatchers.IO) {
        sendLatLngUseCase.execute("EX7342", "a@naver.com", 12.111111, 24.11111).collect {
            Log.d("MapViewModel_", "sendLatLng: $it")
        }


    }


}