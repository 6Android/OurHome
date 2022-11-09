package com.ssafy.ourhome.screens.home.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.repository.user.UsersResponse
import com.ssafy.domain.usecase.user.GetFamilyUsersUseCase
import com.ssafy.domain.utils.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getFamilyUsersUseCase: GetFamilyUsersUseCase
): ViewModel() {

    var usersResponse by mutableStateOf<UsersResponse>(ResultType.Uninitialized)
        private set

    fun getFamilyUsers(familyCode: String) = viewModelScope.launch(Dispatchers.IO) {
        getFamilyUsersUseCase.execute(familyCode).collect { response ->
            usersResponse = response
        }
    }
}