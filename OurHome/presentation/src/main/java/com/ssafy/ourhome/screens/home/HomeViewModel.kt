package com.ssafy.ourhome.screens.home

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.model.schedule.DomainScheduleDTO
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.usecase.schedule.DeleteFamilyScheduleUseCase
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
    private val getFamilyScheduleUseCase: GetFamilyScheduleUseCase,
    private val deleteFamilyScheduleUseCase: DeleteFamilyScheduleUseCase
) : ViewModel() {
    val familyUsersState = mutableStateOf<List<DomainUserDTO>>(emptyList())
    val familyUsersProcessState = mutableStateOf(State.DEFAULT)

    val scheduleMap = mutableStateMapOf<String, List<DomainScheduleDTO>>()

    val scheduleDetailState = mutableStateOf(DomainScheduleDTO())
    var scheduleDetailPeople = listOf<DomainUserDTO>()

    val deleteScheduleProcessState = mutableStateOf(State.DEFAULT)


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
                            scheduleMap.clear()
                            scheduleMap.putAll(response.data.groupBy { schedule -> schedule.date })
                            Log.d("TAG", "getFamilySchedules: ${scheduleMap.entries}")
                        }
                        else -> {
                        }
                    }
                }

            }
        }

    fun setScheduleDetail(schedule: DomainScheduleDTO) {
        scheduleDetailState.value = schedule
        scheduleDetailPeople = familyUsersState.value.filter { user ->
            schedule.participants.any { email ->
                email == user.email
            }
        }
    }

    // 일정 삭제
    fun deleteScheduleDetail() = viewModelScope.launch(Dispatchers.IO) {

        deleteFamilyScheduleUseCase.execute(Prefs.familyCode, scheduleDetailState.value.uid)
            .collect { response ->
                when (response) {
                    is ResultType.Success -> {
                        deleteScheduleProcessState.value = State.SUCCESS
                    }
                    else -> {
                        deleteScheduleProcessState.value = State.FAIL
                    }
                }
            }
    }
}