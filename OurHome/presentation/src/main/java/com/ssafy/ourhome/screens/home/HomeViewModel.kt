package com.ssafy.ourhome.screens.home

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.model.schedule.DomainScheduleDTO
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.usecase.question.CheckAnswerTodayQuestionUseCase
import com.ssafy.domain.usecase.schedule.AddFamilyScheduleUseCase
import com.ssafy.domain.usecase.schedule.DeleteFamilyScheduleUseCase
import com.ssafy.domain.usecase.schedule.GetFamilyScheduleUseCase
import com.ssafy.domain.usecase.user.EditLocationPermissionUseCase
import com.ssafy.domain.usecase.user.GetFamilyUsersUseCase
import com.ssafy.domain.utils.ResultType
import com.ssafy.ourhome.model.schedule.ParticipantDTO
import com.ssafy.ourhome.utils.Prefs
import com.ssafy.ourhome.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val editLocationPermissionUseCase: EditLocationPermissionUseCase,
    private val getFamilyUsersUseCase: GetFamilyUsersUseCase,
    private val getFamilyScheduleUseCase: GetFamilyScheduleUseCase,
    private val deleteFamilyScheduleUseCase: DeleteFamilyScheduleUseCase,
    private val addFamilyScheduleUseCase: AddFamilyScheduleUseCase,
    private val checkAnswerTodayQuestionUseCase: CheckAnswerTodayQuestionUseCase
) : ViewModel() {
    val familyUsersState = mutableStateOf<List<DomainUserDTO>>(emptyList())
    val familyUsersProcessState = mutableStateOf(State.DEFAULT)

    val scheduleMap = mutableStateMapOf<String, List<DomainScheduleDTO>>()

    val scheduleDetailState = mutableStateOf(DomainScheduleDTO())
    var scheduleDetailPeople = listOf<DomainUserDTO>()

    val deleteScheduleProcessState = mutableStateOf(State.DEFAULT)

    val addScheduleProcessState = mutableStateOf(State.DEFAULT)
    val addScheduleTitleState = mutableStateOf("")
    val addScheduleContentState = mutableStateOf("")
    val addScheduleDateState = mutableStateOf(LocalDate.now())
    val addScheduleParticipantsState = mutableStateListOf<ParticipantDTO>()

    val checkAnswerTodayQuestionProcessState = mutableStateOf(State.DEFAULT)


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
                    familyUsersProcessState.value = State.SUCCESS
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

    fun addSchedule() = viewModelScope.launch(Dispatchers.IO) {
        val schedule = DomainScheduleDTO(
            date = "${addScheduleDateState.value}",
            year = addScheduleDateState.value.year,
            month = addScheduleDateState.value.monthValue,
            day = addScheduleDateState.value.dayOfMonth,
            title = addScheduleTitleState.value,
            content = addScheduleContentState.value,
            participants = addScheduleParticipantsState.filter { it.checked }.map { it.email }
        )

        addFamilyScheduleUseCase.execute(Prefs.familyCode, schedule).collect { response ->
            when (response) {
                is ResultType.Success -> {
                    addScheduleProcessState.value = State.SUCCESS
                }
                else -> {
                    addScheduleProcessState.value = State.FAIL
                }
            }
        }
    }

    // 스케줄 추가 상태 초기화
    fun initAddSchedule() {
        addScheduleTitleState.value = ""
        addScheduleContentState.value = ""
        addScheduleDateState.value = LocalDate.now()
        addScheduleParticipantsState.clear()
        addScheduleParticipantsState.addAll(
            familyUsersState.value.map {
                ParticipantDTO(
                    name = it.name,
                    email = it.email,
                    image = it.image
                )
            }
        )
    }

    fun deleteParticipant(email: String) {
        var idx = 0
        addScheduleParticipantsState.forEachIndexed { index, participantDTO ->
            if (participantDTO.email == email) {
                idx = index
                return@forEachIndexed
            }
        }
        addScheduleParticipantsState[idx] = addScheduleParticipantsState[idx].copy(checked = false)
    }

    // 오늘의 질문 대답했는지 체크
    fun checkAnswerTodayQuestion() = viewModelScope.launch(Dispatchers.IO) {

        checkAnswerTodayQuestionUseCase.execute(Prefs.familyCode, Prefs.email)
            .collect { response ->
                when (response) {
                    is ResultType.Success -> {
                        checkAnswerTodayQuestionProcessState.value = State.SUCCESS
                    }
                    is ResultType.Fail -> {
                        checkAnswerTodayQuestionProcessState.value = State.FAIL
                    }
                    else -> {
                        checkAnswerTodayQuestionProcessState.value = State.ERROR
                    }
                }
            }
    }
}