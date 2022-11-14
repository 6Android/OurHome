package com.ssafy.ourhome.screens.home

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.model.schedule.DomainScheduleDTO
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.usecase.schedule.GetFamilyScheduleUseCase
import com.ssafy.domain.usecase.user.EditLocationPermissionUseCase
import com.ssafy.domain.usecase.user.GetFamilyUsersUseCase
import com.ssafy.domain.utils.ResultType
import com.ssafy.ourhome.utils.Prefs
import com.ssafy.ourhome.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val editLocationPermissionUseCase: EditLocationPermissionUseCase,
    private val getFamilyUsersUseCase: GetFamilyUsersUseCase,
    private val getFamilyScheduleUseCase: GetFamilyScheduleUseCase
) : ViewModel() {
    val familyUsersState = mutableStateOf<List<DomainUserDTO>>(emptyList())
    val familyUsersProcessState = mutableStateOf(State.DEFAULT)

    val scheduleMap = mutableStateMapOf<String, List<DomainScheduleDTO>>()

    fun editLocationPermission(permission: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        editLocationPermissionUseCase.execute(Prefs.familyCode, Prefs.email, permission)
            .collect { response ->
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
            when (response) {
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

    // 가족 스케줄 리스트 조회
    fun getFamilySchedules(year: Int, month: Int) =
        viewModelScope.launch(Dispatchers.IO) {

            getFamilyScheduleUseCase.execute(Prefs.familyCode, year, month).collect { response ->
                withContext(Dispatchers.Main) {
                    when (response) {
                        is ResultType.Success -> {
                            Log.d("TAG", "getFamilySchedules: ${response.data}")
                            scheduleMap.putAll(response.data.groupBy { schedule -> schedule.date })
                            Log.d("TAG", "getFamilySchedules: ${scheduleMap}")
                        }
                        else -> {
                        }
                    }
                }

            }
        }
}