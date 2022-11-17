package com.ssafy.ourhome.screens.question

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.model.pet.DomainFamilyPetDTO
import com.ssafy.domain.model.question.DomainQuestionAnswerDTO
import com.ssafy.domain.model.question.DomainQuestionDTO
import com.ssafy.domain.model.user.DomainUserDTO
import com.ssafy.domain.usecase.pet.GetFamilyPetUseCase
import com.ssafy.domain.usecase.pet.LevelUpUseCase
import com.ssafy.domain.usecase.pet.UpdatePetExpUseCase
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
    private val editUserContribution: EditUserContribution,
    private val updatePetExpUseCase: UpdatePetExpUseCase,
    private val levelUpUseCase: LevelUpUseCase,
    private val checkCompleteTodayQuestionUseCase : CheckCompleteTodayQuestionUseCase
): ViewModel(){

    var myProfile by mutableStateOf(DomainUserDTO())
        private set

    var pet by mutableStateOf(DomainFamilyPetDTO())
        private set

    var todayQuestion by mutableStateOf(DomainQuestionDTO())
        private set

    var todayQuestionMyAnswer by mutableStateOf(DomainQuestionAnswerDTO())

    var detailQuestionSeq by mutableStateOf(1)

    var detailQuestion by mutableStateOf(DomainQuestionDTO())
        private set

    var familyAnswers = mutableStateListOf<DomainQuestionAnswerDTO>()
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
    var familyAnswersGetState by mutableStateOf(State.DEFAULT)

    var answerCompleteState by mutableStateOf(State.DEFAULT)

    var updateExpCompleteState by mutableStateOf(State.DEFAULT)

    var getTodayQuestionState by mutableStateOf(State.DEFAULT)

    var getTodayQuestionAnswerState by mutableStateOf(State.DEFAULT)

    lateinit var today: LocalDate
    lateinit var date: String

    // today, date 초기 설정 (lateinit var 때문)
    fun initDate(){
        today = LocalDate.now()
        date = today.year.toString() + "."
        if(today.monthValue < 10){
            date += "0"
        }
        date = date + today.monthValue + "."

        if(today.dayOfMonth < 10){
            date += "0"
        }
        date += today.dayOfMonth
    }

    // Pet 받아오면 levelUp 가능한지 확인
    fun getFamiliyPet() = viewModelScope.launch(Dispatchers.IO) {
        getFamilyPetUseCase.execute(Prefs.familyCode).collect{
            when(it){
                is ResultType.Uninitialized -> {}
                is ResultType.Success -> {
                    pet = it.data!!
                    isLevelUp()
                }
                is ResultType.Error -> {

                }
            }
        }
    }

    // 레벨업 할 수 있는지 확인
    fun isLevelUp() : Boolean {
        if(pet.exp >= pet.next_level_exp){
            levelUp()
            return true
        }
        return false
    }

    // 레벨업 시키기 (pet_info에서 다음 레벨 가져온 뒤 Family pet에 넣어줌)
    fun levelUp() {
        viewModelScope.launch(Dispatchers.IO) {
            levelUpUseCase.execute(Prefs.familyCode, pet.pet_level + 1).collect {
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

    // 오늘의 질문 가져오고 complete 됐는지 재확인
    fun getTodayQuestion() = viewModelScope.launch(Dispatchers.IO) {
        getTodayQuestionUseCase.execute(Prefs.familyCode).collect{
            when(it){
                is ResultType.Uninitialized -> {}
                is ResultType.Success -> {
                    todayQuestion = it.data[0]
                    getTodayQuestionState = State.SUCCESS
                    checkTodayQuestion()
                }
                is ResultType.Error -> {

                }
            }
        }
    }

    // questioinDetailScreen에서 확인할 질문의 seq (detailQeustionSeq)정보를 detailQuestion에 넣어줌
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

    // TodayQuestion의 답변 가져오기
    fun getTodayQuestionAnswers() = viewModelScope.launch(Dispatchers.IO) {
        getQuestionAnswersUseCase.execute(Prefs.familyCode, todayQuestion.question_seq).collect{
            when(it) {
                is ResultType.Uninitialized -> {}
                is ResultType.Success -> {
                    val questionAnswers = it.data!!
                    var familyAnswerListTmp = mutableStateListOf<DomainQuestionAnswerDTO>()

                    for(answer in questionAnswers){
                        if(answer.email == Prefs.email){
                            todayQuestionMyAnswer = answer
                        }else{
                            familyAnswerListTmp.add(answer)
                        }
                    }

                    familyAnswers = familyAnswerListTmp

                    getTodayQuestionAnswerState = State.SUCCESS
                }
                is ResultType.Error -> {

                }
            }
        }
    }

    // detailQuestionSeq에 해당하는 질문의 답변들을 가져옴
    fun getQuestionAnswers() = viewModelScope.launch(Dispatchers.IO) {
        getQuestionAnswersUseCase.execute(Prefs.familyCode, detailQuestionSeq).collect{
            when(it) {
                is ResultType.Uninitialized -> {}
                is ResultType.Success -> {
                    val questionAnswers = it.data!!
                    var familyAnswerListTmp = mutableStateListOf<DomainQuestionAnswerDTO>()

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

                    familyAnswersGetState = State.SUCCESS
                }
                is ResultType.Error -> {

                }
            }
        }
    }

    // questionScreen에 지난 질문 3개 가져오기
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

    // 지난 질문 리스트 가져오기
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

    // todayQuestion 완료 확인
    fun checkTodayQuestion(){
        if(isCompletedAnswer1DayLater()){
            updateTodayQuestion(todayQuestion.question_seq + 1)
        }
    }

    // 오늘의 답변이 완료되었고 하루가 지났는지
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

    // TodayQuestion으로 업데이트
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

    // PetDetail에서 familyUsers 가져오는 코드 (state 관리때문에 따로 뺌)
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

    // 가족 유저들 가져와서 familyUsers(map형태)에 정보들 담아줌
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

    // 수정 시 답변 로직
    fun modifyAnswer(){
        today = LocalDate.now()
        date = today.year.toString() + "."
        if(today.monthValue < 10){
            date += "0"
        }
        date = date + today.monthValue + "."

        if(today.dayOfMonth < 10){
            date += "0"
        }
        date += today.dayOfMonth

        answerDetailQuestion(today, date)   //답변
        isLevelUp()     //레벨업 가능한지 확인
        editContribution()  // 유저 기여도 업데이트
        updateExp() // 펫 경험치 업데이트

    }

    // 수정말고 첫 답변 시 로직
    fun answer() {
        today = LocalDate.now()
        date = today.year.toString() + "."
        if(today.monthValue < 10){
            date += "0"
        }
        date = date + today.monthValue + "."

        if(today.dayOfMonth < 10){
            date += "0"
        }
        date += today.dayOfMonth

        answerDetailQuestion(today, date)   //답변
        isLevelUp()     //레벨업 가능한지 확인
        editContribution(FIRST_ANSWER_POINT)  // 유저 기여도 업데이트 (첫 답변 시 포인트 추가)
        updateExp(FIRST_ANSWER_POINT) // 펫 경험치 업데이트 (첫 답변 시 포인트 추가)


        if(checkCompleteAnswer()){  // 첫 답변 시 answer complete 되었는지 확인
            completeTodayAnswer(today, date)    // answer complete로 바꾸기
        }
    }

    // answer complete 되었는지 false, true 확인
    fun checkCompleteAnswerInQuestionScreen() : Boolean {
        if(familyUsers.size == 1){
            return false
        }

        if(familyAnswers.size + (if(todayQuestionMyAnswer.content == "") 0 else 1) != familyUsers.size){
            return false
        }

        return true
    }

    fun checkCompleteAnswer() : Boolean {
        if(familyUsers.size == 1){
            return false
        }

        if(familyAnswers.size + 1 != familyUsers.size){
            return false
        }

        return true
    }

    // todayAnswer의 completedate 삽입
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

    // 펫의 경험치 업데이트
    fun updateExp(firstAnswerPoint : Int = 0) = viewModelScope.launch(Dispatchers.IO) {
        updatePetExpUseCase.execute(Prefs.familyCode,myAnswer.value.length - myAnswerPoint + firstAnswerPoint).collect{
            when(it) {
                is ResultType.Loading -> {}
                is ResultType.Success -> {
                    updateExpCompleteState = State.SUCCESS
                }
                is ResultType.Error -> {
                    updateExpCompleteState = State.ERROR
                }
                else -> {

                }
            }
        }
    }

    // 유저의 기여도 업데이트
    fun editContribution(firstAnswerPoint : Int = 0) = viewModelScope.launch(Dispatchers.IO) {
        editUserContribution.execute(Prefs.familyCode, Prefs.email, 1L * myAnswer.value.length - myAnswerPoint + firstAnswerPoint).collect{
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


    // 질문 답변하기 (수정도 가능)
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

    //QuestionScreen에서 오늘의 질문 완료되었는지 재확인
    fun checkCompleteAnswerInScreen() {
        if(checkCompleteAnswerInQuestionScreen()){
            checkCompleteTodayQuestionAnswer()
        }
    }

    // 오늘의 질문이 완성되어있지 않으면 재확인하여 갱신
    fun checkCompleteTodayQuestionAnswer() = viewModelScope.launch(Dispatchers.IO){
        val questionMap = mapOf<String, Any>(
            "question_seq" to todayQuestion.question_seq,
            "question_content" to todayQuestion.question_content,
        )
        checkCompleteTodayQuestionUseCase.execute(Prefs.familyCode, todayQuestion.question_seq, questionMap).collect{
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
