package com.ssafy.ourhome.screens.home.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.usecase.user.GetFamilyUsersUseCase
import com.ssafy.domain.utils.ResultType
import com.ssafy.ourhome.utils.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getFamilyUsersUseCase: GetFamilyUsersUseCase,
) : ViewModel() {

    var users by mutableStateOf(listOf<DomainUserDTO>())
        private set

    var errorState by mutableStateOf(false)

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
}