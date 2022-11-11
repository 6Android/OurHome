package com.ssafy.ourhome.screens.question

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.model.pet.DomainFamilyPetDTO
import com.ssafy.domain.model.question.DomainQuestionAnswerDTO
import com.ssafy.domain.model.question.DomainQuestionDTO
import com.ssafy.domain.usecase.pet.GetFamilyPetUseCase
import com.ssafy.domain.usecase.question.*
import com.ssafy.domain.utils.ResultType
import com.ssafy.ourhome.utils.Prefs
import com.ssafy.ourhome.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
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
    private val updateTodayQuestion: UpdateTodayQuestion
): ViewModel(){

    var pet by mutableStateOf(DomainFamilyPetDTO())
        private set

    var todayQuestion by mutableStateOf(DomainQuestionDTO())
        private set

    var questionAnswers by mutableStateOf(listOf<DomainQuestionAnswerDTO>())
        private set

    var last3Questions by mutableStateOf(listOf<DomainQuestionDTO>())
        private set

    var lastAllQuestions by mutableStateOf(listOf<DomainQuestionDTO>())
        private set


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

    fun getQuestionAnswers(questionSeq: Int) = viewModelScope.launch(Dispatchers.IO) {
        getQuestionAnswersUseCase.execute(Prefs.familyCode, questionSeq).collect{
            when(it) {
                is ResultType.Uninitialized -> {}
                is ResultType.Success -> {
                    questionAnswers = it.data!!
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
        updateTodayQuestion.execute(Prefs.familyCode, newQuestionSeq).collect{
            when(it) {
                is ResultType.Uninitialized -> {}
                is ResultType.Success -> {

                }
                is ResultType.Error -> {

                }
            }
        }
    }

}
