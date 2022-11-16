package com.ssafy.ourhome.screens.chat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.data.utils.EMAIL
import com.ssafy.domain.model.chat.DomainChatDTO
import com.ssafy.domain.model.question.DomainQuestionAnswerDTO
import com.ssafy.domain.model.question.DomainQuestionDTO
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.usecase.chat.ChattingUseCase
import com.ssafy.domain.usecase.chat.GetChatsUseCase
import com.ssafy.domain.usecase.user.GetFamilyUsersUseCase
import com.ssafy.domain.utils.ResultType
import com.ssafy.ourhome.utils.Prefs
import com.ssafy.ourhome.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatsUseCase: GetChatsUseCase,
    private val chattingUseCase: ChattingUseCase,
    private val getFamilyUsersUseCase: GetFamilyUsersUseCase
): ViewModel(){

    var chats by mutableStateOf(mapOf<String, List<DomainChatDTO>>())
        private set

    var chatSize by mutableStateOf(1)

    var getChatsProcessState = mutableStateOf(State.DEFAULT)

    var getFamilyProcessState = mutableStateOf(State.DEFAULT)

    var content = mutableStateOf("")

    var familyUsers = mutableStateOf<MutableMap<String, DomainUserDTO>>(mutableMapOf())

    fun getChats() = viewModelScope.launch(Dispatchers.IO) {
        getChatsUseCase.execute(Prefs.familyCode).collect{
            when(it){
                is ResultType.Uninitialized -> {}
                is ResultType.Success -> {
                    chats = it.data.groupBy {
                        it.mapping_date
                    }
                    getChatsProcessState.value = State.SUCCESS
                }
                is ResultType.Error -> {
                    getChatsProcessState.value = State.ERROR
                }
            }
        }
    }

    fun chatting() = viewModelScope.launch(Dispatchers.IO) {
        val today = LocalDateTime.now()

        val chatMap = mapOf<String, Any>(
            EMAIL to Prefs.email,
            "content" to content.value,
            "date" to today.toString(),
            "year" to today.year,
            "month" to today.monthValue,
            "day" to today.dayOfMonth,
            "hour" to today.hour,
            "minute" to today.minute,
            "mapping_date" to "${today.year}년 ${today.monthValue}월 ${today.dayOfMonth}일"
        )

        chattingUseCase.execute(Prefs.familyCode, today.toString(), chatMap).collect{
            when(it){
                is ResultType.Uninitialized -> {}
                is ResultType.Success -> {
                    content.value = ""
                }
                is ResultType.Error -> {

                }
            }
        }

    }

    fun getFamilyUsers() = viewModelScope.launch(Dispatchers.IO) {
        getFamilyUsersUseCase.execute(Prefs.familyCode).collect{
            when(it) {
                is ResultType.Loading -> {}
                is ResultType.Success -> {
                    val familyUserList = it.data

                    for(user in familyUserList){
                        familyUsers.value[user.email] = user
                    }
                    getFamilyProcessState.value = State.SUCCESS
                }
                is ResultType.Error -> {
                }
                else -> {

                }
            }
        }
    }

}