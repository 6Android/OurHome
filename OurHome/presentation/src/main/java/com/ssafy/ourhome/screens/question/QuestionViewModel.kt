package com.ssafy.ourhome.screens.question

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.model.pet.DomainFamilyPetDTO
import com.ssafy.domain.model.question.DomainQuestionAnswerDTO
import com.ssafy.domain.model.question.DomainQuestionDTO
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.usecase.pet.GetFamilyPetUseCase
import com.ssafy.domain.usecase.question.*
import com.ssafy.domain.usecase.user.EditUserContribution
import com.ssafy.domain.usecase.user.GetFamilyUsersUseCase
import com.ssafy.domain.utils.ResultType
import com.ssafy.ourhome.utils.Prefs
import com.ssafy.ourhome.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getFamilyPetUseCase: GetFamilyPetUseCase,
    private val getTodayQuestionUseCase: GetTodayQuestionUseCase,
    private val getQuestionAnswersUseCase: GetQuestionAnswersUseCase,
    private val getLast3QuestionsUseCase: GetLast3QuestionsUseCase,
    private val getLastAllQuestionsUseCase: GetLastAllQuestionsUseCase,
    private val updateTodayQuestionUseCase: UpdateTodayQuestionUseCase,
    private val getFamilyUsersUseCase: GetFamilyUsersUseCase,
    private val answerQuestionUsecase: AnswerQuestionUsecase,
    private val completeTodayQuestionUseCase: CompleteTodayQuestionUseCase,
    private val editUserContribution: EditUserContribution
): ViewModel(){

    var myProfile by mutableStateOf(DomainUserDTO())
        private set

    var pet by mutableStateOf(DomainFamilyPetDTO())
        private set

    var todayQuestion by mutableStateOf(DomainQuestionDTO())
        private set

    var detailQuestionSeq by mutableStateOf(1)

    var detailQuestion by mutableStateOf(DomainQuestionDTO())
        private set

    var familyAnswers by mutableStateOf(listOf<DomainQuestionAnswerDTO>())
        private set

    var myAnswer = mutableStateOf("")

    var myAnswerPoint = 0

    var myAnswerAddedState by mutableStateOf(false)

    var last3Questions by mutableStateOf(listOf<DomainQuestionDTO>())
        private set

    var lastAllQuestions by mutableStateOf(listOf<DomainQuestionDTO>())
        private set

//    var familyUsers = mutableStateOf<MutableMap<String, DomainUserDTO>>(mutableMapOf())
    val familyUsers = mutableStateMapOf<String, DomainUserDTO>()

    var familyPetProcessState by mutableStateOf(State.DEFAULT)

    var familyGetState by mutableStateOf(State.DEFAULT)

    var answerCompleteState by mutableStateOf(State.DEFAULT)

    fun getFamiliyPet() = viewModelScope.launch(Dispatchers.IO) {
        getFamilyPetUseCase.execute(Prefs.familyCode).collect{
            when(it){
                is ResultType.Uninitialized -> {}
                is ResultType.Success -> {
                    pet = it.data!!
                }
                is ResultType.Error -> {

                }
            }
        }
    }

    fun getTodayQuestion() = viewModelScope.launch(Dispatchers.IO) {
        getTodayQuestionUseCase.execute(Prefs.familyCode).collect{
            when(it){
                is ResultType.Uninitialized -> {}
                is ResultType.Success -> {
                    todayQuestion = it.data[0]
                    checkTodayQuestion()
                }
                is ResultType.Error -> {

                }
            }
        }
    }

    fun getDetailQuestion() {
        if(todayQuestion.question_seq == detailQuestionSeq){
            detailQuestion = todayQuestion
        }else{
            for(question in last3Questions){
                if(question.question_seq == detailQuestionSeq){
                    detailQuestion = question
                    return
                }
            }

            for(question in lastAllQuestions){
                if(question.question_seq == detailQuestionSeq){
                    detailQuestion = question
                    return
                }
            }
        }
    }

    fun getQuestionAnswers() = viewModelScope.launch(Dispatchers.IO) {
        getQuestionAnswersUseCase.execute(Prefs.familyCode, detailQuestionSeq).collect{
            when(it) {
                is ResultType.Uninitialized -> {}
                is ResultType.Success -> {
                    val questionAnswers = it.data!!
                    var familyAnswerListTmp = mutableListOf<DomainQuestionAnswerDTO>()

                    for(answer in questionAnswers){
                        if(answer.email == Prefs.email){
                            myAnswer.value = answer.content
                            myAnswerPoint = answer.content.length
                            myAnswerAddedState = true
                        }else{
                            familyAnswerListTmp.add(answer)
                        }
                    }
                    familyAnswers = familyAnswerListTmp

                }
                is ResultType.Error -> {

                }
            }
        }
    }

    fun getLast3Questions() = viewModelScope.launch(Dispatchers.IO) {
        getLast3QuestionsUseCase.execute(Prefs.familyCode).collect{
            when(it) {
                is ResultType.Uninitialized -> {}
                is ResultType.Success -> {
                    last3Questions = it.data
                }
                is ResultType.Error -> {

                }
            }
        }
    }

    fun getLastAllQuestions() = viewModelScope.launch(Dispatchers.IO) {
        getLastAllQuestionsUseCase.execute(Prefs.familyCode).collect{
            when(it) {
                is ResultType.Uninitialized -> {}
                is ResultType.Success -> {
                    lastAllQuestions = it.data
                }
                is ResultType.Error -> {

                }
            }
        }
    }

    fun checkTodayQuestion(){
        if(isCompletedAnswer1DayLater()){
            updateTodayQuestion(todayQuestion.question_seq + 1)
        }
    }

    fun isCompletedAnswer1DayLater() : Boolean{
        if(todayQuestion.completed_date.isNotEmpty()){
            val completedQuestionDate = LocalDate.of(todayQuestion.completed_year, todayQuestion.completed_month, todayQuestion.completed_day)
            val today = LocalDate.now()
            val todayDate = LocalDate.of(today.year, today.monthValue, today.dayOfMonth)
            if(todayDate.compareTo(completedQuestionDate) > 0){
                return true
            }
        }
        return false
    }

    fun updateTodayQuestion(newQuestionSeq: Int) = viewModelScope.launch(Dispatchers.IO) {
        updateTodayQuestionUseCase.execute(Prefs.familyCode, newQuestionSeq).collect{
            when(it) {
                is ResultType.Uninitialized -> {}
                is ResultType.Success -> {

                }
                is ResultType.Error -> {

                }
            }
        }
    }

    fun getFamilyUsersInPetDetail() = viewModelScope.launch(Dispatchers.IO) {
        getFamilyUsersUseCase.execute(Prefs.familyCode).collect{
            when(it) {
                is ResultType.Loading -> {}
                is ResultType.Success -> {
                    val familyUserList = it.data

                    for(user in familyUserList){
                        familyUsers[user.email] = user
                        if(user.email == Prefs.email){
                            myProfile = user
                        }
                    }
                    familyPetProcessState = State.SUCCESS
                }
                is ResultType.Error -> {
                    familyPetProcessState = State.ERROR
                }
                else -> {

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
                        familyUsers[user.email] = user
                        if(user.email == Prefs.email){
                            myProfile = user
                        }
                    }
                    familyPetProcessState = State.SUCCESS
                    familyGetState = State.SUCCESS
                }
                is ResultType.Error -> {
                    familyGetState = State.ERROR
                }
                else -> {

                }
            }
        }
    }

    fun modifyAnswer(){
        val today = LocalDate.now()
        var date = today.year.toString() + "."
        if(today.monthValue < 10){
            date += "0"
        }
        date = date + today.monthValue + "."

        if(today.dayOfMonth < 10){
            date += "0"
        }
        date += today.dayOfMonth

        answerDetailQuestion(today, date)
    }

    fun answer() {
        val today = LocalDate.now()
        var date = today.year.toString() + "."
        if(today.monthValue < 10){
            date += "0"
        }
        date = date + today.monthValue + "."

        if(today.dayOfMonth < 10){
            date += "0"
        }
        date += today.dayOfMonth

        answerDetailQuestion(today, date)

        if(checkCompleteAnswer()){
            completeTodayAnswer(today, date)
        }
    }

    fun answerDetailQuestion(today: LocalDate, date: String) = viewModelScope.launch(Dispatchers.IO) {
        answerQuestionUsecase.execute(Prefs.familyCode, detailQuestionSeq,
            DomainQuestionAnswerDTO(Prefs.email, myAnswer.value, date, today.year, today.monthValue, today.dayOfMonth)
        ).collect {
            when(it) {
                is ResultType.Loading -> {}
                is ResultType.Success -> {
                    answerCompleteState = State.SUCCESS
                }
                is ResultType.Error -> {
                    answerCompleteState = State.ERROR
                }
                else -> {

                }
            }
        }
    }

    fun checkCompleteAnswer() = familyAnswers.size + 1 == familyUsers.size

    fun completeTodayAnswer(today: LocalDate, date: String) = viewModelScope.launch(Dispatchers.IO) {
        val questionMap = mapOf<String, Any>(
            "question_seq" to detailQuestionSeq,
            "question_content" to detailQuestion.question_content,
            "completed_date" to date,
            "completed_year" to today.year,
            "completed_month" to today.monthValue,
            "completed_day" to today.dayOfMonth
        )
        completeTodayQuestionUseCase.execute(Prefs.familyCode, detailQuestionSeq, questionMap).collect{
            when(it) {
                is ResultType.Loading -> {}
                is ResultType.Success -> {

                }
                is ResultType.Error -> {

                }
                else -> {

                }
            }
        }
    }

    fun editContribution() = viewModelScope.launch(Dispatchers.IO) {
        editUserContribution.execute(Prefs.familyCode, Prefs.email, 1L * myAnswer.value.length - myAnswerPoint).collect{
            when(it) {
                is ResultType.Loading -> {}
                is ResultType.Success -> {

                }
                is ResultType.Error -> {

                }
                else -> {

                }
            }
        }
    }


}
